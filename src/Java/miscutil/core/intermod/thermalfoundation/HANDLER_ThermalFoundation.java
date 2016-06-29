package miscutil.core.intermod.thermalfoundation;

import miscutil.core.intermod.thermalfoundation.block.TF_Blocks;
import miscutil.core.intermod.thermalfoundation.fluid.TF_Fluids;
import miscutil.core.intermod.thermalfoundation.item.TF_Items;
import miscutil.core.intermod.thermalfoundation.recipe.TF_Gregtech_Recipes;
import miscutil.core.lib.LoadedMods;

public class HANDLER_ThermalFoundation {

	public static void preInit(){		
		if (LoadedMods.CoFHCore){
			TF_Fluids.preInit();
			TF_Items.preInit();
			TF_Blocks.preInit();
		}		
	}

	public static void init(){
		if (LoadedMods.CoFHCore){
			TF_Fluids.init();
			TF_Blocks.init();	
			TF_Items.init();
		}		
	}

	public static void postInit(){
		if (LoadedMods.CoFHCore){
			TF_Fluids.postInit();
			TF_Items.postInit();
			TF_Blocks.postInit();
			if(LoadedMods.Gregtech){
				TF_Gregtech_Recipes.run();
			}	
		}		
	}	
}
