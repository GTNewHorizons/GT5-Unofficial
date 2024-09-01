package gtPlusPlus.xmod.gregtech;

import static gregtech.api.enums.Mods.AdvancedSolarPanel;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import gregtech.api.GregTech_API;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_Config;
import gtPlusPlus.core.handler.CompatHandler;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.everglades.gen.gt.WorldGen_GT;
import gtPlusPlus.recipes.CokeAndPyrolyseOven;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.api.util.GTPPConfig;
import gtPlusPlus.xmod.gregtech.common.MetaGTProxy;
import gtPlusPlus.xmod.gregtech.common.blocks.fluid.GregtechFluidHandler;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechTools;
import gtPlusPlus.xmod.gregtech.loaders.GTPPBlocks;
import gtPlusPlus.xmod.gregtech.loaders.ProcessingAngleGrinder;
import gtPlusPlus.xmod.gregtech.loaders.ProcessingElectricSnips;
import gtPlusPlus.xmod.gregtech.loaders.misc.AddCustomMachineToPA;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoaderAlgaeFarm;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoaderMolecularTransformer;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoaderTreeFarm;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechConduits;

public class HandlerGT {

    public static GT_Config mMaterialProperties = null;
    public static GTPPConfig sCustomWorldgenFile = null;
    public static final List<WorldGen_GT> sWorldgenListEverglades = new ArrayList<>();
    public static GT_MetaGenerated_Tool sMetaGeneratedToolInstance;

    public static void preInit() {

        if (mMaterialProperties != null) {
            GT_Materials.init(mMaterialProperties);
        }

        GregtechFluidHandler.run();
    }

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

        // Register custom singles to the PA
        AddCustomMachineToPA.register();

        // Register some custom recipe maps for any enabled multiblocks.
        // MultiblockRecipeMapHandler.run();

        if (GregtechItemList.Circuit_BioRecipeSelector.hasBeenSet()) {
            for (int i = 1; i <= 24; i++) {
                GregTech_API.registerConfigurationCircuit(CI.getNumberedBioCircuit(i), 0);
            }
        }

        if (GregtechItemList.Circuit_T3RecipeSelector.hasBeenSet()) {
            for (int i = 1; i <= 24; i++) {
                GregTech_API.registerConfigurationCircuit(CI.getNumberedAdvancedCircuit(i), 3);
            }
        }
    }

    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        CokeAndPyrolyseOven.onLoadComplete();
        MetaGTProxy.fixIC2FluidNames();
        RecipeLoaderAlgaeFarm.generateRecipes();
        RecipeLoaderTreeFarm.generateRecipes();
        if (AdvancedSolarPanel.isModLoaded()) {
            RecipeLoaderMolecularTransformer.run();
        }
    }
}
