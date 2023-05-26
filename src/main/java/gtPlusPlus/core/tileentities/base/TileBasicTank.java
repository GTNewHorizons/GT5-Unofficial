package gtPlusPlus.core.tileentities.base;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

import gregtech.common.covers.CoverInfo;
import gtPlusPlus.api.objects.minecraft.BTF_FluidTank;

public class TileBasicTank extends TileEntityBase implements IFluidHandler, IFluidTank {

    public final BTF_FluidTank mTank;

    public TileBasicTank(int aMaxSlots, int aFluidCapacity) {
        super(aMaxSlots);
        mTank = new BTF_FluidTank(aFluidCapacity);
    }

    @Override
    public boolean onPreTick(long aTick) {

        if (this.isServerSide()) {
            if (mTank.isFluidChangingAllowed() && mTank.getFillableStack() != null
                    && mTank.getFillableStack().amount <= 0) {
                mTank.setFillableStack((FluidStack) null);
            }
        }

        return super.onPreTick(aTick);
    }

    private final boolean canFillEx(ForgeDirection aSide, Fluid aFluid) {
        return this.fill(aSide, new FluidStack(aFluid, 1), false) == 1;
    }

    private final boolean canDrainEx(ForgeDirection aSide, Fluid aFluid) {
        return this.drain(aSide, new FluidStack(aFluid, 1), false) != null;
    }

    private final FluidTankInfo[] getTankInfoEx(ForgeDirection aSide) {
        return mTank.getCapacity() <= 0 ? new FluidTankInfo[0] : new FluidTankInfo[] { mTank.getInfo() };
    }

    private final int fill_default(ForgeDirection aSide, FluidStack aFluid, boolean doFill) {
        return mTank.fill(aFluid, doFill);
    }

    private final int fillEx(ForgeDirection aSide, FluidStack aFluid, boolean doFill) {
        return this.fill_default(aSide, aFluid, doFill);
    }

    private final FluidStack drainEx(ForgeDirection aSide, FluidStack aFluid, boolean doDrain) {
        return mTank.getFluid() != null && aFluid != null && mTank.getFluid().isFluidEqual(aFluid)
                ? mTank.drain(aFluid.amount, doDrain)
                : null;
    }

    private final FluidStack drainEx(ForgeDirection aSide, int maxDrain, boolean doDrain) {
        return mTank.drain(maxDrain, doDrain);
    }

    public boolean isLiquidInput(ForgeDirection side) {
        return true;
    }

    public boolean isLiquidOutput(ForgeDirection side) {
        return true;
    }

    @Override
    public int fill(ForgeDirection side, FluidStack aFluid, boolean doFill) {
        if (mTickTimer > 5 && canAccessData()
                && (mRunningThroughTick || !mInputDisabled)
                && (side == ForgeDirection.UNKNOWN || (this.isLiquidInput(side)
                        && getCoverInfoAtSide(side).letsFluidIn(aFluid == null ? null : aFluid.getFluid()))))
            return this.fillEx(side, aFluid, doFill);
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection side, int maxDrain, boolean doDrain) {
        if (mTickTimer > 5 && canAccessData()
                && (mRunningThroughTick || !mOutputDisabled)
                && (side == ForgeDirection.UNKNOWN || (this.isLiquidOutput(side) && getCoverInfoAtSide(side)
                        .letsFluidOut(this.getFluid() == null ? null : this.getFluid().getFluid()))))
            return this.drainEx(side, maxDrain, doDrain);
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection side, FluidStack aFluid, boolean doDrain) {
        if (mTickTimer > 5 && canAccessData()
                && (mRunningThroughTick || !mOutputDisabled)
                && (side == ForgeDirection.UNKNOWN || (this.isLiquidOutput(side)
                        && getCoverInfoAtSide(side).letsFluidOut(aFluid == null ? null : aFluid.getFluid()))))
            return this.drainEx(side, aFluid, doDrain);
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection side, Fluid aFluid) {
        if (mTickTimer > 5 && canAccessData()
                && (mRunningThroughTick || !mInputDisabled)
                && (side == ForgeDirection.UNKNOWN
                        || (this.isLiquidInput(side) && getCoverInfoAtSide(side).letsFluidIn(aFluid))))
            return this.canFillEx(side, aFluid);
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection side, Fluid aFluid) {
        if (mTickTimer > 5 && canAccessData()
                && (mRunningThroughTick || !mOutputDisabled)
                && (side == ForgeDirection.UNKNOWN
                        || (this.isLiquidOutput(side) && getCoverInfoAtSide(side).letsFluidOut(aFluid))))
            return this.canDrainEx(side, aFluid);
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection side) {
        CoverInfo coverInfo = getCoverInfoAtSide(side);
        if (canAccessData()
                && (side == ForgeDirection.UNKNOWN || (this.isLiquidInput(side) && coverInfo.letsFluidIn(null))
                        || (this.isLiquidOutput(side) && coverInfo.letsFluidOut(null))))
            return this.getTankInfoEx(side);
        return new FluidTankInfo[] {};
    }

    @Override
    public FluidStack getFluid() {
        return mTank.getFluid();
    }

    @Override
    public int getFluidAmount() {
        return mTank.getFluidAmount();
    }

    @Override
    public FluidTankInfo getInfo() {
        return mTank.getInfo();
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return mTank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return mTank.drain(maxDrain, doDrain);
    }
}
