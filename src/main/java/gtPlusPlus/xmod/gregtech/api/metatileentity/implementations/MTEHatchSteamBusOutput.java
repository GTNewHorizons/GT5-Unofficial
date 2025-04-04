package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.ITEM_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;
import static gregtech.api.util.GTUtility.areStacksEqual;
import static gregtech.api.util.GTUtility.isStackInvalid;
import static gregtech.api.util.GTUtility.moveMultipleItemStacks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.modularui.api.forge.ItemHandlerHelper;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.GTMod;
import gregtech.api.enums.Textures;
import gregtech.api.gui.widgets.PhantomItemButton;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IItemLockable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;
import gtPlusPlus.core.lib.GTPPCore;

public class MTEHatchSteamBusOutput extends MTEHatch implements IItemLockable {

    public MTEHatchSteamBusOutput(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            getSlots(aTier),
            new String[] { "Item Output for Steam Multiblocks",
                aTier == 0 ? "Does not automatically export items"
                    : EnumChatFormatting.BOLD + "DOES"
                        + EnumChatFormatting.RESET
                        + EnumChatFormatting.GRAY
                        + " automatically export items",
                "Capacity: " + getSlots(aTier + 1) + " stacks", "Does not work with non-steam multiblocks",
                GTPPCore.GT_Tooltip.get() });
    }

    public MTEHatchSteamBusOutput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, getSlots(aTier + 1), aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return GTMod.gregtechproxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT), TextureFactory.of(ITEM_OUT_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return GTMod.gregtechproxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT), TextureFactory.of(ITEM_OUT_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchSteamBusOutput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    public boolean canPush() {
        return mTier > 0;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork()
            && (aTick & 0x7) == 0
            && canPush()) {
            final IInventory tTileEntity = aBaseMetaTileEntity
                .getIInventoryAtSide(aBaseMetaTileEntity.getFrontFacing());
            if (tTileEntity != null) {
                moveMultipleItemStacks(
                    aBaseMetaTileEntity,
                    tTileEntity,
                    aBaseMetaTileEntity.getFrontFacing(),
                    aBaseMetaTileEntity.getBackFacing(),
                    null,
                    false,
                    (byte) 64,
                    (byte) 1,
                    (byte) 64,
                    (byte) 1,
                    mInventory.length);
                for (int i = 0; i < mInventory.length; i++)
                    if (mInventory[i] != null && mInventory[i].stackSize <= 0) mInventory[i] = null;
            }
        }
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[14][17][];
        for (byte c = -1; c < 16; c++) {
            if (rTextures[0][c + 1] == null) rTextures[0][c + 1] = getSideFacingActive(c);
            if (rTextures[1][c + 1] == null) rTextures[1][c + 1] = getSideFacingInactive(c);
            if (rTextures[2][c + 1] == null) rTextures[2][c + 1] = getFrontFacingActive(c);
            if (rTextures[3][c + 1] == null) rTextures[3][c + 1] = getFrontFacingInactive(c);
            if (rTextures[4][c + 1] == null) rTextures[4][c + 1] = getTopFacingActive(c);
            if (rTextures[5][c + 1] == null) rTextures[5][c + 1] = getTopFacingInactive(c);
            if (rTextures[6][c + 1] == null) rTextures[6][c + 1] = getBottomFacingActive(c);
            if (rTextures[7][c + 1] == null) rTextures[7][c + 1] = getBottomFacingInactive(c);
            if (rTextures[8][c + 1] == null) rTextures[8][c + 1] = getBottomFacingPipeActive(c);
            if (rTextures[9][c + 1] == null) rTextures[9][c + 1] = getBottomFacingPipeInactive(c);
            if (rTextures[10][c + 1] == null) rTextures[10][c + 1] = getTopFacingPipeActive(c);
            if (rTextures[11][c + 1] == null) rTextures[11][c + 1] = getTopFacingPipeInactive(c);
            if (rTextures[12][c + 1] == null) rTextures[12][c + 1] = getSideFacingPipeActive(c);
            if (rTextures[13][c + 1] == null) rTextures[13][c + 1] = getSideFacingPipeInactive(c);
        }
        return rTextures;
    }

    public ITexture[] getSideFacingActive(byte aColor) {
        return new ITexture[] { TextureFactory
            .of(mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE) };
    }

    public ITexture[] getSideFacingInactive(byte aColor) {
        return new ITexture[] { TextureFactory
            .of(mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE) };
    }

    public ITexture[] getFrontFacingActive(byte aColor) {
        return new ITexture[] { TextureFactory
            .of(mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE) };
    }

    public ITexture[] getFrontFacingInactive(byte aColor) {
        return new ITexture[] { TextureFactory
            .of(mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE) };
    }

    public ITexture[] getTopFacingActive(byte aColor) {
        return new ITexture[] { TextureFactory
            .of(mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP : Textures.BlockIcons.MACHINE_BRONZE_TOP) };
    }

    public ITexture[] getTopFacingInactive(byte aColor) {
        return new ITexture[] { TextureFactory
            .of(mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP : Textures.BlockIcons.MACHINE_BRONZE_TOP) };
    }

    public ITexture[] getBottomFacingActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM : Textures.BlockIcons.MACHINE_BRONZE_BOTTOM) };
    }

    public ITexture[] getBottomFacingInactive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM : Textures.BlockIcons.MACHINE_BRONZE_BOTTOM) };
    }

    public ITexture[] getBottomFacingPipeActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM : Textures.BlockIcons.MACHINE_BRONZE_BOTTOM),
            TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getBottomFacingPipeInactive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM : Textures.BlockIcons.MACHINE_BRONZE_BOTTOM),
            TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getTopFacingPipeActive(byte aColor) {
        return new ITexture[] {
            TextureFactory
                .of(mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP : Textures.BlockIcons.MACHINE_BRONZE_TOP),
            TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getTopFacingPipeInactive(byte aColor) {
        return new ITexture[] {
            TextureFactory
                .of(mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP : Textures.BlockIcons.MACHINE_BRONZE_TOP),
            TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getSideFacingPipeActive(byte aColor) {
        return new ITexture[] {
            TextureFactory.of(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE),
            TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getSideFacingPipeInactive(byte aColor) {
        return new ITexture[] {
            TextureFactory.of(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE),
            TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        switch (mTier) {
            case 0 -> getBaseMetaTileEntity().add2by2Slots(builder);
            case 1 -> getBaseMetaTileEntity().add3by3Slots(builder);
            case 2 -> getBaseMetaTileEntity().add4by4Slots(builder);
            default -> getBaseMetaTileEntity().add1by1Slot(builder);
        }

        if (acceptsItemLock()) {
            builder.widget(
                new PhantomItemButton(this).setPos(getGUIWidth() - 25, 40)
                    .setBackground(PhantomItemButton.FILTER_BACKGROUND));
        }
    }

    protected ItemStack lockedItem = null;
    private static final String LOCKED_ITEM_NBT_KEY = "lockedItem";

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (lockedItem != null) {
            aNBT.setTag(LOCKED_ITEM_NBT_KEY, lockedItem.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey(LOCKED_ITEM_NBT_KEY)) {
            lockedItem = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag(LOCKED_ITEM_NBT_KEY));
        }
    }

    @Override
    public void setLockedItem(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            clearLock();
        } else {
            lockedItem = ItemHandlerHelper.copyStackWithSize(itemStack, 1);
        }
    }

    @Nullable
    @Override
    public ItemStack getLockedItem() {
        return lockedItem;
    }

    @Override
    public void clearLock() {
        lockedItem = null;
    }

    @Override
    public boolean isLocked() {
        return lockedItem != null;
    }

    @Override
    public boolean acceptsItemLock() {
        return true;
    }

    public boolean storePartial(ItemStack aStack) {
        return storePartial(aStack, false);
    }

    /**
     * Attempt to store as many items as possible into the internal inventory of this output bus. If you need atomicity
     * you should use {@link gregtech.api.interfaces.tileentity.IHasInventory#addStackToSlot(int, ItemStack)}
     *
     * @param stack    The stack to insert. Will be modified by this method (will contain whatever items could not be
     *                 inserted; stackSize will be 0 when everything was inserted).
     * @param simulate When true this bus will not be modified.
     * @return true if stack is fully accepted. false is stack is partially accepted or nothing is accepted
     */
    public boolean storePartial(ItemStack stack, boolean simulate) {
        markDirty();

        if (lockedItem != null && !lockedItem.isItemEqual(stack)) return false;

        int invLength = mInventory.length;

        for (int i = 0; i < invLength && stack.stackSize > 0; i++) {
            @Nullable
            ItemStack slot = mInventory[i];

            // the slot has an item and the stacks can't be merged; ignore it
            if (!isStackInvalid(slot) && !areStacksEqual(slot, stack)) continue;

            int inSlot = slot == null ? 0 : slot.stackSize;

            int toInsert = Math
                .min(Math.min(getInventoryStackLimit(), stack.getMaxStackSize() - inSlot), stack.stackSize);

            if (toInsert == 0) continue;

            if (!simulate) {
                // if the slot is invalid create an empty stack in it
                if (isStackInvalid(slot)) mInventory[i] = slot = stack.splitStack(0);

                slot.stackSize += toInsert;
            }

            stack.stackSize -= toInsert;
        }

        return stack.stackSize == 0;
    }
}
