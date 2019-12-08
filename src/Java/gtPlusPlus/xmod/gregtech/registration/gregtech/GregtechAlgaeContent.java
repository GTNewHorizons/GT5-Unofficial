package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.algae.GregtechMTE_AlgaePondBase;

public class GregtechAlgaeContent {

	public static void run() {
		if (LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Algae Content.");
			run1();
		}
	}

	private static void run1() {
		// Industrial Centrifuge Multiblock
		GregtechItemList.AlgaeFarm_Controller.set(
				new GregtechMTE_AlgaePondBase(997,	"algaefarm.controller.tier.single", "Algae Farm").getStackForm(1L));

	}

}
