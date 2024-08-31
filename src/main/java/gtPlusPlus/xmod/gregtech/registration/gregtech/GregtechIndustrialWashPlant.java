package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_WashPlant;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialWashPlant;

public class GregtechIndustrialWashPlant {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Wash Plant Multiblock.");
        if (CORE.ConfigSwitches.enableMultiblock_IndustrialWashPlant) {
            run1();
        }
    }

    private static void run1() {
        GregtechItemList.Industrial_WashPlant.set(
            new GregtechMetaTileEntity_IndustrialWashPlant(
                Industrial_WashPlant.ID,
                "industrialwashplant.controller.tier.single",
                "Ore Washing Plant").getStackForm(1L));
    }
}
