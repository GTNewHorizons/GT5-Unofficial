package miscutil.core.handler;

import growthcraft.core.GrowthCraftCore;
import miscutil.core.intermod.growthcraft.Growthcraft_Handler;
import miscutil.core.lib.LoadedMods;

public class COMPAT_IntermodStaging {

	public static void load(){
		if (LoadedMods.Growthcraft || GrowthCraftCore.instance != null){
			Growthcraft_Handler.run();
		}
		
		
	}
	
	
}
