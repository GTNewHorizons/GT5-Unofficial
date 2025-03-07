package gregtech.common.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.redstone.CoverAdvancedWirelessRedstoneBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import net.minecraftforge.common.util.ForgeDirection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.PageButton;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.items.GTGenericItem;
import gregtech.api.net.PacketDebugRedstoneCover;
import gregtech.common.covers.redstone.CoverAdvancedWirelessRedstoneBase.CoverPosition;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;

public class ItemSniffer extends GTGenericItem implements IGuiHolder<GuiData> {

    public static final Logger LOGGER = LogManager.getLogger("SNIFFER");

    private static final Map<Integer, String> regularWirelessLabels = new HashMap<>();
    private static final Map<String, Map<Integer, Map<CoverPosition, String>>> advWirelessLabels = new HashMap<>();
    private String uuid;
    private String freqFilter = "";
    private String ownerFilter = "";

    public ItemSniffer(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);;
        setMaxStackSize(1);
    }

    public Map<CoverAdvancedWirelessRedstoneBase.CoverPosition, String> getCoversOnFrequency(String uuid, int frequency) {
        Map<Integer, Map<CoverPosition, String>> publicCovers = advWirelessLabels.getOrDefault(uuid, new HashMap<>());
        return publicCovers.getOrDefault(frequency, new HashMap<>());
    }

    @Override
    protected void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        aList.add("Author: §9Frosty§4Fire1");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        if (!world.isRemote) {
            this.uuid = String.valueOf(player.getUniqueID());
            this.freqFilter = "";
            loadFromNBT(stack.getTagCompound());
            GuiFactories.item()
                .open(player);

        }
        return super.onItemRightClick(stack, world, player);
    }

    public static NBTTagCompound serializeRegularToNBT(NBTTagCompound base) {
        NBTTagCompound regularWireless = new NBTTagCompound();
        regularWirelessLabels
            .forEach((frequency, label) -> { regularWireless.setString(String.valueOf(frequency), label); });
        base.setTag("Regular", regularWireless);
        return base;
    }

    public static <K, V> NBTTagCompound serializeAdvancedToNBT(NBTTagCompound base, Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue() instanceof Map mapEntry) {
                base.setTag(
                    entry.getKey()
                        .toString(),
                    serializeAdvancedToNBT(new NBTTagCompound(), mapEntry));
            } else if (entry.getKey() instanceof CoverAdvancedWirelessRedstoneBase.CoverPosition data) {
                NBTTagCompound coverTag = new NBTTagCompound();
                coverTag.setInteger("x", data.x);
                coverTag.setInteger("y", data.y);
                coverTag.setInteger("z", data.z);
                coverTag.setInteger("dim", data.dim);
                coverTag.setInteger("side", data.side);
                coverTag.setString("label", (String) entry.getValue());
                base.setTag(String.valueOf(data.hashCode()), coverTag);
            } else if (entry.getValue() instanceof String stringEntry) {
                base.setString(
                    entry.getKey()
                        .toString(),
                    stringEntry);
            } else if (entry.getValue() instanceof Integer intEntry) {
                base.setInteger(
                    entry.getKey()
                        .toString(),
                    intEntry);
            }
        }
        return base;
    }

    public void loadFromNBT(NBTTagCompound tag) {
        if (tag == null || tag.hasNoTags()) return;
        NBTTagCompound regular = tag.getCompoundTag("Regular");
        if (!regular.hasNoTags()) {
            regular.tagMap.keySet()
                .forEach(
                    frequency -> {
                        regularWirelessLabels
                            .put(Integer.valueOf((String) frequency), regular.getString(String.valueOf(frequency)));
                    });
        }
        String[] owners = { "Public", this.uuid };
        for (String owner : owners) {
            if (tag.hasKey(owner)) {
                NBTTagCompound frequencyMap = tag.getCompoundTag(owner);
                if (frequencyMap.hasNoTags()) continue;

                frequencyMap.tagMap.keySet()
                    .forEach(frequency -> {
                        NBTTagCompound coverMap = frequencyMap.getCompoundTag((String) frequency);
                        if (coverMap.hasNoTags()) return;

                        coverMap.tagMap.keySet()
                            .forEach(cover -> {
                                NBTTagCompound data = coverMap.getCompoundTag((String) cover);
                                int x = data.getInteger("x");
                                int y = data.getInteger("y");
                                int z = data.getInteger("z");
                                int dim = data.getInteger("dim");
                                int side = data.getInteger("side");
                                CoverAdvancedWirelessRedstoneBase.CoverPosition coverPosition = new CoverAdvancedWirelessRedstoneBase.CoverPosition(new ChunkCoordinates(x, y, z), dim, side);
                                Map<Integer, Map<CoverPosition, String>> frequencies = advWirelessLabels
                                    .computeIfAbsent(owner, k -> new HashMap<>());
                                Map<CoverAdvancedWirelessRedstoneBase.CoverPosition, String> covers = frequencies
                                    .computeIfAbsent(Integer.valueOf((String) frequency), k -> new HashMap<>());
                                covers.put(coverPosition, data.getString("label"));
                            });

                    });

            }
        }
    }

    @Override
    public ModularPanel buildUI(GuiData guiData, PanelSyncManager guiSyncManager) {
        PagedWidget.Controller controller = new PagedWidget.Controller();

        ModularPanel panel = ModularPanel.defaultPanel("redstone_sniffer");
        panel.flex()
            .sizeRel(0.5f, 0.75f)
            .align(Alignment.Center);

        PagedWidget data = new PagedWidget();
        data.sizeRel(1, 0.8f);
        data.controller(controller);

        // Process regular wireless redstone frequencies
        List<List<IWidget>> regularMatrix = new ArrayList<>();
        GregTechAPI.sWirelessRedstone.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                int freq = entry.getKey();
                boolean isPrivate = freq > 65535;
                int displayFreq = isPrivate ? freq - 65536 : freq;
                List<IWidget> row = new ArrayList<>();
                row.add(new Row() {

                    @Override
                    public Flow setEnabledIf(Predicate<Flow> condition) {
                        return onUpdateListener(w -> {
                            setEnabled(condition.test(w));
                            heightRel(isEnabled() ? 0.2f : 0);
                        }, true);
                    }
                }.setEnabledIf(w -> {
                    try {
                        if (displayFreq == Integer.parseInt(this.freqFilter)) {
                            return true;
                        }
                    } catch (NumberFormatException ignored) {
                        return true;
                    }
                    return false;
                })
                    .background(new Rectangle().setColor(Color.LIGHT_BLUE.main))
                    .sizeRel(1f, 0.2f)
                    .expanded()
                    .child(
                        new TextWidget(String.valueOf(displayFreq)).widthRel(1.0f / 3.0f)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget(isPrivate ? "Yes" : "No").widthRel(1.0f / 3.0f)
                            .alignment(Alignment.Center))
                    .child(
                        new TextFieldWidget().sizeRel(1.0f / 3.0f, 0.5f)
                            .value(
                                SyncHandlers
                                    .string(() -> regularWirelessLabels.getOrDefault(freq, "Description"), label -> {
                                        regularWirelessLabels.put(freq, label);
                                        if (!guiSyncManager.isClient()) {
                                            NBTTagCompound currentTag = guiSyncManager.getPlayer()
                                                .getHeldItem()
                                                .getTagCompound();
                                            if (currentTag == null) currentTag = new NBTTagCompound();
                                            NBTTagCompound tag = serializeRegularToNBT(currentTag);
                                            guiSyncManager.getPlayer()
                                                .getHeldItem()
                                                .setTagCompound(tag);
                                        }
                                    }))));
                regularMatrix.add(row);
            });

        Grid regularGrid = new Grid().minColWidth(0)
            .minRowHeight(0)
            .scrollable()
            .matrix(regularMatrix);
        regularGrid.getScrollArea()
            .setScrollDataX(null);

        data.addPage(
            new Column().child(
                new Row().heightRel(0.1f)
                    .child(
                        new TextWidget("Frequency").widthRel(1.0f / 3.0f)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget("Private").widthRel(1.0f / 3.0f)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget("Label").widthRel(1.0f / 3.0f)
                            .alignment(Alignment.Center)))
                .child(
                    new Row().heightRel(0.9f)
                        .child(regularGrid.sizeRel(1, 1f))));

        // Process advanced wireless redstone frequencies
        Map<Integer, Map<CoverAdvancedWirelessRedstoneBase.CoverPosition, Byte>> publicFreqs = GregTechAPI.sAdvancedWirelessRedstone
            .getOrDefault("null", new ConcurrentHashMap<>());
        Map<String, Map<Integer, Map<CoverAdvancedWirelessRedstoneBase.CoverPosition, Byte>>> allFreqs = GregTechAPI.sAdvancedWirelessRedstone;

        Grid advGrid = new Grid().minColWidth(0)
            .minRowHeight(0)
            .scrollable();
        advGrid.getScrollArea()
            .setScrollDataX(null);

        List<List<IWidget>> advancedMatrix = (processAdvancedFrequencies(
            publicFreqs,
            "Public",
            advGrid,
            guiSyncManager));
        UUID leader = SpaceProjectManager.getLeader(UUID.fromString(this.uuid));
        GregTechAPI.sAdvancedWirelessRedstone.keySet()
            .forEach(uuid -> {
                if (!uuid.equals("null") && SpaceProjectManager.getLeader(UUID.fromString(uuid))
                    .equals(leader)) {
                    advancedMatrix.addAll((processAdvancedFrequencies(allFreqs.getOrDefault(uuid, new ConcurrentHashMap<>()), uuid, advGrid, guiSyncManager)));
                }
            });

        advGrid.matrix(advancedMatrix);

        data.addPage(
            new Column().child(
                new Row().heightRel(0.1f)
                    .child(
                        new TextWidget("Owner").widthRel(0.15f)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget("Frequency").widthRel(0.15f)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget("Coords").widthRel(0.25f)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget("Label").widthRel(0.2f)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget("Action").widthRel(0.25f)
                            .alignment(Alignment.Center)))
                .child(
                    new Row().heightRel(0.9f)
                        .child(advGrid.sizeRel(1f))));

        panel.child(
            new Column().margin(10)
                .child(
                    new Row().heightRel(0.1f)
                        .child(
                            new PageButton(0, controller).widthRel(0.5f)
                                .align(Alignment.CenterLeft)
                                .overlay(IKey.dynamic(() -> "Regular Wireless")))
                        .child(
                            new PageButton(1, controller).widthRel(0.5f)
                                .align(Alignment.CenterRight)
                                .overlay(IKey.dynamic(() -> "Advanced Wireless"))))
                .child(
                    new Row().heightRel(0.1f)
                        .child(
                            new TextWidget("Filter frequency: ").widthRel(0.25f)
                                .alignment(Alignment.Center))
                        .child(
                            new TextFieldWidget().sizeRel(0.25f, 0.5f)
                                .value(SyncHandlers.string(() -> this.freqFilter, filter -> {
                                    this.freqFilter = filter;
                                    if (NetworkUtils.isClient()) {
                                        WidgetTree.resize(regularGrid);
                                        WidgetTree.resize(advGrid);
                                    }
                                })))
                        .child(
                            new TextWidget("Filter owner: ").widthRel(0.25f)
                                .alignment(Alignment.Center))
                        .child(
                            new TextFieldWidget().sizeRel(0.25f, 0.5f)
                                .value(SyncHandlers.string(() -> this.ownerFilter, ownerFilter -> {
                                    this.ownerFilter = ownerFilter;
                                    if (NetworkUtils.isClient()) {
                                        WidgetTree.resize(advGrid);
                                    }
                                }))))
                .child(data));

        return panel;
    }

    public List<List<IWidget>> processAdvancedFrequencies(Map<Integer, Map<CoverPosition, Byte>> frequencyMap, String owner,
                                                          Grid grid, PanelSyncManager guiSyncManager) {

        String ownerString = owner.equals("Public") ? "Public"
            : SpaceProjectManager.getPlayerNameFromUUID(UUID.fromString(owner));
        AtomicInteger size = new AtomicInteger();
        frequencyMap.forEach((k, v) -> size.addAndGet(v.size()));
        List<List<IWidget>> result = new ArrayList<>();
        frequencyMap.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                int freq = entry.getKey();
                Map<CoverPosition, Byte> coverMap = entry.getValue();
                coverMap.forEach((cover, useless) -> {
                    List<IWidget> row = new ArrayList<>();


                    row.add(new Row() {
                        @Override
                        public Flow setEnabledIf(Predicate<Flow> condition) {
                            return onUpdateListener(w -> {
                                setEnabled(condition.test(w));
                                heightRel(isEnabled() ? 0.2f : 0);
                            }, true);
                        }
                    }.setEnabledIf(w -> {
                        try {
                            if (this.ownerFilter.replaceAll(" ", "")
                                .isEmpty() || this.ownerFilter.equals(ownerString)) {
                                if (entry.getKey() == Integer.parseInt(this.freqFilter)) {
                                    return true;
                                }
                            } else {
                                return false;
                            }

                        } catch (NumberFormatException ignored) {
                            return true;
                        }
                        return false;
                    })
                        .background(new Rectangle().setColor(Color.LIGHT_BLUE.main))
                        .sizeRel(1f, 0.2f)
                        .expanded()
                        .child(
                            new TextWidget(ownerString).widthRel(0.15f)
                                .alignment(Alignment.Center))
                        .child(
                            new TextWidget(String.valueOf(freq)).widthRel(0.15f)
                                .alignment(Alignment.Center))
                        .child(
                            new TextWidget(cover.getInfo()).widthRel(0.25f)
                                .alignment(Alignment.Center))
                        .child(
                            new TextFieldWidget().sizeRel(0.2f, 0.5f)
                                .value(SyncHandlers.string(() -> {
                                    Map<CoverAdvancedWirelessRedstoneBase.CoverPosition, String> coversOnFrequency = getCoversOnFrequency(owner, freq);
                                    return coversOnFrequency.getOrDefault(cover, "Description");
                                }, label -> {
                                    if (!guiSyncManager.isClient()) {
                                        Map<Integer, Map<CoverAdvancedWirelessRedstoneBase.CoverPosition, String>> frequencies = advWirelessLabels
                                            .computeIfAbsent(owner, k -> new HashMap<>());
                                        Map<CoverPosition, String> covers = frequencies
                                            .computeIfAbsent(freq, k -> new HashMap<>());
                                        covers.put(cover, label);
                                        NBTTagCompound tag = serializeAdvancedToNBT(
                                            new NBTTagCompound(),
                                            advWirelessLabels);
                                        guiSyncManager.getPlayer()
                                            .getHeldItem()
                                            .setTagCompound(tag);
                                    }

                                })))
                        .child(
                            new ButtonWidget<>().widthRel(0.11f)
                                .marginLeft(5)
                                .overlay(IKey.str("Locate"))
                                .onMousePressed(mouseButton -> {
                                    LOGGER.debug(
                                        "Locating cover " + cover.getInfo()
                                            + " For player "
                                            + guiSyncManager.getPlayer()
                                                .getDisplayName());
                                    GTValues.NW.sendToServer(
                                        new PacketDebugRedstoneCover(cover.dim, cover.x, cover.y, cover.z, false));
                                    grid.getPanel()
                                        .closeIfOpen(false);
                                    return true;
                                }))
                        .child(
                            new ButtonWidget<>().widthRel(0.11f)
                                .marginLeft(5)
                                .overlay(IKey.str("Teleport"))
                                .onMousePressed(mouseButton -> {
                                    LOGGER.debug(
                                        "Teleporting player " + guiSyncManager.getPlayer()
                                            .getDisplayName() + " to " + cover.getInfo());
                                    GTValues.NW.sendToServer(
                                        new PacketDebugRedstoneCover(cover.dim, cover.x, cover.y, cover.z, true));
                                    grid.getPanel()
                                        .closeIfOpen(false);
                                    return true;
                                })));
                    result.add(row);
                });

            });
        return result;
    }

}
