package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaCondensor;

public class GregtechSteamCondenser
{



	public static void run()
	{
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Steam Condensor.");
			if (CORE.configSwitches.enableMachine_SteamConverter) {
				run1();
			}
		}

	}

	private static void run1()
	{
		//Steam Condensors
		GregtechItemList.Condensor_MAX.set(new GregtechMetaCondensor(769, "steamcondensor.01.tier.single", "Steam Condensor").getStackForm(1L));

	}
}
