package miscutil.core.intermod.forestry;

import miscutil.core.intermod.forestry.apiculture.blocks.FR_BlockRegistryApiculture;
import miscutil.core.intermod.forestry.apiculture.items.FR_ItemRegistryApiculture;
import miscutil.core.intermod.forestry.apiculture.tiles.FR_TileHandler;
import miscutil.core.lib.LoadedMods;

public class HANDLER_Forestry {

	public static void preInit(){		
		if (LoadedMods.Forestry){
			FR_ItemRegistryApiculture.RegisterApiculture();
			FR_BlockRegistryApiculture.RegistryApiculture();
		}		
	}

	public static void Init(){
		if (LoadedMods.Forestry){
			FR_TileHandler.init();			
		}		
	}

	public static void postInit(){
		if (LoadedMods.Forestry){
	
		}		
	}	
}
