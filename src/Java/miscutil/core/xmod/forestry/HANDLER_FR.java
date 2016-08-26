package miscutil.core.xmod.forestry;

import miscutil.core.lib.CORE;
import miscutil.core.lib.LoadedMods;
import miscutil.core.xmod.forestry.bees.alveary.TileAlvearyFrameHousing;
import miscutil.core.xmod.forestry.bees.items.FR_ItemRegistry;
import miscutil.core.xmod.forestry.bees.recipe.FR_Gregtech_Recipes;
import cpw.mods.fml.common.registry.GameRegistry;

public class HANDLER_FR {

	public static void preInit(){		
		if (LoadedMods.Forestry){
			FR_ItemRegistry.Register();
			if (CORE.configSwitches.enableCustomAlvearyBlocks){
				GameRegistry.registerTileEntity(TileAlvearyFrameHousing.class, "FrameHousing");
			}
			//FR_BlockRegistryApiculture.RegistryApiculture();
		}		
	}

	public static void Init(){
		if (LoadedMods.Forestry){
			//FR_TileHandler.init();	
			//new FR_GuiIDRegistry();
		}		
	}

	public static void postInit(){
		if (LoadedMods.Forestry){
			FR_Gregtech_Recipes.registerItems();	
		}		
	}	
}
