package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialFluidHeater;

public class GregtechIndustrialFluidHeater {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Thermal Centrifuge Multiblock.");
        run1();
    }

    private static void run1() {
        GregtechItemList.Controller_IndustrialFluidHeater.set(
            new GregtechMetaTileEntity_IndustrialFluidHeater(
                31077,
                "industrialfluidheater.controller.tier.single",
                "Thermic Heating Device").getStackForm(1L));
    }
}
