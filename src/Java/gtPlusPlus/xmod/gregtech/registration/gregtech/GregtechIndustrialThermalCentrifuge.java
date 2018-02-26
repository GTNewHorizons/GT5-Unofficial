package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_IndustrialThermalCentrifuge;

public class GregtechIndustrialThermalCentrifuge {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Industrial Thermal Centrifuge Multiblock.");
			if (CORE.ConfigSwitches.enableMultiblock_IndustrialThermalCentrifuge) {
				run1();
			}
		}

	}

	private static void run1() {
		GregtechItemList.Industrial_ThermalCentrifuge.set(new GregtechMetaTileEntity_IndustrialThermalCentrifuge(849,
				"industrialthermalcentrifuge.controller.tier.single", "Large Thermal Refinery").getStackForm(1L));

	}
}