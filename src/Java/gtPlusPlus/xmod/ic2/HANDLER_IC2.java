package gtPlusPlus.xmod.ic2;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.ic2.item.IC2_Items;
import gtPlusPlus.xmod.ic2.recipe.RECIPE_IC2;

public class HANDLER_IC2{

	public static void preInit() {
		if (LoadedMods.IndustrialCraft2){
			IC2_Items.register();
			//new BlockRTG(InternalName.beer);
		}
	}

	public static void init() {
		if (LoadedMods.IndustrialCraft2){

		}
	}

	public static void postInit() {
		if (LoadedMods.IndustrialCraft2){
			RECIPE_IC2.initRecipes();
		}
	}

}
