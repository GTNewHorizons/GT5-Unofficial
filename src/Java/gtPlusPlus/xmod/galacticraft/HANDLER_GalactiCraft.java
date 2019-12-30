package gtPlusPlus.xmod.galacticraft;

import gtPlusPlus.core.lib.LoadedMods;

public class HANDLER_GalactiCraft {
	
	//private static final HashMap<String, BaseSolarSystem> mSystemsCache = new HashMap<String, BaseSolarSystem>();
	
	public static void preInit(){
		if (LoadedMods.GalacticraftCore){
			//mSystemsCache.put("HD10180", new SystemHD10180());
			/*for (BaseSolarSystem solar : mSystemsCache.values()) {
				Logger.SPACE("Running 'pre-init' for "+solar.mSystemName);
				solar.preInit();
			}*/
		}		
	}

	public static void init(){
		if (LoadedMods.GalacticraftCore){
			/*for (BaseSolarSystem solar : mSystemsCache.values()) {
				Logger.SPACE("Running 'init' for "+solar.mSystemName);
				solar.init();
			}*/
		}
	}

	public static void postInit(){
		if (LoadedMods.GalacticraftCore){
			
		}
	}
	
}
