package gtPlusPlus.core.util.minecraft.gregtech;

import net.minecraft.item.ItemStack;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.GT_Mod;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.common.GT_Pollution;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.item.base.cell.BaseItemCell;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class PollutionUtils {

    public static AutoMap<FluidStack> mPollutionFluidStacks = new AutoMap<>();

    public static boolean isPollutionEnabled() {
        return GT_Mod.gregtechproxy.mPollution;
    }

    public static boolean addPollution(IGregTechTileEntity te, int pollutionValue) {
        if (GT_Mod.gregtechproxy.mPollution) {
            GT_Pollution.addPollution(te, pollutionValue);
            return true;
        }
        return false;
    }

    public static void addPollution(IHasWorldObjectAndCoords aTileOfSomeSort, int pollutionValue) {
        if (GT_Mod.gregtechproxy.mPollution) {
            Chunk c = aTileOfSomeSort.getWorld()
                    .getChunkFromBlockCoords(aTileOfSomeSort.getXCoord(), aTileOfSomeSort.getZCoord());
            addPollution(c, pollutionValue);
        }
    }

    public static void addPollution(Chunk aChunk, int pollutionValue) {
        if (GT_Mod.gregtechproxy.mPollution) {
            GT_Pollution.addPollution(aChunk, pollutionValue);
        }
    }

    public static void removePollution(IGregTechTileEntity te, int pollutionValue) {
        addPollution(te, -pollutionValue);
    }

    public static void removePollution(Chunk aChunk, int pollutionValue) {
        addPollution(aChunk, -pollutionValue);
    }

    public static void nullifyPollution(IGregTechTileEntity te) {
        if (te == null) {
            return;
        }
        nullifyPollution((IHasWorldObjectAndCoords) te);
    }

    public static void nullifyPollution(IHasWorldObjectAndCoords aTileOfSomeSort) {
        if (aTileOfSomeSort == null) {
            return;
        }
        Chunk c = aTileOfSomeSort.getWorld()
                .getChunkFromBlockCoords(aTileOfSomeSort.getXCoord(), aTileOfSomeSort.getZCoord());
        nullifyPollution(c);
    }

    public static void nullifyPollution(Chunk aChunk) {
        if (GT_Mod.gregtechproxy.mPollution) {
            if (aChunk == null) {
                return;
            }
            int getCurrentPollution = getPollution(aChunk);
            if (getCurrentPollution > 0) {
                removePollution(aChunk, getCurrentPollution);
            }
        }
    }

    public static int getPollution(IGregTechTileEntity te) {
        return GT_Pollution.getPollution(te);
    }

    public static int getPollution(Chunk te) {
        return GT_Pollution.getPollution(te);
    }

    public static void setPollutionFluids() {
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
                    } else {
                        Logger.INFO("[PollutionCompat] Did not find carbon dioxide cell, registering new component.");
                        new BaseItemCell(MISC_MATERIALS.CARBON_DIOXIDE);
                    }
                } else {
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
                    } else {
                        Logger.INFO("[PollutionCompat] Did not find carbon monoxide cell, registering new component.");
                        new BaseItemCell(MISC_MATERIALS.CARBON_MONOXIDE);
                    }
                } else {
                    MaterialGenerator.generate(MISC_MATERIALS.CARBON_MONOXIDE, false, false);
                }

                if (SD != null) {
                    Logger.INFO("[PollutionCompat] Found sulfur dioxide fluid, registering it.");
                    PollutionUtils.mPollutionFluidStacks.put(SD);
                }
            }
        } else {
            if (mPollutionFluidStacks.size() != 3) {
                Logger.INFO("Unable to detect all 3 pollution fluids. Found: ");
                Logger.INFO(ArrayUtils.toString(mPollutionFluidStacks));
            }
        }
    }
}
