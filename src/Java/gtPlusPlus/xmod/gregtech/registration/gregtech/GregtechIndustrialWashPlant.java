package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_IndustrialMacerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_IndustrialThermalCentrifuge;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_IndustrialWashPlant;

public class GregtechIndustrialWashPlant {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Utils.LOG_INFO("Gregtech5u Content | Registering Industrial Wash Plant Multiblock.");
			if (CORE.ConfigSwitches.enableMultiblock_IndustrialWashPlant) {
				run1();
			}
		}

	}

	private static void run1() {
		GregtechItemList.Industrial_WashPlant.set(new GregtechMetaTileEntity_IndustrialWashPlant(850,
				"industrialwashplant.controller.tier.single", "Ore Washing Plant").getStackForm(1L));

	}
}