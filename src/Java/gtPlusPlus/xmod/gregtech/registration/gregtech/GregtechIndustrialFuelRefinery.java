package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_Refinery;

public class GregtechIndustrialFuelRefinery {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Utils.LOG_INFO("Gregtech5u Content | Registering Industrial Fuel Processing and Refinery Multiblock.");
			if (CORE.ConfigSwitches.enableMultiblock_NuclearFuelRefinery) {
				run1();
			}
		}

	}

	private static void run1() {
		// Industrial Maceration Stack Multiblock
		GregtechItemList.Industrial_FuelRefinery.set(new GregtechMetaTileEntity_Refinery(835,
				"industrialrefinery.controller.tier.single", "Fission Fuel Processing Plant").getStackForm(1L));

	}
}