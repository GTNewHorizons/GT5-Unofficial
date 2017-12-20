package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_IndustrialCuttingMachine;

public class GregtechIndustrialCuttingFactory {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Industrial Cutting Factory Multiblock.");
			if (CORE.ConfigSwitches.enableMultiblock_IndustrialCuttingMachine) {
				run1();
			}
		}
	}

	private static void run1() {
		// Industrial Wire Factory Multiblock
		GregtechItemList.Industrial_CuttingFactoryController.set(new GregtechMetaTileEntity_IndustrialCuttingMachine(992,
				"industrialcuttingmachine.controller.tier.01", "Cutting Factory Controller").getStackForm(1L));

	}
}