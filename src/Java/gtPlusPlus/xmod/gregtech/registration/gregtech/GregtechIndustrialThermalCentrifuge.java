package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_IndustrialMacerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_IndustrialThermalCentrifuge;

public class GregtechIndustrialThermalCentrifuge {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Utils.LOG_INFO("Gregtech5u Content | Registering Industrial Thermal Centrifuge Multiblock.");
			if (CORE.configSwitches.enableMultiblock_IndustrialThermalCentrifuge) {
				run1();
			}
		}

	}

	private static void run1() {
		GregtechItemList.Industrial_ThermalCentrifuge.set(new GregtechMetaTileEntity_IndustrialThermalCentrifuge(828,
				"industrialthermalcentrifuge.controller.tier.single", "Large Thermal Refinery").getStackForm(1L));

	}
}