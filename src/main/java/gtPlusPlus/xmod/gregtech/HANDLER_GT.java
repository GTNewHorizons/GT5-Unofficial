package gtPlusPlus.xmod.gregtech;

import static gtPlusPlus.core.recipe.common.CI.bits;
import static gtPlusPlus.core.util.minecraft.MaterialUtils.getMaterialName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Element;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_ModHandler.RecipeBits;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import gtPlusPlus.api.helpers.GregtechPlusPlus_API.Multiblock_API;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.api.objects.minecraft.multi.NoEUBonusMultiBehaviour;
import gtPlusPlus.api.objects.minecraft.multi.NoOutputBonusMultiBehaviour;
import gtPlusPlus.api.objects.minecraft.multi.NoSpeedBonusMultiBehaviour;
import gtPlusPlus.core.handler.COMPAT_HANDLER;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.CORE.ConfigSwitches;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.core.util.reflect.AddGregtechRecipe;
import gtPlusPlus.everglades.gen.gt.WorldGen_GT;
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
import gtPlusPlus.xmod.gregtech.recipes.RecipesToRemove;
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

        // Generates recipes for all gregtech smelting and alloy smelting combinations.
        // RecipeGen_BlastSmelterGT.generateRecipes();
        // new RecipeGen_BlastSmelterGT_Ex();

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
        removeCrudeTurbineRotors();
        if (ConfigSwitches.enableHarderRecipesForHighTierCasings) {
            removeOldHighTierCasingRecipes();
        }
        RecipesToRemove.go();
        convertPyroToCokeOven();
        generateElementalDuplicatorRecipes();
        Meta_GT_Proxy.fixIC2FluidNames();
        GT_Computercube_Description.addStandardDescriptions();
        GT_ComputerCube_Setup.init();
        RecipeLoader_AlgaeFarm.generateRecipes();
        if (LoadedMods.AdvancedSolarPanel) {
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

    private static void convertPyroToCokeOven() {
        int aCount = 0;
        for (GT_Recipe g : GT_Recipe_Map.sPyrolyseRecipes.mRecipeList) {
            if (AddGregtechRecipe.importPyroRecipe(g)) {
                aCount++;
            }
        }
        Logger.INFO("Converted " + aCount + " Pyrolyse recipes into Industrial Coke Oven recipes.");
    }

    private static GT_Recipe replaceItemInRecipeWithAnother(GT_Recipe aRecipe, ItemStack aExisting,
            ItemStack aNewItem) {
        ItemStack[] aInputItemsCopy = aRecipe.mInputs;
        String aOutputName = ItemUtils.getItemName(aRecipe.mOutputs[0]);
        boolean aDidChange = false;
        Logger.INFO("Attempting to Modify Recipe for " + aOutputName);
        for (int i = 0; i < aRecipe.mInputs.length; i++) {
            ItemStack aCurrentInputSlot = aRecipe.mInputs[i];
            if (aCurrentInputSlot != null) {
                if (GT_Utility.areStacksEqual(aCurrentInputSlot, aExisting, true)) {
                    aInputItemsCopy[i] = ItemUtils.getSimpleStack(aNewItem, aCurrentInputSlot.stackSize);
                    aDidChange = true;
                }
            }
        }
        if (aDidChange) {
            aRecipe.mInputs = aInputItemsCopy;
            Logger.INFO("Modifed Recipe for " + aOutputName);
            return aRecipe;
        } else {
            Logger.INFO("Failed to Modify Recipe for " + aOutputName);
            return aRecipe;
        }
    }

    private static void updateRecipeMap(GT_Recipe aOld, GT_Recipe aNew, GT_Recipe_Map aMap) {
        RecipeUtils.removeGtRecipe(aOld, aMap);
        RecipeUtils.addGtRecipe(aNew, aMap);
        Logger.INFO("Updating recipe map: " + aMap.mNEIName);
        Logger.INFO("Removed Recipe with hash: " + aOld.hashCode());
        Logger.INFO("Added Recipe with hash: " + aNew.hashCode());
    }

    private static void removeOldHighTierCasingRecipes() {
        // Static objects to save memory
        ItemStack aCasing_LUV = CI.machineCasing_LuV;
        ItemStack aCasing_ZPM = CI.machineCasing_ZPM;
        ItemStack aCasing_UV = CI.machineCasing_UV;
        ItemStack aCasing_MAX = CI.machineCasing_MAX;

        ItemStack aHull_LUV = CI.machineHull_LuV;
        ItemStack aHull_ZPM = CI.machineHull_ZPM;
        ItemStack aHull_UV = CI.machineHull_UV;
        ItemStack aHull_MAX = CI.machineHull_MAX;

        int aTier_LUV = 5;
        int aTier_ZPM = 6;
        int aTier_UV = 7;
        // int aTier_MAX = 8;

        ItemStack[] aCasings = new ItemStack[] { aCasing_LUV, aCasing_ZPM, aCasing_UV, aCasing_MAX };
        ItemStack[] aHulls = new ItemStack[] { aHull_LUV, aHull_ZPM, aHull_UV, aHull_MAX };

        // Remove Hand Crafting Recipes

        // Casings
        Logger.INFO("Removing shaped crafting for Casings.");
        RecipeUtils.removeRecipeByOutput(aCasing_LUV);
        RecipeUtils.removeRecipeByOutput(aCasing_ZPM);
        RecipeUtils.removeRecipeByOutput(aCasing_UV);
        // RecipeUtils.removeRecipeByOutput(aCasing_MAX);

        // Hulls
        Logger.INFO("Removing shaped crafting for Hulls.");
        RecipeUtils.removeRecipeByOutput(aHull_LUV);
        RecipeUtils.removeRecipeByOutput(aHull_ZPM);
        RecipeUtils.removeRecipeByOutput(aHull_UV);
        // RecipeUtils.removeRecipeByOutput(aHull_MAX);

        // Modify Assembler Recipes
        Logger.INFO(
                "Attempting to modify existing Assembly recipes for Casings & Hulls, this should provide best compatibility.");
        int aUpdateCount = 0;

        AutoMap<Pair<GT_Recipe, GT_Recipe>> aDataToModify = new AutoMap<Pair<GT_Recipe, GT_Recipe>>();

        Outer: for (final GT_Recipe r : GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.mRecipeList) {

            if (r != null && r.mOutputs != null && r.mOutputs.length > 0) {

                GT_Recipe aOldRecipeCopy = r;
                GT_Recipe aNewRecipe = r.copy();

                // Casings
                Inner: for (ItemStack aCasingObject : aCasings) {
                    if (GT_Utility.areStacksEqual(aOldRecipeCopy.mOutputs[0], aCasingObject)) {
                        String aOutputName = ItemUtils.getItemName(aOldRecipeCopy.mOutputs[0]);
                        Logger.INFO("Attempting to Modify Assembly Recipe for " + aOutputName);
                        // Replace Chrome
                        if (GT_Utility.areStacksEqual(aOldRecipeCopy.mOutputs[0], aCasing_LUV)) {
                            aNewRecipe = replaceItemInRecipeWithAnother(
                                    aOldRecipeCopy,
                                    ItemUtils.getItemStackOfAmountFromOreDict("plateChrome", 1),
                                    ELEMENT.getInstance().SELENIUM.getPlate(1));
                            aDataToModify.put(new Pair<>(r, aNewRecipe));
                            aUpdateCount++;
                            continue Outer;
                        }
                        // Replace Iridium
                        else if (GT_Utility.areStacksEqual(aOldRecipeCopy.mOutputs[0], aCasing_ZPM)) {
                            aNewRecipe = replaceItemInRecipeWithAnother(
                                    aOldRecipeCopy,
                                    ItemUtils.getItemStackOfAmountFromOreDict("plateIridium", 1),
                                    CI.getPlate(aTier_ZPM, 1));
                            aDataToModify.put(new Pair<>(r, aNewRecipe));
                            aUpdateCount++;
                            continue Outer;
                        }
                        // Replace Osmium
                        else if (GT_Utility.areStacksEqual(aOldRecipeCopy.mOutputs[0], aCasing_UV)) {
                            aNewRecipe = replaceItemInRecipeWithAnother(
                                    aOldRecipeCopy,
                                    ItemUtils.getItemStackOfAmountFromOreDict("plateOsmium", 1),
                                    CI.getPlate(aTier_UV, 1));
                            aDataToModify.put(new Pair<>(r, aNewRecipe));
                            aUpdateCount++;
                            continue Outer;
                        } else {
                            continue Inner;
                        }
                    }
                }

                // Hulls
                Inner: for (ItemStack aHullObject : aHulls) {
                    if (GT_Utility.areStacksEqual(aOldRecipeCopy.mOutputs[0], aHullObject)) {
                        String aOutputName = ItemUtils.getItemName(aOldRecipeCopy.mOutputs[0]);
                        Logger.INFO("Attempting to Modify Assembly Recipe for " + aOutputName);
                        // Replace Chrome
                        if (GT_Utility.areStacksEqual(aOldRecipeCopy.mOutputs[0], aHull_LUV)) {
                            aNewRecipe = replaceItemInRecipeWithAnother(
                                    aOldRecipeCopy,
                                    ItemUtils.getItemStackOfAmountFromOreDict("plateChrome", 1),
                                    ELEMENT.getInstance().SELENIUM.getPlate(1));
                            aDataToModify.put(new Pair<>(r, aNewRecipe));
                            aUpdateCount++;
                            continue Outer;
                        }
                        // Replace Iridium
                        else if (GT_Utility.areStacksEqual(aOldRecipeCopy.mOutputs[0], aHull_ZPM)) {
                            aNewRecipe = replaceItemInRecipeWithAnother(
                                    aOldRecipeCopy,
                                    ItemUtils.getItemStackOfAmountFromOreDict("plateIridium", 1),
                                    CI.getPlate(aTier_ZPM, 1));
                            aDataToModify.put(new Pair<>(r, aNewRecipe));
                            aUpdateCount++;
                            continue Outer;
                        }
                        // Replace Osmium
                        else if (GT_Utility.areStacksEqual(aOldRecipeCopy.mOutputs[0], aHull_UV)) {
                            aNewRecipe = replaceItemInRecipeWithAnother(
                                    aOldRecipeCopy,
                                    ItemUtils.getItemStackOfAmountFromOreDict("plateOsmium", 1),
                                    CI.getPlate(aTier_UV, 1));
                            aDataToModify.put(new Pair<>(r, aNewRecipe));
                            aUpdateCount++;
                            continue Outer;
                        } else {
                            continue Inner;
                        }
                    }
                }
            }
        }

        Logger.INFO("There is " + aUpdateCount + " recipes flagged for update.");

        if (aUpdateCount > 0) {
            for (Pair<GT_Recipe, GT_Recipe> g : aDataToModify) {
                updateRecipeMap(g.getKey(), g.getValue(), GT_Recipe.GT_Recipe_Map.sAssemblerRecipes);
            }
            Logger.INFO("Modified " + aUpdateCount + " recipes.");
        }

        Logger.INFO("Adding new Shaped recipes for Casings.");
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_LuV.get(1),
                bits,
                new Object[] { "PPP", "PwP", "PPP", 'P', ELEMENT.getInstance().SELENIUM.getPlate(1) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_ZPM.get(1),
                bits,
                new Object[] { "PPP", "PwP", "PPP", 'P', CI.getPlate(aTier_ZPM, 1) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_UV.get(1),
                bits,
                new Object[] { "PPP", "PwP", "PPP", 'P', CI.getPlate(aTier_UV, 1) });

        if (!GT_Mod.gregtechproxy.mHardMachineCasings) {
            Logger.INFO("Adding new easy Shaped recipes for Hulls.");
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_LuV.get(1),
                    RecipeBits.NOT_REMOVABLE | RecipeBits.BUFFERED,
                    new Object[] { "CMC", 'M', ItemList.Casing_LuV, 'C',
                            OrePrefixes.cableGt01.get(Materials.VanadiumGallium) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_ZPM.get(1),
                    RecipeBits.NOT_REMOVABLE | RecipeBits.BUFFERED,
                    new Object[] { "CMC", 'M', ItemList.Casing_ZPM, 'C',
                            OrePrefixes.cableGt01.get(Materials.Naquadah) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_UV.get(1),
                    RecipeBits.NOT_REMOVABLE | RecipeBits.BUFFERED,
                    new Object[] { "CMC", 'M', ItemList.Casing_UV, 'C',
                            OrePrefixes.wireGt04.get(Materials.NaquadahAlloy) });
        } else {

            Materials aPolytetrafluoroethylene = MaterialUtils.getMaterial("Polytetrafluoroethylene", "Plastic");

            Logger.INFO("Adding new hard Shaped recipes for Hulls.");
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_LuV.get(1),
                    RecipeBits.NOT_REMOVABLE | RecipeBits.BUFFERED,
                    new Object[] { "PHP", "CMC", 'M', ItemList.Casing_LuV, 'C',
                            OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'H',
                            ELEMENT.getInstance().SELENIUM.getPlate(1), 'P',
                            OrePrefixes.plate.get(Materials.Plastic) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_ZPM.get(1),
                    RecipeBits.NOT_REMOVABLE | RecipeBits.BUFFERED,
                    new Object[] { "PHP", "CMC", 'M', ItemList.Casing_ZPM, 'C',
                            OrePrefixes.cableGt01.get(Materials.Naquadah), 'H', CI.getPlate(aTier_ZPM, 1), 'P',
                            OrePrefixes.plate.get(aPolytetrafluoroethylene) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_UV.get(1),
                    RecipeBits.NOT_REMOVABLE | RecipeBits.BUFFERED,
                    new Object[] { "PHP", "CMC", 'M', ItemList.Casing_UV, 'C',
                            OrePrefixes.wireGt04.get(Materials.NaquadahAlloy), 'H', CI.getPlate(aTier_UV, 1), 'P',
                            OrePrefixes.plate.get(aPolytetrafluoroethylene) });
        }
    }

    private static int removeCrudeTurbineRotors() {
        int aRemoved = 0;
        int CUT = CORE.turbineCutoffBase;
        Item aU;
        Collection<GT_Recipe> aAssRecipes = GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.mRecipeList;
        // 170, 172, 174, 176
        if (aAssRecipes.size() > 0) {
            recipe: for (GT_Recipe aG : aAssRecipes) {
                if (aG.mOutputs != null && aG.mOutputs.length > 0) {
                    outputs: for (ItemStack aI : aG.mOutputs) {
                        if (aI == null) {
                            continue;
                        }
                        aU = aI.getItem();
                        if (aU == null) {
                            continue;
                        }
                        if (aU instanceof GT_MetaGenerated_Tool_01) {
                            int aMeta = aI.getItemDamage();
                            if (aMeta >= 170 && aMeta <= 176) {
                                // Found a Turbine
                                int aCutoff = aMeta == 170 ? CUT
                                        : (aMeta == 172 ? CUT * 2 : (aMeta == 174 ? CUT * 3 : CUT * 4));
                                String aType = aMeta == 170 ? "Small "
                                        : (aMeta == 172 ? "" : (aMeta == 174 ? "Large " : "Huge "));
                                Materials aMainMaterial = GT_MetaGenerated_Tool.getPrimaryMaterial(aI);
                                Materials aSecondaryMaterial = GT_MetaGenerated_Tool.getSecondaryMaterial(aI);
                                long rotorDurabilityMax = GT_MetaGenerated_Tool.getToolMaxDamage(aI);
                                if (rotorDurabilityMax < aCutoff) {
                                    Logger.WARNING(
                                            "[Turbine Cleanup] " + getMaterialName(aMainMaterial)
                                                    + " "
                                                    + aType
                                                    + "Turbines have "
                                                    + rotorDurabilityMax
                                                    + ", which is below the cutoff durability of "
                                                    + aCutoff
                                                    + ", disabling.");
                                    aG.mEnabled = false;
                                    aG.mHidden = true;
                                    aG.mCanBeBuffered = false;
                                    aRemoved++;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        Logger.INFO("Removed " + aRemoved + " useless Turbines.");

        return aRemoved;
    }
}
