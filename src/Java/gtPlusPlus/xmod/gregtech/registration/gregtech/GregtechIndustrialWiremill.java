package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_IndustrialWireMill;

public class GregtechIndustrialWiremill {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Utils.LOG_INFO("Gregtech5u Content | Registering Industrial Wire Factory Multiblock.");
			if (CORE.ConfigSwitches.enableMultiblock_IndustrialWireMill) {
				run1();
			}
		}
	}

	private static void run1() {
		// Industrial Wire Factory Multiblock
		GregtechItemList.Industrial_WireFactory.set(new GregtechMetaTileEntity_IndustrialWireMill(798,
				"industrialwiremill.controller.tier.single", "Wire Factory Controller").getStackForm(1L));

	}
}