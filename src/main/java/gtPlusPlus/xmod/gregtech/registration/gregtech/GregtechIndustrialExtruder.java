package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_Extruder;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialExtruderLegacy;

public class GregtechIndustrialExtruder {

    public static void run() {
        run1();
    }

    private static void run1() {
        // Industrial Presser Multiblock
        GregtechItemList.Industrial_Extruder.set(
            new MTEIndustrialExtruderLegacy(
                Industrial_Extruder.ID,
                "industrialextruder.controller.tier.single",
                "Industrial Extrusion Machine").getStackForm(1L));
    }
}
