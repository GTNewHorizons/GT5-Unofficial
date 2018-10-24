package gtPlusPlus.xmod.galacticraft;

import java.util.HashMap;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.galacticraft.system.core.space.BaseSolarSystem;
import gtPlusPlus.xmod.galacticraft.system.hd10180.SystemHD10180;

public class HANDLER_GalactiCraft {
	
	private static final HashMap<String, BaseSolarSystem> mSystemsCache = new HashMap<String, BaseSolarSystem>();
		
	static {
		mSystemsCache.put("HD10180", new SystemHD10180());
	}
	
	public static void preInit(){
		if (LoadedMods.GalacticraftCore){
			for (BaseSolarSystem solar : mSystemsCache.values()) {
				Logger.SPACE("Running 'pre-init' for "+solar.mSystemName);
				solar.preInit();
			}
		}		
	}

	public static void init(){
		if (LoadedMods.GalacticraftCore){
			for (BaseSolarSystem solar : mSystemsCache.values()) {
				Logger.SPACE("Running 'init' for "+solar.mSystemName);
				solar.init();
			}
		}
	}

	public static void postInit(){
		if (LoadedMods.GalacticraftCore){
			
		}
	}
	
}
