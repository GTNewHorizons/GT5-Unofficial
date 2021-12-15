package gtPlusPlus.xmod.bartcrops;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class HANDLER_CropsPlusPlus {

	public static void preInit(FMLPreInitializationEvent preinit) {
		LoaderOfTheCrops.load(preinit);	
	}
	
	public static void init(FMLInitializationEvent init) {
		//registerItems();		
	}

	public static void postInit(FMLPostInitializationEvent postinit) {
		LoaderOfTheCrops.register();
		LoaderOfTheCrops.registerBaseSeed();
	}
}
