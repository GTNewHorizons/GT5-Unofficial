package miscutil.core.xmod.ic2;

import miscutil.core.lib.LoadedMods;
import miscutil.core.xmod.ic2.item.IC2_Items;
import miscutil.core.xmod.ic2.recipe.RECIPE_IC2;

public class HANDLER_IC2{

	public static void preInit() {
		if (LoadedMods.IndustrialCraft2){
			IC2_Items.register();
		    //new IC2_BlockKineticGenerator(InternalName.blockKineticGenerator);
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
