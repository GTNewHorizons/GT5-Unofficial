package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_CokeOven;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialCokeOven;

public class GregtechIndustrialCokeOven {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Coke Oven Multiblock.");
        if (GTPPCore.ConfigSwitches.enableMultiblock_IndustrialCokeOven) {
            run1();
        }
    }

    private static void run1() {
        // Industrial Centrifuge Multiblock
        GregtechItemList.Industrial_CokeOven.set(
            new MTEIndustrialCokeOven(
                Industrial_CokeOven.ID,
                "industrialcokeoven.controller.tier.single",
                "Industrial Coke Oven").getStackForm(1L));
    }
}
