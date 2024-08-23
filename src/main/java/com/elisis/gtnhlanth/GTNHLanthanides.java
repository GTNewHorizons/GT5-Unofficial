package com.elisis.gtnhlanth;

import java.util.logging.Logger;

import com.elisis.gtnhlanth.common.CommonProxy;
import com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool;
import com.elisis.gtnhlanth.common.register.LanthItemList;
import com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool;
import com.elisis.gtnhlanth.common.tileentity.recipe.beamline.BeamlineRecipeLoader;
import com.elisis.gtnhlanth.loader.BotRecipes;
import com.elisis.gtnhlanth.loader.RecipeLoader;
import com.github.bartimaeusnek.bartworks.API.WerkstoffAdderRegistry;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(
    modid = Tags.MODID,
    version = Tags.VERSION,
    name = Tags.MODNAME,
    dependencies = "required-after:IC2; " + "required-after:gregtech; "
        + "required-after:bartworks; "
        + "required-after:GoodGenerator; "
        + "before:miscutils; ")
public class GTNHLanthanides {

    public static Logger LOG = Logger.getLogger("GTNH:Lanthanides");

    @Mod.Instance(Tags.MODID)
    public static GTNHLanthanides instance;

    @SidedProxy(
        clientSide = "com.elisis.gtnhlanth.client.ClientProxy",
        serverSide = "com.elisis.gtnhlanth.common.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public static void preInit(FMLPreInitializationEvent e) {

        WerkstoffAdderRegistry.addWerkstoffAdder(new WerkstoffMaterialPool());
        WerkstoffAdderRegistry.addWerkstoffAdder(new BotWerkstoffMaterialPool());

        LanthItemList.registerTypical();
        LanthItemList.registerGTMTE();

        // GregTech_API.sAfterGTPostload.add(new ZPMRubberChanges());
        proxy.preInit(e);
    }

    @EventHandler
    public static void init(FMLInitializationEvent e) {
        proxy.init(e);
        WerkstoffMaterialPool.runInit();
    }

    @EventHandler
    public static void postInit(FMLPostInitializationEvent e) {

        RecipeLoader.loadGeneral();
        RecipeLoader.loadLanthanideRecipes();
        RecipeLoader.addRandomChemCrafting();
        RecipeLoader.loadAccelerator();

        BeamlineRecipeLoader.load();

        BotRecipes.addGTRecipe();
        proxy.postInit(e);

    }

    @EventHandler
    public static void onModLoadingComplete(FMLLoadCompleteEvent e) {
        BotRecipes.removeRecipes();
        RecipeLoader.removeCeriumSources();
    }
}
