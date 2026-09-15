package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Controller_IndustrialFluidHeater;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialFluidHeater;

public class GregtechIndustrialFluidHeater {

    public static void run() {
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
