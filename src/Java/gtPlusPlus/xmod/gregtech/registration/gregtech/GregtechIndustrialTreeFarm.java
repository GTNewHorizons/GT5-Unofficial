package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntityTreeFarm;

public class GregtechIndustrialTreeFarm {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Utils.LOG_INFO("Gregtech5u Content | Registering Tree Farm Multiblock.");
			if (CORE.ConfigSwitches.enableMultiblock_TreeFarmer) {
				run1();
			}
		}

	}

	private static void run1() {
		// Industrial Maceration Stack Multiblock
		GregtechItemList.Industrial_TreeFarm
				.set(new GregtechMetaTileEntityTreeFarm(836, "treefarm.controller.tier.single", "Tree Farmer")
						.getStackForm(1L));

	}
}