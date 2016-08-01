package miscutil.core.handler.registration.gregtech;

import miscutil.core.util.Utils;
import miscutil.core.xmod.gregtech.api.enums.GregtechItemList;
import miscutil.core.xmod.gregtech.api.metatileentity.implementations.GregtechMetaCondensor;

public class GregtechSteamCondenser
{



	public static void run()
	{
		if (miscutil.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Steam Condensor.");
			run1();
		}

	}

	private static void run1()
	{
		//Steam Condensors
		GregtechItemList.Condensor_MAX.set(new GregtechMetaCondensor(769, "steamcondensor.01.tier.single", "Steam Condensor").getStackForm(1L));
		
	}
}
