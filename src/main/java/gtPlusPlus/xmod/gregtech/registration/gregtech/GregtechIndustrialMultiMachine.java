package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_MultiMachine;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialMultiMachine;

public class GregtechIndustrialMultiMachine {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Multi-Machine Multiblock.");
        if (CORE.ConfigSwitches.enableMultiblock_IndustrialMultiMachine) {
            run1();
        }
    }

    private static void run1() {
        GregtechItemList.Industrial_MultiMachine.set(
            new GregtechMetaTileEntity_IndustrialMultiMachine(
                Industrial_MultiMachine.ID,
                "industrialmultimachine.controller.tier.single",
                "Large Processing Factory").getStackForm(1L));
    }
}
