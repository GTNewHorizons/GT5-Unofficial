package gtPlusPlus.core.tileentities.general;

import org.lwjgl.input.Keyboard;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.EnchantingUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileEntityXpConverter extends TileEntity implements IFluidHandler {

	public FluidTank tankEssence = new FluidTank((int) (64000*EnchantingUtils.RATIO_MOB_ESSENCE_TO_LIQUID_XP));
	public FluidTank tankLiquidXp = new FluidTank(64000);
	private boolean needsUpdate = false;
	private boolean mConvertToEssence = true;
	private int updateTimer = 0;
	private long mTickTime = 0;

	public TileEntityXpConverter() {
	}

	private void changeMode(){
		if (this.mConvertToEssence){
			this.mConvertToEssence = false;
			return;
		}
		else {
			this.mConvertToEssence = true;
			return;
		}
	}

	private boolean isServerSide(){
		if (this.getWorldObj().isRemote){
			return false;
		}
		else {
			return true;
		}
	}

	@Override
	public int fill(final ForgeDirection from, final FluidStack resource, final boolean doFill) {
		this.needsUpdate = true;
		Logger.WARNING("Ticking. | mConvertToEssence: "+this.mConvertToEssence);
		if (this.mConvertToEssence){
			if (resource.isFluidEqual(EnchantingUtils.getLiquidXP(1))){
				Logger.WARNING("fill(tankLiquidXp)");
				return this.tankLiquidXp.fill(resource, doFill);
			}
			else {
				Logger.WARNING("Looking for Liquid Xp, Instead found "+resource.getLocalizedName()+".");
			}
		}
		else {
			if (resource.isFluidEqual(EnchantingUtils.getMobEssence(1))){
				Logger.WARNING("fill(tankEssence)");
				return this.tankEssence.fill(resource, doFill);
			}
			else {
				Logger.WARNING("Looking for Essence, Instead found "+resource.getLocalizedName()+".");
			}
		}
		Logger.WARNING("fill(0)");
		return 0;
	}

	@Override
	public FluidStack drain(final ForgeDirection from, final FluidStack resource, final boolean doDrain) {
		this.needsUpdate = true;
		if (this.mConvertToEssence){
			if (resource.isFluidEqual(EnchantingUtils.getMobEssence(1))){
				Logger.WARNING("drain(mConvertToEssence)");
				return this.tankEssence.drain(resource.amount, doDrain);
			}
		}
		else {
			if (resource.isFluidEqual(EnchantingUtils.getLiquidXP(1))){
				Logger.WARNING("drain(tankLiquidXp)");
				return this.tankLiquidXp.drain(resource.amount, doDrain);
			}
		}
		Logger.WARNING("drain(null)");
		return null;
	}

	@Override
	public FluidStack drain(final ForgeDirection from, final int maxDrain, final boolean doDrain) {
		this.needsUpdate = true;
		Logger.WARNING("drain(Ex)");
		final FluidStack fluid_Essence = this.tankEssence.getFluid();
		final FluidStack fluid_Xp = this.tankLiquidXp.getFluid();
		if ((fluid_Essence == null) && (fluid_Xp == null)) {
			return null;
		}

		FluidStack fluid;
		FluidTank tank;

		if (this.mConvertToEssence){
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


		if (this.mConvertToEssence){
			this.tankEssence = tank;
		}
		else {
			this.tankLiquidXp = tank;
		}

		Logger.WARNING("drain(Ex2)");
		return stack;
	}

	@Override
	public boolean canFill(final ForgeDirection from, final Fluid fluid) {
		if (this.mConvertToEssence){
			if (this.tankEssence.getFluidAmount() < this.tankEssence.getCapacity()){
				Logger.WARNING("canFill(mConvertToEssence)");
				return true;
			}
		}
		else {
			if (this.tankLiquidXp.getFluidAmount()  < this.tankLiquidXp.getCapacity()){
				Logger.WARNING("canFill(tankLiquidXp)");
				return true;
			}
		}
		Logger.WARNING("canFill(false)");
		return false;
	}

	@Override
	public boolean canDrain(final ForgeDirection from, final Fluid fluid) {
		if (this.mConvertToEssence){
			if (this.tankEssence.getFluidAmount() > 0){
				return true;
			}
		}
		else {
			if (this.tankLiquidXp.getFluidAmount() > 0){
				return true;
			}
		}
		Logger.WARNING("canDrain(false)");
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(final ForgeDirection from) {
		if (this.mConvertToEssence){
			return new FluidTankInfo[] { this.tankEssence.getInfo() };
		}
		else {
			return new FluidTankInfo[] { this.tankLiquidXp.getInfo() };
		}
	}

	public float getAdjustedVolume() {
		Logger.WARNING("AdjustedVolume()");
		this.needsUpdate = true;
		final float amount = this.tankLiquidXp.getFluidAmount();
		final float capacity = this.tankLiquidXp.getCapacity();
		final float volume = (amount / capacity) * 0.8F;
		return volume;
	}

	@Override
	public void updateEntity() {

		if (this.isServerSide()){
			this.mTickTime++;

			if (this.needsUpdate) {
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


			if (this.mConvertToEssence){
				if ((this.tankLiquidXp.getFluid() != null) && (this.tankLiquidXp.getFluidAmount() >= 100) && (this.tankEssence.getFluidAmount() <= (this.tankEssence.getCapacity()-(100*EnchantingUtils.RATIO_MOB_ESSENCE_TO_LIQUID_XP)))){
					final FluidStack bigStorage = EnchantingUtils.getEssenceFromLiquidXp(100);
					this.tankEssence.fill(bigStorage, true);
					this.tankLiquidXp.drain(100, true);
					this.needsUpdate = true;
					Logger.WARNING("B->A");
				}
			}
			else {
				final double rm = EnchantingUtils.RATIO_MOB_ESSENCE_TO_LIQUID_XP;
				if ((this.tankEssence.getFluid() != null) && (this.tankEssence.getFluidAmount() >= rm) && (this.tankLiquidXp.getFluidAmount() <= (this.tankLiquidXp.getCapacity()-rm))){
					final FluidStack bigStorage = EnchantingUtils.getLiquidXP(1);
					this.tankLiquidXp.fill(bigStorage, true);
					this.tankEssence.drain((int) rm, true);
					this.needsUpdate = true;
					Logger.WARNING("A->B");
				}
			}
		}
		else {
		}
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		this.markDirty();

		if ((this.mTickTime % 20) == 0){

		}

	}

	@Override
	public void readFromNBT(final NBTTagCompound tag) {
		this.tankEssence.readFromNBT(tag);
		this.tankLiquidXp.readFromNBT(tag);
		this.mConvertToEssence = tag.getBoolean("mConvertToEssence");
		super.readFromNBT(tag);
	}

	@Override
	public void writeToNBT(final NBTTagCompound tag) {
		this.tankEssence.writeToNBT(tag);
		this.tankLiquidXp.writeToNBT(tag);
		tag.setBoolean("mConvertToEssence", this.mConvertToEssence);
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

	public void onScrewdriverRightClick(final byte aSide, final EntityPlayer aPlayer, final float aX, final float aY, final float aZ) {
		if (this.isServerSide()){
			if (this.mConvertToEssence){
				PlayerUtils.messagePlayer(aPlayer, "Converting from Mob Essence to Liquid Xp.");
			}
			else {
				PlayerUtils.messagePlayer(aPlayer, "Converting from Liquid Xp to Mob Essence.");
			}
			//Mode Change
			this.changeMode();
		}
	}

	public void onRightClick(final byte aSide, final EntityPlayer aPlayer, final int aX, final int aY, final int aZ) {

		if ((Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54))) {
			String mInput;
			String mOutput;

			if (this.mConvertToEssence){
				mInput = "Liquid Xp";
				mOutput = "Mob Essence";
			}
			else {
				mInput = "Mob Essence";
				mOutput = "Liquid Xp";
			}

			PlayerUtils.messagePlayer(aPlayer, "Input: "+mInput+".");
			PlayerUtils.messagePlayer(aPlayer, "Output: "+mOutput+".");
		}

	}

}
