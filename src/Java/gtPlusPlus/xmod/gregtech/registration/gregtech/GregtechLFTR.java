package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GregtechMetaCondensor;

public class GregtechLFTR
{



	public static void run()
	{
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Liquid Fluorine Thorium Reactor [LFTR].");
			run1();
		}

	}

	private static void run1()
	{
		//LFTR
		GregtechItemList.ThoriumReactor.set(new GregtechMetaCondensor(751, "lftr.controller.single", "Thorium Reactor [LFTR]").getStackForm(1L));
		
	}
}
