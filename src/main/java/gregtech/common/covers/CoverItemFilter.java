package gregtech.common.covers;

import static gregtech.api.util.GTUtility.moveMultipleItemStacks;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerSlotWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import io.netty.buffer.ByteBuf;

public class CoverItemFilter extends CoverBehaviorBase<CoverItemFilter.ItemFilterData> {

    private final boolean mExport;

    public CoverItemFilter(CoverContext context, boolean isExport, ITexture coverTexture) {
        super(context, coverTexture);
        this.mExport = isExport;
    }

    public boolean isWhitelist() {
        return coverData.mWhitelist;
    }

    public CoverItemFilter setWhitelist(boolean whitelist) {
        this.coverData.mWhitelist = whitelist;
        return this;
    }

    public ItemStack getFilter() {
        return coverData.mFilter;
    }

    public CoverItemFilter setFilter(ItemStack filter) {
        this.coverData.mFilter = filter;
        return this;
    }

    @Override
    protected ItemFilterData initializeData() {
        return new CoverItemFilter.ItemFilterData();
    }

    public boolean isRedstoneSensitive() {
        return false;
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        final TileEntity tTileEntity = coverable.getTileEntityAtSide(coverSide);
        final Object fromEntity = mExport ? coverable : tTileEntity;
        final Object toEntity = !mExport ? coverable : tTileEntity;
        final ForgeDirection fromSide = !mExport ? coverSide.getOpposite() : coverSide;
        final ForgeDirection toSide = mExport ? coverSide.getOpposite() : coverSide;

        final List<ItemStack> filter = Collections.singletonList(coverData.mFilter);

        moveMultipleItemStacks(
            fromEntity,
            toEntity,
            fromSide,
            toSide,
            filter,
            coverData.mWhitelist,
            (byte) 64,
            (byte) 1,
            (byte) 64,
            (byte) 1,
            64);
    }

    @Override
    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        final ItemStack tStack = aPlayer.inventory.getCurrentItem();
        if (tStack != null) {
            coverData.mFilter = tStack;
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("299", "Item Filter: ") + tStack.getDisplayName());
        } else {
            coverData.mFilter = null;
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("300", "Filter Cleared!"));
        }
        return true;
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        coverData.mWhitelist = !coverData.mWhitelist;
        GTUtility.sendChatToPlayer(
            aPlayer,
            coverData.mWhitelist ? GTUtility.trans("125.1", "Whitelist Mode")
                : GTUtility.trans("124.1", "Blacklist Mode"));
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return true;
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return true;
    }

    @Override
    public boolean letsEnergyIn() {
        return true;
    }

    @Override
    public boolean letsEnergyOut() {
        return true;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return 1;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new ItemFilterUIFactory(buildContext).createWindow();
    }

    private static class ItemFilterUIFactory extends UIFactory<CoverItemFilter> {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public ItemFilterUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected CoverItemFilter adaptCover(Cover cover) {
            if (cover instanceof CoverItemFilter adapterCover) {
                return adapterCover;
            }
            return null;
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            ItemStackHandler filterInvHandler = new ItemStackHandler(1);
            CoverItemFilter cover = getCover();
            if (cover != null) {
                filterInvHandler.setStackInSlot(0, setStackSize1(cover.getFilter()));
            }
            builder
                .widget(
                    new CoverDataControllerWidget<>(this::getCover, getUIBuildContext())
                        .addFollower(
                            new CoverDataFollowerToggleButtonWidget<>(),
                            CoverItemFilter::isWhitelist,
                            CoverItemFilter::setWhitelist,
                            widget -> widget
                                .setToggleTexture(
                                    GTUITextures.OVERLAY_BUTTON_BLACKLIST,
                                    GTUITextures.OVERLAY_BUTTON_WHITELIST)
                                .addTooltip(0, GTUtility.trans("125.1", "Whitelist Mode"))
                                .addTooltip(1, GTUtility.trans("124.1", "Blacklist Mode"))
                                .setPos(spaceX * 0, spaceY * 0))
                        .addFollower(
                            new CoverDataFollowerSlotWidget<>(filterInvHandler, 0, true),
                            coverData -> setStackSize1(coverData.getFilter()),
                            (coverData, stack) -> coverData.setFilter(setStackSize1(stack)),
                            widget -> widget.setBackground(GTUITextures.SLOT_DARK_GRAY)
                                .setPos(spaceX * 0, spaceY * 2))
                        .setPos(startX, startY))
                .widget(
                    new TextWidget(GTUtility.trans("317", "Filter: ")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 0, 3 + startY + spaceY * 1))
                .widget(
                    new TextWidget(GTUtility.trans("318", "Check Mode")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 2, 3 + startY + spaceY * 0));
        }

        private ItemStack setStackSize1(ItemStack stack) {
            if (stack != null) {
                stack.stackSize = 1;
            }
            return stack;
        }
    }

    public static class ItemFilterData implements ISerializableObject {

        private boolean mWhitelist;
        private ItemStack mFilter;

        public ItemFilterData() {}

        public ItemFilterData(boolean mWhitelist, ItemStack mFilter) {
            this.mWhitelist = mWhitelist;
            this.mFilter = mFilter;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new ItemFilterData(mWhitelist, mFilter);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setBoolean("mWhitelist", mWhitelist);
            if (mFilter != null) tag.setTag("mFilter", mFilter.writeToNBT(new NBTTagCompound()));
            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeBoolean(mWhitelist);
            ByteBufUtils.writeItemStack(aBuf, mFilter);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            NBTTagCompound tag = (NBTTagCompound) aNBT;
            mWhitelist = tag.getBoolean("mWhitelist");
            if (tag.hasKey("mFilter", Constants.NBT.TAG_COMPOUND))
                mFilter = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("mFilter"));
            else mFilter = null;
        }

        @Override
        public void readFromPacket(ByteArrayDataInput aBuf) {
            mWhitelist = aBuf.readBoolean();
            mFilter = ISerializableObject.readItemStackFromGreggyByteBuf(aBuf);
        }
    }
}
