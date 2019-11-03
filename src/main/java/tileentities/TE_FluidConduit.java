package tileentities;

import kekztech.ConduitNetworkController;
import kekztech.IConduit;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TE_FluidConduit extends TileEntity implements IConduit, IFluidHandler {
	
	private final Number capacity;
	
	private ConduitNetworkController network;
	
	public TE_FluidConduit(Number capacity) {
		this.capacity = capacity;
	}
	
	@Override
	public void setNetwork(ConduitNetworkController network) {
		this.network = network;
	}
	
	@Override
	public ConduitNetworkController getNetwork() {
		return network;
	}
	
	@Override
	public Number getCapacity() {
		return capacity;
	}

	@Override
	public void onOverload() {
		
	}

	@Override
	public boolean testForInputFilter(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean testForOutputFilter(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		// TODO Auto-generated method stub
		return null;
	}

}
