package gtPlusPlus.xmod.sc2;

import gtPlusPlus.core.lib.LoadedMods;
import vswe.stevescarts.ModuleData.GppModuleData;

public class HANDLER_SC2 {

	public synchronized static void preInit() {
		if (LoadedMods.StevesCarts){
			GppModuleData.loadGpp();
		}
	}

	public static void init(){
	}

	public static void postInit(){
	}


}
