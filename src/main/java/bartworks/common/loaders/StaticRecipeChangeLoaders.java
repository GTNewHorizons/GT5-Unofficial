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

import static gregtech.api.util.GTRecipeBuilder.INGOTS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import bartworks.MainMod;
import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import bwcrossmod.BartWorksCrossmod;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Element;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.GTItemStack;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;

public class StaticRecipeChangeLoaders {

    private StaticRecipeChangeLoaders() {}

    public static void unificationRecipeEnforcer() {
        List<GTRecipe> toRemove = new ArrayList<>();
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
            StaticRecipeChangeLoaders.runMaterialLinker(werkstoff);
            if (werkstoff.getGenerationFeatures().enforceUnification) {
                HashSet<String> oreDictNames = new HashSet<>(werkstoff.getAdditionalOredict());
                oreDictNames.add(werkstoff.getVarName());
                StaticRecipeChangeLoaders.runMoltenUnificationEnforcement(werkstoff);
                StaticRecipeChangeLoaders.runUnficationDeleter(werkstoff);
                for (String s : oreDictNames) {
                    for (OrePrefixes prefixes : OrePrefixes.VALUES) {
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
                                        recipe.mInputs[i] = GTUtility
                                            .copyAmount(recipe.mInputs[i].stackSize, replacement);
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
    }

    private static void runMoltenUnificationEnforcement(Werkstoff werkstoff) {
        if (werkstoff.getGenerationFeatures().enforceUnification && werkstoff.hasItemType(OrePrefixes.cellMolten)) {
            FluidContainerRegistry.FluidContainerData data = new FluidContainerRegistry.FluidContainerData(
                new FluidStack(Objects.requireNonNull(WerkstoffLoader.molten.get(werkstoff)), 1 * INGOTS),
                werkstoff.get(OrePrefixes.cellMolten),
                Materials.Empty.getCells(1));
            ItemStack toReplace = null;
            Iterator<Map.Entry<GTItemStack, FluidContainerRegistry.FluidContainerData>> iterator = GTUtility
                .getFilledContainerToData()
                .entrySet()
                .iterator();
            while (iterator.hasNext()) {
                Map.Entry<GTItemStack, FluidContainerRegistry.FluidContainerData> entry = iterator.next();
                final String MODID = GameRegistry.findUniqueIdentifierFor(data.filledContainer.getItem()).modId;
                if (MainMod.MOD_ID.equals(MODID) || BartWorksCrossmod.MOD_ID.equals(MODID)) continue;
                if (entry.getValue().fluid.equals(data.fluid)
                    && !entry.getValue().filledContainer.equals(data.filledContainer)) {
                    toReplace = entry.getValue().filledContainer;
                    iterator.remove();
                }
            }
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
                                if (map == RecipeMaps.cannerRecipes
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
        }
    }

    private static void runUnficationDeleter(Werkstoff werkstoff) {
        if (werkstoff.getType() == Werkstoff.Types.ELEMENT && werkstoff.getBridgeMaterial() != null
            && Element.get(werkstoff.getToolTip()) != Element._NULL) {
            werkstoff.getBridgeMaterial().mElement = Element.get(werkstoff.getToolTip());
            Element.get(werkstoff.getToolTip()).mLinkedMaterials = new ArrayList<>();
            Element.get(werkstoff.getToolTip()).mLinkedMaterials.add(werkstoff.getBridgeMaterial());
        }

        for (OrePrefixes prefixes : OrePrefixes.VALUES) if (werkstoff.hasItemType(prefixes)) {
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

        for (OrePrefixes prefixes : OrePrefixes.VALUES)
            if (werkstoff.hasItemType(prefixes) && werkstoff.getBridgeMaterial() != null) {
                GTOreDictUnificator.set(prefixes, werkstoff.getBridgeMaterial(), werkstoff.get(prefixes), true, true);
                for (ItemStack stack : OreDictionary.getOres(prefixes + werkstoff.getVarName())) {
                    GTOreDictUnificator.addAssociation(prefixes, werkstoff.getBridgeMaterial(), stack, false);
                }
            }
    }

    public static void addElectricImplosionCompressorRecipes() {
        // Custom EIC recipes.
        new ElectricImplosionCompressorRecipes().run();
    }
}
