package gregtech.common.covers;

import static gregtech.api.util.GTUtility.intToStack;
import static gregtech.api.util.GTUtility.moveMultipleItemStacks;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.utils.item.LimitingItemStackHandler;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.gui.modularui2.CoverGuiData;
import gregtech.api.gui.modularui2.GTGuiTextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.KeyProvider;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.CoverBehaviorBase;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import gregtech.common.gui.modularui2.EnumRowBuilder;
import io.netty.buffer.ByteBuf;

public class CoverItemFilter extends CoverBehaviorBase<CoverItemFilter.ItemFilterData> {

    private final boolean mExport;

    public CoverItemFilter(boolean isExport, ITexture coverTexture) {
        super(ItemFilterData.class, coverTexture);
        this.mExport = isExport;
    }

    @Override
    public ItemFilterData createDataObject(int aLegacyData) {
        return new ItemFilterData((aLegacyData & 0x1) == 0, intToStack(aLegacyData >>> 1));
    }

    @Override
    public ItemFilterData createDataObject() {
        return new ItemFilterData();
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(ForgeDirection side, int aCoverID, ItemFilterData aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    protected ItemFilterData doCoverThingsImpl(ForgeDirection side, byte aInputRedstone, int aCoverID,
        ItemFilterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        final TileEntity tTileEntity = aTileEntity.getTileEntityAtSide(side);
        final Object fromEntity = mExport ? aTileEntity : tTileEntity;
        final Object toEntity = !mExport ? aTileEntity : tTileEntity;
        final ForgeDirection fromSide = !mExport ? side.getOpposite() : side;
        final ForgeDirection toSide = mExport ? side.getOpposite() : side;

        final List<ItemStack> filter = Collections.singletonList(aCoverVariable.filter.getStackInSlot(0));

        moveMultipleItemStacks(
            fromEntity,
            toEntity,
            fromSide,
            toSide,
            filter,
            aCoverVariable.mWhitelist,
            (byte) 64,
            (byte) 1,
            (byte) 64,
            (byte) 1,
            64);

        return aCoverVariable;
    }

    @Override
    protected boolean onCoverRightClickImpl(ForgeDirection side, int aCoverID, ItemFilterData aCoverVariable,
        ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        final ItemStack tStack = aPlayer.inventory.getCurrentItem();
        if (tStack != null) {
            aCoverVariable.filter.setStackInSlot(0, tStack);
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("299", "Item Filter: ") + tStack.getDisplayName());
        } else {
            aCoverVariable.filter.setStackInSlot(0, null);
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("300", "Filter Cleared!"));
        }
        return true;
    }

    @Override
    protected ItemFilterData onCoverScrewdriverClickImpl(ForgeDirection side, int aCoverID,
        ItemFilterData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable.mWhitelist = !aCoverVariable.mWhitelist;
        GTUtility.sendChatToPlayer(
            aPlayer,
            aCoverVariable.mWhitelist ? GTUtility.trans("124.1", "Blacklist Mode")
                : GTUtility.trans("125.1", "Whitelist Mode"));
        return aCoverVariable;
    }

    @Override
    protected boolean letsRedstoneGoInImpl(ForgeDirection side, int aCoverID, ItemFilterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsRedstoneGoOutImpl(ForgeDirection side, int aCoverID, ItemFilterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyInImpl(ForgeDirection side, int aCoverID, ItemFilterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyOutImpl(ForgeDirection side, int aCoverID, ItemFilterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidInImpl(ForgeDirection side, int aCoverID, ItemFilterData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return false;
    }

    @Override
    protected boolean letsFluidOutImpl(ForgeDirection side, int aCoverID, ItemFilterData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return false;
    }

    @Override
    protected boolean letsItemsInImpl(ForgeDirection side, int aCoverID, ItemFilterData aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsOutImpl(ForgeDirection side, int aCoverID, ItemFilterData aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean alwaysLookConnectedImpl(ForgeDirection side, int aCoverID, ItemFilterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected int getTickRateImpl(ForgeDirection side, int aCoverID, ItemFilterData aCoverVariable,
        ICoverable aTileEntity) {
        return 1;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    protected String getGuiId() {
        return "cover.item_filter";
    }

    @Override
    public void addUIWidgets(CoverGuiData guiData, PanelSyncManager syncManager, Flow column) {
        EnumSyncValue<FilterType> filterTypeSyncValue = new EnumSyncValue<>(
            FilterType.class,
            () -> getFilterType(guiData),
            value -> setFilterType(value, guiData));
        syncManager.syncValue("filter_type", filterTypeSyncValue);

        column.child(
            Flow.column()
                .coverChildren()
                .crossAxisAlignment(Alignment.CrossAxis.START)
                .marginLeft(WIDGET_MARGIN)
                .child(
                    Flow.row()
                        .coverChildren()
                        .childPadding(WIDGET_MARGIN)
                        .child(
                            new EnumRowBuilder<>(FilterType.class).value(filterTypeSyncValue)
                                .overlay(GTGuiTextures.OVERLAY_BUTTON_WHITELIST, GTGuiTextures.OVERLAY_BUTTON_BLACKLIST)
                                .build())
                        .child(
                            IKey.str(GTUtility.trans("318", "Check Mode"))
                                .asWidget()))
                .child(
                    IKey.str(GTUtility.trans("317", "Filter: "))
                        .asWidget()
                        .marginTop(WIDGET_MARGIN))
                .child(
                    new ItemSlot().slot(new ModularSlot(getCoverData(guiData).filter, 0, true))
                        .marginTop(WIDGET_MARGIN)));
    }

    private enum FilterType implements KeyProvider {

        WHITELIST(IKey.str(GTUtility.trans("125.1", "Whitelist Mode"))),
        BLACKLIST(IKey.str(GTUtility.trans("124.1", "Blacklist Mode")));

        private final IKey key;

        FilterType(IKey key) {
            this.key = key;
        }

        @Override
        public IKey getKey() {
            return this.key;
        }
    }

    private FilterType getFilterType(CoverGuiData guiData) {
        return getCoverData(guiData).mWhitelist ? FilterType.BLACKLIST : FilterType.WHITELIST;
    }

    private void setFilterType(FilterType filterType, CoverGuiData guiData) {
        ItemFilterData coverData = getCoverData(guiData);
        FilterType oldFilterType = getFilterType(guiData);
        if (filterType == oldFilterType) return;

        coverData.mWhitelist = filterType == FilterType.BLACKLIST;
        guiData.setCoverData(coverData);
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new ItemFilterUIFactory(buildContext).createWindow();
    }

    private class ItemFilterUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public ItemFilterUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            ItemFilterData filterCoverData = getCoverData();
            assert filterCoverData != null;
            builder.widget(
                new CoverDataControllerWidget<>(this::getCoverData, this::setCoverData, CoverItemFilter.this)
                    .addFollower(
                        new CoverDataFollowerToggleButtonWidget<>(),
                        coverData -> coverData.mWhitelist,
                        (coverData, state) -> {
                            coverData.mWhitelist = state;
                            return coverData;
                        },
                        widget -> widget
                            .setToggleTexture(
                                GTUITextures.OVERLAY_BUTTON_BLACKLIST,
                                GTUITextures.OVERLAY_BUTTON_WHITELIST)
                            .addTooltip(0, GTUtility.trans("125.1", "Whitelist Mode"))
                            .addTooltip(1, GTUtility.trans("124.1", "Blacklist Mode"))
                            .setPos(spaceX * 0, spaceY * 0))
                    .setPos(startX, startY))
                .widget(
                    new TextWidget(GTUtility.trans("317", "Filter: ")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 0, 3 + startY + spaceY * 1))
                .widget(
                    new TextWidget(GTUtility.trans("318", "Check Mode")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 2, 3 + startY + spaceY * 0))
                .widget(
                    SlotWidget.phantom(filterCoverData.filter, 0)
                        .setPos(startX + spaceX * 0, startY + spaceY * 2));
        }
    }

    public static class ItemFilterData implements ISerializableObject {

        /**
         * This variable is extremely confusing... mWhitelist == false means whitelist mode, true means blacklist mode
         */
        private boolean mWhitelist;
        private ItemStackHandler filter;

        public ItemFilterData() {
            this(false, new LimitingItemStackHandler(1, 1));
        }

        public ItemFilterData(boolean mWhitelist, ItemStack mFilter) {
            this(mWhitelist, new LimitingItemStackHandler(1, 1));
            this.filter.setStackInSlot(0, mFilter);
        }

        public ItemFilterData(boolean mWhitelist, ItemStackHandler filter) {
            this.mWhitelist = mWhitelist;
            this.filter = filter;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new ItemFilterData(mWhitelist, filter);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setBoolean("mWhitelist", mWhitelist);
            tag.setTag("filter", filter.serializeNBT());
            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeBoolean(mWhitelist);
            ByteBufUtils.writeTag(aBuf, filter.serializeNBT());
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            NBTTagCompound tag = (NBTTagCompound) aNBT;
            mWhitelist = tag.getBoolean("mWhitelist");
            if (tag.hasKey("mFilter", Constants.NBT.TAG_COMPOUND)) {
                // Old format
                filter = new ItemStackHandler(1);
                filter.setStackInSlot(0, ItemStack.loadItemStackFromNBT(tag.getCompoundTag("mFilter")));
            } else {
                filter.deserializeNBT(tag.getCompoundTag("filter"));
            }
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            mWhitelist = aBuf.readBoolean();
            filter.deserializeNBT(ISerializableObject.readCompoundTagFromGreggyByteBuf(aBuf));
            return this;
        }
    }
}
