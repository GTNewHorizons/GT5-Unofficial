package gtPlusPlus.core.handler;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gtPlusPlus.recipes.RecipeRemovals;
import gtPlusPlus.xmod.bartcrops.HANDLER_CropsPlusPlus;
import gtPlusPlus.xmod.bop.HANDLER_BiomesOPlenty;
import gtPlusPlus.xmod.forestry.HANDLER_FR;
import gtPlusPlus.xmod.gregtech.HANDLER_GT;
import gtPlusPlus.xmod.ic2.HANDLER_IC2;
import gtPlusPlus.xmod.railcraft.HANDLER_Railcraft;
import gtPlusPlus.xmod.thermalfoundation.HANDLER_TF;
import gtPlusPlus.xmod.tinkers.HANDLER_Tinkers;

public class COMPAT_IntermodStaging {

    public static void preInit(FMLPreInitializationEvent preinit) {
        HANDLER_GT.preInit();
        HANDLER_TF.preInit();
        HANDLER_FR.preInit();
        HANDLER_IC2.preInit();
        HANDLER_BiomesOPlenty.preInit();
        HANDLER_CropsPlusPlus.preInit(preinit);
        HANDLER_Railcraft.preInit();
    }

    public static void init(FMLInitializationEvent init) {
        HANDLER_GT.init();
        HANDLER_TF.init();
    }

    public static void postInit(FMLPostInitializationEvent postinit) {
        HANDLER_GT.postInit();
        HANDLER_TF.postInit();
        HANDLER_FR.postInit();
        HANDLER_IC2.postInit();
        HANDLER_BiomesOPlenty.postInit();
        HANDLER_Tinkers.postInit();
        HANDLER_CropsPlusPlus.postInit(postinit);
        HANDLER_Railcraft.postInit();
        RecipeRemovals.postInit();
    }

    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        HANDLER_GT.onLoadComplete(event);
        RecipeRemovals.onLoadComplete();
    }
}
