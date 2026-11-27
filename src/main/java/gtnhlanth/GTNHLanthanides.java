package gtnhlanth;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.api.enums.Mods;
import gtnhlanth.common.CommonProxy;
import gtnhlanth.common.register.LanthItemList;
import gtnhlanth.common.tileentity.recipe.beamline.BeamlineRecipeLoader;
import gtnhlanth.loader.BotRecipes;
import gtnhlanth.loader.RecipeLoader;

@Mod(
    modid = Mods.ModIDs.G_T_N_H_LANTHANIDES,
    version = Tags.VERSION,
    name = Tags.MODNAME,
    dependencies = "required-after:IC2; " + "required-after:gregtech; "
        + "required-after:bartworks; "
        + "required-after:GoodGenerator; "
        + "before:miscutils; ")
public class GTNHLanthanides {

    @Mod.Instance(Mods.ModIDs.G_T_N_H_LANTHANIDES)
    public static GTNHLanthanides instance;

    @SidedProxy(clientSide = "gtnhlanth.client.ClientProxy", serverSide = "gtnhlanth.common.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public static void preInit(FMLPreInitializationEvent e) {

        LanthItemList.registerTypical();
        LanthItemList.registerGTMTE();

        proxy.preInit(e);
    }

    @EventHandler
    public static void init(FMLInitializationEvent e) {
        proxy.init(e);
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
}
