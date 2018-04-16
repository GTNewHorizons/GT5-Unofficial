package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialVacuumFreezer;

public class GregtechIndustrialCryogenicFreezer {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Industrial Freezer Multiblock.");
			run1();
		}

	}

	private static void run1() {
		GregtechItemList.Industrial_Cryogenic_Freezer.set(new GregtechMetaTileEntity_IndustrialVacuumFreezer(910, "industrialfreezer.controller.tier.single", "Advanced Cryogenic Freezer").getStackForm(1L));
	}
}
