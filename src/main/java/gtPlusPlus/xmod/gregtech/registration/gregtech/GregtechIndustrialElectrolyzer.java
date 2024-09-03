package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_Electrolyzer;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEndustrialElectrolyzer;

public class GregtechIndustrialElectrolyzer {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Electrolyzer Multiblock.");
        if (GTPPCore.ConfigSwitches.enableMultiblock_IndustrialElectrolyzer) {
            run1();
        }
    }

    private static void run1() {
        // Industrial Electrolyzer Multiblock
        GregtechItemList.Industrial_Electrolyzer.set(
            new MTEndustrialElectrolyzer(
                Industrial_Electrolyzer.ID,
                "industrialelectrolyzer.controller.tier.single",
                "Industrial Electrolyzer").getStackForm(1L));
    }
}
