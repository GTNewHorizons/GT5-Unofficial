package gtPlusPlus.xmod.gregtech;

import static gregtech.api.enums.Mods.AdvancedSolarPanel;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Element;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import gtPlusPlus.api.helpers.GregtechPlusPlus_API.Multiblock_API;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.multi.NoEUBonusMultiBehaviour;
import gtPlusPlus.api.objects.minecraft.multi.NoOutputBonusMultiBehaviour;
import gtPlusPlus.api.objects.minecraft.multi.NoSpeedBonusMultiBehaviour;
import gtPlusPlus.core.handler.COMPAT_HANDLER;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.everglades.gen.gt.WorldGen_GT;
import gtPlusPlus.recipes.CokeAndPyrolyseOven;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.api.util.GTPP_Config;
import gtPlusPlus.xmod.gregtech.api.world.GTPP_Worldgen;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import gtPlusPlus.xmod.gregtech.common.blocks.fluid.GregtechFluidHandler;
import gtPlusPlus.xmod.gregtech.common.computer.GT_ComputerCube_Setup;
import gtPlusPlus.xmod.gregtech.common.computer.GT_Computercube_Description;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechTools;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMTE_ElementalDuplicator;
import gtPlusPlus.xmod.gregtech.loaders.Gregtech_Blocks;
import gtPlusPlus.xmod.gregtech.loaders.ProcessingAngleGrinder;
import gtPlusPlus.xmod.gregtech.loaders.ProcessingElectricButcherKnife;
import gtPlusPlus.xmod.gregtech.loaders.ProcessingElectricLighter;
import gtPlusPlus.xmod.gregtech.loaders.ProcessingElectricSnips;
import gtPlusPlus.xmod.gregtech.loaders.ProcessingToolHeadChoocher;
import gtPlusPlus.xmod.gregtech.loaders.misc.AddCustomMachineToPA;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoader_AlgaeFarm;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoader_MolecularTransformer;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechConduits;

public class HANDLER_GT {

    public static GT_Config mMaterialProperties = null;
    public static GTPP_Config sCustomWorldgenFile = null;
    public static final List<WorldGen_GT> sWorldgenListEverglades = new ArrayList<>();
    public static final List<GTPP_Worldgen> sCustomWorldgenList = new ArrayList<>();
    public static GT_MetaGenerated_Tool sMetaGeneratedToolInstance;

    public static void preInit() {

        if (mMaterialProperties != null) {
            GT_Materials.init(mMaterialProperties);
        }

        GregtechFluidHandler.run();
    }

    public static void init() {

        // Load General Blocks and set up some Basic Meta Tile Entity states
        Gregtech_Blocks.run();

        // Add Custom Pipes, Wires and Cables.
        GregtechConduits.run();

        // Register Tile Entities
        COMPAT_HANDLER.registerGregtechMachines();

        // Only loads if the config option is true (default: true)
        if (CORE.ConfigSwitches.enableSkookumChoochers) {
            sMetaGeneratedToolInstance = MetaGeneratedGregtechTools.getInstance();
        }
    }

    public static void postInit() {

        // Only loads if the config option is true (default: true)
        if (CORE.ConfigSwitches.enableSkookumChoochers) {
            new ProcessingToolHeadChoocher().run();
        }
        new ProcessingAngleGrinder().run();
        new ProcessingElectricSnips().run();
        new ProcessingElectricButcherKnife().run();
        new ProcessingElectricLighter().run();

        // Register custom singles to the PA
        AddCustomMachineToPA.register();

        // Register the No-Bonus Special Behaviour.
        Multiblock_API.registerSpecialMultiBehaviour(new NoOutputBonusMultiBehaviour());
        Multiblock_API.registerSpecialMultiBehaviour(new NoSpeedBonusMultiBehaviour());
        Multiblock_API.registerSpecialMultiBehaviour(new NoEUBonusMultiBehaviour());

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
        generateElementalDuplicatorRecipes();
        Meta_GT_Proxy.fixIC2FluidNames();
        GT_Computercube_Description.addStandardDescriptions();
        GT_ComputerCube_Setup.init();
        RecipeLoader_AlgaeFarm.generateRecipes();
        if (AdvancedSolarPanel.isModLoaded()) {
            RecipeLoader_MolecularTransformer.run();
        }
    }

    private static void generateElementalDuplicatorRecipes() {
        for (GT_Recipe aRecipe : GT_Recipe_Map.sReplicatorFakeRecipes.mRecipeList) {
            Object aDataOrb = aRecipe.mSpecialItems;
            if (aDataOrb != null) {
                ItemStack aOutput = aRecipe.mOutputs[0];
                if (aOutput != null) {
                    FluidStack aFluid = aRecipe.mFluidInputs[0];
                    if (aFluid != null && aFluid.amount > 0) {
                        ItemStack tDataOrb = GregtechMTE_ElementalDuplicator.getSpecialSlotStack(aRecipe);
                        Materials tMaterial = Element.get(Behaviour_DataOrb.getDataName(tDataOrb)).mLinkedMaterials
                                .get(0);
                        FluidStack aOutputFluid = null;
                        ItemStack aOutputItem = null;
                        if (tMaterial != null) {
                            boolean aUsingFluid = false;
                            if ((aOutputItem = GT_OreDictUnificator.get(OrePrefixes.dust, tMaterial, 1L)) == null) {
                                if ((aOutputItem = GT_OreDictUnificator.get(OrePrefixes.cell, tMaterial, 1L)) != null) {
                                    aOutputFluid = GT_Utility.getFluidForFilledItem(aOutputItem, true);
                                    aUsingFluid = true;
                                }
                            }
                            GTPP_Recipe aNewRecipe = new GTPP_Recipe(
                                    false,
                                    new ItemStack[] {},
                                    new ItemStack[] { !aUsingFluid ? aOutputItem : null },
                                    aRecipe.mSpecialItems,
                                    null,
                                    aRecipe.mFluidInputs,
                                    new FluidStack[] { aUsingFluid ? aOutputFluid : null },
                                    aRecipe.mDuration,
                                    aRecipe.mEUt,
                                    aRecipe.mFluidInputs[0].amount);
                            GTPP_Recipe_Map.sElementalDuplicatorRecipes.add(aNewRecipe);

                            Logger.INFO(
                                    "[EM] Generated recipe for " + tMaterial.mLocalizedName
                                            + ", Outputs "
                                            + (aUsingFluid ? "Fluid" : "Dust"));
                        }
                    } else {
                        Logger.INFO("[EM] Bad UU Requirement. " + RecipeUtils.getRecipeInfo(aRecipe));
                    }
                } else {
                    Logger.INFO("[EM] Bad Output. " + RecipeUtils.getRecipeInfo(aRecipe));
                }
            } else {
                Logger.INFO("[EM] Bad Data Orb. " + RecipeUtils.getRecipeInfo(aRecipe));
            }
        }
        int aSize = GTPP_Recipe_Map.sElementalDuplicatorRecipes.mRecipeList.size();
        Logger.INFO(
                "[EM] Generated " + aSize
                        + "/"
                        + GT_Recipe_Map.sReplicatorFakeRecipes.mRecipeList.size()
                        + " Replicator recipes.");
    }
}
