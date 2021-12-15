package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialAlloySmelter;

public class GregtechIndustrialAlloySmelter {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Industrial Alloy Smelter Multiblock.");
			run1();		
		}

	}

	private static void run1() {
		GregtechItemList.Industrial_AlloySmelter.set(new GregtechMetaTileEntity_IndustrialAlloySmelter(31023,
				"industrialalloysmelter.controller.tier.single", "Zyngen").getStackForm(1L));

	}
}