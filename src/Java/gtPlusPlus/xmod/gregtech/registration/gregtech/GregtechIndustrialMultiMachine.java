package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_IndustrialMultiMachine;

public class GregtechIndustrialMultiMachine {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Industrial Multi-Machine Multiblock.");
			if (CORE.ConfigSwitches.enableMultiblock_IndustrialMultiMachine) {
				run1();
			}
		}

	}

	private static void run1() {
		GregtechItemList.Industrial_MultiMachine.set(new GregtechMetaTileEntity_IndustrialMultiMachine(860,
				"industrialmultimachine.controller.tier.single", "Large Processing Factory").getStackForm(1L));

	}
}