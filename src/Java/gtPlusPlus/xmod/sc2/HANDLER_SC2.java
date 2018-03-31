package gtPlusPlus.xmod.sc2;

import gtPlusPlus.core.lib.LoadedMods;
import vswe.stevescarts.ModuleData.ModuleDataLoader;

public class HANDLER_SC2 {

	public static void preInit(){
		if (LoadedMods.StevesCarts){
			ModuleDataLoader.load();
		}
	}

	public static void init(){
		if (LoadedMods.StevesCarts){

		}
	}

	public static void postInit(){
		if (LoadedMods.StevesCarts){
			
		}
	}


}
