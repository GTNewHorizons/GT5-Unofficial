package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GregtechMetaTreeFarmerStructural;

public class GregtechTreeFarmerTE
{
	public static void run()
	{
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Tree Farmer Structural Block.");
			if (CORE.configSwitches.enableMachine_Safes) run1();
		}

	}

	private static void run1()
	{

		GregtechItemList.TreeFarmer_Structural.set(new GregtechMetaTreeFarmerStructural(752, "treefarmer.structural", "Farm Keeper", 0).getStackForm(1L));

		




	}
}
