package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialCentrifuge;

import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.Industrial_Centrifuge;

public class GregtechIndustrialCentrifuge {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Centrifuge Multiblock.");
        if (CORE.ConfigSwitches.enableMultiblock_IndustrialCentrifuge) {
            run1();
        }
    }

    private static void run1() {
        // Industrial Centrifuge Multiblock
        GregtechItemList.Industrial_Centrifuge.set(
            new GregtechMetaTileEntity_IndustrialCentrifuge(
                Industrial_Centrifuge.ID,
                "industrialcentrifuge.controller.tier.single",
                "Industrial Centrifuge").getStackForm(1L));
    }
}
