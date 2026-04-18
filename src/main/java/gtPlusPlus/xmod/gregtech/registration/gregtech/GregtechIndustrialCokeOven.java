package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_CokeOven;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialCokeOvenLegacy;

public class GregtechIndustrialCokeOven {

    public static void run() {
        run1();
    }

    private static void run1() {
        // Industrial Centrifuge Multiblock
        GregtechItemList.Industrial_CokeOven.set(
            new MTEIndustrialCokeOvenLegacy(
                Industrial_CokeOven.ID,
                "industrialcokeoven.controller.tier.single",
                "Industrial Coke Oven").getStackForm(1L));
    }
}
