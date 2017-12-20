package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntityGeneratorArray;

public class GregtechIndustrialGeneratorArray {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Industrial Generator Array Multiblock.");
			//if (CORE.ConfigSwitches.enableMultiblock_IndustrialSifter) { // TODO
				run1();
			//}
		}

	}

	private static void run1() {
		// Industrial Maceration Stack Multiblock
		GregtechItemList.Generator_Array_Controller.set(new GregtechMetaTileEntityGeneratorArray(
				990,
				"generatorarray.controller.tier.01",
				"Large Generator Array").getStackForm(1L));
	}
}