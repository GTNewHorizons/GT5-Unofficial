package gtPlusPlus.xmod.gregtech.common.covers;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.minecraft.LangUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class GTPP_Cover_Overflow extends GT_CoverBehavior {

	public final int mTransferRate;
	public final int mInitialTransferRate;
	public final int mMaxTransferRate;

	public GTPP_Cover_Overflow(int aTransferRate) {
		this.mTransferRate = aTransferRate * 1000 / 10;
		this.mInitialTransferRate = aTransferRate;
		this.mMaxTransferRate = aTransferRate * 1000;
	}

	public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
			long aTimer) {
		if (aCoverVariable == 0) {
			return aCoverVariable;
		}
		if ((aTileEntity instanceof IFluidHandler)) {
			//Logger.INFO("Trying to Void via Overflow.");
			IFluidHandler tTank1;
			ForgeDirection directionFrom;
			directionFrom = ForgeDirection.UNKNOWN;
			tTank1 = (IFluidHandler) aTileEntity;			
			if (tTank1 != null) {
				//Logger.INFO("Found Self. "+aSide);
				//FluidStack aTankStack = tTank1.drain(ForgeDirection.UNKNOWN, 1, false);
				FluidStack aTankStack = tTank1.getTankInfo(directionFrom)[0].fluid;
				if (aTankStack != null) {
					//Logger.INFO("Found Fluid inside self - "+aTankStack.getLocalizedName()+", overflow point set at "+aCoverVariable+"L and we have "+aTankStack.amount+"L inside.");
					if (aTankStack.amount > aCoverVariable) {
						int aAmountToDrain = aTankStack.amount - aCoverVariable;
						//Logger.INFO("There is "+aAmountToDrain+" more fluid in the tank than we would like.");
						if (aAmountToDrain > 0) {
							FluidStack tLiquid = tTank1.drain(directionFrom, Math.abs(aAmountToDrain), true);	
							if (tLiquid != null) {
								//Logger.INFO("Drained "+aAmountToDrain+"L.");
							}
						}
					}
				}
				else {
					//Logger.INFO("Could not simulate drain on self.");
				}
			}
		}
		return aCoverVariable;
	}

	public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
			EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (GT_Utility.getClickedFacingCoords(aSide, aX, aY, aZ)[0] >= 0.5F) {
			aCoverVariable += aPlayer.isSneaking() ? 4096 : 1024;
		} else {
			aCoverVariable -= aPlayer.isSneaking() ? 4096 : 1024;
		}
		if (aCoverVariable > mMaxTransferRate) {
			aCoverVariable = mInitialTransferRate;
		}
		if (aCoverVariable <= 0) {
			aCoverVariable = mInitialTransferRate;
		}
		GT_Utility.sendChatToPlayer(aPlayer, LangUtils.trans("009", "Overflow point: ") + aCoverVariable + trans("010", "L"));
		return aCoverVariable;
	}

	public boolean onCoverRightclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
			EntityPlayer aPlayer, float aX, float aY, float aZ) {
		boolean aShift = aPlayer.isSneaking();
		int aAmount = aShift ? 128 : 8;
		if (GT_Utility.getClickedFacingCoords(aSide, aX, aY, aZ)[0] >= 0.5F) {
			aCoverVariable += aAmount;
		} else {
			aCoverVariable -= aAmount;
		}
		if (aCoverVariable > mMaxTransferRate) {
			aCoverVariable = mInitialTransferRate;
		}
		if (aCoverVariable <= 0) {
			aCoverVariable = mInitialTransferRate;
		}
		GT_Utility.sendChatToPlayer(aPlayer, LangUtils.trans("009", "Overflow point: ") + aCoverVariable + trans("010", "L"));
		aTileEntity.setCoverDataAtSide(aSide, aCoverVariable);
		return true;
	}

	public boolean letsRedstoneGoIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	public boolean letsRedstoneGoOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	public boolean letsEnergyIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	public boolean letsEnergyOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	public boolean letsItemsIn(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
		return true;
	}

	public boolean letsItemsOut(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
		return true;
	}

	public boolean letsFluidIn(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
		return false;
	}

	public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
		return true;
	}

	public boolean alwaysLookConnected(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return 5;
	}
}
