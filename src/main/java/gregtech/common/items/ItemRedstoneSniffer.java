package gregtech.common.items;

import static gregtech.api.enums.Mods.GregTech;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import appeng.api.util.DimensionalCoord;
import appeng.client.render.BlockPosHighlighter;
import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.cleanroommc.modularui.utils.serialization.IByteBufDeserializer;
import com.cleanroommc.modularui.utils.serialization.IByteBufSerializer;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import gregtech.api.net.PacketTeleportToCover;
import gregtech.common.covers.Cover;
import gregtech.common.gui.modularui.syncvalue.GenericMapSyncHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ChunkCoordinates;
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
import com.cleanroommc.modularui.value.sync.StringSyncValue;
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
import gregtech.common.covers.CoverPosition;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import org.jetbrains.annotations.NotNull;

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
        AtomicReference<String> freqFilter = new AtomicReference<>("");
        AtomicReference<String> ownerFilter = new AtomicReference<>("");
        AtomicInteger lastPage = new AtomicInteger(0);
        if(guiData.getMainHandItem().getTagCompound() != null && guiData.getMainHandItem().getTagCompound().hasKey("last_page")){
            lastPage.set(guiData.getMainHandItem().getTagCompound().getInteger("last_page"));
        }
        IntSyncValue pageSyncer = new IntSyncValue(lastPage::get, (page) -> {
            lastPage.set(page);
            if(guiData.getMainHandItem().getTagCompound() == null) guiData.getMainHandItem().setTagCompound(new NBTTagCompound());
            NBTTagCompound tag = guiData.getMainHandItem().getTagCompound();
            tag.setInteger("last_page",page);
        });
        guiSyncManager.syncValue("last_page", pageSyncer);



        PagedWidget.Controller controller = new PagedWidget.Controller() {

            @Override
            public void setPage(int page) {
                super.setPage(page);
                ((IntSyncValue) guiSyncManager.getSyncHandler("last_page:0")).setValue(page);
                System.out.println(String.format("set page to %d", page));
            }
        };
        ListWidget regularListWidget = new ListWidget<>();
        regularListWidget.sizeRel(1);
        ListWidget advancedListWidget = new ListWidget();
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

        GenericMapSyncHandler<String, Map<String, Map<CoverPosition, Byte>>> bla = new GenericMapSyncHandler<>(
            () -> GregTechAPI.sAdvancedWirelessRedstone
        );
        guiSyncManager.syncValue("adv_map",bla);

        PagedWidget data = new PagedWidget() {

            @Override
            public void afterInit() {
                setPage(lastPage.get());
            }
        };
        data.sizeRel(1, 0.7f);
        data.controller(controller);
        // Process regular wireless redstone frequencies
        GenericListSyncHandler<SnifferEntry> regularMapSyncer = new GenericListSyncHandler<>(
            () -> {
                List<SnifferEntry> result = new ArrayList<>();
                GregTechAPI.sWirelessRedstone.forEach((frequency, ignored) -> {
                    boolean isPrivate = frequency > 65535;
                    int displayFreq = isPrivate ? frequency - 65536 : frequency;
                    result.add(new SnifferEntry(String.valueOf(displayFreq), isPrivate));
                });
                return result;
            }, new SnifferEntryAdapter()
        );
        regularMapSyncer.setChangeListener(() -> {
            List<SnifferEntry> entries = new ArrayList<>(regularMapSyncer.getValue());
            List<IWidget> regularList = new ArrayList<>();
            entries.forEach(entry -> {
                regularList.add(new Row()
                    .setEnabledIf(w -> ((StringSyncValue) guiSyncManager.getSyncHandler("freq_filter:0")).getStringValue().isEmpty()
                        || entry.freq.equals(((StringSyncValue) guiSyncManager.getSyncHandler("freq_filter:0")).getStringValue()))
                    .background(new Rectangle().setColor(Color.LIGHT_BLUE.main))
                    .sizeRel(1f, 0.3f)
                    .expanded()
                    .child(
                        new TextWidget(entry.freq).widthRel(0.5f)
                            .alignment(Alignment.Center))
                    .child(
                        new TextWidget(entry.isPrivate ? "Yes" : "No").widthRel(0.5f)
                            .alignment(Alignment.Center)));
            });
            regularList.forEach(regularListWidget::child);
            WidgetTree.resize(regularListWidget);
        });
        guiSyncManager.syncValue("regular_map",regularMapSyncer);

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
        GenericListSyncHandler<SnifferEntry> advancedMapSyncer = new GenericListSyncHandler<>(
            () -> {
                List<SnifferEntry> result = new ArrayList<>();
                GregTechAPI.sAdvancedWirelessRedstone.forEach((uuid, coverMap) -> {
                    if(canSeeCovers(guiData, uuid)){
                        String owner = uuid.equals("null") ? "Public" : SpaceProjectManager.getPlayerNameFromUUID(UUID.fromString(uuid));
                        coverMap.forEach((frequency, covers) -> {
                            covers.forEach((coverPosition, ignored) -> {
                                result.add(new SnifferEntry(owner, frequency, coverPosition));
                            });
                        });
                    }
                });
                return result;
            }, new SnifferEntryAdapter()
        );
        advancedMapSyncer.setChangeListener(() -> {
            List<SnifferEntry> entries = new ArrayList<>(advancedMapSyncer.getValue());
            entries.sort((a,b) -> {
                if(a.owner.equals("Public")) return 1;
                return a.owner.compareTo(b.owner);
            });
            List<IWidget> advancedList = (processAdvancedFrequencies(
                entries,
                advancedListWidget,
                guiSyncManager));

            advancedList.forEach(advancedListWidget::child);
            WidgetTree.resize(advancedListWidget);
        });
        guiSyncManager.syncValue("adv_map", advancedMapSyncer);
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
                        new TextWidget("Dimension").widthRel(0.25f)
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
                                .value(
                                    SyncHandlers.string(
                                        () -> ((StringSyncValue) guiSyncManager.getSyncHandler("freq_filter:0"))
                                            .getStringValue(),
                                        filter -> {
                                            ((StringSyncValue) guiSyncManager.getSyncHandler("freq_filter:0"))
                                                .setStringValue(filter);
                                        })))
                        .child(
                            new TextWidget("Owner: ").widthRel(0.25f)
                                .alignment(Alignment.Center))
                        .child(
                            new TextFieldWidget().sizeRel(0.25f, 0.5f)
                                .value(
                                    SyncHandlers.string(
                                        () -> ((StringSyncValue) guiSyncManager.getSyncHandler("owner_filter:0"))
                                            .getStringValue(),
                                        filter -> {
                                            ((StringSyncValue) guiSyncManager.getSyncHandler("owner_filter:0"))
                                                .setStringValue(filter);
                                        }))))
                .child(data));
        return panel;
    }

    public List<IWidget> processAdvancedFrequencies(List<SnifferEntry> entryList,
        ListWidget listWidget, PanelSyncManager guiSyncManager) {
        List<IWidget> result = new ArrayList<>();
        entryList
            .forEach(entry -> {
                CoverPosition cover = entry.coverPosition;
                result.add(
                    new Row().setEnabledIf(
                        w -> (((StringSyncValue) guiSyncManager.getSyncHandler("owner_filter:0")).getStringValue()
                            .isEmpty()
                            || ((StringSyncValue) guiSyncManager.getSyncHandler("owner_filter:0")).getStringValue()
                                .equals(entry.owner))
                            && entry.freq
                                .contains(
                                    ((StringSyncValue) guiSyncManager.getSyncHandler("freq_filter:0"))
                                        .getStringValue()))
                        .background(new Rectangle().setColor(Color.LIGHT_BLUE.main))
                        .sizeRel(1f, 0.3f)
                        .expanded()
                        .child(
                            new TextWidget(entry.owner).widthRel(0.15f)
                                .alignment(Alignment.Center))
                        .child(
                            new TextWidget(entry.freq).widthRel(0.35f)
                                .alignment(Alignment.Center))
                        .child(
                            new TextWidget(cover.getDimName()).widthRel(0.25f)
                                .alignment(Alignment.Center))
                        .child(
                            new Row().widthRel(0.25f)
                                .child(
                                    new Column().widthRel(0.5f)
                                        .child(
                                            new ButtonWidget<>().size(25, 25)
                                                .align(Alignment.Center)
                                                .overlay(
                                                    UITexture
                                                        .fullImage(
                                                            GregTech.ID,
                                                            "gui/overlay_button/redstoneSnifferLocate")
                                                        .asIcon()
                                                        .size(19, 19)
                                                        .margin(3))
                                                .tooltip(tooltip -> { tooltip.addLine(IKey.str("Locate")); })
                                                .onMousePressed(mouseButton -> {
                                                    GTValues.NW.sendToServer(
                                                        new PacketTeleportToCover(
                                                            cover.dim,
                                                            cover.x,
                                                            cover.y,
                                                            cover.z,
                                                            false));
                                                    ArrayList<DimensionalCoord> list = new ArrayList<>();
                                                    list.add(new DimensionalCoord(cover.x, cover.y, cover.z, cover.dim));
                                                    String foundMsg = String
                                                        .format("Highlighting cover at %d,%d,%d", cover.x, cover.y, cover.z);
                                                    BlockPosHighlighter
                                                        .highlightBlocks(guiSyncManager.getPlayer(), list, foundMsg, "Cannot highlight because you're not in the same dimension!");
                                                    listWidget.getPanel()
                                                        .closeIfOpen(false);
                                                    return true;
                                                })))
                                .child(
                                    new Column()
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
                                                .tooltip(tooltip -> { tooltip.addLine(IKey.str("Teleport")); })
                                                .onMousePressed(mouseButton -> {
                                                    GTValues.NW.sendToServer(
                                                        new PacketTeleportToCover(
                                                            cover.dim,
                                                            cover.x,
                                                            cover.y,
                                                            cover.z,
                                                            true));
                                                    ArrayList<DimensionalCoord> list = new ArrayList<>();
                                                    list.add(new DimensionalCoord(cover.x, cover.y, cover.z, cover.dim));
                                                    String foundMsg = String
                                                        .format("Highlighting cover at %d,%d,%d", cover.x, cover.y, cover.z);
                                                    BlockPosHighlighter
                                                        .highlightBlocks(guiSyncManager.getPlayer(), list, foundMsg, "Cannot highlight because you're not in the same dimension!");
                                                    listWidget.getPanel()
                                                        .closeIfOpen(false);
                                                    return true;
                                                })))));
            });
        return result;
    }

    public boolean canSeeCovers(GuiData guiData, String uuid){
        UUID leader = SpaceProjectManager.getLeader(
            guiData.getPlayer()
                .getUniqueID());
        return uuid.equals("null") || SpaceProjectManager.getLeader(UUID.fromString(uuid)).equals(leader);
    }

    public class SnifferEntry{
        private final String owner;
        private final String freq;
        private boolean isPrivate;
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
    public class SnifferEntryAdapter implements IByteBufAdapter<SnifferEntry> {

        public SnifferEntry deserialize(PacketBuffer buffer) throws IOException {
            String owner = buffer.readStringFromBuffer(buffer.readInt());
            String freq = buffer.readStringFromBuffer(buffer.readInt());
            boolean isPrivate = buffer.readBoolean();

            if(buffer.readBoolean()){
                int x,y,z,dim,side;
                x = buffer.readInt();
                y = buffer.readInt();
                z = buffer.readInt();
                dim = buffer.readInt();
                side = buffer.readInt();
                String dimName = buffer.readStringFromBuffer(buffer.readInt());
                CoverPosition coverPosition = new CoverPosition(new ChunkCoordinates(x,y,z),dimName,dim,side);
                return new SnifferEntry(owner,freq,coverPosition);
            } else{
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
            if(coverPositionExists){
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
            if(t1.coverPosition == null) return t1.freq.equals(t2.freq) && t1.isPrivate == t2.isPrivate;
            return t1.coverPosition.equals(t2.coverPosition) && t1.owner.equals(t2.owner) && t1.freq.equals(t2.freq);
        }
    }
}
