package gtPlusPlus.core.tileentities.general;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityInfiniteFluid extends TileEntity implements IFluidHandler {

	public FluidTank tank = new FluidTank(Integer.MAX_VALUE);
	private boolean needsUpdate = false;
	private int updateTimer = 0;

	public TileEntityInfiniteFluid() {
	}

	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		needsUpdate = true;
		return this.tank.fill(resource, doFill);
	}

	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return this.tank.drain(resource.amount, doDrain);
	}

	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		needsUpdate = true;
		FluidStack fluid = this.tank.getFluid();
		// return this.tank.drain(maxDrain, doDrain);
		if (fluid == null) {
			return null;
		}

		int drained = maxDrain;
		if (fluid.amount < drained) {
			drained = fluid.amount;
		}

		FluidStack stack = new FluidStack(fluid, drained);
		if (doDrain) {
			fluid.amount -= drained;
			if (fluid.amount <= 0) {
				fluid = null;
			}

			if (this != null) {
				FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(fluid, this.getWorldObj(), this.xCoord,
						this.yCoord, this.zCoord, this.tank, 0));
			}
		}
		return stack;
	}

	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return true;
	}

	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}

	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { this.tank.getInfo() };
	}

	public float getAdjustedVolume() {
		float amount = tank.getFluidAmount();
		float capacity = tank.getCapacity();
		float volume = (amount / capacity) * 0.8F;
		return volume;
	}

	public void updateEntity() {
		if (needsUpdate) {
			
			if (this.tank.getFluid() != null){
				FluidStack bigStorage = this.tank.getFluid();
				bigStorage.amount = this.tank.getCapacity();
				this.tank.setFluid(bigStorage);
			}
			
			if (updateTimer == 0) {
				updateTimer = 10; // every 10 ticks it will send an update
			} else {
				--updateTimer;
				if (updateTimer == 0) {
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					needsUpdate = false;
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		tank.readFromNBT(tag);
		super.readFromNBT(tag);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tank.writeToNBT(tag);
		super.writeToNBT(tag);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, this.blockMetadata, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound tag = pkt.func_148857_g();
		readFromNBT(tag);
	}

}
