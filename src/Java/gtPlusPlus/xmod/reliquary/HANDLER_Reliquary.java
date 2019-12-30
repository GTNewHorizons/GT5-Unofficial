package gtPlusPlus.xmod.reliquary;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.reliquary.util.ReliquaryRecipeHandler;

public class HANDLER_Reliquary {

	public static void preInit() {
		if (LoadedMods.Reliquary) {

		}
	}

	public static void init() {
		if (LoadedMods.Reliquary) {

		}
	}

	public static void postInit() {
		if (LoadedMods.Reliquary) {
			ReliquaryRecipeHandler.gregifyDefaultRecipes();
		}
	}

}
