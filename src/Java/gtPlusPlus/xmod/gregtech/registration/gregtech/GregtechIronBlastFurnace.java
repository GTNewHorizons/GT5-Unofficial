package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IronBlastFurnace;

public class GregtechIronBlastFurnace {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Iron Blast Furnace.");
			if (CORE.ConfigSwitches.enableMultiblock_IronBlastFurnace) {
				run1();
			}
		}

	}

	private static void run1() {
		GregtechItemList.Machine_Iron_BlastFurnace.set(new GregtechMetaTileEntity_IronBlastFurnace(768,
				"ironmachine.blastfurnace", "Iron Plated Blast Furnace").getStackForm(1L));
	}
}
