package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_MacerationStack;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialMacerator;

public class GregtechIndustrialMacerator {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Maceration Stack Multiblock.");
        if (GTPPCore.ConfigSwitches.enableMultiblock_IndustrialMacerationStack) {
            run1();
        }
    }

    private static void run1() {
        // Industrial Maceration Stack Multiblock
        GregtechItemList.Industrial_MacerationStack.set(
            new MTEIndustrialMacerator(
                Industrial_MacerationStack.ID,
                "industrialmacerator.controller.tier.single",
                "Maceration Stack Controller").getStackForm(1L));
    }
}
