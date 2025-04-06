package gregtech.common.items;

import static gregtech.api.enums.Mods.GregTech;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.cleanroommc.modularui.widgets.CategoryList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

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
import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.ButtonWidget;
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
import gregtech.api.items.GTGenericItem;
import gregtech.api.net.PakcetDebugRedstoneCover;
import gregtech.common.covers.CoverPosition;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;

public class ItemRedstoneSniffer extends GTGenericItem implements IGuiHolder<GuiData> {

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
            GuiFactories.item()
                .open(player);

        }
        return super.onItemRightClick(stack, world, player);
    }

    @Override
    public ModularPanel buildUI(GuiData guiData, PanelSyncManager guiSyncManager) {
        int scale;
        if (NetworkUtils.isClient()) {
            GameSettings settings = Minecraft.getMinecraft().gameSettings;
            scale = settings.guiScale > 0 ? settings.guiScale : 4;
        } else {
            scale = 1;
        }
        int textColor = Color.rgb(255, 255, 255);

        AtomicReference<String> freqFilter = new AtomicReference<>("");
        AtomicReference<String> ownerFilter = new AtomicReference<>("");
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
                ((IntSyncValue) guiSyncManager.getSyncHandler("last_page:0")).setValue(page);
            }
        };
        ListWidget<IWidget, CategoryList.Root> regularListWidget = new ListWidget<>();
        regularListWidget.sizeRel(1);
        ListWidget<IWidget, CategoryList.Root> advancedListWidget = new ListWidget<>();
        advancedListWidget.sizeRel(1);

        guiSyncManager.syncValue("player_is_op", new BooleanSyncValue(() -> false, () -> {
            EntityPlayerMP player = (EntityPlayerMP) guiData.getPlayer();
            return player.mcServer.getConfigurationManager()
                .func_152596_g(player.getGameProfile());
        }));
        StringSyncValue freqFilterSyncer = new StringSyncValue(freqFilter::get, freqFilter::set);
        freqFilterSyncer.setChangeListener(() -> {
            if (NetworkUtils.isClient()) {
                WidgetTree.resize(regularListWidget);
                WidgetTree.resize(advancedListWidget);
            }
        });
        guiSyncManager.syncValue("freq_filter", freqFilterSyncer);
        StringSyncValue ownerFilterSyncer = new StringSyncValue(ownerFilter::get, ownerFilter::set);
        ownerFilterSyncer.setChangeListener(() -> {
            if (NetworkUtils.isClient()) {
                WidgetTree.resize(advancedListWidget);
            }
        });
        guiSyncManager.syncValue("owner_filter", ownerFilterSyncer);
        ModularPanel panel = ModularPanel.defaultPanel("redstone_sniffer");
        panel.flex()
            .sizeRel(0.5f, 0.75f)
            .align(Alignment.Center);

        PagedWidget<?> data = new PagedWidget() {

            @Override
            public void afterInit() {
                setPage(lastPage.get());
            }
        };
        data.sizeRel(1, 0.7f);
        data.controller(controller);
        // Process regular wireless redstone frequencies
        GenericListSyncHandler<SnifferEntry> regularMapSyncer = new GenericListSyncHandler<>(() -> {
            List<SnifferEntry> result = new ArrayList<>();
            GregTechAPI.sWirelessRedstone.forEach((frequency, ignored) -> {
                boolean isPrivate = frequency > 65535;
                int displayFreq = isPrivate ? frequency - 65536 : frequency;
                result.add(new SnifferEntry(String.valueOf(displayFreq), isPrivate));
            });
            return result;
        }, new SnifferEntryAdapter());
        regularMapSyncer.setChangeListener(() -> {
            AtomicInteger bgStripe = new AtomicInteger(0);
            int stripe1 = Color.rgb(79, 82, 119);
            int stripe2 = Color.rgb(67, 58, 96);
            List<SnifferEntry> entries = new ArrayList<>(regularMapSyncer.getValue());
            entries.sort(Comparator.comparingInt(a -> (a.isPrivate ? 1 : 0)));
            List<IWidget> regularList = new ArrayList<>();
            entries.forEach(entry -> {
                bgStripe.getAndIncrement();
                regularList.add(
                    new Row().setEnabledIf(
                        w -> ((StringSyncValue) guiSyncManager.getSyncHandler("freq_filter:0")).getStringValue()
                            .isEmpty()
                            || entry.freq.equals(
                                ((StringSyncValue) guiSyncManager.getSyncHandler("freq_filter:0")).getStringValue()))
                        .sizeRel(1f, 0.1f * scale)
                        .expanded()
                        .background(
                            bgStripe.get() % 2 == 0 ? new Rectangle().setColor(stripe1)
                                : new Rectangle().setColor(stripe2))
                        .child(
                            new TextWidget(entry.freq).widthRel(0.5f)
                                .color(textColor)
                                .alignment(Alignment.Center))
                        .child(
                            new TextWidget(entry.isPrivate ? "Yes" : "No").widthRel(0.5f)
                                .color(textColor)
                                .alignment(Alignment.Center)));
            });
            regularList.forEach(regularListWidget::child);
            WidgetTree.resize(regularListWidget);
        });
        guiSyncManager.syncValue("regular_map", regularMapSyncer);

        data.addPage(
            new Column().child(
                new Row().heightRel(0.1f)
                    .child(
                        new TextWidget(IKey.lang("gt.item.redstone_sniffer.frequency")).widthRel(0.5f)
                            .color(textColor)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget(IKey.lang("gt.item.redstone_sniffer.private")).widthRel(0.5f)
                            .color(textColor)
                            .alignment(Alignment.Center)))
                .child(
                    new SingleChildWidget<>().sizeRel(1, 0.9f)
                        .child(regularListWidget)));

        // Process advanced wireless redstone frequencies
        GenericListSyncHandler<SnifferEntry> advancedMapSyncer = new GenericListSyncHandler<>(() -> {
            List<SnifferEntry> result = new ArrayList<>();
            GregTechAPI.sAdvancedWirelessRedstone.forEach((uuid, coverMap) -> {
                if (canSeeCovers(guiData, uuid)) {
                    String owner = uuid.equals("null") ? "Public"
                        : SpaceProjectManager.getPlayerNameFromUUID(UUID.fromString(uuid));
                    coverMap.forEach(
                        (frequency, covers) -> {
                            covers.forEach(
                                (coverPosition, ignored) -> {
                                    result.add(new SnifferEntry(owner, frequency, coverPosition));
                                });
                        });
                }
            });
            return result;
        }, new SnifferEntryAdapter());
        advancedMapSyncer.setChangeListener(() -> {
            List<SnifferEntry> entries = new ArrayList<>(advancedMapSyncer.getValue());
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
            WidgetTree.resize(advancedListWidget);
        });
        guiSyncManager.syncValue("adv_map", advancedMapSyncer);
        data.addPage(
            new Column().child(
                new Row().heightRel(0.1f)
                    .child(
                        new TextWidget(IKey.lang("gt.item.redstone_sniffer.owner")).widthRel(0.15f)
                            .color(textColor)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget(IKey.lang("gt.item.redstone_sniffer.frequency")).widthRel(0.35f)
                            .color(textColor)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget(IKey.lang("gt.item.redstone_sniffer.dimension")).widthRel(0.25f)
                            .color(textColor)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget(IKey.lang("gt.item.redstone_sniffer.action")).widthRel(0.25f)
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
                            new TextWidget(IKey.lang("gt.item.redstone_sniffer.frequency_filter")).widthRel(0.25f)
                                .color(textColor)
                                .alignment(Alignment.Center))
                        .child(
                            new TextFieldWidget().sizeRel(0.25f, 0.5f)
                                .setTextColor(textColor)
                                .value(
                                    SyncHandlers.string(
                                        () -> ((StringSyncValue) guiSyncManager.getSyncHandler("freq_filter:0"))
                                            .getStringValue(),
                                        filter -> {
                                            ((StringSyncValue) guiSyncManager.getSyncHandler("freq_filter:0"))
                                                .setStringValue(filter);
                                        })))
                        .child(
                            new TextWidget(IKey.lang("gt.item.redstone_sniffer.owner_filter")).widthRel(0.25f)
                                .color(textColor)
                                .alignment(Alignment.Center))
                        .child(
                            new TextFieldWidget().sizeRel(0.25f, 0.5f)
                                .setTextColor(textColor)
                                .value(
                                    SyncHandlers.string(
                                        () -> ((StringSyncValue) guiSyncManager.getSyncHandler("owner_filter:0"))
                                            .getStringValue(),
                                        filter -> {
                                            ((StringSyncValue) guiSyncManager.getSyncHandler("owner_filter:0"))
                                                .setStringValue(filter);
                                        }))))
                .child(data));
        panel.background(new Rectangle().setColor(Color.rgb(53, 46, 77)));
        return panel;
    }

    public List<IWidget> processAdvancedFrequencies(List<SnifferEntry> entryList, ListWidget<IWidget, CategoryList.Root> listWidget,
        PanelSyncManager guiSyncManager, int scale, int textColor) {
        List<IWidget> result = new ArrayList<>();
        AtomicInteger bgStripe = new AtomicInteger(0);
        int stripe1 = Color.rgb(79, 82, 119);
        int stripe2 = Color.rgb(67, 58, 96);
        entryList.forEach(entry -> {
            bgStripe.getAndIncrement();
            CoverPosition cover = entry.coverPosition;
            result.add(
                new Row()
                    .setEnabledIf(
                        w -> ((((StringSyncValue) guiSyncManager.getSyncHandler("owner_filter:0")).getStringValue()
                            .isEmpty()
                            || entry.owner.contains(
                                ((StringSyncValue) guiSyncManager.getSyncHandler("owner_filter:0")).getStringValue()))
                            && entry.freq.contains(
                                ((StringSyncValue) guiSyncManager.getSyncHandler("freq_filter:0")).getStringValue())))
                    .sizeRel(1f, 0.1f * scale)
                    .background(
                        (bgStripe.get() % 2 == 0) ? new Rectangle().setColor(stripe1)
                            : new Rectangle().setColor(stripe2))
                    .expanded()
                    .child(
                        new TextWidget(entry.owner).widthRel(0.15f)
                            .color(textColor)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget(entry.freq).widthRel(0.35f)
                            .color(textColor)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget(cover.getDimName()).widthRel(0.25f)
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
                                                UITexture
                                                    .fullImage(GregTech.ID, "gui/overlay_button/redstoneSnifferLocate")
                                                    .asIcon()
                                                    .size(19, 19)
                                                    .margin(3))
                                            .tooltip(
                                                tooltip -> {
                                                    tooltip.addLine(IKey.lang("gt.item.redstone_sniffer.locate"));
                                                })
                                            .onMousePressed(mouseButton -> {
                                                GTValues.NW.sendToServer(
                                                    new PakcetDebugRedstoneCover(
                                                        cover.dim,
                                                        cover.x,
                                                        cover.y,
                                                        cover.z,
                                                        false));
                                                ArrayList<DimensionalCoord> list = new ArrayList<>();
                                                list.add(new DimensionalCoord(cover.x, cover.y, cover.z, cover.dim));
                                                String foundMsg = String.format(
                                                    "Highlighting cover at %d,%d,%d",
                                                    cover.x,
                                                    cover.y,
                                                    cover.z);
                                                BlockPosHighlighter.highlightBlocks(
                                                    guiSyncManager.getPlayer(),
                                                    list,
                                                    foundMsg,
                                                    "Cannot highlight because you're not in the same dimension!");
                                                listWidget.getPanel()
                                                    .closeIfOpen(false);
                                                return true;
                                            })))
                            .child(
                                new SingleChildWidget<>()
                                    .setEnabledIf(
                                        w -> ((BooleanSyncValue) guiSyncManager.getSyncHandler("player_is_op:0"))
                                            .getBoolValue())
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
                                                    new PakcetDebugRedstoneCover(
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
        return result;
    }

    public boolean canSeeCovers(GuiData guiData, String uuid) {
        UUID leader = SpaceProjectManager.getLeader(
            guiData.getPlayer()
                .getUniqueID());
        return uuid.equals("null") || SpaceProjectManager.getLeader(UUID.fromString(uuid))
            .equals(leader);
    }

    public static class SnifferEntry {

        private final String owner;
        private final String freq;
        private final boolean isPrivate;
        private final CoverPosition coverPosition;

        public SnifferEntry(String owner, String freq, CoverPosition coverPosition) {
            this.owner = owner;
            this.freq = freq;
            this.isPrivate = false;
            this.coverPosition = coverPosition;
        }

        public SnifferEntry(String freq, boolean isPrivate) {
            this.owner = "";
            this.freq = freq;
            this.isPrivate = isPrivate;
            this.coverPosition = null;
        }
    }

    public static class SnifferEntryAdapter implements IByteBufAdapter<SnifferEntry> {

        public SnifferEntry deserialize(PacketBuffer buffer) throws IOException {
            String owner = buffer.readStringFromBuffer(buffer.readInt());
            String freq = buffer.readStringFromBuffer(buffer.readInt());
            boolean isPrivate = buffer.readBoolean();

            if (buffer.readBoolean()) {
                int x, y, z, dim, side;
                x = buffer.readInt();
                y = buffer.readInt();
                z = buffer.readInt();
                dim = buffer.readInt();
                side = buffer.readInt();
                String dimName = buffer.readStringFromBuffer(buffer.readInt());
                CoverPosition coverPosition = new CoverPosition(new ChunkCoordinates(x, y, z), dimName, dim, side);
                return new SnifferEntry(owner, freq, coverPosition);
            } else {
                return new SnifferEntry(freq, isPrivate);
            }

        }

        @Override
        public void serialize(PacketBuffer buffer, SnifferEntry value) throws IOException {
            buffer.writeInt(value.owner.length());
            buffer.writeStringToBuffer(value.owner);

            buffer.writeInt(value.freq.length());
            buffer.writeStringToBuffer(value.freq);

            buffer.writeBoolean(value.isPrivate);
            boolean coverPositionExists = value.coverPosition != null;
            buffer.writeBoolean(coverPositionExists);
            if (coverPositionExists) {
                buffer.writeInt(value.coverPosition.x);
                buffer.writeInt(value.coverPosition.y);
                buffer.writeInt(value.coverPosition.z);
                buffer.writeInt(value.coverPosition.dim);
                buffer.writeInt(value.coverPosition.side);
                buffer.writeInt(value.coverPosition.dimName.length());
                buffer.writeStringToBuffer(value.coverPosition.dimName);
            }

        }

        @Override
        public boolean areEqual(@NotNull SnifferEntry t1, @NotNull SnifferEntry t2) {
            if (t1.coverPosition == null && t2.coverPosition != null // ensure both entries are of the same type
                || t1.coverPosition != null && t2.coverPosition == null) return false;
            if (t1.coverPosition == null) return t1.freq.equals(t2.freq) && t1.isPrivate == t2.isPrivate;
            return t1.coverPosition.equals(t2.coverPosition) && t1.owner.equals(t2.owner) && t1.freq.equals(t2.freq);
        }
    }
}
