package gtPlusPlus.core.tileentities.general;

import org.lwjgl.input.Keyboard;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import gtPlusPlus.api.objects.minecraft.BTF_FluidTank;
import gtPlusPlus.core.tileentities.base.TileBasicTank;
import gtPlusPlus.core.util.minecraft.EnchantingUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileEntityXpConverter extends TileBasicTank {

	public BTF_FluidTank tankEssence = new BTF_FluidTank((int) (64000*EnchantingUtils.RATIO_MOB_ESSENCE_TO_LIQUID_XP));
	public BTF_FluidTank tankLiquidXp = new BTF_FluidTank(64000);
	private boolean mConvertToEssence = true;

	public TileEntityXpConverter() {
		super (4, 32000);
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

	public float getAdjustedVolume() {		
		if (this.mConvertToEssence){
			if ((this.tankLiquidXp.getFluid() != null) && (this.tankLiquidXp.getFluidAmount() >= 100) && (this.tankEssence.getFluidAmount() <= (this.tankEssence.getCapacity()-(100*EnchantingUtils.RATIO_MOB_ESSENCE_TO_LIQUID_XP)))){
				final FluidStack bigStorage = EnchantingUtils.getEssenceFromLiquidXp(100);
				this.tankEssence.fill(bigStorage, true);
				this.tankLiquidXp.drain(100, true);
				return (this.tankEssence.getCapacity()-this.tankEssence.getFluidAmount());
			}
		}
		else {
			final double rm = EnchantingUtils.RATIO_MOB_ESSENCE_TO_LIQUID_XP;
			if ((this.tankEssence.getFluid() != null) && (this.tankEssence.getFluidAmount() >= rm) && (this.tankLiquidXp.getFluidAmount() <= (this.tankLiquidXp.getCapacity()-rm))){
				final FluidStack bigStorage = EnchantingUtils.getLiquidXP(1);
				this.tankLiquidXp.fill(bigStorage, true);
				this.tankEssence.drain((int) rm, true);
				return (this.tankLiquidXp.getCapacity()-this.tankLiquidXp.getFluidAmount());
			}
		}		
		return 0f;
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

	@Override
	public boolean onPreTick(long aTick) {
		boolean aSuperResult = super.onPreTick(aTick);		
		long aTankSpaceLeft = 0;
		double aAmount = 0;
		int aRuns = 0;
		if (this.mConvertToEssence) {
			aTankSpaceLeft = (this.tankEssence.getCapacity()-this.tankEssence.getFluidAmount());
			aAmount = EnchantingUtils.getEssenceFromLiquidXp(100).amount;	
		}
		else {
			aTankSpaceLeft = (this.tankLiquidXp.getCapacity()-this.tankLiquidXp.getFluidAmount());
			aAmount = EnchantingUtils.RATIO_MOB_ESSENCE_TO_LIQUID_XP;	
		}
		aRuns = (int) (aTankSpaceLeft / aAmount);
		for (int i=0;i<aRuns;i++) {
			if (getAdjustedVolume() == 0) {
				break;
			}
		}		
		return aSuperResult;		
	}

	@Override
	public boolean isLiquidInput(byte aSide) {
		if (mConvertToEssence) {
			if (aSide == 0 || aSide == 1) {
				return false;
			}
		} else {
			if (aSide == 0 || aSide == 1) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isLiquidOutput(byte aSide) {
		if (mConvertToEssence) {
			if (aSide == 0 || aSide == 1) {
				return true;
			}
		} else {
			if (aSide == 0 || aSide == 1) {
				return false;
			}
		}
		return false;
	}

	@Override
	public int fill(ForgeDirection aSide, FluidStack aFluid, boolean doFill) {
		if (mConvertToEssence) {
			if (aSide != ForgeDirection.UP && aSide != ForgeDirection.DOWN) {
				return this.tankLiquidXp.fill(aFluid, doFill);
			}
		} else {
			if (aSide == ForgeDirection.UP || aSide == ForgeDirection.DOWN) {
				return this.tankEssence.fill(aFluid, doFill);
			}
		}
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection aSide, int maxDrain, boolean doDrain) {
		if (mConvertToEssence) {
			if (aSide == ForgeDirection.UP || aSide == ForgeDirection.DOWN) {
				return this.tankEssence.drain(maxDrain, doDrain);
			}
		} else {
			if (aSide != ForgeDirection.UP && aSide != ForgeDirection.DOWN) {
				return this.tankLiquidXp.drain(maxDrain, doDrain);
			}
		}
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection aSide, FluidStack aFluid, boolean doDrain) {
		if (mConvertToEssence) {
			if (aSide == ForgeDirection.UP || aSide == ForgeDirection.DOWN) {
				return this.tankEssence.drain(aFluid, doDrain);
			}
		} else {
			if (aSide != ForgeDirection.UP && aSide != ForgeDirection.DOWN) {
				return this.tankLiquidXp.drain(aFluid, doDrain);
			}
		}
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection aSide, Fluid aFluid) {
		if (mConvertToEssence) {
			if (aSide != ForgeDirection.UP && aSide != ForgeDirection.DOWN) {
				return this.tankLiquidXp.canTankBeFilled();
			}
		} else {
			if (aSide == ForgeDirection.UP || aSide == ForgeDirection.DOWN) {
				return this.tankEssence.canTankBeFilled();
			}
		}
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection aSide, Fluid aFluid) {
		if (mConvertToEssence) {
			if (aSide == ForgeDirection.UP || aSide == ForgeDirection.DOWN) {
				return this.tankEssence.canTankBeEmptied();
			}
		} else {
			if (aSide != ForgeDirection.UP && aSide != ForgeDirection.DOWN) {
				return this.tankLiquidXp.canTankBeEmptied();
			}
		}
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection aSide) {
		if (aSide == ForgeDirection.UP || aSide == ForgeDirection.DOWN) {
			return new FluidTankInfo[] { this.tankEssence.getInfo() };
		} else {
			return new FluidTankInfo[] { this.tankLiquidXp.getInfo() };

		}
	}

	@Override
	public FluidStack getFluid() {
		if (mConvertToEssence) {
			return this.tankEssence.getFluid();
		}
		else {
			return this.tankLiquidXp.getFluid();			
		}
	}

	@Override
	public int getFluidAmount() {
		if (mConvertToEssence) {
			return this.tankEssence.getFluidAmount();
		}
		else {
			return this.tankLiquidXp.getFluidAmount();			
		}
	}

	@Override
	public FluidTankInfo getInfo() {
		if (mConvertToEssence) {
			return this.tankEssence.getInfo();
		} else {
			return this.tankLiquidXp.getInfo();
		}
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (mConvertToEssence) {
			return this.tankEssence.fill(resource, doFill);
		} else {
			return this.tankLiquidXp.fill(resource, doFill);
		}
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (mConvertToEssence) {
			return this.tankEssence.drain(maxDrain, doDrain);
		} else {
			return this.tankLiquidXp.drain(maxDrain, doDrain);
		}
	}

}
