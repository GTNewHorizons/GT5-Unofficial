package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_Centrifuge;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialCentrifuge;

public class GregtechIndustrialCentrifuge {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Centrifuge Multiblock.");
        if (GTPPCore.ConfigSwitches.enableMultiblock_IndustrialCentrifuge) {
            run1();
        }
    }

    private static void run1() {
        // Industrial Centrifuge Multiblock
        GregtechItemList.Industrial_Centrifuge.set(
            new MTEIndustrialCentrifuge(
                Industrial_Centrifuge.ID,
                "industrialcentrifuge.controller.tier.single",
                "Industrial Centrifuge").getStackForm(1L));
    }
}
