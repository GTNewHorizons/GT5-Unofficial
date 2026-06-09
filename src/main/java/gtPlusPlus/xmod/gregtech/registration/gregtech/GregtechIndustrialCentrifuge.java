package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_Centrifuge;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialCentrifugeLegacy;

public class GregtechIndustrialCentrifuge {

    public static void run() {
        run1();
    }

    private static void run1() {
        // Industrial Centrifuge Multiblock
        GregtechItemList.Industrial_Centrifuge.set(
            new MTEIndustrialCentrifugeLegacy(
                Industrial_Centrifuge.ID,
                "industrialcentrifuge.controller.tier.single",
                "Industrial Centrifuge").getStackForm(1L));
    }
}
