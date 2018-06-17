package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialMixer;

public class GregtechIndustrialMixer {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Industrial Mixer Multiblock.");
			if (CORE.ConfigSwitches.enableMultiblock_IndustrialPlatePress) {
				run1();
			}
		}

	}

	private static void run1() {
		// Industrial Mixer Multiblock
		GregtechItemList.Industrial_Mixer.set(new GregtechMetaTileEntity_IndustrialMixer(811,
				"industrialmixer.controller.tier.single", "Industrial Mixing Machine").getStackForm(1L));

	}
	
}
