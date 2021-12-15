package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaCondensor;

public class GregtechSteamCondenser {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Steam Condensor.");
			if (CORE.ConfigSwitches.enableMachine_SteamConverter) {
				run1();
			}
		}

	}

	private static void run1() {
		// Steam Condensors
		GregtechItemList.Condensor_MAX.set(
				new GregtechMetaCondensor(769, "steamcondensor.01.tier.single", "Steam Condensor").getStackForm(1L));

	}
}
