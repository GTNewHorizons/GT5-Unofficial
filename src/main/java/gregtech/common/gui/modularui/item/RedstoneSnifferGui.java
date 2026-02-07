package gregtech.common.gui.modularui.item;

import static gregtech.api.enums.Mods.GregTech;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.CategoryList;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.PageButton;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import appeng.api.util.DimensionalCoord;
import appeng.client.render.BlockPosHighlighter;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.net.PacketTeleportPlayer;
import gregtech.common.covers.CoverPosition;
import gregtech.common.gui.modularui.synchandler.SnifferEntryListSyncHandler;
import gregtech.common.items.ItemRedstoneSniffer;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;

public class RedstoneSnifferGui {

    private final GuiData guiData;
    private final PanelSyncManager guiSyncManager;
    private String freqFilter = "";
    private String ownerFilter = "";

    public RedstoneSnifferGui(GuiData guiData, PanelSyncManager guiSyncManager) {
        this.guiData = guiData;
        this.guiSyncManager = guiSyncManager;
    }

    public ModularPanel build() {
        int scale;
        if (NetworkUtils.isClient()) {
            GameSettings settings = Minecraft.getMinecraft().gameSettings;
            scale = settings.guiScale > 0 ? settings.guiScale : 4;
        } else {
            scale = 1;
        }
        int textColor = Color.rgb(255, 255, 255);

        AtomicInteger lastPage = new AtomicInteger(0);
        if (guiData.getMainHandItem()
            .getTagCompound() != null && guiData.getMainHandItem()
                .getTagCompound()
                .hasKey("last_page")) {
            lastPage.set(
                guiData.getMainHandItem()
                    .getTagCompound()
                    .getInteger("last_page"));
        }
        IntSyncValue pageSyncer = new IntSyncValue(lastPage::get, (page) -> {
            lastPage.set(page);
            if (guiData.getMainHandItem()
                .getTagCompound() == null)
                guiData.getMainHandItem()
                    .setTagCompound(new NBTTagCompound());
            NBTTagCompound tag = guiData.getMainHandItem()
                .getTagCompound();
            tag.setInteger("last_page", page);
        });
        guiSyncManager.syncValue("last_page", pageSyncer);

        PagedWidget.Controller controller = new PagedWidget.Controller() {

            @Override
            public void setPage(int page) {
                super.setPage(page);
                pageSyncer.setValue(page);
            }
        };
        ListWidget<IWidget, CategoryList.Root> regularListWidget = new ListWidget<>();
        regularListWidget.sizeRel(1);
        ListWidget<IWidget, CategoryList.Root> advancedListWidget = new ListWidget<>();
        advancedListWidget.sizeRel(1);

        BooleanSyncValue playerIsOpSyncer = new BooleanSyncValue(() -> false, () -> {
            EntityPlayerMP player = (EntityPlayerMP) guiData.getPlayer();
            return player.mcServer.getConfigurationManager()
                .func_152596_g(player.getGameProfile());
        });
        guiSyncManager.syncValue("player_is_op", playerIsOpSyncer);
        ModularPanel panel = ModularPanel.defaultPanel("redstone_sniffer");
        panel.flex()
            .sizeRel(0.5f, 0.75f)
            .align(Alignment.Center);

        @SuppressWarnings({ "rawtypes" })
        PagedWidget<?> data = new PagedWidget() {

            @Override
            public void afterInit() {
                setPage(lastPage.get());
            }
        };
        data.sizeRel(1, 0.7f);
        data.controller(controller);
        // Process regular wireless redstone frequencies
        GenericListSyncHandler<ItemRedstoneSniffer.SnifferEntry> regularMapSyncer = new SnifferEntryListSyncHandler(
            () -> {
                List<ItemRedstoneSniffer.SnifferEntry> result = new ArrayList<>();
                GregTechAPI.sWirelessRedstone.forEach((frequency, ignored) -> {
                    boolean isPrivate = frequency > 65535;
                    int displayFreq = isPrivate ? frequency - 65536 : frequency;
                    result.add(new ItemRedstoneSniffer.SnifferEntry(String.valueOf(displayFreq), isPrivate));
                });
                return result;
            });
        regularMapSyncer.setChangeListener(() -> {
            if (!regularListWidget.getChildren()
                .isEmpty()) return;
            AtomicInteger bgStripe = new AtomicInteger(0);
            int stripe1 = Color.rgb(79, 82, 119);
            int stripe2 = Color.rgb(67, 58, 96);
            List<ItemRedstoneSniffer.SnifferEntry> entries = new ArrayList<>(regularMapSyncer.getValue());
            entries.sort(Comparator.comparingInt(a -> (a.isPrivate ? 1 : 0)));
            List<IWidget> regularList = new ArrayList<>();
            entries.forEach(entry -> {
                bgStripe.getAndIncrement();
                regularList.add(
                    new Row().setEnabledIf(w -> (freqFilter.isEmpty() || entry.freq.equals(freqFilter)))
                        .sizeRel(1f, 0.1f * scale)
                        .expanded()
                        .background(
                            bgStripe.get() % 2 == 0 ? new Rectangle().color(stripe1) : new Rectangle().color(stripe2))
                        .child(
                            new TextWidget<>(entry.freq).widthRel(0.5f)
                                .color(textColor)
                                .alignment(Alignment.Center))
                        .child(
                            new TextWidget<>(
                                entry.isPrivate ? IKey.lang("gui.yes")
                                    .toString()
                                    : IKey.lang("gui.no")
                                        .toString()).widthRel(0.5f)
                                            .color(textColor)
                                            .alignment(Alignment.Center)));
            });
            regularList.forEach(regularListWidget::child);
        });
        guiSyncManager.syncValue("regular_map", regularMapSyncer);

        data.addPage(
            new Column().child(
                new Row().heightRel(0.1f)
                    .child(
                        new TextWidget<>(IKey.lang("gt.item.redstone_sniffer.frequency")).widthRel(0.5f)
                            .color(textColor)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget<>(IKey.lang("gt.item.redstone_sniffer.private")).widthRel(0.5f)
                            .color(textColor)
                            .alignment(Alignment.Center)))
                .child(
                    new SingleChildWidget<>().sizeRel(1, 0.9f)
                        .child(regularListWidget)));

        // Process advanced wireless redstone frequencies
        GenericListSyncHandler<ItemRedstoneSniffer.SnifferEntry> advancedMapSyncer = new SnifferEntryListSyncHandler(
            () -> {
                List<ItemRedstoneSniffer.SnifferEntry> result = new ArrayList<>();
                if (NetworkUtils.isClient()) {
                    return result;
                }
                GregTechAPI.sAdvancedWirelessRedstone.forEach((uuid, coverMap) -> {
                    if (playerIsOpSyncer.getValue() || canSeeCovers(guiData, uuid)) {
                        String owner = uuid.equals("null") ? "Public"
                            : SpaceProjectManager.getPlayerNameFromUUID(UUID.fromString(uuid));
                        coverMap.forEach((frequency, covers) -> {
                            covers.forEach(
                                (coverPosition, ignored) -> {
                                    result.add(new ItemRedstoneSniffer.SnifferEntry(owner, frequency, coverPosition));
                                });
                        });
                    }
                });
                return result;
            });
        advancedMapSyncer.setChangeListener(() -> {
            if (!advancedListWidget.getChildren()
                .isEmpty()) return;
            List<ItemRedstoneSniffer.SnifferEntry> entries = new ArrayList<>(advancedMapSyncer.getValue());
            entries.sort((a, b) -> {
                if (a.owner.equals("Public")) return -1;
                if (b.owner.equals("Public")) return 1;
                return a.owner.compareTo(b.owner);
            });
            List<IWidget> advancedList = (processAdvancedFrequencies(
                entries,
                advancedListWidget,
                guiSyncManager,
                scale,
                textColor));

            advancedList.forEach(advancedListWidget::child);
        });
        guiSyncManager.syncValue("adv_map", advancedMapSyncer);
        data.addPage(
            new Column().child(
                new Row().heightRel(0.1f)
                    .child(
                        new TextWidget<>(IKey.lang("gt.item.redstone_sniffer.owner")).widthRel(0.15f)
                            .color(textColor)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget<>(IKey.lang("gt.item.redstone_sniffer.frequency")).widthRel(0.35f)
                            .color(textColor)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget<>(IKey.lang("gt.item.redstone_sniffer.dimension")).widthRel(0.25f)
                            .color(textColor)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget<>(IKey.lang("gt.item.redstone_sniffer.action")).widthRel(0.25f)
                            .color(textColor)
                            .alignment(Alignment.Center)))
                .child(
                    new SingleChildWidget<>().sizeRel(1, 0.9f)
                        .child(advancedListWidget)));
        panel.child(
            new Column().margin(10)
                .child(
                    new Row().heightRel(0.1f)
                        .marginBottom(10)
                        .child(
                            new PageButton(0, controller).widthRel(0.5f)
                                .align(Alignment.CenterLeft)
                                .overlay(IKey.lang("gt.item.redstone_sniffer.regular_wireless")))
                        .child(
                            new PageButton(1, controller).widthRel(0.5f)
                                .align(Alignment.CenterRight)
                                .overlay(IKey.lang("gt.item.redstone_sniffer.advanced_wireless"))))
                .child(
                    new Row().heightRel(0.1f)
                        .marginBottom(10)
                        .child(
                            new TextWidget<>(IKey.lang("gt.item.redstone_sniffer.frequency_filter")).widthRel(0.25f)
                                .color(textColor)
                                .alignment(Alignment.Center))
                        .child(
                            new TextFieldWidget().sizeRel(0.25f, 0.5f)
                                .setTextColor(textColor)
                                .value(SyncHandlers.string(() -> freqFilter, filter -> freqFilter = filter)))
                        .child(
                            new TextWidget<>(IKey.lang("gt.item.redstone_sniffer.owner_filter")).widthRel(0.25f)
                                .color(textColor)
                                .alignment(Alignment.Center))
                        .child(
                            new TextFieldWidget().sizeRel(0.25f, 0.5f)
                                .setTextColor(textColor)
                                .value(SyncHandlers.string(() -> ownerFilter, filter -> { ownerFilter = filter; }))))
                .child(data));

        return panel.widgetTheme(GTWidgetThemes.BACKGROUND_REDSTONE_SNIFFER);
    }

    public boolean canSeeCovers(GuiData guiData, String uuid) {
        UUID leader = SpaceProjectManager.getLeader(
            guiData.getPlayer()
                .getUniqueID());
        return uuid.equals("null") || SpaceProjectManager.getLeader(UUID.fromString(uuid))
            .equals(leader);
    }

    public List<IWidget> processAdvancedFrequencies(List<ItemRedstoneSniffer.SnifferEntry> entryList,
        ListWidget<IWidget, CategoryList.Root> listWidget, PanelSyncManager guiSyncManager, int scale, int textColor) {
        List<IWidget> result = new ArrayList<>();
        BooleanSyncValue playerIsOpSyncer = (BooleanSyncValue) guiSyncManager
            .getSyncHandlerFromMapKey("player_is_op:0");
        AtomicInteger bgStripe = new AtomicInteger(0);
        int stripe1 = Color.rgb(79, 82, 119);
        int stripe2 = Color.rgb(67, 58, 96);
        for (ItemRedstoneSniffer.SnifferEntry entry : entryList) {
            bgStripe.getAndIncrement();
            CoverPosition cover = entry.coverPosition;
            if (cover == null) continue;
            result.add(
                new Row()
                    .setEnabledIf(
                        w -> ((ownerFilter.isEmpty() || entry.owner.contains(ownerFilter))
                            && entry.freq.contains((freqFilter))))
                    .sizeRel(1f, 0.1f * scale)
                    .background(
                        (bgStripe.get() % 2 == 0) ? new Rectangle().color(stripe1) : new Rectangle().color(stripe2))
                    .expanded()
                    .child(
                        new TextWidget<>(entry.owner).widthRel(0.15f)
                            .color(textColor)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget<>(entry.freq).widthRel(0.35f)
                            .color(textColor)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget<>(cover.getDimName()).widthRel(0.25f)
                            .color(textColor)
                            .alignment(Alignment.Center))
                    .child(
                        new Row().widthRel(0.25f)
                            .child(
                                new SingleChildWidget<>().widthRel(0.5f)
                                    .child(
                                        new ButtonWidget<>().size(25, 25)
                                            .align(Alignment.Center)
                                            .overlay(
                                                GTGuiTextures.OVERLAY_BUTTON_REDSTONESNIFFERLOCATE.asIcon()
                                                    .size(19, 19)
                                                    .margin(3))
                                            .tooltip(
                                                tooltip -> {
                                                    tooltip.addLine(IKey.lang("gt.item.redstone_sniffer.locate"));
                                                })
                                            .onMousePressed(mouseButton -> {
                                                GTValues.NW.sendToServer(
                                                    new PacketTeleportPlayer(
                                                        cover.dim,
                                                        cover.x,
                                                        cover.y,
                                                        cover.z,
                                                        false));
                                                if (NetworkUtils.isClient()) {
                                                    ArrayList<DimensionalCoord> list = new ArrayList<>();
                                                    list.add(
                                                        new DimensionalCoord(cover.x, cover.y, cover.z, cover.dim));
                                                    String foundMsg = StatCollector.translateToLocalFormatted(
                                                        "gt.item.redstone_sniffer.highlight_message",
                                                        cover.x,
                                                        cover.y,
                                                        cover.z);
                                                    BlockPosHighlighter.highlightBlocks(
                                                        guiSyncManager.getPlayer(),
                                                        list,
                                                        foundMsg,
                                                        StatCollector.translateToLocal(
                                                            "gt.item.redstone_sniffer.wrong_dim_message"));
                                                    listWidget.getPanel()
                                                        .closeIfOpen();
                                                }
                                                return true;
                                            })))
                            .child(
                                new SingleChildWidget<>().setEnabledIf(w -> playerIsOpSyncer.getValue())
                                    .widthRel(0.5f)
                                    .child(
                                        new ButtonWidget<>().size(25, 25)
                                            .align(Alignment.Center)
                                            .overlay(
                                                UITexture
                                                    .fullImage(
                                                        GregTech.ID,
                                                        "gui/overlay_button/redstoneSnifferTeleport")
                                                    .asIcon()
                                                    .size(19, 19)
                                                    .margin(3))
                                            .tooltip(
                                                tooltip -> {
                                                    tooltip.addLine(IKey.lang("gt.item.redstone_sniffer.teleport"));
                                                })
                                            .onMousePressed(mouseButton -> {
                                                GTValues.NW.sendToServer(
                                                    new PacketTeleportPlayer(
                                                        cover.dim,
                                                        cover.x,
                                                        cover.y,
                                                        cover.z,
                                                        true));
                                                listWidget.getPanel()
                                                    .closeIfOpen();
                                                return true;
                                            })))));
        }
        return result;
    }
}
