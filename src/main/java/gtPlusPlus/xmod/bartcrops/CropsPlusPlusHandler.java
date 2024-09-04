package gtPlusPlus.xmod.bartcrops;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CropsPlusPlusHandler {

    public static void preInit(FMLPreInitializationEvent preinit) {
        LoaderOfTheCrops.load(preinit);
    }

    public static void postInit(FMLPostInitializationEvent postinit) {
        LoaderOfTheCrops.register();
        LoaderOfTheCrops.registerBaseSeed();
    }
}
