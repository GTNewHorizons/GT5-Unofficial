package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_Sifter;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialSifterLegacy;

public class GregtechIndustrialSifter {

    public static void run() {
        run1();
    }

    private static void run1() {
        GregtechItemList.Industrial_Sifter.set(
            new MTEIndustrialSifterLegacy(
                Industrial_Sifter.ID,
                "industrialsifter.controller.tier.single",
                "Large Sifter Control Block").getStackForm(1L));
    }
}
