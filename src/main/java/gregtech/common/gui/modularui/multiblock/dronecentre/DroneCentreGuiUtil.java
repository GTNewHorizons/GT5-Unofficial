package gregtech.common.gui.modularui.multiblock.dronecentre;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.api.MCHelper;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import appeng.api.util.DimensionalCoord;
import appeng.client.render.highlighter.BlockPosHighlighter;
import gregtech.api.enums.TickTime;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.dronecentre.sync.DroneConnectionListSyncHandler;
import gregtech.common.gui.modularui.multiblock.dronecentre.sync.ProductionStatsSyncHandler;
import gregtech.common.tileentities.machines.multi.drone.DroneConnection;
import gregtech.common.tileentities.machines.multi.drone.MTEDroneCentre;
import mcp.mobius.waila.api.SpecialChars;

public class DroneCentreGuiUtil {

    public static final Map<Integer, String> TIME_OPTIONS = new LinkedHashMap<>();
    static {
        DroneCentreGuiUtil.TIME_OPTIONS.put(10, "10s");
        DroneCentreGuiUtil.TIME_OPTIONS.put(60, "1min");
        DroneCentreGuiUtil.TIME_OPTIONS.put(600, "10min");
        DroneCentreGuiUtil.TIME_OPTIONS.put(3600, "1h");
        DroneCentreGuiUtil.TIME_OPTIONS.put(86400, "24h");
        DroneCentreGuiUtil.TIME_OPTIONS.put(-1, "All");
    }

    public static ButtonWidget<?> createHighLightButton(DroneConnection conn, PanelSyncManager dynamicSyncManager) {
        return new ButtonWidget<>().syncHandler(
            dynamicSyncManager.getOrCreateSyncHandler(
                "teleportPlayer" + conn.uuid.toString(),
                InteractionSyncHandler.class,
                () -> new InteractionSyncHandler().setOnMousePressed(var -> {
                    EntityPlayer player = dynamicSyncManager.getPlayer();
                    if (!NetworkUtils.isClient() && var.mouseButton == 1
                        && conn.getLinkedCentre() instanceof MTEDroneCentre dc) {
                        int level = dc.getDroneLevel();
                        if (level < 4) {
                            player.addChatMessage(new ChatComponentTranslation("GT5U.gui.chat.drone_level_low"));
                            player.closeScreen();
                            return;
                        }
                        DroneCentreGuiUtil.teleportPlayerToMachine(conn, player);
                        player.closeScreen();
                    } else if (var.mouseButton == 0) {
                        if (NetworkUtils.isClient()) {
                            ChunkCoordinates machineCoord = conn.getMachineCoord();
                            DimensionalCoord blockPos = new DimensionalCoord(
                                machineCoord.posX,
                                machineCoord.posY,
                                machineCoord.posZ,
                                player.dimension);
                            BlockPosHighlighter
                                .highlightBlocks(player, Collections.singletonList(blockPos), null, null);
                        } else {
                            player.closeScreen();
                        }
                    }
                })))
            .size(16)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
            .tooltipBuilder(
                t -> t.addLine("x:" + conn.getMachineCoord().posX)
                    .addLine("y:" + conn.getMachineCoord().posY)
                    .addLine("z:" + conn.getMachineCoord().posZ)
                    .addLine(IKey.lang("GT5U.infodata.dimension", conn.getMachineWorld()))
                    .addLine(IKey.lang("GT5U.gui.button.drone_highlight"))
                    .addLine(IKey.lang("GT5U.gui.button.drone_teleport")));
    }

    public static ModularPanel createConnectionKeyPanel(PanelSyncManager syncManager, ModularPanel parent) {
        return new ModularPanel("connectionKey").relative(parent)
            .leftRel(1)
            .topRel(0)
            .size(120, 36)
            .child(
                Flow.column()
                    .sizeRel(1f)
                    .padding(2)
                    .childPadding(2)
                    .child(
                        IKey.lang("GT5U.gui.text.drone_key")
                            .asWidget()
                            .textAlign(Alignment.CENTER))
                    .child(
                        new TextFieldWidget().height(16)
                            .widthRel(0.8f)
                            .tooltip(t -> t.add(IKey.lang("GT5U.gui.tooltip.drone_key")))
                            .value(syncManager.findSyncHandler("setkey", StringSyncValue.class))));
    }

    public static void getTooltipFromItemSafely(RichTooltip tooltipBuilder, ItemStack itemStack) {
        List<String> lines = itemStack
            .getTooltip(MCHelper.getPlayer(), MCHelper.getMc().gameSettings.advancedItemTooltips);

        for (int i = 0; i < lines.size(); ++i) {
            if (i == 0) {
                lines.set(
                    i,
                    itemStack.getItem()
                        .getRarity(itemStack).rarityColor + lines.get(i));
            } else {
                lines.set(i, EnumChatFormatting.GRAY + lines.get(i));
            }
        }
        tooltipBuilder.add(lines.getFirst())
            .newLine();
        if (lines.size() > 1) {
            tooltipBuilder.spaceLine();
            int i = 1;

            for (int n = lines.size(); i < n; ++i) {
                tooltipBuilder.add(lines.get(i))
                    .newLine();
            }
        }
    }

    public static String formatValueWithUnits(long value) {
        if (value < 1000) {
            return String.valueOf(value);
        }
        int exp = (int) (Math.log(value) / Math.log(1000));
        char unit = "KMGTP".charAt(exp - 1);
        double formattedValue = value / Math.pow(1000, exp);
        if (formattedValue == Math.floor(formattedValue)) {
            return String.format(Locale.ROOT, "%.0f%c", formattedValue, unit);
        } else {
            return String.format(Locale.ROOT, "%.1f%c", formattedValue, unit);
        }
    }

    public static void teleportPlayerToMachine(DroneConnection connection, EntityPlayer player) {
        IGregTechTileEntity igt = connection.getLinkedMachine()
            .getBaseMetaTileEntity();
        if (igt == null || !(player instanceof EntityPlayerMP playerMP)) return;
        ChunkCoordinates machineCoord = connection.getMachineCoord();
        int x = machineCoord.posX;
        int y = machineCoord.posY;
        int z = machineCoord.posZ;
        int dim = connection.getMachineWorld();
        ForgeDirection facing = igt.getFrontFacing();
        double frontX = x + facing.offsetX + 0.5;
        double frontY = y + facing.offsetY;
        double frontZ = z + facing.offsetZ + 0.5;

        double dx = (x + 0.5) - frontX;
        double dy = (y + 0.5) - (frontY + playerMP.getEyeHeight());
        double dz = (z + 0.5) - frontZ;

        double d3 = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) (Math.atan2(dz, dx) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (-(Math.atan2(dy, d3) * 180.0D / Math.PI));
        if (playerMP.dimension != dim) {
            playerMP.mcServer.getConfigurationManager()
                .transferPlayerToDimension(playerMP, dim);
        }
        playerMP.playerNetServerHandler.setPlayerLocation(frontX, frontY, frontZ, yaw, pitch);
    }

    public static void syncDroneValues(PanelSyncManager syncManager, MTEDroneCentre multiblock) {
        syncManager.syncValue("onall", new InteractionSyncHandler().setOnMousePressed(var -> {
            if (!NetworkUtils.isClient()) {
                multiblock.turnOnAll();
                syncManager.getPlayer()
                    .closeScreen();
                syncManager.getPlayer()
                    .addChatComponentMessage(
                        new ChatComponentTranslation(
                            "GT5U.machines.dronecentre.turnon",
                            multiblock.group.get(multiblock.getActiveGroup())));
            }
        }));
        syncManager.syncValue("offall", new InteractionSyncHandler().setOnMousePressed(var -> {
            if (!NetworkUtils.isClient()) {
                multiblock.turnOffAll(var.shift);
                syncManager.getPlayer()
                    .closeScreen();
                syncManager.getPlayer()
                    .addChatComponentMessage(
                        new ChatComponentTranslation(
                            var.shift ? "GT5U.machines.dronecentre.forceturnoff" : "GT5U.machines.dronecentre.turnoff",
                            multiblock.group.get(multiblock.getActiveGroup())));
            }
        }));

        DroneConnectionListSyncHandler droneConnectionListSyncHandler = new DroneConnectionListSyncHandler(
            multiblock::getConnectionList);
        EnumSyncValue<SortMode, ?> sortModeSyncHandler = new EnumSyncValue<>(
            DroneCentreGuiUtil.SortMode.class,
            multiblock::getSortMode,
            multiblock::setSortMode).allowC2S();
        StringSyncValue searchFilterSyncHandler = new StringSyncValue(
            multiblock::getSearchBarText,
            multiblock::setSearchBarText).allowC2S();
        StringSyncValue productionSearchFilterSyncHandler = new StringSyncValue(
            multiblock::getProductionSearchFilter,
            multiblock::setProductionSearchFilter).allowC2S();
        IntSyncValue activeGroupSyncHandler = new IntSyncValue(multiblock::getActiveGroup, multiblock::setActiveGroup)
            .allowC2S();
        BooleanSyncValue searchOriSyncHandler = new BooleanSyncValue(
            multiblock::getSearchOriginalName,
            multiblock::setSearchOriginalName).allowC2S();
        BooleanSyncValue editModeSyncHandler = new BooleanSyncValue(multiblock::getEditMode, multiblock::setEditMode)
            .allowC2S();
        BooleanSyncValue updateSyncHandler = new BooleanSyncValue(multiblock::shouldUpdate, multiblock::setUpdate)
            .allowC2S();
        BooleanSyncValue renamingActiveGroupSyncHandler = new BooleanSyncValue(
            multiblock::getRenamingActiveGroup,
            multiblock::setRenamingActiveGroup).allowC2S();

        GenericListSyncHandler<String> groupSyncHandler = new GenericListSyncHandler<>(
            () -> multiblock.group,
            null,
            packetBuffer -> packetBuffer.readStringFromBuffer(32768),
            PacketBuffer::writeStringToBuffer,
            String::equals,
            null);

        syncManager.syncValue("droneList", droneConnectionListSyncHandler);
        syncManager.syncValue("sortMode", sortModeSyncHandler);
        syncManager.syncValue("searchFilter", searchFilterSyncHandler);
        syncManager.syncValue("productionSearchFilter", productionSearchFilterSyncHandler);
        syncManager.syncValue("groupNameList", groupSyncHandler);
        syncManager.syncValue("activeGroup", activeGroupSyncHandler);
        syncManager.syncValue("searchOri", searchOriSyncHandler);
        syncManager.syncValue("editMode", editModeSyncHandler);
        syncManager.syncValue("update", updateSyncHandler);
        syncManager.syncValue("renamingActiveGroup", renamingActiveGroupSyncHandler);
        syncManager.syncValue("addNewGroup", new InteractionSyncHandler().setOnMousePressed(var -> {
            if (!NetworkUtils.isClient()) {
                multiblock.addNewGroup();
                syncManager.findSyncHandler("groupNameList", GenericListSyncHandler.class)
                    .notifyUpdate();
            }
        }));
        syncManager.syncValue("deleteGroup", new InteractionSyncHandler().setOnMousePressed(var -> {
            if (!NetworkUtils.isClient()) {
                multiblock.deleteGroup(multiblock.getActiveGroup());
                multiblock.setActiveGroup(0);
                syncManager.findSyncHandler("activeGroup", IntSyncValue.class)
                    .notifyUpdate();
                syncManager.findSyncHandler("groupNameList", GenericListSyncHandler.class)
                    .notifyUpdate();
                syncManager.findSyncHandler("droneList", DroneConnectionListSyncHandler.class)
                    .notifyUpdate();
            }
        }));
        syncManager.syncValue("toggleSelectAll", new InteractionSyncHandler().setOnMousePressed(var -> {
            if (!NetworkUtils.isClient()) {
                int activeGroup = multiblock.getActiveGroup();
                if (activeGroup == 0) return;
                List<DroneConnection> allConnections = multiblock.getConnectionList();
                if (allConnections == null || allConnections.isEmpty()) return;
                List<DroneConnection> filtered = allConnections.stream()
                    .filter(conn -> matchesSearchFilter(conn, multiblock))
                    .toList();
                if (filtered.isEmpty()) return;
                long groupBit = 1L << activeGroup;
                boolean allInGroup = filtered.stream()
                    .allMatch(conn -> (conn.getGroupMask() & groupBit) != 0);
                for (DroneConnection conn : filtered) {
                    long mask = conn.getGroupMask();
                    if (allInGroup) {
                        conn.setGroupMask(mask & ~groupBit);
                    } else {
                        conn.setGroupMask(mask | groupBit);
                    }
                }
                syncManager.findSyncHandler("droneList", DroneConnectionListSyncHandler.class)
                    .notifyUpdate();
            }
        }));

        IntSyncValue selectTimeSyncHandler = new IntSyncValue(multiblock::getSelectedTime, multiblock::setSelectedTime)
            .allowC2S();
        ProductionStatsSyncHandler productionStatsSyncHandler = new ProductionStatsSyncHandler(
            () -> multiblock.productionDataRecorder.getStatsInDuration(multiblock.getSelectedTime() * 1000L))
                .allowC2S();
        syncManager.syncValue("selectTime", selectTimeSyncHandler);
        syncManager.syncValue("productionStats", productionStatsSyncHandler);
    }

    private static final int SHORT_RECIPE_MAX_TICKS = 2 * TickTime.SECOND;

    /** WAILA formatting */
    private static final Pattern STRAY_WAILA_CHARACTERS = Pattern.compile(
        "[" + SpecialChars.WailaStyle
            + SpecialChars.WailaIcon
            + SpecialChars.WailaRenderer
            + SpecialChars.WailaRendererComma
            + "\u0001-\u0004]");

    public static String cleanWailaLine(String line) {
        if (line == null) return "";

        int contentStart = 0;
        while (contentStart < line.length()) {
            char c = line.charAt(contentStart);
            if (c == ' ' || c == ' ') {
                contentStart++;
            } else if (line.startsWith(SpecialChars.MCStyle, contentStart) && contentStart + 1 < line.length()) {
                contentStart += 2;
            } else {
                break;
            }
        }
        String prefix = line.substring(0, contentStart);
        String content = line.substring(contentStart);

        Matcher renderer = SpecialChars.patternRender.matcher(content);
        if (renderer.matches()) {
            String rendererText = rendererToText(
                renderer.group("name"),
                renderer.group("args")
                    .split(SpecialChars.WailaRendererComma));
            if (rendererText != null) {
                return prefix + rendererText;
            }
        }

        content = SpecialChars.patternRender.matcher(content)
            .replaceAll("");
        content = SpecialChars.patternWaila.matcher(content)
            .replaceAll("");
        content = STRAY_WAILA_CHARACTERS.matcher(content)
            .replaceAll("");
        return prefix + content.trim();
    }

    private static String rendererToText(String rendererName, String[] args) {
        try {
            return switch (rendererName) {
                case "waila.gt.progress" -> formatProgress(Long.parseLong(args[0]), Long.parseLong(args[1]));
                case "waila.stack" -> formatStack(args);
                default -> null;
            };
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    private static String formatProgress(long progressTicks, long maxProgressTicks) {
        String progressText;
        if (maxProgressTicks <= SHORT_RECIPE_MAX_TICKS) {
            progressText = StatCollector
                .translateToLocalFormatted("GT5U.waila.machine.progress_tick", progressTicks, maxProgressTicks);
        } else {
            double ratio = (double) progressTicks / maxProgressTicks;
            progressText = StatCollector.translateToLocalFormatted(
                "GT5U.waila.machine.in_progress",
                (double) progressTicks / TickTime.SECOND,
                (double) maxProgressTicks / TickTime.SECOND,
                Math.round(ratio * 1000) / 10.0);
        }
        return StatCollector.translateToLocalFormatted("GT5U.gui.text.drone_progress", progressText);
    }

    private static String formatStack(String[] args) {
        String registryName = args[1];
        int amount = Integer.parseInt(args[2]);
        int meta = Integer.parseInt(args[3]);
        ItemStack stack = switch (args[0]) {
            case "0" -> Block.blockRegistry.getObject(registryName) instanceof Block block
                ? new ItemStack(block, amount, meta)
                : null;
            case "1" -> Item.itemRegistry.getObject(registryName) instanceof Item item
                ? new ItemStack(item, amount, meta)
                : null;
            default -> null;
        };
        if (stack == null) {
            return null;
        }
        return StatCollector
            .translateToLocalFormatted("GT5U.gui.text.drone_output_item", stack.getDisplayName(), amount);
    }

    public static boolean matchesSearchFilter(DroneConnection conn, MTEDroneCentre centre) {
        String searchText = centre.getSearchBarText();
        if (searchText == null || searchText.isEmpty()) return true;
        String searchTextLower = searchText.toLowerCase();

        String customName = conn.getCustomName();
        String customNameLower = customName == null ? "" : customName.toLowerCase();
        if (customNameLower.contains(searchTextLower)) {
            return true;
        }
        if (centre.getSearchOriginalName()) {
            String locName = conn.getLocalizedName();
            String locNameLower = locName == null ? "" : locName.toLowerCase();
            return locNameLower.contains(searchTextLower);
        }
        return false;
    }

    public enum SortMode {
        DISTANCE,
        NAME,
        STATUS
    }
}
