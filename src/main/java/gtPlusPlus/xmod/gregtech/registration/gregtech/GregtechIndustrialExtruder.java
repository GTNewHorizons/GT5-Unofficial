package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialExtruder;

public class GregtechIndustrialExtruder {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Industrial Extrusion Multiblock.");
			if (CORE.ConfigSwitches.enableMultiblock_IndustrialExtrudingMachine) {
				run1();
			}
		}

	}

	private static void run1() {
		// Industrial Presser Multiblock
		GregtechItemList.Industrial_Extruder.set(new GregtechMetaTileEntity_IndustrialExtruder(859,
				"industrialextruder.controller.tier.single", "Industrial Extrusion Machine").getStackForm(1L));

	}
}