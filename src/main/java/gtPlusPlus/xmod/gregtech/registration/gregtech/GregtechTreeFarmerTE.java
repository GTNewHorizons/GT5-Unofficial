package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GregtechMetaTreeFarmerStructural;

public class GregtechTreeFarmerTE {
	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Tree Farmer Structural Block.");
			if (CORE.ConfigSwitches.enableMultiblock_TreeFarmer) {
				run1();
			}
		}
	}

	private static void run1() {
		GregtechItemList.TreeFarmer_Structural.set(
				new GregtechMetaTreeFarmerStructural(752, "treefarmer.structural", "Farm Keeper", 0).getStackForm(1L));
	}
}
