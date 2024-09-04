package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_ThermalCentrifuge;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialThermalCentrifuge;

public class GregtechIndustrialThermalCentrifuge {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Thermal Centrifuge Multiblock.");
        if (GTPPCore.ConfigSwitches.enableMultiblock_IndustrialThermalCentrifuge) {
            run1();
        }
    }

    private static void run1() {
        GregtechItemList.Industrial_ThermalCentrifuge.set(
            new MTEIndustrialThermalCentrifuge(
                Industrial_ThermalCentrifuge.ID,
                "industrialthermalcentrifuge.controller.tier.single",
                "Large Thermal Refinery").getStackForm(1L));
    }
}
