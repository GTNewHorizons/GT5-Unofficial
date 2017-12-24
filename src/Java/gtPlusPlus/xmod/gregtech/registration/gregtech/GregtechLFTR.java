package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMTE_NuclearReactor;

public class GregtechLFTR {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Liquid Fluorine Thorium Reactor [LFTR].");
			if (CORE.ConfigSwitches.enableMultiblock_LiquidFluorideThoriumReactor) {
				run1();
			}
		}

	}

	private static void run1() {
		// LFTR
		GregtechItemList.ThoriumReactor
				.set(new GregtechMTE_NuclearReactor(751, "lftr.controller.single", "Thorium Reactor [LFTR]")
						.getStackForm(1L));

	}
}
