package miscutil.core.xmod.growthcraft;

import miscutil.core.lib.LoadedMods;
import miscutil.core.xmod.growthcraft.booze.Register_Booze;

public class HANDLER_GC {


	//Run me during Pre-Init
	public static void preInit(){
		if (LoadedMods.Growthcraft){
			Register_Booze.preInit();			
		}
	}

	public static void init(){
		if (LoadedMods.Growthcraft){

		}
	}

	public static void postInit(){
		if (LoadedMods.Growthcraft){

		}
	}

}
