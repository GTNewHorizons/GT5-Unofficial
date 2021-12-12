package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.GregtechMetaTileEntity_RTG;

public class GregtechRTG {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering RTG.");
			run1();
		}
	}

	private static void run1() {
		GregtechItemList.RTG.set(
				new GregtechMetaTileEntity_RTG(869, "basicgenerator.rtg.tier.01", "Radioisotope Thermoelectric Generator", 3)
						.getStackForm(1L));
	}

}
