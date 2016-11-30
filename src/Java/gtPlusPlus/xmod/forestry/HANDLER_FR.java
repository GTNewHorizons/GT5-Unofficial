package gtPlusPlus.xmod.forestry;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.forestry.bees.alveary.AlvearyHandler;
import gtPlusPlus.xmod.forestry.bees.items.FR_ItemRegistry;
import gtPlusPlus.xmod.forestry.bees.recipe.FR_Gregtech_Recipes;

public class HANDLER_FR {


	public static void preInit(){		
		if (LoadedMods.Forestry){
			FR_ItemRegistry.Register();
			if (CORE.configSwitches.enableCustomAlvearyBlocks){
				AlvearyHandler.run();
			}
		}		
	}

	public static void Init(){
		if (LoadedMods.Forestry){
		
		}		
	}

	public static void postInit(){
		if (LoadedMods.Forestry){
			FR_Gregtech_Recipes.registerItems();	
		}		
	}	
}
