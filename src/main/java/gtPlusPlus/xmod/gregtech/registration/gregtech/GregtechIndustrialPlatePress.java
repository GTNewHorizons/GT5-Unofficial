package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_PlatePress;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialPlatePressLegacy;

public class GregtechIndustrialPlatePress {

    public static void run() {
        run1();
    }

    private static void run1() {
        // Industrial Presser Multiblock
        GregtechItemList.Industrial_PlatePress.set(
            new MTEIndustrialPlatePressLegacy(
                Industrial_PlatePress.ID,
                "industrialplatepress.controller.tier.single",
                "Industrial Material Press").getStackForm(1L));
    }
}
