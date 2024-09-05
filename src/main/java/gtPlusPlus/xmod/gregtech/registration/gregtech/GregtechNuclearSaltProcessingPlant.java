package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Nuclear_Salt_Processing_Plant;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTENuclearSaltProcessingPlant;

public class GregtechNuclearSaltProcessingPlant {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Nuclear Salt Processing Plant Multiblock.");
        if (GTPPCore.ConfigSwitches.enableMultiblock_NuclearSaltProcessingPlant) {
            // Nuclear Salt Processing Plant Multiblock
            GregtechItemList.Nuclear_Salt_Processing_Plant.set(
                new MTENuclearSaltProcessingPlant(
                    Nuclear_Salt_Processing_Plant.ID,
                    "nuclearsaltprocessingplant.controller.tier.single",
                    "Nuclear Salt Processing Plant").getStackForm(1L));
        }
    }
}
