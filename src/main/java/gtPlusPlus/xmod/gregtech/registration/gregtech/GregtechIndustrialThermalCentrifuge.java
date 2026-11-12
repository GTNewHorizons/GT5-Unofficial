package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_ThermalCentrifuge;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialThermalCentrifugeLegacy;

public class GregtechIndustrialThermalCentrifuge {

    public static void run() {
        run1();
    }

    private static void run1() {
        GregtechItemList.Industrial_ThermalCentrifuge.set(
            new MTEIndustrialThermalCentrifugeLegacy(
                Industrial_ThermalCentrifuge.ID,
                "industrialthermalcentrifuge.controller.tier.single",
                "Large Thermal Refinery").getStackForm(1L));
    }
}
