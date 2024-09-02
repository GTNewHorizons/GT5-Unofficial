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

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.CoverBehaviorBase;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerSlotWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
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

        final List<ItemStack> filter = Collections.singletonList(aCoverVariable.mFilter);

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
            aCoverVariable.mFilter = tStack;
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("299", "Item Filter: ") + tStack.getDisplayName());
        } else {
            aCoverVariable.mFilter = null;
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
            aCoverVariable.mWhitelist ? GTUtility.trans("125.1", "Whitelist Mode")
                : GTUtility.trans("124.1", "Blacklist Mode"));
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
            ItemStackHandler filterInvHandler = new ItemStackHandler(1);
            if (getCoverData() != null) {
                filterInvHandler.setStackInSlot(0, setStackSize1(getCoverData().mFilter));
            }
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
                    .addFollower(
                        new CoverDataFollowerSlotWidget<>(filterInvHandler, 0, true),
                        coverData -> setStackSize1(coverData.mFilter),
                        (coverData, stack) -> {
                            coverData.mFilter = setStackSize1(stack);
                            return coverData;
                        },
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

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            mWhitelist = aBuf.readBoolean();
            mFilter = ISerializableObject.readItemStackFromGreggyByteBuf(aBuf);
            return this;
        }
    }
}
