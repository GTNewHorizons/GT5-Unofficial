package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import java.lang.reflect.Field;

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
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.random.XSTR;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public abstract class GT_MetaTileEntity_Hatch_FluidGenerator extends GT_MetaTileEntity_Hatch_Input {

    protected static XSTR floatGen = new XSTR();
    public int mProgresstime = 0, mMaxProgresstime = 0;

    public GT_MetaTileEntity_Hatch_FluidGenerator(final int aID, final String aName, final String aNameRegional,
            final int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public GT_MetaTileEntity_Hatch_FluidGenerator(final String aName, final int aTier, final String aDescription,
            final ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    private static String[] S;
    private static Field F;

    public abstract String[] getCustomTooltip();

    public abstract Fluid getFluidToGenerate();

    public abstract int getAmountOfFluidToGenerate();

    public abstract int getMaxTickTime();

    @Override
    public synchronized String[] getDescription() {
        try {
            if (F == null || S == null) {
                Field t = ReflectionUtils.getField(this.getClass(), "mDescriptionArray");
                if (t != null) {
                    F = t;
                } else {
                    F = ReflectionUtils.getField(this.getClass(), "mDescription");
                }
                if (S == null && F != null) {
                    Object o = F.get(this);
                    if (o instanceof String[]) {
                        S = (String[]) o;
                    } else if (o instanceof String) {
                        S = new String[] { (String) o };
                    }
                }
            }
        } catch (Throwable t) {
            Logger.INFO("1");
            t.printStackTrace();
        }
        try {
            if (S != null) {
                String[] aCustomTips = getCustomTooltip();
                final String[] desc = new String[S.length + aCustomTips.length + 1];
                System.arraycopy(S, 0, desc, 0, S.length);
                for (int i = 0; i < aCustomTips.length; i++) {
                    desc[S.length + i] = aCustomTips[i];
                }
                desc[S.length + aCustomTips.length] = CORE.GT_Tooltip.get();
                return desc;
            }
        } catch (Throwable t) {
            Logger.INFO("2");
            t.printStackTrace();
        }

        return new String[] { "Broken Tooltip - Report on Github" };
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
        if (this.getCapacity() - this.getFluidAmount() > 0) {
            // Logger.INFO("Total Space: "+this.getCapacity());
            // Logger.INFO("Current amount: "+this.getFluidAmount());
            // Logger.INFO("To add: "+this.getAmountOfFluidToGenerate());
            // Logger.INFO("Space Free: "+(this.getCapacity()-this.getFluidAmount()));
        }
        if (this.mFluid == null || (this.mFluid != null
                && (this.getCapacity() - this.getFluidAmount() >= this.getAmountOfFluidToGenerate()))) {
            return true;
        }
        return false;
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
