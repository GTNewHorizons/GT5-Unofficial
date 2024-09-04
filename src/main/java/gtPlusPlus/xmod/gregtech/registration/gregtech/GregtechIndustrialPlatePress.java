package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_PlatePress;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialPlatePress;

public class GregtechIndustrialPlatePress {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Press Multiblock.");
        if (GTPPCore.ConfigSwitches.enableMultiblock_IndustrialPlatePress) {
            run1();
        }
    }

    private static void run1() {
        // Industrial Presser Multiblock
        GregtechItemList.Industrial_PlatePress.set(
            new MTEIndustrialPlatePress(
                Industrial_PlatePress.ID,
                "industrialbender.controller.tier.single",
                "Industrial Material Press").getStackForm(1L));
    }
}
