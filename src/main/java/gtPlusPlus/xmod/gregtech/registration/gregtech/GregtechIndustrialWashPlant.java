package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_WashPlant;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialWashPlant;

public class GregtechIndustrialWashPlant {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Wash Plant Multiblock.");
        if (GTPPCore.ConfigSwitches.enableMultiblock_IndustrialWashPlant) {
            run1();
        }
    }

    private static void run1() {
        GregtechItemList.Industrial_WashPlant.set(
            new MTEIndustrialWashPlant(
                Industrial_WashPlant.ID,
                "industrialwashplant.controller.tier.single",
                "Ore Washing Plant").getStackForm(1L));
    }
}
