package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_IndustrialCokeOven;

public class GregtechIndustrialCokeOven {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Industrial Coke Oven Multiblock.");
			if (CORE.ConfigSwitches.enableMultiblock_IndustrialCokeOven) {
				run1();
			}
		}

	}

	private static void run1() {
		// Industrial Centrifuge Multiblock
		GregtechItemList.Industrial_CokeOven.set(new GregtechMetaTileEntity_IndustrialCokeOven(791,
				"industrialcokeoven.controller.tier.single", "Industrial Coke Oven").getStackForm(1L));

	}
}