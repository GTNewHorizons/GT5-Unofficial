package gtPlusPlus.xmod.forestry;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.forestry.bees.alveary.AlvearyHandler;
import gtPlusPlus.xmod.forestry.bees.items.FR_ItemRegistry;
import gtPlusPlus.xmod.forestry.bees.recipe.FR_Gregtech_Recipes;
import cpw.mods.fml.common.Optional;

public class HANDLER_FR {

	@Optional.Method(modid = "Forestry")
	public static void preInit(){		
		if (LoadedMods.Forestry){
			FR_ItemRegistry.Register();
			if (CORE.configSwitches.enableCustomAlvearyBlocks){
				AlvearyHandler.run();
			}
			//FR_BlockRegistryApiculture.RegistryApiculture();
		}		
	}
	@Optional.Method(modid = "Forestry")
	public static void Init(){
		if (LoadedMods.Forestry){
			//FR_TileHandler.init();	
			//new FR_GuiIDRegistry();
		}		
	}
	@Optional.Method(modid = "Forestry")
	public static void postInit(){
		if (LoadedMods.Forestry){
			FR_Gregtech_Recipes.registerItems();	
		}		
	}	
}
