package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_Sifter;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialSifter;

public class GregtechIndustrialSifter {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Sifter Multiblock.");
        if (GTPPCore.ConfigSwitches.enableMultiblock_IndustrialSifter) {
            run1();
        }
    }

    private static void run1() {
        GregtechItemList.Industrial_Sifter.set(
            new MTEIndustrialSifter(
                Industrial_Sifter.ID,
                "industrialsifter.controller.tier.single",
                "Large Sifter Control Block").getStackForm(1L));
    }
}
