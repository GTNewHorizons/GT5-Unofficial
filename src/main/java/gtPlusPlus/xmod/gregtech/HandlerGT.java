package gtPlusPlus.xmod.gregtech;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import gregtech.api.items.MetaGeneratedTool;
import gtPlusPlus.core.handler.CompatHandler;
import gtPlusPlus.recipes.CokeAndPyrolyseOven;
import gtPlusPlus.xmod.gregtech.common.MetaGTProxy;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechTools;
import gtPlusPlus.xmod.gregtech.loaders.GTPPBlocks;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoaderMolecularTransformer;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoaderTreeFarm;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechAdvancedBoilers;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechConduits;

public class HandlerGT {

    public static MetaGeneratedTool sMetaGeneratedToolInstance;

    public static void init() {

        // Load General Blocks and set up some Basic Meta Tile Entity states
        GTPPBlocks.run();

        // Add Custom Pipes, Wires and Cables.
        GregtechConduits.run();

        // Register Tile Entities
        CompatHandler.registerGregtechMachines();

        sMetaGeneratedToolInstance = MetaGeneratedGregtechTools.getInstance();
    }

    public static void postInit() {

        // Add recipes
        CokeAndPyrolyseOven.postInit();
        GregtechAdvancedBoilers.addRecipes();

        // Register some custom recipe maps for any enabled multiblocks.
        // MultiblockRecipeMapHandler.run();
    }

    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        CokeAndPyrolyseOven.onLoadComplete();
        MetaGTProxy.fixIC2FluidNames();
        RecipeLoaderTreeFarm.generateRecipes();
        RecipeLoaderMolecularTransformer.run();
    }
}
