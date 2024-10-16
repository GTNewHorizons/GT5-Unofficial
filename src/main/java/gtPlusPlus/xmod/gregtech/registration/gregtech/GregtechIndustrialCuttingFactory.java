package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_CuttingFactoryController;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialCuttingMachine;

public class GregtechIndustrialCuttingFactory {

    public static void run() {
        GregtechItemList.Industrial_CuttingFactoryController.set(
            new MTEIndustrialCuttingMachine(
                Industrial_CuttingFactoryController.ID,
                "industrialcuttingmachine.controller.tier.01",
                "Industrial Cutting Factory").getStackForm(1L));
    }

}
