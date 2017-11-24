package gtPlusPlus.core.tileentities.general;

import gtPlusPlus.core.util.enchantment.EnchantmentUtils;
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

public class TileEntityXpConverter extends TileEntity implements IFluidHandler {

	public FluidTank tankEssence = new FluidTank((int) (64000*EnchantmentUtils.RATIO_MOB_ESSENCE_TO_LIQUID_XP));
	public FluidTank tankLiquidXp = new FluidTank(64000);
	private boolean needsUpdate = false;
	private int updateTimer = 0;

	public TileEntityXpConverter() {
	}

	@Override
	public int fill(final ForgeDirection from, final FluidStack resource, final boolean doFill) {
		this.needsUpdate = true;
		if (resource.isFluidEqual(EnchantmentUtils.getLiquidXP(1))){
			return this.tankLiquidXp.fill(resource, doFill);
		}
		else if (resource.isFluidEqual(EnchantmentUtils.getMobEssence(1))){
			return this.tankEssence.fill(resource, doFill);
		}
		else {
			return 0;
		}
	}

	@Override
	public FluidStack drain(final ForgeDirection from, final FluidStack resource, final boolean doDrain) {
		this.needsUpdate = true;
		if (resource.isFluidEqual(EnchantmentUtils.getLiquidXP(1))){
			return this.tankLiquidXp.drain(resource.amount, doDrain);
		}
		else if (resource.isFluidEqual(EnchantmentUtils.getMobEssence(1))){
			return this.tankEssence.drain(resource.amount, doDrain);
		}
		else {
			return null;
		}

	}

	@Override
	public FluidStack drain(final ForgeDirection from, final int maxDrain, final boolean doDrain) {
		this.needsUpdate = true;
		final FluidStack fluid_Essence = this.tankEssence.getFluid();
		final FluidStack fluid_Xp = this.tankLiquidXp.getFluid();
		if ((fluid_Essence == null) && (fluid_Xp == null)) {
			return null;
		}

		FluidStack fluid;
		FluidTank tank;

		if ((from == ForgeDirection.UP) || (from == ForgeDirection.DOWN)){
			fluid = fluid_Essence;
			tank = this.tankEssence;
		}
		else {
			fluid = fluid_Xp;
			tank = this.tankLiquidXp;
		}

		int drained = maxDrain;
		if (fluid.amount < drained) {
			drained = fluid.amount;
		}

		final FluidStack stack = new FluidStack(fluid, drained);
		if (doDrain) {
			fluid.amount -= drained;
			if (fluid.amount <= 0) {
				fluid = null;
			}

			if (this != null) {
				FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(fluid, this.getWorldObj(), this.xCoord,
						this.yCoord, this.zCoord, tank, 0));
			}
		}
		return stack;
	}

	@Override
	public boolean canFill(final ForgeDirection from, final Fluid fluid) {
		return true;
	}

	@Override
	public boolean canDrain(final ForgeDirection from, final Fluid fluid) {
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(final ForgeDirection from) {
		if ((from == ForgeDirection.UP) || (from == ForgeDirection.DOWN)){
			return new FluidTankInfo[] { this.tankEssence.getInfo() };
		}
		else {
			return new FluidTankInfo[] { this.tankLiquidXp.getInfo() };
		}
	}

	public float getAdjustedVolume() {
		this.needsUpdate = true;
		final float amount = this.tankLiquidXp.getFluidAmount();
		final float capacity = this.tankLiquidXp.getCapacity();
		final float volume = (amount / capacity) * 0.8F;
		return volume;
	}

	@Override
	public void updateEntity() {

		if (this.tankEssence.getFluid() != null){
			final FluidStack bigStorage = this.tankEssence.getFluid();
			bigStorage.amount = this.tankEssence.getCapacity();
			this.tankEssence.setFluid(bigStorage);
		}

		if (this.tankLiquidXp.getFluid() != null){
			final FluidStack bigStorage = this.tankLiquidXp.getFluid();
			bigStorage.amount = this.tankLiquidXp.getCapacity();
			this.tankLiquidXp.setFluid(bigStorage);
		}

		if (this.needsUpdate) {

			if (this.tankEssence.getFluid() != null){
				final FluidStack bigStorage = this.tankEssence.getFluid();
				bigStorage.amount = this.tankEssence.getCapacity();
				this.tankEssence.setFluid(bigStorage);
			}

			if (this.tankLiquidXp.getFluid() != null){
				final FluidStack bigStorage = this.tankLiquidXp.getFluid();
				bigStorage.amount = this.tankLiquidXp.getCapacity();
				this.tankLiquidXp.setFluid(bigStorage);
			}

			if (this.updateTimer == 0) {
				this.updateTimer = 10; // every 10 ticks it will send an update
			} else {
				--this.updateTimer;
				if (this.updateTimer == 0) {
					this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
					this.needsUpdate = false;
				}
			}
		}
	}

	@Override
	public void readFromNBT(final NBTTagCompound tag) {
		this.tankEssence.readFromNBT(tag);
		this.tankLiquidXp.readFromNBT(tag);
		super.readFromNBT(tag);
	}

	@Override
	public void writeToNBT(final NBTTagCompound tag) {
		this.tankEssence.writeToNBT(tag);
		this.tankLiquidXp.writeToNBT(tag);
		super.writeToNBT(tag);
	}

	@Override
	public Packet getDescriptionPacket() {
		final NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, this.blockMetadata, tag);
	}

	@Override
	public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity pkt) {
		final NBTTagCompound tag = pkt.func_148857_g();
		this.readFromNBT(tag);
	}

}
