package gregtech.common.items;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.V;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.world.BlockEvent;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.gtnhlib.GTNHLib;
import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import appeng.api.implementations.items.IAEWrench;
import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crazypants.enderio.api.tool.ITool;
import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.ToolboxSlot;
import gregtech.api.interfaces.IDamagableItem;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.interfaces.item.IPickBlockHandler;
import gregtech.api.items.GTGenericItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.modularui2.ToolboxSelectGuiFactory;
import gregtech.api.net.GTPacketToolboxEvent;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.item.ToolboxInventoryGui;
import gregtech.common.items.toolbox.ToolboxElectricManager;
import gregtech.common.items.toolbox.ToolboxItemStackHandler;
import gregtech.common.items.toolbox.ToolboxPickBlockDecider;
import gregtech.common.items.toolbox.ToolboxUtil;
import gregtech.crossmod.backhand.Backhand;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import mods.railcraft.api.core.items.IToolCrowbar;
import mrtjp.projectred.api.IScrewdriver;

@InterfaceList(
    value = { @Interface(iface = "mods.railcraft.api.core.items.IToolCrowbar", modid = Mods.ModIDs.RAILCRAFT),
        @Interface(iface = "buildcraft.api.tools.IToolWrench", modid = Mods.ModIDs.BUILD_CRAFT_CORE),
        @Interface(iface = "crazypants.enderio.api.tool.ITool", modid = Mods.ModIDs.ENDER_I_O),
        @Interface(iface = "mrtjp.projectred.api.IScrewdriver", modid = Mods.ModIDs.PROJECT_RED_CORE), })
public class ItemGTToolbox extends GTGenericItem implements IGuiHolder<PlayerInventoryGuiData>, ISpecialElectricItem,
    IPickBlockHandler, IDamagableItem, IToolCrowbar, IToolWrench, ITool, IScrewdriver, IAEWrench {

    public static final String CONTENTS_KEY = "gt5u.toolbox:Contents";
    public static final String TOOLBOX_OPEN_KEY = "gt5u.toolbox:ToolboxOpen";
    public static final String CURRENT_TOOL_KEY = "gt5u.toolbox:SelectedSlot";
    public static final String RECENTLY_BROKEN_SLOT_KEY = "gt5u.toolbox:RecentlyBroken";
    public static final String BROKEN_TOOL_ANIMATION_END_KEY = "gt5u.toolbox:BrokenToolAnimationEnd";
    public static final int NO_TOOL_SELECTED = -1;

    /**
     * The charging mechanic of the toolbox happens every one in CHARGE_TICK ticks. All numbers are multiplied by this
     * factor, so this value can be freely changed without having to do anything else. All tier limits imposed by the
     * battery and items being charged will be respected.
     */
    private static final int CHARGE_TICK = 20;
    private static final long BROKEN_TOOL_ANIMATION_LENGTH = 60;
    private static final long GUI_OPEN_DELAY = 5;

    public ItemGTToolbox(final String aUnlocalized, final String aEnglish, final String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        setMaxStackSize(1);
        setNoRepair();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(final IIconRegister iconRegister) {
        super.registerIcons(iconRegister);
        for (ToolboxSlot slot : ToolboxSlot.TOOL_SLOTS) {
            slot.registerIcon(iconRegister);
        }
    }

    @Override
    public void onUpdate(final ItemStack toolbox, final World world, final Entity entity, final int timer, final boolean isInHand) {
        if (world.isRemote) {
            return;
        }

        if (entity instanceof final EntityPlayerMP player) {
            final ToolboxItemStackHandler handler = new ToolboxItemStackHandler(toolbox);
            boolean shouldUpdate = false;

            // If the tool being used has a very bad mining speed, it's possible for it to not be able to finish mining
            // a block because the equipped stack changes. We can, after a fashion, check to see if the tool is
            // currently being used by checking the arm swing animation.
            if (isInHand && player.ticksExisted % CHARGE_TICK == 0 && ToolboxUtil.canCharge(toolbox) && !player.isSwingInProgress) {
                final ItemStack battery = handler.extractItem(ToolboxSlot.BATTERY.getSlotID(), 1, true);
                if (battery != null && battery.getItem() instanceof final IElectricItem batteryItem) {
                    shouldUpdate = getElectricManager(battery).map(batteryManager -> {
                        boolean dirty = false;
                        double remainingCharge = batteryManager.discharge(
                            battery,
                            getMaxVoltage(batteryItem.getTier(battery)) * CHARGE_TICK,
                            Integer.MAX_VALUE,
                            true,
                            true,
                            true
                        );

                        for (final ToolboxSlot slot : ToolboxSlot.values()) {
                            if (slot == ToolboxSlot.BATTERY) {
                                continue;
                            }

                            if (remainingCharge <= 0) {
                                break;
                            }
                            final double availableCharge = remainingCharge;

                            final ItemStack slotStack = handler.extractItem(slot.getSlotID(), 1, true);
                            if (slotStack == null || !(slotStack.getItem() instanceof final IElectricItem slotItem)) {
                                continue;
                            }

                            final double powerUsed = getElectricManager(slotStack).map(slotManager -> slotManager.charge(
                                slotStack,
                                (int) Math.min(availableCharge, getMaxVoltage(slotItem.getTier(slotStack)) * CHARGE_TICK),
                                Integer.MAX_VALUE,
                                true,
                                false)).orElse(0d);

                            if (powerUsed > 0) {
                                batteryManager.discharge(battery, powerUsed, Integer.MAX_VALUE, true, true, false);
                                remainingCharge -= powerUsed;
                                dirty = true;
                                handler.setStackInSlot(slot.getSlotID(), slotStack);
                            }
                        }

                        if (dirty) {
                            handler.setStackInSlot(ToolboxSlot.BATTERY.getSlotID(), battery);
                        }

                        return dirty;
                    }).orElse(false);
                }
            }

            if (shouldUpdate) {
                ToolboxUtil.saveToolbox(toolbox, handler);
            }

            if (!toolbox.hasTagCompound()) {
                toolbox.setTagCompound(new NBTTagCompound());
            }
            final NBTTagCompound tag = toolbox.getTagCompound();

            // Handle broken tool animation
            if (tag.hasKey(RECENTLY_BROKEN_SLOT_KEY)) {
                if (tag.getBoolean(TOOLBOX_OPEN_KEY) || (tag.hasKey(CURRENT_TOOL_KEY) && tag.getInteger(CURRENT_TOOL_KEY) != NO_TOOL_SELECTED)) {
                    tag.removeTag(BROKEN_TOOL_ANIMATION_END_KEY);
                    tag.removeTag(RECENTLY_BROKEN_SLOT_KEY);
                } else if (!tag.hasKey(BROKEN_TOOL_ANIMATION_END_KEY)) {
                    tag.setLong(BROKEN_TOOL_ANIMATION_END_KEY, player.getEntityWorld().getTotalWorldTime() + BROKEN_TOOL_ANIMATION_LENGTH);
                } else if (tag.getLong(BROKEN_TOOL_ANIMATION_END_KEY) < world.getTotalWorldTime()) {
                    tag.removeTag(BROKEN_TOOL_ANIMATION_END_KEY);
                    tag.removeTag(RECENTLY_BROKEN_SLOT_KEY);
                }
            }
        }
    }

    @Override
    public ItemStack onItemRightClick(final ItemStack toolbox, final World world, final EntityPlayer player) {
        if (canOpenInventoryGui(toolbox, world)) {
            if (toolbox == Backhand.getOffhandItem(player)) {
                GuiFactories.playerInventory()
                    .openFromPlayerInventory(player, Backhand.getOffhandSlot(player));
            } else {
                GuiFactories.playerInventory()
                    .openFromMainHand(player);
            }
        }

        return super.onItemRightClick(toolbox, world, player);
    }

    @Override
    public boolean onItemUseFirst(final ItemStack toolbox, final EntityPlayer player, final World world, final int x,
        final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ) {
        return ToolboxUtil.getSelectedToolType(toolbox)
            .map(slot -> {
                final ToolboxItemStackHandler handler = new ToolboxItemStackHandler(toolbox);
                final ItemStack tool = handler.getStackInSlot(slot.getSlotID());
                boolean result = false;

                if (tool != null && tool.getItem() != null) {
                    result = tool.getItem()
                        .onItemUseFirst(tool, player, world, x, y, z, side, hitX, hitY, hitZ);
                    ToolboxUtil.saveItemInside(toolbox, tool, slot);
                }

                return result;
            })
            .orElse(false);
    }

    @Override
    public String getItemStackDisplayName(final ItemStack toolbox) {
        final String base = super.getItemStackDisplayName(toolbox);

        return ToolboxUtil.getSelectedToolType(toolbox)
            .map(slot -> {
                final ToolboxItemStackHandler handler = new ToolboxItemStackHandler(toolbox);
                final String toolName = StatCollector.translateToLocal("GT5U.gui.text.toolbox.slot_title." + slot.name().toLowerCase());
                final Optional<ItemStack> potentialTool = handler.getCurrentTool();
                final byte toolMode = potentialTool.map(MetaGeneratedTool::getToolMode).orElse((byte) 0);

                //noinspection SimplifyOptionalCallChains
                return toolMode > 0
                    ? StatCollector.translateToLocalFormatted(
                    "GT5U.item.toolbox.name_template.mode",
                    base,
                    toolName,
                    potentialTool.map(currentTool -> currentTool.getItem() instanceof final MetaGeneratedTool mgToolItem
                        ? mgToolItem.getToolModeName(currentTool)
                        : "").orElse(""))
                    : StatCollector
                    .translateToLocalFormatted("GT5U.item.toolbox.name_template", base, toolName);
            })
            .orElse(base);

    }

    @Override
    public void addInformation(final ItemStack toolbox, final EntityPlayer player, final List<String> tooltipList,
        final boolean f3mode) {
        final Optional<ToolboxSlot> selectedToolType = ToolboxUtil.getSelectedToolType(toolbox);

        final GameSettings settings = Minecraft.getMinecraft().gameSettings;

        // noinspection SimplifyOptionalCallChains
        if (!selectedToolType.isPresent()) {
            tooltipList.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.item.toolbox.tooltip.open_toolbox",
                    GameSettings.getKeyDisplayString(settings.keyBindUseItem.getKeyCode())));
        }

        tooltipList.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.item.toolbox.tooltip.select_tool",
                GameSettings.getKeyDisplayString(settings.keyBindPickBlock.getKeyCode())));

        if (selectedToolType.isPresent()) {
            // noinspection OptionalGetWithoutIsPresent
            final ItemStack tool = ToolboxUtil.getSelectedTool(toolbox)
                .get();
            final long maxDamage = MetaGeneratedTool.getToolMaxDamage(tool);

            tooltipList.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.item.toolbox.tooltip.deselect_tool",
                    GameSettings.getKeyDisplayString(settings.keyBindPickBlock.getKeyCode())));
            tooltipList.add(
                StatCollector.translateToLocalFormatted(
                    "gt.behaviour.switch_mode.tooltip",
                    GameSettings.getKeyDisplayString(GTMod.proxy.TOOL_MODE_SWITCH_KEYBIND.getKeyCode())));
            tooltipList.add(
                EnumChatFormatting.WHITE
                    + translateToLocalFormatted(
                        "gt.item.desc.durability",
                        EnumChatFormatting.GREEN + formatNumber(maxDamage - MetaGeneratedTool.getToolDamage(tool))
                            + " ",
                        " " + formatNumber(maxDamage))
                    + EnumChatFormatting.GRAY);
        }

        ToolboxUtil.withBatteryAndManager(toolbox, (battery, manager) -> {
            final IElectricItem batteryItem = Objects.requireNonNull((IElectricItem) battery.getItem());
            final int voltageTier = GTUtility.clamp(batteryItem.getTier(battery), 0, V.length - 1);

            tooltipList.add(
                EnumChatFormatting.AQUA + StatCollector.translateToLocalFormatted(
                    "gt.item.desc.eu_info",
                    formatNumber(manager.getCharge(battery)),
                    formatNumber(batteryItem.getMaxCharge(battery)),
                    formatNumber(V[voltageTier])));
        });

        tooltipList.addAll(
            Arrays.asList(
                translateToLocalFormatted(
                    "GT5U.item.toolbox.byline.format",
                    StatCollector.translateToLocal(
                        "GT5U.item.toolbox.byline." + selectedToolType.map(
                            slot -> slot.name()
                                .toLowerCase())
                            .orElse("closed"))).split("\\\\n")));
    }

    @Override
    public ModularPanel buildUI(final PlayerInventoryGuiData data, final PanelSyncManager syncManager,
        final UISettings settings) {
        final int slot = data.getSlotIndex();
        final ToolboxItemStackHandler stackHandler = new ToolboxItemStackHandler(data.getPlayer(), slot);

        if (data.getUsedItemStack() != null) {
            syncManager.addOpenListener(player -> {
                // Despite the Javadoc's insistence, this function only runs on the client.
                // Keeping this check in here just in case it gets fixed upstream, so it doesn't break later.
                if (player.worldObj.isRemote) {
                    GTValues.NW.sendToServer(new GTPacketToolboxEvent(GTPacketToolboxEvent.Action.UI_OPEN, slot));
                }
            })
                .addCloseListener(player -> {
                    if (!player.worldObj.isRemote) {
                        // Retrieve stack from player again. Persist the toolbox contents and allow charging again.
                        final ItemStack toolbox = player.inventory.getStackInSlot(slot);

                        ToolboxUtil.saveToolbox(toolbox, stackHandler, tag -> {
                            tag.setBoolean(TOOLBOX_OPEN_KEY, false);
                            tag.removeTag(BROKEN_TOOL_ANIMATION_END_KEY);
                            tag.removeTag(RECENTLY_BROKEN_SLOT_KEY);

                            // Unselect the active tool if it was removed from the toolbox.
                            if (tag.hasKey(CURRENT_TOOL_KEY)) {
                                final int selectedToolSlot = tag.getInteger(CURRENT_TOOL_KEY);
                                if (selectedToolSlot >= 0 && selectedToolSlot < stackHandler.getSlots()
                                    && stackHandler.getStackInSlot(selectedToolSlot) == null) {
                                    tag.removeTag(CURRENT_TOOL_KEY);
                                }
                            }
                        });

                        player.inventory.setInventorySlotContents(data.getSlotIndex(), toolbox);

                        GTUtility.sendSoundToPlayers(
                            player.worldObj,
                            SoundResource.GT_TOOLBOX_CLOSE,
                            1.0F,
                            1,
                            player.posX,
                            player.posY,
                            player.posZ);
                    }
                });
        }
        return new ToolboxInventoryGui(syncManager, data, stackHandler).build();
    }

    @Override
    public boolean doDamageToItem(final ItemStack toolbox, final int vanillaDamage) {
        if (toolbox == null || !(toolbox.getItem() instanceof ItemGTToolbox)) {
            return false;
        }

        return ToolboxUtil.getSelectedTool(toolbox).map(toolStack -> {
            if (toolStack.getItem() instanceof final MetaGeneratedTool tool) {
                final ToolboxItemStackHandler handler = new ToolboxItemStackHandler(toolbox);

                if (tool.doDamageToItem(toolStack, vanillaDamage)) {
                    handler.setCurrentTool(toolStack);
                    ToolboxUtil.saveToolbox(toolbox, handler);

                    return true;
                }
            }
            return false;
        }).orElse(false);
    }

    public boolean shouldDrawHighlightGrid(DrawBlockHighlightEvent event) {
        if (event.currentItem == null || !(event.currentItem.getItem() instanceof ItemGTToolbox)) {
            return false;
        }

        return !ToolboxPickBlockDecider.getSuggestedTool(event)
            .isEmpty();
    }

    /**
     * Imposes a small delay between breaking a tool inside and opening the GUI. If a user breaks a tool via methods
     * other than breaking a block (e.g.: connecting pipes with a wrench), it can open the GUI immediately after the
     * tool breaks because the user has the use item bind depressed.
     *
     * @param toolbox The item stack of the toolbox
     * @param world   The world where the GUI is opening
     * @return true if it isn't a few ticks immediately after breaking a tool
     */
    private static boolean canOpenInventoryGui(final ItemStack toolbox, final World world) {
        final NBTTagCompound tag = toolbox.hasTagCompound() ? toolbox.getTagCompound() : new NBTTagCompound();
        final boolean recentlyBrokenTool = tag.hasKey(RECENTLY_BROKEN_SLOT_KEY);

        if (recentlyBrokenTool && (!tag.hasKey(BROKEN_TOOL_ANIMATION_END_KEY)
            || tag.getLong(BROKEN_TOOL_ANIMATION_END_KEY) - world.getTotalWorldTime() <= GUI_OPEN_DELAY)) {
            return false;
        }

        // noinspection SimplifyOptionalCallChains
        return !world.isRemote && !ToolboxUtil.getSelectedToolType(toolbox)
            .isPresent();
    }

    // region Event Handlers

    @Override
    public boolean onPickBlock(final ItemStack toolbox, final EntityPlayer player) {
        final int inventorySlot = player.inventory.getCurrentItem() == toolbox ? player.inventory.currentItem
            : Backhand.getOffhandSlot(player);
        final Optional<ToolboxSlot> selectedToolType = ToolboxUtil.getSelectedToolType(toolbox);

        if (player.isSneaking()) {
            if (selectedToolType.isPresent()) {
                sendChangeToolPacket(inventorySlot, NO_TOOL_SELECTED);
            } else {
                ToolboxSelectGuiFactory.INSTANCE.open(player);
            }

            return true;
        }

        final ToolboxItemStackHandler handler = new ToolboxItemStackHandler(toolbox);

        for (ToolboxSlot suggested : ToolboxPickBlockDecider.getSuggestedTool(player)) {
            if (handler.getStackInSlot(suggested.getSlotID()) != null) {
                sendChangeToolPacket(inventorySlot, suggested.getSlotID());
                return true;
            }
        }

        int toolCount = 0;
        int lastSlot = -1;
        for (ToolboxSlot slot : ToolboxSlot.TOOL_SLOTS) {
            if (handler.getStackInSlot(slot.getSlotID()) != null) {
                toolCount++;
                lastSlot = slot.getSlotID();
            }
        }

        switch (toolCount) {
            case 0:
                GTNHLib.proxy.printMessageAboveHotbar(
                    StatCollector.translateToLocal("GT5U.gui.text.toolbox.error.no_tools"),
                    120,
                    true,
                    true);
                break;
            case 1:
                GTValues.NW.sendToServer(
                    new GTPacketToolboxEvent(
                        GTPacketToolboxEvent.Action.CHANGE_ACTIVE_TOOL,
                        inventorySlot,
                        selectedToolType.isPresent() ? lastSlot : NO_TOOL_SELECTED));
                return true;
            default:
                ToolboxSelectGuiFactory.INSTANCE.open(player);
                return true;
        }

        return false;
    }

    /**
     * Handler for tool mode switch keybind. The toolbox delegates this action to the currently selected tool, switching
     * its mode while still inside.
     *
     * @param player  The player doing the switching
     * @param keybind The keybind responsible for triggering this action
     * @param keyDown true if the key is depressed
     */
    public static void switchToolMode(EntityPlayerMP player, @SuppressWarnings("unused") SyncedKeybind keybind,
        boolean keyDown) {
        if (!keyDown) {
            return;
        }

        getToolboxIfEquipped(player).ifPresent(toolboxStack -> {
            if (toolboxStack.hasTagCompound()) {
                ToolboxItemStackHandler handler = new ToolboxItemStackHandler(toolboxStack);
                handler.mutateCurrentTool(MetaGeneratedTool::switchToolMode);
                ToolboxUtil.saveToolbox(toolboxStack, handler);
            }
        });
    }

    @SubscribeEvent
    public void onBlockBreakingEvent(BlockEvent.BreakEvent event) {
        getToolboxIfEquipped(event.getPlayer()).flatMap(ToolboxUtil::getSelectedTool).ifPresent(tool -> {
            if (tool.getItem() instanceof final MetaGeneratedTool toolItem) {
                IToolStats stats = toolItem.getToolStats(tool);
                if (stats != null) {
                    TileEntity tile = event.world.getTileEntity(event.x, event.y, event.z);
                    stats.onBreakBlock(event.getPlayer(), event.x, event.y, event.z, event.block, event.blockMetadata, tile, event);
                }
            }
        });
    }

    @SubscribeEvent
    public void onBlockHarvestingEvent(BlockEvent.HarvestDropsEvent aEvent) {
        getToolboxIfEquipped(aEvent.harvester).flatMap(ToolboxUtil::getSelectedTool)
            .ifPresent(tool -> {
                if ((tool.getItem() instanceof MetaGeneratedTool toolItem)) {
                    toolItem.onHarvestBlockEvent(
                        aEvent.drops,
                        tool,
                        aEvent.harvester,
                        aEvent.block,
                        aEvent.x,
                        aEvent.y,
                        aEvent.z,
                        aEvent.blockMetadata,
                        aEvent.fortuneLevel,
                        aEvent.isSilkTouching,
                        aEvent);
                }
            });
    }

    // endregion

    /**
     * Gets the currently equipped toolbox if the player is holding it in their main hand or offhand.
     *
     * @param player The player to interrogate
     * @return An optional with the toolbox's item stack, or empty if the user is not wielding a toolbox
     */
    private static Optional<ItemStack> getToolboxIfEquipped(EntityPlayer player) {
        if (player != null) {
            for (ItemStack stack : new ItemStack[] { player.inventory.getCurrentItem(),
                Backhand.getOffhandItem(player) }) {
                if (stack != null && stack.getItem() instanceof ItemGTToolbox) {
                    return Optional.of(stack);
                }
            }
        }

        return Optional.empty();
    }

    private static void sendChangeToolPacket(int inventorySlot, int newToolType) {
        GTValues.NW.sendToServer(
            new GTPacketToolboxEvent(GTPacketToolboxEvent.Action.CHANGE_ACTIVE_TOOL, inventorySlot, newToolType));
    }

    // region Vanilla Tool Harvesting Methods
    @Override
    public boolean canHarvestBlock(final Block block, final ItemStack toolbox) {
        return ToolboxUtil.getSelectedTool(toolbox).map(
            tool -> tool.getItem() instanceof final MetaGeneratedTool toolItem && toolItem.canHarvestBlock(block, tool))
            .orElse(false);
    }

    @Override
    public float getDigSpeed(final ItemStack toolbox, final Block block, final int metadata) {
        return ToolboxUtil.getSelectedTool(toolbox)
            .filter(tool -> tool.getItem() instanceof MetaGeneratedTool)
            .map(
                tool -> tool.getItem()
                    .getDigSpeed(tool, block, metadata))
            .orElse(0f);
    }

    @Override
    public EnumAction getItemUseAction(final ItemStack toolbox) {
        return ToolboxUtil.getSelectedTool(toolbox)
            .filter(tool -> tool.getItem() != null)
            .map(
                tool -> tool.getItem()
                    .getItemUseAction(tool))
            .orElse(EnumAction.none);
    }

    @Override
    public boolean onBlockDestroyed(final ItemStack toolbox, final World worldIn, final Block blockIn, final int x,
        final int y, final int z, final EntityLivingBase entity) {

        return ToolboxUtil.getSelectedToolType(toolbox)
            .map(
                toolboxSlot -> ToolboxUtil.getSelectedTool(toolbox)
                    .filter(tool -> tool.getItem() != null)
                    .map(tool -> {
                        final boolean result = tool.getItem()
                            .onBlockDestroyed(tool, worldIn, blockIn, x, y, z, entity);
                        if (result) {
                            ToolboxUtil.saveItemInside(toolbox, tool, toolboxSlot);
                        }

                        return result;
                    })
                    .orElse(false))
            .orElse(false);

    }

    @Override
    public boolean onBlockStartBreak(final ItemStack toolbox, final int x, final int y, final int z,
        final EntityPlayer player) {

        return ToolboxUtil.getSelectedTool(toolbox)
            .filter(tool -> tool.getItem() != null)
            .map(
                tool -> tool.getItem()
                    .onBlockStartBreak(tool, x, y, z, player))
            .orElse(false);
    }

    @Override
    public int getHarvestLevel(final ItemStack toolbox, final String toolClass) {
        return ToolboxUtil.getSelectedTool(toolbox)
            .filter(tool -> tool.getItem() instanceof MetaGeneratedTool)
            .map(
                tool -> tool.getItem()
                    .getHarvestLevel(tool, toolClass))
            .orElse(-1);
    }
    // endregion

    // region Electric Item Functions

    public static Optional<IElectricItemManager> getElectricManager(final ItemStack itemStack) {
        if (itemStack == null) {
            return Optional.empty();
        }

        final Item item = itemStack.getItem();

        IElectricItemManager manager = null;
        if (item instanceof final ISpecialElectricItem special) {
            manager = special.getManager(itemStack);
        } else if (item instanceof IElectricItem) {
            manager = ic2.api.item.ElectricItem.manager;
        }

        return Optional.ofNullable(manager);
    }

    private static long getMaxVoltage(int tier) {
        if (tier >= GTValues.V.length) {
            tier = GTValues.V.length - 1;
        }

        return GTValues.V[tier];
    }

    @Override
    public IElectricItemManager getManager(final ItemStack toolbox) {
        return new ToolboxElectricManager();
    }

    @Override
    public boolean canProvideEnergy(final ItemStack itemStack) {
        return false;
    }

    @Override
    public Item getChargedItem(final ItemStack itemStack) {
        return this;
    }

    @Override
    public Item getEmptyItem(final ItemStack itemStack) {
        return this;
    }

    @Override
    public double getMaxCharge(final ItemStack toolbox) {
        return ToolboxUtil.getBattery(toolbox).map(battery -> battery.getItem() instanceof final IElectricItem item ? item.getMaxCharge(battery) : 0d).orElse(0d);
    }

    @Override
    public double getTransferLimit(final ItemStack toolbox) {
        return ToolboxUtil.getBattery(toolbox).map(battery -> battery.getItem() instanceof final IElectricItem item ? item.getTransferLimit(battery) : 0d).orElse(0d);
    }

    @Override
    public int getTier(final ItemStack toolbox) {
        return ToolboxUtil.getBattery(toolbox)
            .map(battery -> {
                if (GTModHandler.isElectricItem(battery)) {
                    final IElectricItem item = (IElectricItem) battery.getItem();
                    return item == null ? null : item.getTier(battery);
                }

                return null;
            })
            .orElse(0);

    }

    // endregion

    // region Cross-mod Compatibility
    @Override
    public boolean canWrench(final ItemStack toolbox, final EntityPlayer player, final int x, final int y,
        final int z) {
        return ToolboxUtil.isToolOfType(toolbox, ToolboxSlot.WRENCH);
    }

    @Override
    public boolean canWrench(final EntityPlayer entityPlayer, final int x, final int y, final int z) {
        return getToolboxIfEquipped(entityPlayer).map(toolbox -> ToolboxUtil.isToolOfType(toolbox, ToolboxSlot.WRENCH))
            .orElse(false);
    }

    @Override
    public void wrenchUsed(final EntityPlayer entityPlayer, final int x, final int y, final int z) {}

    @Override
    public boolean canUse(final ItemStack toolbox, final EntityPlayer player, final int x, final int y, final int z) {
        return ToolboxUtil.isToolOfType(toolbox, ToolboxSlot.WRENCH);
    }

    @Override
    public void used(final ItemStack stack, final EntityPlayer player, final int x, final int y, final int z) {}

    @Override
    public boolean shouldHideFacades(final ItemStack toolbox, final EntityPlayer player) {
        return ToolboxUtil.isToolOfType(toolbox, ToolboxSlot.WRENCH);
    }

    @Override
    public boolean canWhack(final EntityPlayer player, final ItemStack toolbox, final int x, final int y, final int z) {
        return ToolboxUtil.isToolOfType(toolbox, ToolboxSlot.CROWBAR);
    }

    @Override
    public void onWhack(final EntityPlayer player, final ItemStack toolbox, final int x, final int y, final int z) {
        ToolboxUtil.damageSelectedTool(toolbox);
    }

    @Override
    public boolean canLink(final EntityPlayer player, final ItemStack toolbox, final EntityMinecart cart) {
        return player.isSneaking() && ToolboxUtil.isToolOfType(toolbox, ToolboxSlot.CROWBAR);
    }

    @Override
    public void onLink(final EntityPlayer player, final ItemStack toolbox, final EntityMinecart cart) {
        ToolboxUtil.damageSelectedTool(toolbox);
    }

    @Override
    public boolean canBoost(final EntityPlayer player, final ItemStack toolbox, final EntityMinecart cart) {
        return !player.isSneaking() && ToolboxUtil.isToolOfType(toolbox, ToolboxSlot.CROWBAR);
    }

    @Override
    public void onBoost(final EntityPlayer player, final ItemStack toolbox, final EntityMinecart cart) {
        ToolboxUtil.damageSelectedTool(toolbox);
    }

    @Override
    public boolean canUse(final EntityPlayer player, final ItemStack stack) {
        return ToolboxUtil.isToolOfType(stack, ToolboxSlot.SCREWDRIVER);
    }

    @Override
    public void damageScrewdriver(final EntityPlayer player, final ItemStack toolbox) {
        ToolboxUtil.damageSelectedTool(toolbox);
    }
    // endregion
}
