package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_AlloySmelter;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialAlloySmelter;

public class GregtechIndustrialAlloySmelter {

    public static void run() {
        run1();
    }

    private static void run1() {
        GregtechItemList.Industrial_AlloySmelter.set(
            new MTEIndustrialAlloySmelter(
                Industrial_AlloySmelter.ID,
                "industrialalloysmelter.controller.tier.single",
                "Zyngen").getStackForm(1L));
    }
}
