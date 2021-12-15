package gtPlusPlus.core.util.minecraft.gregtech;

import static gtPlusPlus.core.lib.CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.GT_Mod;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.common.GT_Pollution;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.item.base.cell.BaseItemCell;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidStack;

public class PollutionUtils {

	private static boolean mIsPollutionEnabled = true;

	public static AutoMap<FluidStack> mPollutionFluidStacks = new AutoMap<FluidStack>();

	static {
		if (MAIN_GREGTECH_5U_EXPERIMENTAL_FORK || CORE.GTNH) {
			mIsPollutionEnabled = mPollution();
		} else {
			mIsPollutionEnabled = false;
		}
	}

	public static boolean isPollutionEnabled() {
		return mIsPollutionEnabled;
	}

	private static boolean mPollution() {
		return GT_Mod.gregtechproxy.mPollution;
	}

	public static boolean addPollution(IGregTechTileEntity te, int pollutionValue) {
		if (mIsPollutionEnabled) {
			GT_Pollution.addPollution(te, pollutionValue);
			return true;
		}
		return false;
	}

	public static boolean addPollution(IHasWorldObjectAndCoords aTileOfSomeSort, int pollutionValue) {
		if (mIsPollutionEnabled) {
			IHasWorldObjectAndCoords j = (IHasWorldObjectAndCoords) aTileOfSomeSort;
			Chunk c = j.getWorld().getChunkFromBlockCoords(j.getXCoord(), j.getZCoord());
			return addPollution(c, pollutionValue);
		}
		return false;
	}

	public static boolean addPollution(Chunk aChunk, int pollutionValue) {
		if (mIsPollutionEnabled) {
			GT_Pollution.addPollution(aChunk, pollutionValue);
			return true;
		}
		return false;
	}

	public static boolean removePollution(IGregTechTileEntity te, int pollutionValue) {
		return addPollution(te, -pollutionValue);
	}

	public static boolean removePollution(IHasWorldObjectAndCoords aTileOfSomeSort, int pollutionValue) {
		return addPollution(aTileOfSomeSort, -pollutionValue);
	}

	public static boolean removePollution(Chunk aChunk, int pollutionValue) {
		return addPollution(aChunk, -pollutionValue);
	}

	public static boolean nullifyPollution(IGregTechTileEntity te) {
		if (te == null) {
			return false;
		}
		return nullifyPollution((IHasWorldObjectAndCoords) te);
	}

	public static boolean nullifyPollution(IHasWorldObjectAndCoords aTileOfSomeSort) {
		if (aTileOfSomeSort == null) {
			return false;
		}
		IHasWorldObjectAndCoords j = (IHasWorldObjectAndCoords) aTileOfSomeSort;
		Chunk c = j.getWorld().getChunkFromBlockCoords(j.getXCoord(), j.getZCoord());
		return nullifyPollution(c);
	}

	public static boolean nullifyPollution(Chunk aChunk) {
		if (mIsPollutionEnabled) {
			if (aChunk == null) {
				return false;
			}
			int getCurrentPollution = getPollution(aChunk);
			if (getCurrentPollution <= 0) {
				return false;
			}
			else {
				return removePollution(aChunk, getCurrentPollution);
			}
		}
		return false;
	}

	public static int getPollution(IGregTechTileEntity te) {
		return GT_Pollution.getPollution(te);
	}

	public static int getPollution(Chunk te) {
		return GT_Pollution.getPollution(te);
	}

	public static boolean setPollutionFluids() {		
		if (mPollutionFluidStacks.isEmpty()) {
			FluidStack CD, CM, SD;
			CD = FluidUtils.getFluidStack("carbondioxide", 1000);
			CM = FluidUtils.getFluidStack("carbonmonoxide", 1000);
			SD = FluidUtils.getFluidStack("sulfurdioxide", 1000);
			if (PollutionUtils.mPollutionFluidStacks.size() == 0) {
				if (CD != null) {
					Logger.INFO("[PollutionCompat] Found carbon dioxide fluid, registering it.");
					PollutionUtils.mPollutionFluidStacks.put(CD);
					MISC_MATERIALS.CARBON_DIOXIDE.registerComponentForMaterial(CD);
					ItemStack cellCD = ItemUtils.getItemStackOfAmountFromOreDict("cellCarbonDioxide", 1);
					if (ItemUtils.checkForInvalidItems(cellCD)) {
						Logger.INFO("[PollutionCompat] Found carbon dioxide cell, registering component.");
						MISC_MATERIALS.CARBON_DIOXIDE.registerComponentForMaterial(OrePrefixes.cell, cellCD);
					}
					else {
						Logger.INFO("[PollutionCompat] Did not find carbon dioxide cell, registering new component.");
						new BaseItemCell(MISC_MATERIALS.CARBON_DIOXIDE);
					}
				}
				else {
					MaterialGenerator.generate(MISC_MATERIALS.CARBON_DIOXIDE, false, false);
				}

				if (CM != null) {
					Logger.INFO("[PollutionCompat] Found carbon monoxide fluid, registering it.");
					PollutionUtils.mPollutionFluidStacks.put(CM);
					MISC_MATERIALS.CARBON_MONOXIDE.registerComponentForMaterial(CM);
					ItemStack cellCD = ItemUtils.getItemStackOfAmountFromOreDict("cellCarbonMonoxide", 1);
					if (ItemUtils.checkForInvalidItems(cellCD)) {
						Logger.INFO("[PollutionCompat] Found carbon monoxide cell, registering component.");
						MISC_MATERIALS.CARBON_MONOXIDE.registerComponentForMaterial(OrePrefixes.cell, cellCD);
					}
					else {
						Logger.INFO("[PollutionCompat] Did not find carbon monoxide cell, registering new component.");
						new BaseItemCell(MISC_MATERIALS.CARBON_MONOXIDE);
					}
				}
				else {
					MaterialGenerator.generate(MISC_MATERIALS.CARBON_MONOXIDE, false, false);
				}

				if (SD != null) {
					Logger.INFO("[PollutionCompat] Found sulfur dioxide fluid, registering it.");
					PollutionUtils.mPollutionFluidStacks.put(SD);
				}
			}
			if (PollutionUtils.mPollutionFluidStacks.size() > 0) {
				return true;
			}
			return false;
		}
		else {
			if (mPollutionFluidStacks.size() != 3) {
				Logger.INFO("Unable to detect all 3 pollution fluids. Found: ");
				Logger.INFO(ArrayUtils.toString(mPollutionFluidStacks));
				return false;
			}
			else {
				return true;
			}
		}



	}

}
