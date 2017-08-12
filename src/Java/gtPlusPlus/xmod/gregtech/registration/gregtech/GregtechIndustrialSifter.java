package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_IndustrialMacerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_IndustrialSifter;

public class GregtechIndustrialSifter {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Utils.LOG_INFO("Gregtech5u Content | Registering Industrial Sifter Multiblock.");
			if (CORE.configSwitches.enableMultiblock_IndustrialSifter) { // TODO
				run1();
			}
		}

	}

	private static void run1() {
		// Industrial Maceration Stack Multiblock
		GregtechItemList.Industrial_Sifter.set(new GregtechMetaTileEntity_IndustrialSifter(840,
				"industrialsifter.controller.tier.single", "Large Sifter Control Block").getStackForm(1L));

	}
}