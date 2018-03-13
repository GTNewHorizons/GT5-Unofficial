package gtPlusPlus.xmod.tinkers;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.tinkers.util.TinkersUtils;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class HANDLER_Tinkers {

	public static final void preInit() {
		if (LoadedMods.TiCon) {

		}
	}

	public static final void init() {
		if (LoadedMods.TiCon) {
			//Migrate TiCon further back in the oreDict so that I never grab items from it.
			//TinkersUtils.stopTiconLoadingFirst();
			Fluid pyrotheumFluid = FluidRegistry.getFluid("pyrotheum");
			if (pyrotheumFluid != null) {
				//Enable Pyrotheum as Fuel for the Smeltery
				TinkersUtils.addSmelteryFuel(pyrotheumFluid, 5000, 70); // pyrotheum lasts 3.5 seconds per 15 mb
			}
		}
	}

	public static final void postInit() {
		if (LoadedMods.TiCon) {

		}	
	}

}
