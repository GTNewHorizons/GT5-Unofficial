package miscutil.core.xmod.forestry;

import miscutil.core.lib.LoadedMods;
import miscutil.core.xmod.forestry.apiculture.blocks.FR_BlockRegistryApiculture;
import miscutil.core.xmod.forestry.apiculture.items.FR_ItemRegistryApiculture;
import miscutil.core.xmod.forestry.apiculture.tiles.FR_TileHandler;

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
