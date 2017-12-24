package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_MultiTank;

public class GregtechIndustrialMultiTank {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Industrial Multitank controller blocks.");
			if (CORE.ConfigSwitches.enableMultiblock_MultiTank) {
				run1();
			}
		}

	}

	private static void run1() {
		GregtechItemList.Industrial_MultiTank
				.set(new GregtechMetaTileEntity_MultiTank(827, "multitank.controller.tier.single", "Gregtech Multitank")
						.getStackForm(1L));
		// GregtechItemList.Industrial_MultiTankDense.set(new
		// GregtechMetaTileEntityMultiTankDense(828,
		// "multitankdense.controller.tier.single", "Gregtech Dense
		// Multitank").getStackForm(1L));

	}
}
