package gtPlusPlus.core.util.minecraft.gregtech;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.OrePrefixes;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.base.cell.BaseItemCell;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class PollutionUtils {

    public static void setPollutionFluids() {
        FluidStack CD, CM, SD;
        CD = FluidUtils.getFluidStack("carbondioxide", 1000);
        CM = FluidUtils.getFluidStack("carbonmonoxide", 1000);
        SD = FluidUtils.getFluidStack("sulfurdioxide", 1000);
        if (CD != null) {
            Logger.INFO("[PollutionCompat] Found carbon dioxide fluid, registering it.");
            MaterialMisc.CARBON_DIOXIDE.registerComponentForMaterial(CD);
            ItemStack cellCD = ItemUtils.getItemStackOfAmountFromOreDict("cellCarbonDioxide", 1);
            if (cellCD != null) {
                Logger.INFO("[PollutionCompat] Found carbon dioxide cell, registering component.");
                MaterialMisc.CARBON_DIOXIDE.registerComponentForMaterial(OrePrefixes.cell, cellCD);
            } else {
                Logger.INFO("[PollutionCompat] Did not find carbon dioxide cell, registering new component.");
                new BaseItemCell(MaterialMisc.CARBON_DIOXIDE);
            }
        } else {
            MaterialGenerator.generate(MaterialMisc.CARBON_DIOXIDE, false, false);
        }
        if (CM != null) {
            Logger.INFO("[PollutionCompat] Found carbon monoxide fluid, registering it.");
            MaterialMisc.CARBON_MONOXIDE.registerComponentForMaterial(CM);
            ItemStack cellCD = ItemUtils.getItemStackOfAmountFromOreDict("cellCarbonMonoxide", 1);
            if (cellCD != null) {
                Logger.INFO("[PollutionCompat] Found carbon monoxide cell, registering component.");
                MaterialMisc.CARBON_MONOXIDE.registerComponentForMaterial(OrePrefixes.cell, cellCD);
            } else {
                Logger.INFO("[PollutionCompat] Did not find carbon monoxide cell, registering new component.");
                new BaseItemCell(MaterialMisc.CARBON_MONOXIDE);
            }
        } else {
            MaterialGenerator.generate(MaterialMisc.CARBON_MONOXIDE, false, false);
        }
        if (SD != null) {
            Logger.INFO("[PollutionCompat] Found sulfur dioxide fluid, registering it.");
        }
    }
}
