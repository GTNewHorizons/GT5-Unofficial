package gtPlusPlus.core.handler;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gtPlusPlus.recipes.RecipeRemovals;
import gtPlusPlus.xmod.bartcrops.CropsPlusPlusHandler;
import gtPlusPlus.xmod.bop.BiomesOPlentyHandler;
import gtPlusPlus.xmod.forestry.ForestryHandler;
import gtPlusPlus.xmod.gregtech.HandlerGT;
import gtPlusPlus.xmod.ic2.HandlerIC2;
import gtPlusPlus.xmod.railcraft.HandlerRailcraft;
import gtPlusPlus.xmod.thermalfoundation.HandlerTF;
import gtPlusPlus.xmod.tinkers.HandlerTinkers;

public class CompatIntermodStaging {

    public static void preInit(FMLPreInitializationEvent preinit) {
        HandlerGT.preInit();
        HandlerTF.preInit();
        ForestryHandler.preInit();
        HandlerIC2.preInit();
        BiomesOPlentyHandler.preInit();
        CropsPlusPlusHandler.preInit(preinit);
        HandlerRailcraft.preInit();
    }

    public static void init(FMLInitializationEvent init) {
        HandlerGT.init();
        HandlerTF.init();
    }

    public static void postInit(FMLPostInitializationEvent postinit) {
        HandlerGT.postInit();
        HandlerTF.postInit();
        ForestryHandler.postInit();
        HandlerIC2.postInit();
        BiomesOPlentyHandler.postInit();
        HandlerTinkers.postInit();
        CropsPlusPlusHandler.postInit(postinit);
        HandlerRailcraft.postInit();
        RecipeRemovals.postInit();
    }

    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        HandlerGT.onLoadComplete(event);
        RecipeRemovals.onLoadComplete();
    }
}
