package gtPlusPlus.xmod.thermalfoundation;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.thermalfoundation.block.TF_Blocks;
import gtPlusPlus.xmod.thermalfoundation.fluid.TF_Fluids;
import gtPlusPlus.xmod.thermalfoundation.item.TF_Items;
import gtPlusPlus.xmod.thermalfoundation.recipe.TF_Gregtech_Recipes;

public class HANDLER_TF{

	public static void preInit(){
		if (LoadedMods.CoFHCore && !LoadedMods.ThermalFoundation){
			TF_Fluids.preInit();
			TF_Items.preInit();
			TF_Blocks.preInit();
			if (LoadedMods.Gregtech){
				if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
					//mGregMatLoader.enableMaterial(Materials.Enderium);
				}
			}
		}
	}

	public static void init(){
		if (LoadedMods.CoFHCore && !LoadedMods.ThermalFoundation){
			TF_Fluids.init();
			TF_Blocks.init();
			TF_Items.init();
		}
	}

	public static void postInit(){
		if (LoadedMods.CoFHCore && !LoadedMods.ThermalFoundation){
			TF_Fluids.postInit();
			TF_Items.postInit();
			TF_Blocks.postInit();
			if(LoadedMods.Gregtech){
				TF_Gregtech_Recipes.run();
			}
		}
	}
}
