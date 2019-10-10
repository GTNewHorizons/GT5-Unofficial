package tileentities;

import java.util.List;

import kekztech.MultiFluidHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TE_TFFTMultiHatch extends TileEntity implements IFluidHandler {
	
	private MultiFluidHandler mfh;
	
	public void setMultiFluidHandler(MultiFluidHandler mfh) {
		this.mfh = mfh;
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return (mfh != null) ? mfh.pushFluid(resource, doFill) : 0; 
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return (mfh != null) ? new FluidStack(resource.getFluid(), mfh.pullFluid(resource, doDrain)) : null; 
	}
	
	/**
     * Drains fluid out of 0th internal tank.
     * 
     * @param from
     *            Orientation the fluid is drained to.
     * @param maxDrain
     *            Maximum amount of fluid to drain.
     * @param doDrain
     *            If false, drain will only be simulated.
     * @return FluidStack representing the Fluid and amount that was (or would have been, if
     *         simulated) drained.
     */
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if(mfh != null) {
			final FluidStack drain = mfh.getFluid(0);
			if(drain != null) {
				return new FluidStack(
						drain.getFluid(), 
						mfh.pullFluid(new FluidStack(drain.getFluid(), maxDrain), 0, doDrain)
						);			
			}
		}
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return (mfh != null) ? mfh.couldPush(new FluidStack(fluid, 1)) : false; 
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return (mfh != null) ? mfh.contains(new FluidStack(fluid, 1)) : false; 
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		if(mfh == null) {
			return null;
		}
		final List<FluidStack> fluids = mfh.getFluids();
		final FluidTankInfo[] tankInfo = new FluidTankInfo[fluids.size()];
		for(int i = 0; i < tankInfo.length; i++) {
			tankInfo[i] = new FluidTankInfo(fluids.get(i), mfh.getCapacity());
		}
		return tankInfo;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt = (nbt == null) ? new NBTTagCompound() : nbt;
		
		super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		nbt = (nbt == null) ? new NBTTagCompound() : nbt;
		
		super.readFromNBT(nbt);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
