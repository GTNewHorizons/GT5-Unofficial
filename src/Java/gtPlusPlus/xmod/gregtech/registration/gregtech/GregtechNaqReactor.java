package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMTE_LargeNaqReactor;

public class GregtechNaqReactor {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Futuristic Naquadah Reactor {LNR].");
				run1();
		}

	}

	private static void run1() {
		// LFTR
		GregtechItemList.Controller_Naq_Reactor.set(new GregtechMTE_LargeNaqReactor(991, "lnr.controller.single", "Large Naquadah Reactor").getStackForm(1L));

	}
}
