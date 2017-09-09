package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class GregtechSuperConductionPoint {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			//Utils.LOG_INFO("Gregtech5u Content | Registering Super Conductor Input Node.");
			run1();
		}

	}

	private static void run1() {
		// Steam Condensors
		//GregtechItemList.SuperConductorInputNode.set(new GregtechMetaTileEntitySuperCondensor(811,
		//		"superconductor.01.input.single", "Power Phase Shifting Station", 8).getStackForm(1L));

	}
}
