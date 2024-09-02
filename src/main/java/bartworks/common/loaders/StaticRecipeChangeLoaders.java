/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.common.loaders;

import static bartworks.API.recipe.BartWorksRecipeMaps.electricImplosionCompressorRecipes;
import static gregtech.api.enums.Mods.TinkerConstruct;
import static gregtech.api.enums.TickTime.TICK;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ArrayListMultimap;

import bartworks.API.recipe.DynamicGTRecipe;
import bartworks.MainMod;
import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import bartworks.util.BWUtil;
import bartworks.util.log.DebugLog;
import bwcrossmod.BartWorksCrossmod;
import cpw.mods.fml.common.registry.GameRegistry;
import gnu.trove.map.hash.TObjectDoubleHashMap;
import gregtech.api.enums.Element;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.objects.GTItemStack;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;

public class StaticRecipeChangeLoaders {

    private static TObjectDoubleHashMap<Materials> gtEbfGasRecipeTimeMultipliers = null;
    private static TObjectDoubleHashMap<Materials> gtEbfGasRecipeConsumptionMultipliers = null;

    public static final List<ItemStack> whitelistForEBFNoGasRecipeDontCheckItemData = Arrays
        .asList(GTModHandler.getModItem(TinkerConstruct.ID, "materials", 1L, 12) // Raw Aluminum -> Aluminium Ingot
        // (coremod)
        );

    private StaticRecipeChangeLoaders() {}

    public static void addEBFGasRecipes() {
        if (gtEbfGasRecipeTimeMultipliers == null) {
            // For Werkstoff gases, use Werkstoff.Stats.setEbfGasRecipeTimeMultiplier
            gtEbfGasRecipeTimeMultipliers = new TObjectDoubleHashMap<>(10, 0.5F, -1.0D); // keep default value as -1
            // Example to make Argon cut recipe times into a third of the original:
            // gtEbfGasRecipeTimeMultipliers.put(Materials.Argon, 1.0D / 3.0D);

            gtEbfGasRecipeTimeMultipliers.put(Materials.Nitrogen, 1.0D);
            gtEbfGasRecipeTimeMultipliers.put(Materials.Helium, 0.9D);
            gtEbfGasRecipeTimeMultipliers.put(Materials.Argon, 0.8D);
            gtEbfGasRecipeTimeMultipliers.put(Materials.Radon, 0.7D);
        }
        if (gtEbfGasRecipeConsumptionMultipliers == null) {
            // For Werkstoff gases, use Werkstoff.Stats.setEbfGasRecipeConsumedAmountMultiplier
            gtEbfGasRecipeConsumptionMultipliers = new TObjectDoubleHashMap<>(10, 0.5F, 1.0D); // keep default value as
                                                                                               // 1
            // Example to make Argon recipes use half the gas amount of the primary recipe (1000L->500L, 2000L->1000L
            // etc.):
            // gtEbfGasRecipeConsumptionMultipliers.put(Materials.Argon, 1.0D / 2.0D);
            gtEbfGasRecipeConsumptionMultipliers.put(Materials.Nitrogen, 1.0D);
            gtEbfGasRecipeConsumptionMultipliers.put(Materials.Helium, 1.0D);
            gtEbfGasRecipeConsumptionMultipliers.put(Materials.Argon, 0.85D);
            gtEbfGasRecipeConsumptionMultipliers.put(Materials.Radon, 0.7D);
        }
        ArrayListMultimap<SubTag, GTRecipe> toChange = getRecipesToChange(
            WerkstoffLoader.NOBLE_GAS,
            WerkstoffLoader.ANAEROBE_GAS);
        editRecipes(toChange, getNoGasItems(toChange));
    }

    public static void unificationRecipeEnforcer() {
        List<GTRecipe> toRemove = new ArrayList<>();
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
            StaticRecipeChangeLoaders.runMaterialLinker(werkstoff);
            if (werkstoff.getGenerationFeatures().enforceUnification) {
                HashSet<String> oreDictNames = new HashSet<>(werkstoff.getADDITIONAL_OREDICT());
                oreDictNames.add(werkstoff.getVarName());
                StaticRecipeChangeLoaders.runMoltenUnificationEnfocement(werkstoff);
                StaticRecipeChangeLoaders.runUnficationDeleter(werkstoff);
                for (String s : oreDictNames) for (OrePrefixes prefixes : OrePrefixes.values()) {
                    if (!werkstoff.hasItemType(prefixes)) continue;
                    String fullOreName = prefixes + s;
                    List<ItemStack> ores = OreDictionary.getOres(fullOreName, false);
                    if (ores.size() <= 1) // empty or one entry, i.e. no unification needed
                        continue;
                    for (ItemStack toReplace : ores) {
                        ItemStack replacement = werkstoff.get(prefixes);
                        if (toReplace == null || GTUtility.areStacksEqual(toReplace, replacement)
                            || replacement == null
                            || replacement.getItem() == null) continue;
                        for (RecipeMap<?> map : RecipeMap.ALL_RECIPE_MAPS.values()) {
                            toRemove.clear();
                            nextRecipe: for (GTRecipe recipe : map.getAllRecipes()) {
                                boolean removal = map.equals(RecipeMaps.fluidExtractionRecipes)
                                    || map.equals(RecipeMaps.fluidSolidifierRecipes);
                                for (int i = 0; i < recipe.mInputs.length; i++) {
                                    if (!GTUtility.areStacksEqual(recipe.mInputs[i], toReplace)) continue;
                                    if (removal) {
                                        toRemove.add(recipe);
                                        continue nextRecipe;
                                    }
                                    recipe.mInputs[i] = GTUtility.copyAmount(recipe.mInputs[i].stackSize, replacement);
                                }
                                for (int i = 0; i < recipe.mOutputs.length; i++) {
                                    if (!GTUtility.areStacksEqual(recipe.mOutputs[i], toReplace)) continue;
                                    if (removal) {
                                        toRemove.add(recipe);
                                        continue nextRecipe;
                                    }
                                    recipe.mOutputs[i] = GTUtility
                                        .copyAmount(recipe.mOutputs[i].stackSize, replacement);
                                }
                                if (recipe.mSpecialItems instanceof ItemStack specialItemStack) {
                                    if (!GTUtility.areStacksEqual(specialItemStack, toReplace)) continue;
                                    if (removal) {
                                        toRemove.add(recipe);
                                        continue nextRecipe;
                                    }
                                    recipe.mSpecialItems = GTUtility
                                        .copyAmount(specialItemStack.stackSize, replacement);
                                }
                            }
                            map.getBackend()
                                .removeRecipes(toRemove);
                        }
                    }
                }
            }
        }
    }

    private static void runMoltenUnificationEnfocement(Werkstoff werkstoff) {
        if (werkstoff.getGenerationFeatures().enforceUnification && werkstoff.hasItemType(OrePrefixes.cellMolten)) {
            try {
                FluidContainerRegistry.FluidContainerData data = new FluidContainerRegistry.FluidContainerData(
                    new FluidStack(Objects.requireNonNull(WerkstoffLoader.molten.get(werkstoff)), 144),
                    werkstoff.get(OrePrefixes.cellMolten),
                    Materials.Empty.getCells(1));
                Field f = GTUtility.class.getDeclaredField("sFilledContainerToData");
                f.setAccessible(true);
                @SuppressWarnings("unchecked")
                Map<GTItemStack, FluidContainerRegistry.FluidContainerData> sFilledContainerToData = (Map<GTItemStack, FluidContainerRegistry.FluidContainerData>) f
                    .get(null);
                Set<Map.Entry<GTItemStack, FluidContainerRegistry.FluidContainerData>> toremFilledContainerToData = new HashSet<>();
                ItemStack toReplace = null;
                for (Map.Entry<GTItemStack, FluidContainerRegistry.FluidContainerData> entry : sFilledContainerToData
                    .entrySet()) {
                    final String MODID = GameRegistry.findUniqueIdentifierFor(data.filledContainer.getItem()).modId;
                    if (MainMod.MOD_ID.equals(MODID) || BartWorksCrossmod.MOD_ID.equals(MODID)) continue;
                    if (entry.getValue().fluid.equals(data.fluid)
                        && !entry.getValue().filledContainer.equals(data.filledContainer)) {
                        toReplace = entry.getValue().filledContainer;
                        toremFilledContainerToData.add(entry);
                    }
                }
                sFilledContainerToData.entrySet()
                    .removeAll(toremFilledContainerToData);
                Set<GTRecipe> toremRecipeList = new HashSet<>();
                if (toReplace != null) {
                    for (RecipeMap<?> map : RecipeMap.ALL_RECIPE_MAPS.values()) {
                        toremRecipeList.clear();
                        for (GTRecipe recipe : map.getAllRecipes()) {
                            for (ItemStack mInput : recipe.mInputs) {
                                if (GTUtility.areStacksEqual(mInput, toReplace)) {
                                    toremRecipeList.add(recipe);
                                    // recipe.mInputs[i] = data.filledContainer;
                                }
                            }
                            for (ItemStack mOutput : recipe.mOutputs) {
                                if (GTUtility.areStacksEqual(mOutput, toReplace)) {
                                    toremRecipeList.add(recipe);
                                    // recipe.mOutputs[i] = data.filledContainer;
                                    if (map == RecipeMaps.fluidCannerRecipes
                                        && GTUtility.areStacksEqual(mOutput, data.filledContainer)
                                        && !recipe.mFluidInputs[0].equals(data.fluid)) {
                                        toremRecipeList.add(recipe);
                                        // recipe.mOutputs[i] = data.filledContainer;
                                    }
                                }
                            }
                            if (recipe.mSpecialItems instanceof ItemStack
                                && GTUtility.areStacksEqual((ItemStack) recipe.mSpecialItems, toReplace)) {
                                toremRecipeList.add(recipe);
                                // recipe.mSpecialItems = data.filledContainer;
                            }
                        }
                        map.getBackend()
                            .removeRecipes(toremRecipeList);
                    }
                }
                GTUtility.addFluidContainerData(data);
            } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    private static void runUnficationDeleter(Werkstoff werkstoff) {
        if (werkstoff.getType() == Werkstoff.Types.ELEMENT && werkstoff.getBridgeMaterial() != null
            && Element.get(werkstoff.getToolTip()) != Element._NULL) {
            werkstoff.getBridgeMaterial().mElement = Element.get(werkstoff.getToolTip());
            Element.get(werkstoff.getToolTip()).mLinkedMaterials = new ArrayList<>();
            Element.get(werkstoff.getToolTip()).mLinkedMaterials.add(werkstoff.getBridgeMaterial());
        }

        for (OrePrefixes prefixes : OrePrefixes.values()) if (werkstoff.hasItemType(prefixes)) {
            GTOreDictUnificator.set(prefixes, werkstoff.getBridgeMaterial(), werkstoff.get(prefixes), true, true);
            for (ItemStack stack : OreDictionary.getOres(prefixes + werkstoff.getVarName())) {
                GTOreDictUnificator.addAssociation(prefixes, werkstoff.getBridgeMaterial(), stack, false);
                GTOreDictUnificator.getAssociation(stack).mUnificationTarget = werkstoff.get(prefixes);
            }
        }
    }

    private static void runMaterialLinker(Werkstoff werkstoff) {
        if (werkstoff.getType() == Werkstoff.Types.ELEMENT && werkstoff.getBridgeMaterial() != null
            && Element.get(werkstoff.getToolTip()) != Element._NULL) {
            werkstoff.getBridgeMaterial().mElement = Element.get(werkstoff.getToolTip());
            Element.get(werkstoff.getToolTip()).mLinkedMaterials = new ArrayList<>();
            Element.get(werkstoff.getToolTip()).mLinkedMaterials.add(werkstoff.getBridgeMaterial());
        }

        for (OrePrefixes prefixes : OrePrefixes.values())
            if (werkstoff.hasItemType(prefixes) && werkstoff.getBridgeMaterial() != null) {
                GTOreDictUnificator.set(prefixes, werkstoff.getBridgeMaterial(), werkstoff.get(prefixes), true, true);
                for (ItemStack stack : OreDictionary.getOres(prefixes + werkstoff.getVarName())) {
                    GTOreDictUnificator.addAssociation(prefixes, werkstoff.getBridgeMaterial(), stack, false);
                }
            }
    }

    /**
     * Constructs a list of recipes to change by scanning all EBF recipes for uses of noble gases.
     *
     * @param GasTags list of gas tags to look out for in EBF recipes
     * @return A multimap from the gas tag (noble and/or anaerobic) to all the recipes containing a gas with that tag
     */
    private static ArrayListMultimap<SubTag, GTRecipe> getRecipesToChange(SubTag... GasTags) {
        ArrayListMultimap<SubTag, GTRecipe> toAdd = ArrayListMultimap.create();
        for (GTRecipe recipe : RecipeMaps.blastFurnaceRecipes.getAllRecipes()) {
            if (recipe.mFluidInputs != null && recipe.mFluidInputs.length > 0) {
                Materials mat = getMaterialFromInputFluid(recipe);
                if (mat != Materials._NULL) {
                    for (SubTag tag : GasTags) {
                        if (mat.contains(tag)) {
                            DebugLog.log(
                                "Found EBF Recipe to change, Output:"
                                    + BWUtil.translateGTItemStack(recipe.mOutputs[0]));
                            toAdd.put(tag, recipe);
                        }
                    }
                }
            }
        }
        return toAdd;
    }

    /**
     * Scans EBF recipes for no-gas variants of the recipes present in base. Adds these recipes to the base multimap.
     *
     * @param base The recipe multimap to scan and modify
     * @return Set of item outputs (recipe.mOutputs[0]) of the no-gas recipes
     */
    private static HashSet<ItemStack> getNoGasItems(ArrayListMultimap<SubTag, GTRecipe> base) {
        HashSet<ItemStack> toAdd = new HashSet<>();
        ArrayListMultimap<SubTag, GTRecipe> repToAdd = ArrayListMultimap.create();
        for (GTRecipe recipe : RecipeMaps.blastFurnaceRecipes.getAllRecipes()) {
            for (SubTag tag : base.keySet()) recipeLoop: for (GTRecipe baseRe : base.get(tag)) {
                if (recipe.mInputs.length == baseRe.mInputs.length && recipe.mOutputs.length == baseRe.mOutputs.length)
                    for (int i = 0; i < recipe.mInputs.length; i++) {
                        ItemStack tmpInput = recipe.mInputs[i];
                        if ((recipe.mFluidInputs == null || recipe.mFluidInputs.length == 0)
                            && (whitelistForEBFNoGasRecipeDontCheckItemData.stream()
                                .anyMatch(s -> GTUtility.areStacksEqual(s, tmpInput))
                                || BWUtil.checkStackAndPrefix(recipe.mInputs[i])
                                    && BWUtil.checkStackAndPrefix(baseRe.mInputs[i])
                                    && GTOreDictUnificator.getAssociation(recipe.mInputs[i]).mMaterial.mMaterial.equals(
                                        GTOreDictUnificator.getAssociation(baseRe.mInputs[i]).mMaterial.mMaterial)
                                    && GTUtility.areStacksEqual(recipe.mOutputs[0], baseRe.mOutputs[0]))) {
                            toAdd.add(recipe.mOutputs[0]);
                            repToAdd.put(tag, recipe);
                            continue recipeLoop;
                        }
                    }
            }
        }
        base.putAll(repToAdd);
        return toAdd;
    }

    private static int transformEBFGasRecipeTime(int originalDuration, long originalGasProtons, long newGasProtons) {
        double protonTerm = originalGasProtons * (newGasProtons >= originalGasProtons ? 1.0D : 2.75D) - newGasProtons;
        return Math.max(1, (int) (originalDuration / 200D * Math.max(200D + protonTerm, 1D)));
    }

    private static int transformEBFGasRecipeTime(GTRecipe recipe, Materials originalGas, Materials newGas) {
        double newEbfMul = gtEbfGasRecipeTimeMultipliers.get(newGas);
        double originalEbfMul = gtEbfGasRecipeTimeMultipliers.get(originalGas);
        if (newEbfMul < 0.0D || originalEbfMul < 0.0D) {
            return transformEBFGasRecipeTime(recipe.mDuration, originalGas.getProtons(), newGas.getProtons());
        }
        return Math.max(1, (int) (recipe.mDuration * newEbfMul / originalEbfMul));
    }

    private static int transformEBFGasRecipeTime(GTRecipe recipe, Materials originalGas, Werkstoff newGas) {
        double newEbfMul = newGas.getStats()
            .getEbfGasRecipeTimeMultiplier();
        double originalEbfMul = gtEbfGasRecipeTimeMultipliers.get(originalGas);
        if (newEbfMul < 0.0D || originalEbfMul < 0.0D) {
            return transformEBFGasRecipeTime(
                recipe.mDuration,
                originalGas.getProtons(),
                newGas.getStats()
                    .getProtons());
        }
        return Math.max(1, (int) (recipe.mDuration * newEbfMul / originalEbfMul));
    }

    private static int transformEBFNoGasRecipeTime(GTRecipe recipe, Materials originalGas) {
        return transformEBFGasRecipeTime(recipe.mDuration, originalGas.getProtons(), 0);
    }

    private static void editEBFMaterialRecipes(SubTag GasTag, GTRecipe recipe, Materials originalGas,
        HashSet<GTRecipe> toAdd) {
        for (Materials newGas : Materials.values()) {
            if (newGas.contains(GasTag)) {
                int time = transformEBFGasRecipeTime(recipe, originalGas, newGas);
                int gasAmount = Math.max(
                    1,
                    (int) Math.round(recipe.mFluidInputs[0].amount * gtEbfGasRecipeConsumptionMultipliers.get(newGas)));
                if (recipe.mFluidInputs != null && recipe.mFluidInputs.length == 1
                    && recipe.mFluidInputs[0].isFluidEqual(newGas.getGas(0))) {
                    // preserve original recipe owner
                    toAdd.add(
                        new DynamicGTRecipe(
                            false,
                            recipe.mInputs,
                            recipe.mOutputs,
                            recipe.mSpecialItems,
                            recipe.mChances,
                            new FluidStack[] { newGas.getGas(gasAmount) },
                            recipe.mFluidOutputs,
                            time,
                            recipe.mEUt,
                            recipe.mSpecialValue,
                            recipe));
                } else {
                    // new recipe
                    toAdd.add(
                        new GTRecipe(
                            false,
                            recipe.mInputs,
                            recipe.mOutputs,
                            recipe.mSpecialItems,
                            recipe.mChances,
                            new FluidStack[] { newGas.getGas(gasAmount) },
                            recipe.mFluidOutputs,
                            time,
                            recipe.mEUt,
                            recipe.mSpecialValue));
                }
            }
        }
    }

    private static void editEBFWerkstoffRecipes(SubTag GasTag, GTRecipe recipe, Materials originalGas,
        HashSet<GTRecipe> toAdd) {
        for (Werkstoff newGas : Werkstoff.werkstoffHashMap.values()) {
            if (newGas.contains(GasTag)) {
                int time = transformEBFGasRecipeTime(recipe, originalGas, newGas);
                int gasAmount = Math.max(
                    1,
                    (int) Math.round(
                        recipe.mFluidInputs[0].amount * newGas.getStats()
                            .getEbfGasRecipeConsumedAmountMultiplier()));
                if (recipe.mFluidInputs != null && recipe.mFluidInputs.length == 1
                    && recipe.mFluidInputs[0]
                        .isFluidEqual(new FluidStack(Objects.requireNonNull(WerkstoffLoader.fluids.get(newGas)), 0))) {
                    // preserve original recipe owner
                    toAdd.add(
                        new DynamicGTRecipe(
                            false,
                            recipe.mInputs,
                            recipe.mOutputs,
                            recipe.mSpecialItems,
                            recipe.mChances,
                            new FluidStack[] {
                                new FluidStack(Objects.requireNonNull(WerkstoffLoader.fluids.get(newGas)), gasAmount) },
                            recipe.mFluidOutputs,
                            time,
                            recipe.mEUt,
                            recipe.mSpecialValue,
                            recipe));
                } else {
                    // new recipe
                    toAdd.add(
                        new GTRecipe(
                            false,
                            recipe.mInputs,
                            recipe.mOutputs,
                            recipe.mSpecialItems,
                            recipe.mChances,
                            new FluidStack[] {
                                new FluidStack(Objects.requireNonNull(WerkstoffLoader.fluids.get(newGas)), gasAmount) },
                            recipe.mFluidOutputs,
                            time,
                            recipe.mEUt,
                            recipe.mSpecialValue));
                }
            }
        }
    }

    private static void editEBFNoGasRecipes(GTRecipe recipe, Materials originalGas, HashSet<GTRecipe> toAdd,
        HashSet<ItemStack> noGas) {
        for (ItemStack is : noGas) {
            byte circuitConfiguration = 1;
            if (GTUtility.areStacksEqual(is, recipe.mOutputs[0])) {
                ArrayList<ItemStack> inputs = new ArrayList<>(recipe.mInputs.length);
                for (ItemStack stack : recipe.mInputs)
                    if (!GTUtility.areStacksEqual(GTUtility.getIntegratedCircuit(11), stack)
                        && !GTUtility.areStacksEqual(GTUtility.getIntegratedCircuit(14), stack)
                        && !GTUtility.areStacksEqual(GTUtility.getIntegratedCircuit(19), stack)) {
                            if (BWUtil.checkStackAndPrefix(stack)) circuitConfiguration = (byte) (OrePrefixes.dustSmall
                                .equals(GTOreDictUnificator.getAssociation(stack).mPrefix) ? 4
                                    : OrePrefixes.dustTiny.equals(GTOreDictUnificator.getAssociation(stack).mPrefix) ? 9
                                        : 1);
                            inputs.add(stack);
                        }
                inputs.add(GTUtility.getIntegratedCircuit(circuitConfiguration));
                toAdd.add(
                    new DynamicGTRecipe(
                        false,
                        inputs.toArray(new ItemStack[0]),
                        recipe.mOutputs,
                        recipe.mSpecialItems,
                        recipe.mChances,
                        null,
                        recipe.mFluidOutputs,
                        transformEBFNoGasRecipeTime(recipe, originalGas),
                        recipe.mEUt,
                        recipe.mSpecialValue,
                        recipe));
                break;
            }
        }
    }

    private static void removeDuplicateGasRecipes(HashSet<GTRecipe> toAdd) {
        HashSet<GTRecipe> duplicates = new HashSet<>();
        for (GTRecipe recipe : toAdd) {
            for (GTRecipe recipe2 : toAdd) {
                if (recipe.mEUt != recipe2.mEUt || recipe.mDuration != recipe2.mDuration
                    || recipe.mSpecialValue != recipe2.mSpecialValue
                    || recipe == recipe2
                    || recipe.mInputs.length != recipe2.mInputs.length
                    || recipe.mFluidInputs.length != recipe2.mFluidInputs.length) continue;
                boolean isSame = true;
                for (int i = 0; i < recipe.mInputs.length; i++) {
                    if (!GTUtility.areStacksEqual(recipe.mInputs[i], recipe2.mInputs[i])) isSame = false;
                }
                for (int i = 0; i < recipe.mFluidInputs.length; i++) {
                    if (!GTUtility.areFluidsEqual(recipe.mFluidInputs[i], recipe2.mFluidInputs[i])) isSame = false;
                }
                if (isSame) duplicates.add(recipe2);
            }
        }
        toAdd.removeAll(duplicates);
    }

    private static Materials getMaterialFromInputFluid(GTRecipe recipe) {
        String materialString = recipe.mFluidInputs[0].getFluid()
            .getName();
        materialString = StringUtils.removeStart(materialString, "molten");
        materialString = StringUtils.removeStart(materialString, "fluid");
        materialString = StringUtils.capitalize(materialString);
        return Materials.get(materialString);
    }

    private static void editRecipes(ArrayListMultimap<SubTag, GTRecipe> base, HashSet<ItemStack> noGas) {
        HashSet<GTRecipe> toAdd = new HashSet<>();

        for (SubTag gasTag : base.keySet()) {
            for (GTRecipe recipe : base.get(gasTag)) {
                if (recipe.mFluidInputs != null && recipe.mFluidInputs.length > 0) {
                    Materials originalGas = getMaterialFromInputFluid(recipe);
                    if (originalGas != Materials._NULL) {
                        editEBFWerkstoffRecipes(gasTag, recipe, originalGas, toAdd);
                        editEBFMaterialRecipes(gasTag, recipe, originalGas, toAdd);
                        editEBFNoGasRecipes(recipe, originalGas, toAdd, noGas);
                    }
                }
            }
            RecipeMaps.blastFurnaceRecipes.getBackend()
                .removeRecipes(base.get(gasTag));
        }

        removeDuplicateGasRecipes(toAdd);
        toAdd.forEach(RecipeMaps.blastFurnaceRecipes::add);
    }

    public static void addElectricImplosionCompressorRecipes() {
        RecipeMaps.implosionRecipes.getAllRecipes()
            .stream()
            .filter(e -> e.mInputs != null)
            .forEach(
                recipe -> GTValues.RA.stdBuilder()
                    .itemInputs(
                        Arrays.stream(recipe.mInputs)
                            .filter(e -> !StaticRecipeChangeLoaders.checkForExplosives(e))
                            .distinct()
                            .toArray(ItemStack[]::new))
                    .itemOutputs(recipe.mOutputs)
                    .duration(1 * TICK)
                    .eut(TierEU.RECIPE_UEV)
                    .addTo(electricImplosionCompressorRecipes));

        // Custom EIC recipes.
        new ElectricImplosionCompressorRecipes().run();
    }

    private static boolean checkForExplosives(ItemStack input) {
        return GTUtility.areStacksEqual(input, new ItemStack(Blocks.tnt))
            || GTUtility.areStacksEqual(input, GTModHandler.getIC2Item("industrialTnt", 1L))
            || GTUtility.areStacksEqual(input, GTModHandler.getIC2Item("dynamite", 1L))
            || GTUtility.areStacksEqual(input, ItemList.Block_Powderbarrel.get(1L));
    }
}
