package gtPlusPlus.xmod.gregtech.common.covers;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.minecraft.LangUtils;
import gtPlusPlus.core.util.sys.KeyboardUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class GTPP_Cover_Overflow2 extends GT_CoverBehavior {

	public final int mTransferRate;
	public final int mInitialTransferRate;
	public final int mMaxTransferRate;

	public GTPP_Cover_Overflow2(int aTransferRate) {
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
			IFluidHandler tTank1;
			ForgeDirection directionFrom;
			directionFrom = ForgeDirection.UNKNOWN;
			if (aCoverVariable > 0) {
				tTank1 = (IFluidHandler) aTileEntity;
			} else {
				tTank1 = aTileEntity.getITankContainerAtSide(aSide);
			}
			if (tTank1 != null) {
				FluidStack aTankStack = tTank1.drain(ForgeDirection.UNKNOWN, 0, false);
				if (aTankStack != null) {
					if (aTankStack.amount > aCoverVariable) {
						int aAmountToDrain = aTankStack.amount - aCoverVariable;
						if (aAmountToDrain > 0) {
							FluidStack tLiquid = tTank1.drain(directionFrom, Math.abs(aAmountToDrain), false);							
						}
					}
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
		GT_Utility.sendChatToPlayer(aPlayer, LangUtils.trans("009", "Overflow point: ") + aCoverVariable + trans("010", "L/5T"));
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
		GT_Utility.sendChatToPlayer(aPlayer, LangUtils.trans("009", "Overflow point: ") + aCoverVariable + trans("010", "L/5T"));
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
