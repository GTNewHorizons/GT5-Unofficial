package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.util.FishPondFakeRecipe;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntity_IndustrialFishingPond;

public class GregtechIndustrialFishPond {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Industrial Fishing Pond Multiblock.");
			//if (CORE.ConfigSwitches.enableMultiblock_IndustrialWashPlant) {
			FishPondFakeRecipe.generateFishPondRecipes();
			run1();
			//}
		}

	}

	private static void run1() {
		GregtechItemList.Industrial_FishingPond.set(new GregtechMetaTileEntity_IndustrialFishingPond(829,
				"industrial.fishpond.controller.tier.single", "Zhuhai - Fishing Port").getStackForm(1L));

	}
}