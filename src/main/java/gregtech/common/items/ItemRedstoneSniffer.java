package gregtech.common.items;

import static gregtech.api.enums.Mods.GregTech;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.PageButton;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.items.GTGenericItem;
import gregtech.api.net.PacketDebugRedstoneCover;
import gregtech.common.covers.CoverPosition;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;

public class ItemRedstoneSniffer extends GTGenericItem implements IGuiHolder<GuiData> {

    private String freqFilter = "";
    private String ownerFilter = "";
    private int lastPage = 0;
    private boolean isOP = false;

    public ItemRedstoneSniffer(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);;
        setMaxStackSize(1);
    }

    @Override
    protected void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        aList.add("Author: §9Frosty§4Fire1");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        if (!world.isRemote) {
            this.freqFilter = "";
            this.ownerFilter = "";
            GuiFactories.item()
                .open(player);

        }
        return super.onItemRightClick(stack, world, player);
    }

    @Override
    public ModularPanel buildUI(GuiData guiData, PanelSyncManager guiSyncManager) {
        PagedWidget.Controller controller = new PagedWidget.Controller() {

            @Override
            public void setPage(int page) {
                super.setPage(page);
                lastPage = page;
            }
        };

        BooleanSyncValue playerIsOP = new BooleanSyncValue(() -> false, () -> {
            EntityPlayerMP player = (EntityPlayerMP) guiData.getPlayer();
            boolean result = player.mcServer.getConfigurationManager()
                .func_152596_g(player.getGameProfile());
            isOP = result;
            return result;
        });

        ModularPanel panel = ModularPanel.defaultPanel("redstone_sniffer");
        panel.flex()
            .sizeRel(0.5f, 0.75f)
            .align(Alignment.Center);

        PagedWidget data = new PagedWidget() {

            @Override
            public void afterInit() {
                setPage(lastPage);
            }
        };
        data.sizeRel(1, 0.7f);
        data.controller(controller);
        // Process regular wireless redstone frequencies
        List<IWidget> regularList = new ArrayList<>();
        GregTechAPI.sWirelessRedstone.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                int freq = entry.getKey();
                boolean isPrivate = freq > 65535;
                int displayFreq = isPrivate ? freq - 65536 : freq;
                regularList.add(new Row().setEnabledIf(w -> {
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
                    .sizeRel(1f, 0.3f)
                    .expanded()
                    .child(
                        new TextWidget(String.valueOf(displayFreq)).widthRel(0.5f)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget(isPrivate ? "Yes" : "No").widthRel(0.5f)
                            .alignment(Alignment.Center)));
            });

        ListWidget regularListWidget = new ListWidget<>();
        regularListWidget.sizeRel(1);
        regularList.forEach(regularListWidget::child);

        data.addPage(
            new Column().child(
                new Row().heightRel(0.1f)
                    .child(
                        new TextWidget("Frequency").widthRel(0.5f)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget("Private").widthRel(0.5f)
                            .alignment(Alignment.Center)))
                .child(
                    new Row().heightRel(0.9f)
                        .child(regularListWidget)));

        // Process advanced wireless redstone frequencies
        Map<String, Map<CoverPosition, Byte>> publicFreqs = GregTechAPI.sAdvancedWirelessRedstone
            .getOrDefault("null", new ConcurrentHashMap<>());
        Map<String, Map<String, Map<CoverPosition, Byte>>> allFreqs = GregTechAPI.sAdvancedWirelessRedstone;

        ListWidget advancedListWidget = new ListWidget();
        advancedListWidget.sizeRel(1);
        List<IWidget> advancedList = (processAdvancedFrequencies(publicFreqs, "Public", advancedListWidget, isOP));

        UUID leader = SpaceProjectManager.getLeader(
            guiData.getPlayer()
                .getUniqueID());
        GregTechAPI.sAdvancedWirelessRedstone.keySet()
            .forEach(uuid -> {
                if (!uuid.equals("null") && SpaceProjectManager.getLeader(UUID.fromString(uuid))
                    .equals(leader)) {
                    advancedList.addAll(
                        (processAdvancedFrequencies(
                            allFreqs.getOrDefault(uuid, new ConcurrentHashMap<>()),
                            uuid,
                            advancedListWidget,
                            isOP)));
                }
            });

        advancedList.forEach(advancedListWidget::child);

        data.addPage(
            new Column().child(
                new Row().heightRel(0.1f)
                    .child(
                        new TextWidget("Owner").widthRel(0.15f)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget("Frequency").widthRel(0.35f)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget("Coords").widthRel(0.25f)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget("Action").widthRel(0.25f)
                            .alignment(Alignment.Center)))
                .child(
                    new Row().heightRel(0.9f)
                        .child(advancedListWidget)));

        panel.child(
            new Column().margin(10)
                .child(
                    new Row().heightRel(0.1f)
                        .marginBottom(10)
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
                        .marginBottom(10)
                        .child(
                            new TextWidget("Frequency: ").widthRel(0.25f)
                                .alignment(Alignment.Center))
                        .child(
                            new TextFieldWidget().sizeRel(0.25f, 0.5f)
                                .value(SyncHandlers.string(() -> this.freqFilter, filter -> {
                                    this.freqFilter = filter;
                                    if (NetworkUtils.isClient()) {
                                        WidgetTree.resize(regularListWidget); // The scroll area doesn't update
                                                                              // automatically
                                        WidgetTree.resize(advancedListWidget);
                                    }
                                })))
                        .child(
                            new TextWidget("Owner: ").widthRel(0.25f)
                                .alignment(Alignment.Center))
                        .child(
                            new TextFieldWidget().sizeRel(0.25f, 0.5f)
                                .value(SyncHandlers.string(() -> this.ownerFilter, ownerFilter -> {
                                    this.ownerFilter = ownerFilter;
                                    if (NetworkUtils.isClient()) {
                                        WidgetTree.resize(advancedListWidget);
                                    }
                                }))))
                .child(data));
        return panel;
    }

    public List<IWidget> processAdvancedFrequencies(Map<String, Map<CoverPosition, Byte>> frequencyMap, String owner,
        ListWidget listWidget, boolean playerIsOP) {

        String ownerString = owner.equals("Public") ? "Public"
            : SpaceProjectManager.getPlayerNameFromUUID(UUID.fromString(owner));
        AtomicInteger size = new AtomicInteger();
        frequencyMap.forEach((k, v) -> size.addAndGet(v.size()));
        List<IWidget> result = new ArrayList<>();
        frequencyMap.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                String freq = entry.getKey();
                Map<CoverPosition, Byte> coverMap = entry.getValue();
                coverMap.forEach((cover, useless) -> {
                    result.add(
                        new Row()
                            .setEnabledIf(
                                w -> (this.ownerFilter.isEmpty() || this.ownerFilter.equals(ownerString))
                                    && entry.getKey()
                                        .contains(this.freqFilter))
                            .background(new Rectangle().setColor(Color.LIGHT_BLUE.main))
                            .sizeRel(1f, 0.3f)
                            .expanded()
                            .child(
                                new TextWidget(ownerString).widthRel(0.15f)
                                    .alignment(Alignment.Center))
                            .child(
                                new TextWidget(freq).widthRel(0.35f)
                                    .alignment(Alignment.Center))
                            .child(
                                new TextWidget(cover.getInfo()).widthRel(0.25f)
                                    .alignment(Alignment.Center))
                            .child(
                                new Row().widthRel(0.25f)
                                    .child(
                                        new Column().widthRel(playerIsOP ? 0.5f : 1)
                                            .child(
                                                new ButtonWidget<>().size(22, 22)
                                                    .align(Alignment.Center)
                                                    .overlay(
                                                        UITexture.fullImage(GregTech.ID, "items/redstoneSnifferLocate"))
                                                    .tooltip(tooltip -> { tooltip.addLine(IKey.str("Locate")); })
                                                    .onMousePressed(mouseButton -> {
                                                        GTValues.NW.sendToServer(
                                                            new PacketDebugRedstoneCover(
                                                                cover.dim,
                                                                cover.x,
                                                                cover.y,
                                                                cover.z,
                                                                false));
                                                        listWidget.getPanel()
                                                            .closeIfOpen(false);
                                                        return true;
                                                    })))
                                    .child(
                                        new Column().setEnabledIf(w -> playerIsOP)
                                            .widthRel(0.5f)
                                            .child(
                                                new ButtonWidget<>().size(22, 22)
                                                    .align(Alignment.Center)
                                                    .overlay(
                                                        UITexture
                                                            .fullImage(GregTech.ID, "items/redstoneSnifferTeleport"))
                                                    .tooltip(tooltip -> { tooltip.addLine(IKey.str("Teleport")); })
                                                    .onMousePressed(mouseButton -> {
                                                        GTValues.NW.sendToServer(
                                                            new PacketDebugRedstoneCover(
                                                                cover.dim,
                                                                cover.x,
                                                                cover.y,
                                                                cover.z,
                                                                true));
                                                        listWidget.getPanel()
                                                            .closeIfOpen(false);
                                                        return true;
                                                    })))));
                });

            });
        return result;
    }

}
