package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.Industrial_Sifter;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialSifter;

public class GregtechIndustrialSifter {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Sifter Multiblock.");
        if (CORE.ConfigSwitches.enableMultiblock_IndustrialSifter) {
            run1();
        }
    }

    private static void run1() {
        GregtechItemList.Industrial_Sifter.set(
            new GregtechMetaTileEntity_IndustrialSifter(
                Industrial_Sifter.ID,
                "industrialsifter.controller.tier.single",
                "Large Sifter Control Block").getStackForm(1L));
    }
}
