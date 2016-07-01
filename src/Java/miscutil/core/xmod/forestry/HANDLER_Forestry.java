package miscutil.core.xmod.forestry;

import miscutil.core.lib.LoadedMods;
import miscutil.core.xmod.forestry.apiculture.items.FR_ItemRegistry;

public class HANDLER_Forestry {

	public static void preInit(){		
		if (LoadedMods.Forestry){
			FR_ItemRegistry.Register();
			//FR_BlockRegistryApiculture.RegistryApiculture();
		}		
	}

	public static void Init(){
		if (LoadedMods.Forestry){
			//FR_TileHandler.init();			
		}		
	}

	public static void postInit(){
		if (LoadedMods.Forestry){
	
		}		
	}	
}
