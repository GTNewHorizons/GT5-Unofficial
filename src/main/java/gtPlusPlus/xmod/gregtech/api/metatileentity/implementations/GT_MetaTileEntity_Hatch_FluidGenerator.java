package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.FluidUtils;

public abstract class GT_MetaTileEntity_Hatch_FluidGenerator extends GT_MetaTileEntity_Hatch_Input {

    protected static XSTR floatGen = new XSTR();
    public int mProgresstime = 0, mMaxProgresstime = 0;

    public GT_MetaTileEntity_Hatch_FluidGenerator(final int aID, final String aName, final String aNameRegional,
            final int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public GT_MetaTileEntity_Hatch_FluidGenerator(final String aName, final int aTier, final String[] aDescription,
            final ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public abstract String[] getCustomTooltip();

    public abstract Fluid getFluidToGenerate();

    public abstract int getAmountOfFluidToGenerate();

    public abstract int getMaxTickTime();

    @Override
    public synchronized String[] getDescription() {
        mDescriptionArray[1] = "Capacity: " + GT_Utility.formatNumbers(getCapacity()) + "L";
        final String[] hatchTierString = new String[] { "Hatch Tier: " + GT_Utility.getColoredTierNameFromTier(mTier) };

        String[] aCustomTips = getCustomTooltip();
        final String[] desc = new String[mDescriptionArray.length + aCustomTips.length + 2];
        System.arraycopy(mDescriptionArray, 0, desc, 0, mDescriptionArray.length);
        System.arraycopy(hatchTierString, 0, desc, mDescriptionArray.length, 1);
        System.arraycopy(aCustomTips, 0, desc, mDescriptionArray.length + 1, aCustomTips.length);
        desc[mDescriptionArray.length + aCustomTips.length] = CORE.GT_Tooltip.get();
        return desc;
    }

    @Override
    public ITexture[] getTexturesActive(final ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
                new GT_RenderedTexture((IIconContainer) Textures.BlockIcons.OVERLAY_MUFFLER) };
    }

    @Override
    public ITexture[] getTexturesInactive(final ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
                new GT_RenderedTexture((IIconContainer) Textures.BlockIcons.OVERLAY_MUFFLER) };
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(final ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(final EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(final int aIndex) {
        return false;
    }

    @Override
    public abstract MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity);

    public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex,
            final ForgeDirection side, final ItemStack aStack) {
        return false;
    }

    public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex,
            final ForgeDirection side, final ItemStack aStack) {
        return false;
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!aBaseMetaTileEntity.isAllowedToWork()) {
            aBaseMetaTileEntity.setActive(false);
            mProgresstime = 0;
            mMaxProgresstime = 0;
        } else {
            aBaseMetaTileEntity.setActive(true);
            mMaxProgresstime = getMaxTickTime();
            if (++mProgresstime >= mMaxProgresstime) {
                if (this.canTankBeFilled()) {
                    addFluidToHatch(aTick);
                }
                mProgresstime = 0;
            }
        }
    }

    @Override
    public int getProgresstime() {
        return mProgresstime;
    }

    @Override
    public int maxProgresstime() {
        return mMaxProgresstime;
    }

    @Override
    public int increaseProgress(int aProgress) {
        mProgresstime += aProgress;
        return mMaxProgresstime - mProgresstime;
    }

    public abstract void generateParticles(final World aWorld, final String name);

    @Override
    public int getTankPressure() {
        return 100;
    }

    @Override
    public abstract int getCapacity();

    @Override
    public boolean canTankBeEmptied() {
        return true;
    }

    public abstract boolean doesHatchMeetConditionsToGenerate();

    public boolean addFluidToHatch(long aTick) {
        if (!doesHatchMeetConditionsToGenerate()) {
            return false;
        }
        int aFillAmount = this.fill(FluidUtils.getFluidStack(getFluidToGenerate(), getAmountOfFluidToGenerate()), true);
        if (aFillAmount > 0) {
            if (this.getBaseMetaTileEntity().isClientSide()) {
                generateParticles(this.getBaseMetaTileEntity().getWorld(), "cloud");
            }
        }
        return aFillAmount > 0;
    }

    @Override
    public boolean canTankBeFilled() {
        return true;
    }

    @Override
    public boolean doesEmptyContainers() {
        return false;
    }

    @Override
    public boolean doesFillContainers() {
        return true;
    }

    @Override
    public int fill(FluidStack aFluid, boolean doFill) {
        if (aFluid == null || aFluid.getFluid().getID() <= 0
                || aFluid.amount <= 0
                || aFluid.getFluid() != getFluidToGenerate()
                || !canTankBeFilled()) {
            return 0;
        }

        if (getFillableStack() == null || getFillableStack().getFluid().getID() <= 0) {
            if (aFluid.amount <= getCapacity()) {
                if (doFill) {
                    setFillableStack(aFluid.copy());
                    getBaseMetaTileEntity().markDirty();
                }
                return aFluid.amount;
            }
            if (doFill) {
                setFillableStack(aFluid.copy());
                getFillableStack().amount = getCapacity();
                getBaseMetaTileEntity().markDirty();
            }
            return getCapacity();
        }

        if (!getFillableStack().isFluidEqual(aFluid)) return 0;

        int space = getCapacity() - getFillableStack().amount;
        if (aFluid.amount <= space) {
            if (doFill) {
                getFillableStack().amount += aFluid.amount;
                getBaseMetaTileEntity().markDirty();
            }
            return aFluid.amount;
        }
        if (doFill) getFillableStack().amount = getCapacity();
        return space;
    }

    @Override
    public boolean canFill(ForgeDirection aSide, Fluid aFluid) {
        return false;
    }

    @Override
    public int fill(ForgeDirection arg0, FluidStack arg1, boolean arg2) {
        return 0;
    }

    @Override
    public int fill_default(ForgeDirection aSide, FluidStack aFluid, boolean doFill) {
        return 0;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mProgresstime", mProgresstime);
        aNBT.setInteger("mMaxProgresstime", mMaxProgresstime);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mProgresstime = aNBT.getInteger("mProgresstime");
        mMaxProgresstime = aNBT.getInteger("mMaxProgresstime");
        super.loadNBTData(aNBT);
    }
}
