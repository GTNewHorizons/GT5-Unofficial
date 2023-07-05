package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.ITEM_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;
import static gregtech.api.util.GT_Utility.moveMultipleItemStacks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.GT_Mod;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.extensions.ArrayExt;

public class GT_MetaTileEntity_Hatch_OutputBus extends GT_MetaTileEntity_Hatch {

    public GT_MetaTileEntity_Hatch_OutputBus(int aID, String aName, String aNameRegional, int aTier) {
        this(aID, aName, aNameRegional, aTier, getSlots(aTier));
    }

    public GT_MetaTileEntity_Hatch_OutputBus(int id, String name, String nameRegional, int tier, int slots) {
        super(
            id,
            name,
            nameRegional,
            tier,
            slots,
            ArrayExt.of(
                "Item Output for Multiblocks",
                "Capacity: " + getSlots(tier) + " stack" + (getSlots(tier) >= 2 ? "s" : "")));
    }

    public GT_MetaTileEntity_Hatch_OutputBus(int aID, String aName, String aNameRegional, int aTier,
        String[] aDescription) {
        super(aID, aName, aNameRegional, aTier, getSlots(aTier), aDescription);
    }

    public GT_MetaTileEntity_Hatch_OutputBus(int aID, String aName, String aNameRegional, int aTier,
        String[] aDescription, int inventorySize) {
        super(aID, aName, aNameRegional, aTier, inventorySize, aDescription);
    }

    @Deprecated
    // having too many constructors is bad, don't be so lazy, use GT_MetaTileEntity_Hatch_OutputBus(String, int,
    // String[], ITexture[][][])
    public GT_MetaTileEntity_Hatch_OutputBus(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        this(aName, aTier, getSlots(aTier), ArrayExt.of(aDescription), aTextures);
    }

    public GT_MetaTileEntity_Hatch_OutputBus(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, getSlots(aTier), aDescription, aTextures);
    }

    public GT_MetaTileEntity_Hatch_OutputBus(String name, int tier, int slots, String[] description,
        ITexture[][][] textures) {
        super(name, tier, slots, description, textures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return GT_Mod.gregtechproxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT), TextureFactory.of(ITEM_OUT_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return GT_Mod.gregtechproxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT), TextureFactory.of(ITEM_OUT_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_OutputBus(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    /**
     * Attempt to store as many items as possible into the internal inventory of this output bus. If you need atomicity
     * you should use {@link gregtech.api.interfaces.tileentity.IHasInventory#addStackToSlot(int, ItemStack)}
     *
     * @param aStack Assume valid. Will be mutated. Take over the ownership. Caller should not retain a reference to
     *               this stack if the call returns true.
     * @return true if stack is fully accepted. false is stack is partially accepted or nothing is accepted
     */
    public boolean storeAll(ItemStack aStack) {
        markDirty();
        for (int i = 0, mInventoryLength = mInventory.length; i < mInventoryLength && aStack.stackSize > 0; i++) {
            ItemStack tSlot = mInventory[i];
            if (GT_Utility.isStackInvalid(tSlot)) {
                if (aStack.stackSize <= getInventoryStackLimit()) {
                    mInventory[i] = aStack;
                    return true;
                }
                mInventory[i] = aStack.splitStack(getInventoryStackLimit());
            } else {
                int tRealStackLimit = Math.min(getInventoryStackLimit(), tSlot.getMaxStackSize());
                if (tSlot.stackSize < tRealStackLimit && tSlot.isItemEqual(aStack)
                    && ItemStack.areItemStackTagsEqual(tSlot, aStack)) {
                    if (aStack.stackSize + tSlot.stackSize <= tRealStackLimit) {
                        mInventory[i].stackSize += aStack.stackSize;
                        return true;
                    } else {
                        // more to serve
                        aStack.stackSize -= tRealStackLimit - tSlot.stackSize;
                        mInventory[i].stackSize = tRealStackLimit;
                    }
                }
            }
        }
        return false;
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

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork() && (aTick & 0x7) == 0) {
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

    // @Override
    // public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
    // switch (mTier) {
    // case 0 -> getBaseMetaTileEntity().add1by1Slot(builder);
    // case 1 -> getBaseMetaTileEntity().add2by2Slots(builder);
    // case 2 -> getBaseMetaTileEntity().add3by3Slots(builder);
    // default -> getBaseMetaTileEntity().add4by4Slots(builder);
    // }
    // }
}
