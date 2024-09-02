package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Controller_IndustrialFluidHeater;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialFluidHeater;

public class GregtechIndustrialFluidHeater {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Thermal Centrifuge Multiblock.");
        run1();
    }

    private static void run1() {
        GregtechItemList.Controller_IndustrialFluidHeater.set(
            new MTEIndustrialFluidHeater(
                Controller_IndustrialFluidHeater.ID,
                "industrialfluidheater.controller.tier.single",
                "Thermic Heating Device").getStackForm(1L));
    }
}
