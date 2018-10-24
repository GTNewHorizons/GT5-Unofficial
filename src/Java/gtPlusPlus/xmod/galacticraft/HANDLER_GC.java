package gtPlusPlus.xmod.galacticraft;

import java.util.HashMap;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.galacticraft.system.BaseSolarSystem;
import gtPlusPlus.xmod.galacticraft.system.hd10180.SystemHD10180;

public class HANDLER_GC {
	
	private static final HashMap<String, BaseSolarSystem> mSystemsCache = new HashMap<String, BaseSolarSystem>();
		
	static {
		mSystemsCache.put("HD10180", new SystemHD10180());
	}
	
	public static void preInit(){
		if (LoadedMods.GalacticraftCore){
			for (BaseSolarSystem solar : mSystemsCache.values()) {
				solar.preInit();
			}
		}		
	}

	public static void Init(){
		if (LoadedMods.GalacticraftCore){
			for (BaseSolarSystem solar : mSystemsCache.values()) {
				solar.init();
			}
		}
	}

	public static void postInit(){
		if (LoadedMods.GalacticraftCore){
			
		}
	}
	
}
