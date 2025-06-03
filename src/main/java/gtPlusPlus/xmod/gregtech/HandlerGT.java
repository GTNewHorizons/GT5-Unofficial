package gtPlusPlus.xmod.gregtech;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import gregtech.api.items.MetaGeneratedTool;
import gtPlusPlus.core.handler.CompatHandler;
import gtPlusPlus.recipes.CokeAndPyrolyseOven;
import gtPlusPlus.xmod.gregtech.common.MetaGTProxy;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechTools;
import gtPlusPlus.xmod.gregtech.loaders.GTPPBlocks;
import gtPlusPlus.xmod.gregtech.loaders.ProcessingAngleGrinder;
import gtPlusPlus.xmod.gregtech.loaders.ProcessingElectricSnips;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoaderMolecularTransformer;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoaderTreeFarm;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechAdvancedBoilers;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechConduits;
import toxiceverglades.gen.WorldGenEverglades;

public class HandlerGT {

    public static final List<WorldGenEverglades> sWorldgenListEverglades = new ArrayList<>();
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

        // Only loads if the config option is true (default: true)
        new ProcessingAngleGrinder().run();
        new ProcessingElectricSnips().run();

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
