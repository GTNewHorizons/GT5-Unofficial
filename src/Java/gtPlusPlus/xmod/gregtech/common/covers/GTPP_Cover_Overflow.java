package gtPlusPlus.xmod.gregtech.common.covers;

import java.util.HashMap;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.ObjMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class GTPP_Cover_Overflow extends GT_CoverBehavior {
	public final int mMaxTransferRate;
	
	public static final ObjMap<String, HashMap<String, Object>> mOverflowCache = new ObjMap<String, HashMap<String, Object>>(10000, 0.5f);

	public GTPP_Cover_Overflow(int aTransferRate) {
		this.mMaxTransferRate = aTransferRate*1000;
	}

	public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
			long aTimer) {
		
		HashMap<String, Object> aCoverData = getCover(aSide, aCoverID, aCoverVariable, aTileEntity);
		//Do things
		
		if (aCoverData != null) {
			if (aCoverData.containsKey("aCoverVariable"))
			return (int) aCoverData.get("aCoverVariable");
		}
		
		
		if (aCoverVariable % 6 > 1 && aTileEntity instanceof IMachineProgress
				&& ((IMachineProgress) aTileEntity).isAllowedToWork() != aCoverVariable % 6 < 4) {
			return aCoverVariable;
		} else {
			if (aTileEntity instanceof IFluidHandler) {
				IFluidHandler tTank2 = aTileEntity.getITankContainerAtSide(aSide);
				if (tTank2 != null) {
					IFluidHandler tTank1 = (IFluidHandler) aTileEntity;
					FluidStack tLiquid;
					if (aCoverVariable % 2 == 0) {
						tLiquid = tTank1.drain(ForgeDirection.getOrientation(aSide), this.mMaxTransferRate, false);
						if (tLiquid != null) {
							tLiquid = tLiquid.copy();
							tLiquid.amount = tTank2.fill(ForgeDirection.getOrientation(aSide).getOpposite(), tLiquid,
									false);
							if (tLiquid.amount > 0) {
								if ((aCoverVariable % 2 == 0 || aSide != 1) && (aCoverVariable % 2 != 0 || aSide != 0)
										&& aTileEntity.getUniversalEnergyCapacity() >= (long) Math.min(1,
												tLiquid.amount / 10)) {
									if (aTileEntity.isUniversalEnergyStored((long) Math.min(1, tLiquid.amount / 10))) {
										aTileEntity.decreaseStoredEnergyUnits((long) Math.min(1, tLiquid.amount / 10),
												true);
										tTank2.fill(ForgeDirection.getOrientation(aSide).getOpposite(), tTank1.drain(
												ForgeDirection.getOrientation(aSide), tLiquid.amount, true), true);
									}
								} else {
									tTank2.fill(ForgeDirection.getOrientation(aSide).getOpposite(),
											tTank1.drain(ForgeDirection.getOrientation(aSide), tLiquid.amount, true),
											true);
								}
							}
						}
					} else {
						tLiquid = tTank2.drain(ForgeDirection.getOrientation(aSide).getOpposite(), this.mMaxTransferRate,
								false);
						if (tLiquid != null) {
							tLiquid = tLiquid.copy();
							tLiquid.amount = tTank1.fill(ForgeDirection.getOrientation(aSide), tLiquid, false);
							if (tLiquid.amount > 0) {
								if ((aCoverVariable % 2 == 0 || aSide != 1) && (aCoverVariable % 2 != 0 || aSide != 0)
										&& aTileEntity.getUniversalEnergyCapacity() >= (long) Math.min(1,
												tLiquid.amount / 10)) {
									if (aTileEntity.isUniversalEnergyStored((long) Math.min(1, tLiquid.amount / 10))) {
										aTileEntity.decreaseStoredEnergyUnits((long) Math.min(1, tLiquid.amount / 10),
												true);
										tTank1.fill(ForgeDirection.getOrientation(aSide),
												tTank2.drain(ForgeDirection.getOrientation(aSide).getOpposite(),
														tLiquid.amount, true),
												true);
									}
								} else {
									tTank1.fill(ForgeDirection.getOrientation(aSide),
											tTank2.drain(ForgeDirection.getOrientation(aSide).getOpposite(),
													tLiquid.amount, true),
											true);
								}
							}
						}
					}
				}
			}

			return aCoverVariable;
		}
	}

	public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
			EntityPlayer aPlayer, float aX, float aY, float aZ) {
		
		
		if ((double) aX > 0.375D && (double) aX < 0.625D || aSide <= 3 || (double) aY > 0.375D && (double) aY < 0.625D
				|| (double) aZ <= 0.375D || (double) aZ >= 0.625D) {			
			HashMap<String, Object> aCoverData = getCover(aSide, aCoverID, aCoverVariable, aTileEntity);			
			float[] tCoords = GT_Utility.getClickedFacingCoords(aSide, aX, aY, aZ);
			switch ((byte) ((byte) ((int) (tCoords[0] * 2.0F)) + 2 * (byte) ((int) (tCoords[1] * 2.0F)))) {
				case 0 :
					aCoverVariable -= 1000;
					break;
				case 1 :
					aCoverVariable += 1000;
					break;
				case 2 :
					aCoverVariable -= 32000;
					break;
				case 3 :
					aCoverVariable += 32000;
			}			
			aCoverData.remove("aCoverVariable");
			aCoverData.put("aCoverVariable", aCoverVariable);			
			updateCoverMap(aCoverData);
			
		}
		GT_Utility.sendChatToPlayer(aPlayer, "Overflow Limit: " + aCoverVariable);

		return aCoverVariable;
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
		return aCoverVariable > 1 && aTileEntity instanceof IMachineProgress
				&& ((IMachineProgress) aTileEntity).isAllowedToWork() != aCoverVariable % 6 < 4
						? false
						: aCoverVariable >= 6 || aCoverVariable % 2 != 0;
	}

	public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
		return aCoverVariable > 1 && aTileEntity instanceof IMachineProgress
				&& ((IMachineProgress) aTileEntity).isAllowedToWork() != aCoverVariable % 6 < 4
						? false
						: aCoverVariable >= 6 || aCoverVariable % 2 == 0;
	}

	public boolean alwaysLookConnected(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return 1;
	}
	
	public HashMap<String, Object> getCover(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity){
		//Map this cover
		String aTileDataKey = "|"+aTileEntity.getXCoord()+"|"+aTileEntity.getYCoord()+"|"+aTileEntity.getZCoord()+"|"+aTileEntity.getWorld().provider.dimensionId+"|"+aSide+"|";		
		HashMap<String, Object> aTileData;
		long aCurrentTime = System.currentTimeMillis();
		if (mOverflowCache.get(aTileDataKey) != null) {
			aTileData = mOverflowCache.get(aTileDataKey);	
			aTileData.remove("aLastUpdatedTime");
			aTileData.put("aLastUpdateTime", aCurrentTime);
			//Logger.INFO("Found Existing Cover in Cache.");
		}
		else {
			aTileData = new HashMap<String, Object>();
			aTileData.put("aCoverKey", aTileDataKey);
			aTileData.put("aSide", aSide);
			aTileData.put("aCoverID", aCoverID);
			aTileData.put("aCoverVariable", aCoverVariable);
			aTileData.put("aLastUpdateTime", aCurrentTime);
			mOverflowCache.put(aTileDataKey, aTileData);
			Logger.INFO("Adding new Cover to Cache. Storing under key: "+aTileDataKey);
		}
		return aTileData;		
	}
	
	public void updateCoverMap(HashMap<String, Object> aCoverData) {
		String mAccessKey = (String) aCoverData.get("aCoverKey");
		if (mOverflowCache.get(mAccessKey) != null) {
			mOverflowCache.remove(mAccessKey);
		}
		mOverflowCache.put(mAccessKey, aCoverData);		
		return;
	}
}