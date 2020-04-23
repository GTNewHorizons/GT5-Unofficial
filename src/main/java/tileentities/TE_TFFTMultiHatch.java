package tileentities;

import java.util.List;

import blocks.Block_TFFTStorageFieldBlockT1;
import blocks.Block_TFFTStorageFieldBlockT2;
import blocks.Block_TFFTStorageFieldBlockT3;
import blocks.Block_TFFTStorageFieldBlockT4;
import blocks.Block_TFFTStorageFieldBlockT5;
import kekztech.MultiFluidHandler;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TE_TFFTMultiHatch extends TileEntity implements IFluidHandler {
	
	private static final int OUTPUT_SPEED = 1000; // L/s
	
	private MultiFluidHandler mfh;
	private int tickCounter = 0;
	
	public void setMultiFluidHandler(MultiFluidHandler mfh) {
		System.out.println("Set MFH");
		this.mfh = mfh;
	}
	
	@Override
	public void updateEntity() {
		tickCounter++;
		if(tickCounter == 100 && mfh != null) {
			
			final ForgeDirection d = getOutwardsFacingDirection();
			if(d == ForgeDirection.UNKNOWN) {
				return;
			}
			final TileEntity t = this.getWorldObj().getTileEntity(
					this.xCoord + d.offsetX, 
					this.yCoord + d.offsetY, 
					this.zCoord + d.offsetZ);
			
			if(t != null && t instanceof IFluidHandler) {
				
				final IFluidHandler fh = (IFluidHandler) t;
				
				System.out.println("Found connecting tank");
				
				// Cycle through fluids
				for(FluidStack volume : mfh.getFluids()) {
					
					// Use API methods
					if(fh.canFill(d.getOpposite(), volume.getFluid())) {
						System.out.println("Can fill " + volume.getLocalizedName());
						
						// Test how much can be output
						final FluidStack copy = volume.copy();
						copy.amount = Math.min(volume.amount, OUTPUT_SPEED);
												
						final int drawn = mfh.pullFluid(copy, false);
						copy.amount = drawn;
						
						System.out.println("Can output " + copy.amount + "L of" + copy.getLocalizedName());
						
						// Test how much can be filled (and fill if possible)
						final int filled = fh.fill(d.getOpposite(), copy, true);
						copy.amount = Math.min(drawn, filled);
						
						// Actually deplete storage
						mfh.pullFluid(copy, true);
					}
				}
			}
			
			tickCounter = 0;
		}
	}
	
	private ForgeDirection getOutwardsFacingDirection() {
		// TODO Revisit this once the hatch has a facing side
		// Look up which side has the storage field block and choose the other side.
		// This is important so the tank doesn't output into itself in case
		// there is another hatch next to this one. 
		for(ForgeDirection direction : ForgeDirection.values()) {
			
			final Block b = this.getWorldObj().getBlock(this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ);
			if(b != null && (
					b.equals(Block_TFFTStorageFieldBlockT1.getInstance())
					|| b.equals(Block_TFFTStorageFieldBlockT2.getInstance())
					|| b.equals(Block_TFFTStorageFieldBlockT3.getInstance())
					|| b.equals(Block_TFFTStorageFieldBlockT4.getInstance())
					|| b.equals(Block_TFFTStorageFieldBlockT5.getInstance()))) {
				return direction.getOpposite();
			}
		}
		return ForgeDirection.UNKNOWN;
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
				// If there's no integrated circuit in the TFFT controller, output slot 0
				final byte selectedSlot = (mfh.getSelectedFluid() == -1) ? 0 : mfh.getSelectedFluid();
				
				return new FluidStack(
						drain.getFluid(), 
						mfh.pullFluid(new FluidStack(drain.getFluid(), maxDrain), selectedSlot, doDrain)
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
