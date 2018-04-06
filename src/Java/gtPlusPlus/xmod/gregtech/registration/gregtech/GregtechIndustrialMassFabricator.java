package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntity_MassFabricator;

public class GregtechIndustrialMassFabricator {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Industrial Matter Fabricator Multiblock.");
			if (CORE.ConfigSwitches.enableMultiblock_MatterFabricator) {
				run1();
			}
		}

	}

	private static void run1() {
		// Industrial Matter Fabricator Multiblock
		GregtechItemList.Industrial_MassFab.set(new GregtechMetaTileEntity_MassFabricator(799,
				"industrialmassfab.controller.tier.single", "Matter Fabrication CPU").getStackForm(1L));

	}
}