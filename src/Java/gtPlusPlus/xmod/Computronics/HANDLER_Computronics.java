package gtPlusPlus.xmod.Computronics;

import gregtech.GT_Mod;
import gregtech.api.enums.*;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.lib.LoadedMods;

public class HANDLER_Computronics {

	public static void init() {
		if (LoadedMods.Computronics) {

		}
		else {
			GT_Mod.gregtechproxy.addFluid("Argon", "Argon", Materials.Argon, 2, 295,
					GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Argon, 1L),
					GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1L), 1000);
			GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 1L),
					Materials.Air.getGas(1000L), Materials.Nitrogen.getGas(780L), 1600, 32, false);
			GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 2L),
					Materials.Air.getGas(1000L), Materials.Oxygen.getGas(210L), 1600, 128, false);
			GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 3L),
					Materials.Air.getGas(1000L), Materials.Argon.getGas(5L), 6000, 512, false);
			GT_Values.RA.addElectrolyzerRecipe(ItemList.Cell_Air.get(1), null, null, Materials.Air.getGas(2000L),
					ItemList.Cell_Empty.get(1), null, null, null, null, null, null, 800, 30);
		}
	}

	public static void postInit() {
		if (LoadedMods.Computronics) {

		}

	}

	public static void preInit() {
		if (LoadedMods.Computronics) {

		}

	}

}
