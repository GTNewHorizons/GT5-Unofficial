package gtPlusPlus.xmod.psychedelicraft;

import gtPlusPlus.core.lib.LoadedMods;

public class HANDLER_Psych {

	public static void init() {
		if (LoadedMods.Psychedelicraft) {
			// PS_Fluids.registerAlcohols();
		}
	}

	public static void postInit() {
		if (LoadedMods.Psychedelicraft) {

		}
	}

	public static void preInit() {
		if (LoadedMods.Psychedelicraft) {
			// PS_Fluids.registerFluids();
		}
	}

}
