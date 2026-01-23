package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.Arrays;
import java.util.stream.Stream;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.objects.XSTR;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;

public abstract class MTEHatchFluidGenerator extends MTEHatchInput {

    protected static XSTR floatGen = new XSTR();
    public int mProgresstime = 0, mMaxProgresstime = 0;

    public MTEHatchFluidGenerator(final int aID, final String aName, final String aNameRegional, final int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchFluidGenerator(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public abstract String[] getCustomTooltip();

    public abstract Fluid getFluidToGenerate();

    public abstract int getAmountOfFluidToGenerate();

    public abstract int getMaxTickTime();

    @Override
    public synchronized String[] getDescription() {
        return Stream
            .concat(
                Stream.of(
                    mDescriptionArray[0],
                    "Capacity: " + formatNumber(getCapacity()) + "L",
                    "Hatch Tier: " + GTUtility.getColoredTierNameFromTier(mTier)),
                Arrays.stream(getCustomTooltip()))
            .toArray(String[]::new);
    }

    @Override
    public ITexture[] getTexturesActive(final ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_MUFFLER) };
    }

    @Override
    public ITexture[] getTexturesInactive(final ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_MUFFLER) };
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
    public abstract int getCapacity();

    public abstract boolean doesHatchMeetConditionsToGenerate();

    public boolean addFluidToHatch(long aTick) {
        if (!doesHatchMeetConditionsToGenerate()) {
            return false;
        }
        int aFillAmount = this.fill(new FluidStack(getFluidToGenerate(), getAmountOfFluidToGenerate()), true);
        if (aFillAmount > 0) {
            if (this.getBaseMetaTileEntity()
                .isClientSide()) {
                generateParticles(
                    this.getBaseMetaTileEntity()
                        .getWorld(),
                    "cloud");
            }
        }
        return aFillAmount > 0;
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
        if (aFluid == null || aFluid.getFluid()
            .getID() <= 0 || aFluid.amount <= 0 || aFluid.getFluid() != getFluidToGenerate() || !canTankBeFilled()) {
            return 0;
        }

        if (getFillableStack() == null || getFillableStack().getFluid()
            .getID() <= 0) {
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
    public int fill(ForgeDirection side, FluidStack aFluid, boolean doFill) {
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
