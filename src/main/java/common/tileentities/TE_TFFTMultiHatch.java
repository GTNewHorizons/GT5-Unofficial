package common.tileentities;

import kekztech.MultiFluidHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

@Deprecated
public class TE_TFFTMultiHatch extends TileEntity implements IFluidHandler {
	
	public static final int BASE_OUTPUT_PER_SECOND = 2000; // L/s

	private byte facings = 0x0;

	private MultiFluidHandler mfh;
	private int tickCounter = 0;
	private boolean autoOutput = false;

	public boolean hasFacingOnSide(byte side) {
		final byte key = (byte) Math.pow(0x2, side);
		return (facings & key) == key;
	}

	public void setFacingToSide(byte side) {
		facings = (byte) Math.pow(0x2, side);
	}

	public void setMultiFluidHandler(MultiFluidHandler mfh) {
		this.mfh = mfh;
	}
	
	public void toggleAutoOutput() {
		autoOutput = !autoOutput;
	}
	
	public boolean isOutputting() {
		return autoOutput;
	}
	
	@Override
	public void updateEntity() {
		// Removed deprecated code
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		// Removed deprecated code
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		// Removed deprecated code
		return null;
	}
	
	/**
     * Drains fluid out of 0th internal tank. 
     * If the TFFT Controller contains an Integrated Circuit, drain fluid
     * from the slot equal to the circuit configuration.
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
		// Removed deprecated code
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		// Removed deprecated code
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		// Removed deprecated code
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		// Removed deprecated code
		return null;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("autoOutput", autoOutput);
		nbt.setByte("facings", facings);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		autoOutput = nbt.getBoolean("autoOutput");
		facings = nbt.getByte("facings");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
