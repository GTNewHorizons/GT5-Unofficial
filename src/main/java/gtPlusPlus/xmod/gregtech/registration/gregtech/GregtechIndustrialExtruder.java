package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_Extruder;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialExtruder;

public class GregtechIndustrialExtruder {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Extrusion Multiblock.");
        if (GTPPCore.ConfigSwitches.enableMultiblock_IndustrialExtrudingMachine) {
            run1();
        }
    }

    private static void run1() {
        // Industrial Presser Multiblock
        GregtechItemList.Industrial_Extruder.set(
            new MTEIndustrialExtruder(
                Industrial_Extruder.ID,
                "industrialextruder.controller.tier.single",
                "Industrial Extrusion Machine").getStackForm(1L));
    }
}
