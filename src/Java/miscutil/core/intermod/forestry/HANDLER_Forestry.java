package miscutil.core.intermod.forestry;

import miscutil.core.intermod.forestry.apiculture.items.FR_ItemRegistryApiculture;
import miscutil.core.lib.LoadedMods;

public class HANDLER_Forestry {

	public static void preInit(){		
		if (LoadedMods.Forestry){
			FR_ItemRegistryApiculture.RegisterApiculture();
		}		
	}

	public static void Init(){
		if (LoadedMods.Forestry){
			
		}		
	}

	public static void postInit(){
		if (LoadedMods.Forestry){
	
		}		
	}	
}
