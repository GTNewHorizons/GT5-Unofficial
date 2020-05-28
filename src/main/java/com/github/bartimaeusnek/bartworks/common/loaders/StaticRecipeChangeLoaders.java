/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.common.loaders;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.Pair;
import com.github.bartimaeusnek.bartworks.util.StreamUtils;
import com.github.bartimaeusnek.bartworks.util.log.DebugLog;
import com.github.bartimaeusnek.crossmod.BartWorksCrossmod;
import com.google.common.collect.ArrayListMultimap;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.*;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_ElectricImplosionCompressor.eicMap;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.*;
import static gregtech.api.enums.GT_Values.VN;

public class StaticRecipeChangeLoaders {

    private StaticRecipeChangeLoaders() {
    }

    public static void addEBFGasRecipes() {
        ArrayListMultimap<SubTag, GT_Recipe> toChange = getRecipesToChange(NOBLE_GAS, ANAEROBE_GAS);
        editRecipes(toChange, getNoGasItems(toChange));
    }

    public static void fixEnergyRequirements() {
        GT_Recipe.GT_Recipe_Map.sMappings.stream()
                .filter(StreamUtils::filterVisualMaps)
                .forEach(gt_recipe_map ->
                        gt_recipe_map.mRecipeList.parallelStream().forEach(gt_recipe -> {
                            for (int i = 0; i < (VN.length - 1); i++) {
                                if (gt_recipe.mEUt > BW_Util.getMachineVoltageFromTier(i) && gt_recipe.mEUt <= BW_Util.getTierVoltage(i)) {
                                    gt_recipe.mEUt = BW_Util.getMachineVoltageFromTier(i);
                                }
                            }
                        }));
    }

    private static void replaceWrongFluidOutput(Werkstoff werkstoff, GT_Recipe recipe, FluidStack wrongNamedFluid) {
        for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
            if (GT_Utility.areFluidsEqual(recipe.mFluidOutputs[i], wrongNamedFluid)) {
                recipe.mFluidOutputs[i] = werkstoff.getFluidOrGas(recipe.mFluidOutputs[i].amount);
            }
        }
    }

    private static void replaceWrongFluidInput(Werkstoff werkstoff, GT_Recipe.GT_Recipe_Map map, GT_Recipe recipe, FluidStack wrongNamedFluid) {
        for (int i = 0; i < recipe.mFluidInputs.length; i++) {
            if (GT_Utility.areFluidsEqual(recipe.mFluidInputs[i], wrongNamedFluid)) {
                Collection<GT_Recipe> col = map.mRecipeFluidMap.get(wrongNamedFluid.getFluid());
                map.mRecipeFluidMap.remove(wrongNamedFluid.getFluid());
                map.mRecipeFluidMap.put(werkstoff.getFluidOrGas(1).getFluid(), col);
                recipe.mFluidInputs[i] = werkstoff.getFluidOrGas(recipe.mFluidInputs[i].amount);
                map.mRecipeFluidNameMap.add(werkstoff.getFluidOrGas(1).getFluid().getName());
            }
        }
    }

    private static void addConversionRecipe(Werkstoff werkstoff, FluidStack wrongNamedFluid) {
        GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.add(new BWRecipes.DynamicGTRecipe(false, null, null, null, null, new FluidStack[]{wrongNamedFluid}, new FluidStack[]{werkstoff.getFluidOrGas(1)}, 1, 1, 0));
    }

    private static FluidStack getWrongNameFluid(Werkstoff werkstoff) {
        String name = werkstoff.getFluidOrGas(1).getFluid().getName();
        String wrongname = "molten." + name;
        return FluidRegistry.getFluidStack(wrongname, 1);
    }

    private static void enforceNobleGas(Werkstoff werkstoff) {
        FluidStack wrongNamedFluid = getWrongNameFluid(werkstoff);
        if (wrongNamedFluid != null) {
            GT_Recipe.GT_Recipe_Map.sMappings.forEach(map -> map.mRecipeList.forEach(recipe -> {
                replaceWrongFluidInput(werkstoff, map, recipe, wrongNamedFluid);
                replaceWrongFluidOutput(werkstoff, recipe, wrongNamedFluid);
            }));
            addConversionRecipe(werkstoff, wrongNamedFluid);
        }
    }

    public static void unificationRecipeEnforcer() {
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
            if (werkstoff.getGenerationFeatures().enforceUnification) {

                if (werkstoff.contains(NOBLE_GAS))
                    enforceNobleGas(werkstoff);

                HashSet<String> oreDictNames = new HashSet<>(werkstoff.getADDITIONAL_OREDICT());
                oreDictNames.add(werkstoff.getVarName());
                StaticRecipeChangeLoaders.runMoltenUnificationEnfocement(werkstoff);
                StaticRecipeChangeLoaders.runUnficationDeleter(werkstoff);
                for (String s : oreDictNames)
                    for (OrePrefixes prefixes : OrePrefixes.values()) {
                        if (!werkstoff.hasItemType(prefixes))
                            continue;
                        if (OreDictionary.getOres(prefixes + s).size() <= 1)
                            continue;
                        for (int j = 0; j < OreDictionary.getOres(prefixes + s).size(); j++) {
                            ItemStack toReplace = OreDictionary.getOres(prefixes + s).get(j);
                            ItemStack replacement = werkstoff.get(prefixes);
                            if (toReplace == null || GT_Utility.areStacksEqual(toReplace, replacement) || replacement == null || replacement.getItem() == null)
                                continue;
                            for (GT_Recipe.GT_Recipe_Map map : GT_Recipe.GT_Recipe_Map.sMappings) {
                                HashSet<GT_Recipe> toRem = new HashSet<>();
                                for (GT_Recipe recipe : map.mRecipeList) {
                                    boolean removal = map.equals(GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes) || map.equals(GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes);
                                    for (int i = 0; i < recipe.mInputs.length; i++) {
                                        if (!GT_Utility.areStacksEqual(recipe.mInputs[i], toReplace))
                                            continue;
                                        if (removal)
                                            toRem.add(recipe);
                                        else {
                                            int amount = recipe.mInputs[i].stackSize;
                                            recipe.mInputs[i] = BW_Util.setStackSize(replacement, amount);
                                        }
                                    }
                                    for (int i = 0; i < recipe.mOutputs.length; i++) {
                                        if (!GT_Utility.areStacksEqual(recipe.mOutputs[i], toReplace))
                                            continue;
                                        if (removal)
                                            toRem.add(recipe);
                                        else {
                                            int amount = recipe.mOutputs[i].stackSize;
                                            recipe.mOutputs[i] = BW_Util.setStackSize(replacement, amount);
                                        }
                                    }
                                    if (recipe.mSpecialItems instanceof ItemStack) {
                                        if (!GT_Utility.areStacksEqual((ItemStack) recipe.mSpecialItems, toReplace))
                                            continue;
                                        if (removal)
                                            toRem.add(recipe);
                                        else {
                                            int amount = ((ItemStack) recipe.mSpecialItems).stackSize;
                                            recipe.mSpecialItems = BW_Util.setStackSize(replacement, amount);
                                        }
                                    }
                                }
                                map.mRecipeList.removeAll(toRem);
                            }
                        }
                    }
            }
        }
    }

    @SuppressWarnings("ALL")
    private static void runMoltenUnificationEnfocement(Werkstoff werkstoff) {
        if (werkstoff.getGenerationFeatures().enforceUnification && werkstoff.hasItemType(WerkstoffLoader.cellMolten)) {
            try {
                FluidContainerRegistry.FluidContainerData data = new FluidContainerRegistry.FluidContainerData(new FluidStack(Objects.requireNonNull(molten.get(werkstoff)), 144), werkstoff.get(WerkstoffLoader.cellMolten), Materials.Empty.getCells(1));
                Field f = GT_Utility.class.getDeclaredField("sFilledContainerToData");
                f.setAccessible(true);
                Map<GT_ItemStack, FluidContainerRegistry.FluidContainerData> sFilledContainerToData = (Map<GT_ItemStack, FluidContainerRegistry.FluidContainerData>) f.get(null);
                HashSet torem = new HashSet<>();
                ItemStack toReplace = null;
                for (Map.Entry<GT_ItemStack, FluidContainerRegistry.FluidContainerData> entry : sFilledContainerToData.entrySet()) {
                    final String MODID = GameRegistry.findUniqueIdentifierFor(data.filledContainer.getItem()).modId;
                    if (MODID.equals(MainMod.MOD_ID) || MODID.equals(BartWorksCrossmod.MOD_ID))
                        continue;
                    if (entry.getValue().fluid.equals(data.fluid) && !entry.getValue().filledContainer.equals(data.filledContainer)) {
                        toReplace = entry.getValue().filledContainer;
                        torem.add(entry);
                    }
                }
                sFilledContainerToData.entrySet().removeAll(torem);
                torem.clear();
                if (toReplace != null) {
                    for (GT_Recipe.GT_Recipe_Map map : GT_Recipe.GT_Recipe_Map.sMappings) {
                        torem.clear();
                        for (GT_Recipe recipe : map.mRecipeList) {
                            for (int i = 0; i < recipe.mInputs.length; i++) {
                                if (GT_Utility.areStacksEqual(recipe.mInputs[i], toReplace)) {
                                    torem.add(recipe);
                                    // recipe.mInputs[i] = data.filledContainer;
                                }
                            }
                            for (int i = 0; i < recipe.mOutputs.length; i++) {
                                if (GT_Utility.areStacksEqual(recipe.mOutputs[i], toReplace)) {
                                    torem.add(recipe);
                                    // recipe.mOutputs[i] = data.filledContainer;
                                    if (map == GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes && GT_Utility.areStacksEqual(recipe.mOutputs[i], data.filledContainer) && !recipe.mFluidInputs[0].equals(data.fluid)) {
                                        torem.add(recipe);
                                        // recipe.mOutputs[i] = data.filledContainer;
                                    }
                                }
                            }
                            if (recipe.mSpecialItems instanceof ItemStack) {
                                if (GT_Utility.areStacksEqual((ItemStack) recipe.mSpecialItems, toReplace)) {
                                    torem.add(recipe);
                                    //  recipe.mSpecialItems = data.filledContainer;
                                }
                            }
                        }
                        map.mRecipeList.removeAll(torem);
                    }
                }
                GT_Utility.addFluidContainerData(data);
            } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    private static void runUnficationDeleter(Werkstoff werkstoff) {
        if (werkstoff.getType() == Werkstoff.Types.ELEMENT) {
            if (werkstoff.getBridgeMaterial() != null) {
                werkstoff.getBridgeMaterial().mElement = Element.get(werkstoff.getToolTip());
                Element.get(werkstoff.getToolTip()).mLinkedMaterials = new ArrayList<>();
                Element.get(werkstoff.getToolTip()).mLinkedMaterials.add(werkstoff.getBridgeMaterial());
            }
        }

        for (OrePrefixes prefixes : OrePrefixes.values())
            if (werkstoff.hasItemType(prefixes)) {
                GT_OreDictUnificator.set(prefixes, werkstoff.getBridgeMaterial(), werkstoff.get(prefixes), true, true);
                for (ItemStack stack : OreDictionary.getOres(prefixes + werkstoff.getVarName())) {
                    GT_OreDictUnificator.addAssociation(prefixes, werkstoff.getBridgeMaterial(), stack, false);
                    GT_OreDictUnificator.getAssociation(stack).mUnificationTarget = werkstoff.get(prefixes);
                }
            }
    }

    private static ArrayListMultimap<SubTag, GT_Recipe> getRecipesToChange(SubTag... GasTags) {
        ArrayListMultimap<SubTag, GT_Recipe> toAdd = ArrayListMultimap.create();
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sBlastRecipes.mRecipeList) {
            if (recipe.mFluidInputs != null && recipe.mFluidInputs.length > 0) {
                String FluidString = recipe.mFluidInputs[0].getFluid().getName().replaceAll("molten", "").replaceAll("fluid", "");
                Materials mat = Materials.get(FluidString.substring(0, 1).toUpperCase() + FluidString.substring(1));
                if (mat != Materials._NULL) {
                    for (SubTag tag : GasTags) {
                        if (mat.contains(tag)) {
                            DebugLog.log("Found EBF Recipe to change, Output:" + BW_Util.translateGTItemStack(recipe.mOutputs[0]));
                            toAdd.put(tag, recipe);
                        }
                    }
                }
            }
        }
        return toAdd;
    }

    private static HashSet<ItemStack> getNoGasItems(ArrayListMultimap<SubTag, GT_Recipe> base) {
        HashSet<ItemStack> toAdd = new HashSet<>();
        ArrayListMultimap<SubTag, GT_Recipe> repToAdd = ArrayListMultimap.create();
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sBlastRecipes.mRecipeList) {
            for (SubTag tag : base.keySet())
                recipeLoop:
                        for (GT_Recipe baseRe : base.get(tag)) {
                            if (recipe.mInputs.length == baseRe.mInputs.length && recipe.mOutputs.length == baseRe.mOutputs.length)
                                for (int i = 0; i < recipe.mInputs.length; i++) {
                                    if ((recipe.mFluidInputs == null || recipe.mFluidInputs.length == 0) && BW_Util.checkStackAndPrefix(recipe.mInputs[i]) && BW_Util.checkStackAndPrefix(baseRe.mInputs[i]) && GT_OreDictUnificator.getAssociation(recipe.mInputs[i]).mMaterial.mMaterial.equals(GT_OreDictUnificator.getAssociation(baseRe.mInputs[i]).mMaterial.mMaterial) && GT_Utility.areStacksEqual(recipe.mOutputs[0], baseRe.mOutputs[0])) {
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

    private static void editEBFMaterialRecipes(SubTag GasTag, GT_Recipe recipe, Materials mat, HashSet<GT_Recipe> toAdd) {
        for (Materials materials : Materials.values()) {
            if (materials.contains(GasTag)) {
                int time = (int) ((double) recipe.mDuration / 200D * (200D + (materials.getProtons() >= mat.getProtons() ? (double) mat.getProtons() - (double) materials.getProtons() : (double) mat.getProtons() * 2.75D - (double) materials.getProtons())));
                toAdd.add(new BWRecipes.DynamicGTRecipe(false, recipe.mInputs, recipe.mOutputs, recipe.mSpecialItems, recipe.mChances, new FluidStack[]{materials.getGas(recipe.mFluidInputs[0].amount)}, recipe.mFluidOutputs, time, recipe.mEUt, recipe.mSpecialValue));
            }
        }
    }

    private static void editEBFWerkstoffRecipes(SubTag GasTag, GT_Recipe recipe, Materials mat, HashSet<GT_Recipe> toAdd) {
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashMap.values()) {
            if (werkstoff.contains(GasTag)) {
                int time = (int) ((double) recipe.mDuration / 200D * (200D + (werkstoff.getStats().getProtons() >= mat.getProtons() ? (double) mat.getProtons() - (double) werkstoff.getStats().getProtons() : (double) mat.getProtons() * 2.75D - (double) werkstoff.getStats().getProtons())));
                toAdd.add(new BWRecipes.DynamicGTRecipe(false, recipe.mInputs, recipe.mOutputs, recipe.mSpecialItems, recipe.mChances, new FluidStack[]{new FluidStack(Objects.requireNonNull(fluids.get(werkstoff)), recipe.mFluidInputs[0].amount)}, recipe.mFluidOutputs, time, recipe.mEUt, recipe.mSpecialValue));
            }
        }
    }

    private static void editEBFNoGasRecipes(GT_Recipe recipe, Materials mat, HashSet<GT_Recipe> toAdd, HashSet<ItemStack> noGas) {
        for (ItemStack is : noGas) {
            byte circuitConfiguration = 1;
            if (GT_Utility.areStacksEqual(is, recipe.mOutputs[0])) {
                ArrayList<ItemStack> inputs = new ArrayList<>(recipe.mInputs.length);
                for (ItemStack stack : recipe.mInputs)
                    if (!GT_Utility.areStacksEqual(GT_Utility.getIntegratedCircuit(11), stack) && !GT_Utility.areStacksEqual(GT_Utility.getIntegratedCircuit(14), stack) && !GT_Utility.areStacksEqual(GT_Utility.getIntegratedCircuit(19), stack)) {
                        if (BW_Util.checkStackAndPrefix(stack))
                            circuitConfiguration = (byte) (GT_OreDictUnificator.getAssociation(stack).mPrefix.equals(OrePrefixes.dustSmall) ? 4 : GT_OreDictUnificator.getAssociation(stack).mPrefix.equals(OrePrefixes.dustTiny) ? 9 : 1);
                        inputs.add(stack);
                    }
                inputs.add(GT_Utility.getIntegratedCircuit(circuitConfiguration));
                toAdd.add(new BWRecipes.DynamicGTRecipe(false, inputs.toArray(new ItemStack[0]), recipe.mOutputs, recipe.mSpecialItems, recipe.mChances, null, recipe.mFluidOutputs, (int) ((double) recipe.mDuration / 200D * (200D + ((double) mat.getProtons() * 2.75D))), recipe.mEUt, recipe.mSpecialValue));
                break;
            }
        }
    }

    private static void removeDuplicateGasRecipes(HashSet<GT_Recipe> toAdd) {
        HashSet<GT_Recipe> duplicates = new HashSet<>();
        for (GT_Recipe recipe : toAdd) {
            for (GT_Recipe recipe2 : toAdd) {
                if (recipe.mEUt != recipe2.mEUt || recipe.mDuration != recipe2.mDuration || recipe.mSpecialValue != recipe2.mSpecialValue || recipe == recipe2 || recipe.mInputs.length != recipe2.mInputs.length || recipe.mFluidInputs.length != recipe2.mFluidInputs.length)
                    continue;
                boolean isSame = true;
                for (int i = 0; i < recipe.mInputs.length; i++) {
                    if (!GT_Utility.areStacksEqual(recipe.mInputs[i], recipe2.mInputs[i]))
                        isSame = false;
                }
                for (int i = 0; i < recipe.mFluidInputs.length; i++) {
                    if (!GT_Utility.areFluidsEqual(recipe.mFluidInputs[i], recipe2.mFluidInputs[i]))
                        isSame = false;
                }
                if (isSame)
                    duplicates.add(recipe2);
            }
        }
        toAdd.removeAll(duplicates);
    }

    private static Materials getMaterialFromInputFluid(GT_Recipe recipe) {
        String materialString = recipe.mFluidInputs[0].getFluid().getName().replaceAll("molten", "").replaceAll("fluid", "");
        return Materials.get(materialString.substring(0, 1).toUpperCase() + materialString.substring(1));
    }

    private static void editRecipes(ArrayListMultimap<SubTag, GT_Recipe> base, HashSet<ItemStack> noGas) {
        if (GT_Recipe.GT_Recipe_Map.sBlastRecipes.mRecipeFluidNameMap.contains(Objects.requireNonNull(fluids.get(Oganesson)).getName()))
            return;

        HashSet<GT_Recipe> toAdd = new HashSet<>();

        for (SubTag gasTag : base.keySet()) {
            for (GT_Recipe recipe : base.get(gasTag)) {
                if (recipe.mFluidInputs != null && recipe.mFluidInputs.length > 0) {
                    Materials mat = getMaterialFromInputFluid(recipe);
                    if (mat != Materials._NULL) {
                        editEBFWerkstoffRecipes(gasTag, recipe, mat, toAdd);
                        editEBFMaterialRecipes(gasTag, recipe, mat, toAdd);
                        editEBFNoGasRecipes(recipe, mat, toAdd, noGas);
                    }
                }
            }
            GT_Recipe.GT_Recipe_Map.sBlastRecipes.mRecipeList.removeAll(base.get(gasTag));
        }

        removeDuplicateGasRecipes(toAdd);
        toAdd.forEach(GT_Recipe.GT_Recipe_Map.sBlastRecipes::add);
    }

    @SuppressWarnings("ALL")
    public static void addElectricImplosionCompressorRecipes() {
        if (eicMap == null) {
            eicMap = new GT_Recipe.GT_Recipe_Map(new HashSet<>(GT_Recipe.GT_Recipe_Map.sImplosionRecipes.mRecipeList.size()), "gt.recipe.electricimplosioncompressor", "Electric Implosion Compressor", (String) null, "gregtech:textures/gui/basicmachines/Default", 1, 2, 1, 0, 1, "", 1, "", true, true);
            GT_Recipe.GT_Recipe_Map.sImplosionRecipes.mRecipeList.stream().filter(e -> e.mInputs != null).forEach(recipe -> eicMap.addRecipe(true, Arrays.stream(recipe.mInputs).filter(e -> !StaticRecipeChangeLoaders.checkForExplosives(e)).distinct().toArray(ItemStack[]::new), recipe.mOutputs, null, null, null, 1, BW_Util.getMachineVoltageFromTier(10), 0));
        }
    }

    private static boolean checkForExplosives(ItemStack input) {
        return (GT_Utility.areStacksEqual(input, new ItemStack(Blocks.tnt)) || GT_Utility.areStacksEqual(input, GT_ModHandler.getIC2Item("industrialTnt", 1L)) || GT_Utility.areStacksEqual(input, GT_ModHandler.getIC2Item("dynamite", 1L)) || GT_Utility.areStacksEqual(input, ItemList.Block_Powderbarrel.get(1L)));
    }

    public static void patchEBFMapForCircuitUnification() {
        Field mUsualInputCount = FieldUtils.getField(GT_Recipe.GT_Recipe_Map.class, "mUsualInputCount", true);
        mUsualInputCount.setAccessible(true);

        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(mUsualInputCount, mUsualInputCount.getModifiers() & ~Modifier.FINAL);
            mUsualInputCount.setInt(GT_Recipe.GT_Recipe_Map.sBlastRecipes, 3);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void synchroniseCircuitUseMulti() {
        GT_Recipe.GT_Recipe_Map[] gt_recipe_maps = {
                GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes,
                GT_Recipe.GT_Recipe_Map.sBlastRecipes
        };
        getRecipesByCircuitID(gt_recipe_maps).forEach(StaticRecipeChangeLoaders::transformCircuitRecipes);
    }

    private static int getBlastLogic(GT_Recipe recipe) {
        int ret = 0;

        for (ItemStack stack : recipe.mInputs) {
            if (stack != null) {
                ret += Math.max(stack.stackSize, 1);
            }
        }

        while (ret > 14) {
            ret /= 10;
        }

        if (recipe.mFluidInputs.length != 0)
            ret += 10;

        return ret;
    }

    private static int getLogicFuntion(GT_Recipe.GT_Recipe_Map gt_recipe_map, GT_Recipe recipe, Pair<Integer, Integer> counts) {
        if (gt_recipe_map == GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes)
            return getChemicalLogic(recipe, counts);
        else if (gt_recipe_map == GT_Recipe.GT_Recipe_Map.sBlastRecipes)
            return getBlastLogic(recipe);
        throw new NoSuchMethodError("Could not find a Supported Method for " + gt_recipe_map.mUnlocalizedName);
    }

    private static int getChemicalLogic(GT_Recipe recipe, Pair<Integer, Integer> counts) {
        Pair<Integer, Integer> toSet;

        if (counts.getKey() == 0 || counts.getValue() == 0)
            toSet = new Pair<>(Math.max(recipe.mFluidOutputs.length, recipe.mOutputs.length), 0);
        else if (counts.getValue() > 2)
            toSet = new Pair<>(counts.getKey() + counts.getValue(), 1);
        else
            toSet = counts;

        return toSet.getValue() * 10 + toSet.getKey();
    }

    private static void transformCircuitRecipes(GT_Recipe.GT_Recipe_Map gtRecipeMap, Map<GT_Recipe, Pair<Integer, Integer>> mapGtRecipeCounts) {
        mapGtRecipeCounts.forEach((recipe, counts) ->
                StaticRecipeChangeLoaders.rewriteForCorrectCircuit(gtRecipeMap, recipe, counts));

        gtRecipeMap.mRecipeList.clear();
        gtRecipeMap.mRecipeList.addAll(mapGtRecipeCounts.keySet());

        fixRecipeClashes(gtRecipeMap);
    }

    private static void fixRecipeClashes(GT_Recipe.GT_Recipe_Map gtRecipeMap) {
        boolean hasClashes;
        do {
            hasClashes = false;
            gtRecipeMap.reInit();
            for (GT_Recipe re : gtRecipeMap.mRecipeList) {
                if (gtRecipeMap.findRecipe(null, false, Long.MAX_VALUE, re.mFluidInputs, re.mInputs) != re) {
                    hasClashes = true;
                    fixRecipeCircuitClashes(re);
                }
            }
        } while (hasClashes);
    }

    private static void fixRecipeCircuitClashes(GT_Recipe recipe) {
        for (int i = 0; i < recipe.mInputs.length; i++) {
            if (GT_Utility.areStacksEqual(GT_Utility.getIntegratedCircuit(Short.MAX_VALUE), recipe.mInputs[i])) {
                int nudmg = recipe.mInputs[i].getItemDamage() + 1 > 24 ? 1 : recipe.mInputs[i].getItemDamage() + 1;
                recipe.mInputs[i].setItemDamage(nudmg);
            }
        }
    }

    private static Map<GT_Recipe.GT_Recipe_Map, Map<GT_Recipe, Pair<Integer, Integer>>> getRecipesByCircuitID(GT_Recipe.GT_Recipe_Map[] ref) {
        return Arrays.stream(ref)
                .collect(Collectors
                        .toMap(k -> k, k ->
                                getArrayListMultiMapFromRecipeList(
                                        gatherNoCircuitRecipes(
                                                new HashSet<>(k.mRecipeList)
                                        )
                                )
                        )
                );
    }

    private static GT_Recipe rewriteForCorrectCircuit(GT_Recipe.GT_Recipe_Map gt_recipe_map, GT_Recipe recipe, Pair<Integer, Integer> counts) {
        ItemStack[] old = BW_Util.copyAndRemoveNulls(recipe.mInputs, ItemStack.class);
        ItemStack[] nu = Arrays.copyOf(old, old.length + 1);

        nu[old.length] = GT_Utility.getIntegratedCircuit( //Max 24, Min 1
                Math.min(
                        Math.max(
                                getLogicFuntion(gt_recipe_map, recipe, counts),
                                1),
                        24)
        );
        recipe.mInputs = nu;

        return recipe;
    }

    @SuppressWarnings("unchecked")
    private static Collection<GT_Recipe> gatherNoCircuitRecipes(Collection<GT_Recipe> mRecipeList) {
        Collection<GT_Recipe> newColl;
        try {
            newColl = (Collection<GT_Recipe>) mRecipeList.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            newColl = new HashSet<>();
        }

        recipeloop: for (GT_Recipe r : mRecipeList) {
            for (int i = 0; i < r.mInputs.length; i++)
                if (GT_Utility.areStacksEqual(GT_Utility.getIntegratedCircuit(Short.MAX_VALUE), r.mInputs[i])) {
                    continue recipeloop;
                }
            newColl.add(r);
        }

        return newColl;
    }

    private static Map<GT_Recipe, Pair<Integer, Integer>> getArrayListMultiMapFromRecipeList(Collection<GT_Recipe> mRecipeList) {
        return mRecipeList.stream().collect(Collectors.toMap(recipe -> recipe, recipe ->
                new Pair<>(
                        (int) Arrays.stream(recipe.mInputs)
                                .filter(Objects::nonNull)
                                .count(),
                        (int) Arrays.stream(recipe.mFluidInputs)
                                .filter(Objects::nonNull)
                                .count()
                )
        ));
    }
}