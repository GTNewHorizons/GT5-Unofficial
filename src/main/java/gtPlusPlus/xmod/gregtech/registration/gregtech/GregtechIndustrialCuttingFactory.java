package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_CuttingFactoryController;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialCuttingMachine;

public class GregtechIndustrialCuttingFactory {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Cutting Factory Multiblock.");
        if (GTPPCore.ConfigSwitches.enableMultiblock_IndustrialCuttingMachine) {
            run1();
        }
    }

    private static void run1() {
        // Industrial Wire Factory Multiblock
        GregtechItemList.Industrial_CuttingFactoryController.set(
            new MTEIndustrialCuttingMachine(
                Industrial_CuttingFactoryController.ID,
                "industrialcuttingmachine.controller.tier.01",
                "Cutting Factory Controller").getStackForm(1L));
    }
}
