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

package com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration;

import com.github.bartimaeusnek.bartworks.ASM.BWCoreStaticReplacementMethodes;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import java.util.HashSet;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class CircuitImprintLoader {

    public static short reverseIDs = Short.MAX_VALUE - 1;

    public static final ArrayListMultimap<NBTTagCompound, GT_Recipe> recipeTagMap = ArrayListMultimap.create();
    public static final HashBiMap<Short, ItemList> circuitIIconRefs = HashBiMap.create(20);
    public static final HashSet<ItemStack> blacklistSet = new HashSet<>();
    static final HashBiMap<CircuitData, ItemStack> bwCircuitTagMap = HashBiMap.create(20);
    private static final HashSet<IRecipe> recipeWorldCache = new HashSet<>();
    private static final HashSet<GT_Recipe> gtrecipeWorldCache = new HashSet<>();
    private static final HashSet<GT_Recipe> ORIGINAL_CAL_RECIPES = new HashSet<>();
    private static final HashSet<GT_Recipe> MODIFIED_CAL_RECIPES = new HashSet<>();

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public static void run() {
        HashSet<GT_Recipe> toRem = new HashSet<>();
        HashSet<GT_Recipe> toAdd = new HashSet<>();

        deleteCALRecipesAndTags();
        rebuildCircuitAssemblerMap(toRem, toAdd);
        exchangeRecipesInList(toRem, toAdd);
        makeCircuitImprintRecipes();

        toRem = null;
        toAdd = null;
    }

    private static void reAddOriginalRecipes() {
        GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.mRecipeList.removeAll(MODIFIED_CAL_RECIPES);
        GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.mRecipeList.addAll(ORIGINAL_CAL_RECIPES);
        ORIGINAL_CAL_RECIPES.clear();
        MODIFIED_CAL_RECIPES.clear();
    }

    private static void rebuildCircuitAssemblerMap(HashSet<GT_Recipe> toRem, HashSet<GT_Recipe> toAdd) {
        reAddOriginalRecipes();
        GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.mRecipeList.forEach(
                e -> CircuitImprintLoader.handleCircuitRecipeRebuilding(e, toRem, toAdd));
    }

    private static void handleCircuitRecipeRebuilding(
            GT_Recipe circuitRecipe, HashSet<GT_Recipe> toRem, HashSet<GT_Recipe> toAdd) {
        ItemStack[] outputs = circuitRecipe.mOutputs;
        boolean isOrePass = isCircuitOreDict(outputs[0]);
        String unlocalizedName = outputs[0].getUnlocalizedName();
        if (isOrePass || unlocalizedName.contains("Circuit") || unlocalizedName.contains("circuit")) {

            CircuitImprintLoader.recipeTagMap.put(
                    CircuitImprintLoader.getTagFromStack(outputs[0]), circuitRecipe.copy());

            Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
                    ? FluidRegistry.getFluid("molten.indalloy140")
                    : FluidRegistry.getFluid("molten.solderingalloy");

            if (circuitRecipe.mFluidInputs[0].isFluidEqual(Materials.SolderingAlloy.getMolten(0))
                    || circuitRecipe.mFluidInputs[0].isFluidEqual(new FluidStack(solderIndalloy, 0))) {
                GT_Recipe newRecipe = CircuitImprintLoader.reBuildRecipe(circuitRecipe);
                if (newRecipe != null)
                    BWRecipes.instance
                            .getMappingsFor(BWRecipes.CIRCUITASSEMBLYLINE)
                            .addRecipe(newRecipe);
                addCutoffRecipeToSets(toRem, toAdd, circuitRecipe);
            } else {
                if (circuitRecipe.mEUt > BW_Util.getTierVoltage(ConfigHandler.cutoffTier)) toRem.add(circuitRecipe);
            }
        }
    }

    @Deprecated
    private static String getTypeFromOreDict(ItemStack[] outputs) {
        int[] oreIDS = OreDictionary.getOreIDs(outputs[0]);

        if (oreIDS.length < 1) return "";

        return OreDictionary.getOreName(oreIDS[0]);
    }

    private static boolean isCircuitOreDict(ItemStack item) {
        return BW_Util.isTieredCircuit(item)
                || BW_Util.getOreNames(item).stream().anyMatch(s -> s.equals("circuitPrimitiveArray"));
    }

    private static void exchangeRecipesInList(HashSet<GT_Recipe> toRem, HashSet<GT_Recipe> toAdd) {
        GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.mRecipeList.addAll(toAdd);
        GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.mRecipeList.removeAll(toRem);
        ORIGINAL_CAL_RECIPES.addAll(toRem);
        MODIFIED_CAL_RECIPES.addAll(toAdd);
    }

    private static void addCutoffRecipeToSets(
            HashSet<GT_Recipe> toRem, HashSet<GT_Recipe> toAdd, GT_Recipe circuitRecipe) {
        if (circuitRecipe.mEUt > BW_Util.getTierVoltage(ConfigHandler.cutoffTier)) {
            toRem.add(circuitRecipe);
            toAdd.add(CircuitImprintLoader.makeMoreExpensive(circuitRecipe));
        }
    }

    @SuppressWarnings("deprecation")
    public static GT_Recipe makeMoreExpensive(GT_Recipe original) {
        GT_Recipe newRecipe = original.copy();
        for (ItemStack is : newRecipe.mInputs) {
            if (!BW_Util.isTieredCircuit(is)) {
                is.stackSize = Math.min(is.stackSize * 6, 64);
                if (is.stackSize > is.getItem().getItemStackLimit() || is.stackSize > is.getMaxStackSize())
                    is.stackSize = is.getMaxStackSize();
            }
        }
        newRecipe.mFluidInputs[0].amount *= 4;
        newRecipe.mDuration *= 4;
        return newRecipe;
    }

    public static GT_Recipe reBuildRecipe(GT_Recipe original) {
        ItemStack[] in = new ItemStack[6];
        BiMap<ItemList, Short> inversed = CircuitImprintLoader.circuitIIconRefs.inverse();

        for (int i = 0; i < 6; i++) {
            try {
                replaceCircuits(inversed, original, in, i);
                replaceComponents(in, original, i);
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        if (CircuitImprintLoader.checkForBlacklistedComponents(in)) {
            return null;
        }

        return new BWRecipes.DynamicGTRecipe(
                false,
                in,
                new ItemStack[] {getOutputMultiplied(original)},
                BW_Meta_Items.getNEWCIRCUITS()
                        .getStackWithNBT(CircuitImprintLoader.getTagFromStack(original.mOutputs[0]), 0, 0),
                null,
                original.mFluidInputs,
                null,
                original.mDuration * 12,
                original.mEUt,
                0);
    }

    private static ItemStack getOutputMultiplied(GT_Recipe original) {
        ItemStack out = original.copy().getOutput(0);
        out.stackSize *= 16;
        return out;
    }

    private static void replaceCircuits(
            BiMap<ItemList, Short> inversed, GT_Recipe original, ItemStack[] in, int index) {
        for (ItemList il : inversed.keySet()) {
            if (GT_Utility.areStacksEqual(il.get(1), original.mInputs[index])) {
                in[index] =
                        BW_Meta_Items.getNEWCIRCUITS().getStack(inversed.get(il), original.mInputs[index].stackSize);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private static void replaceComponents(ItemStack[] in, GT_Recipe original, int index)
            throws ArrayIndexOutOfBoundsException {
        if (original.mInputs[index] != null && in[index] == null) {
            // big wires
            if (BW_Util.checkStackAndPrefix(original.mInputs[index])
                    && GT_OreDictUnificator.getAssociation(original.mInputs[index]).mPrefix == OrePrefixes.wireGt01) {
                in[index] = GT_OreDictUnificator.get(
                        OrePrefixes.wireGt16,
                        GT_OreDictUnificator.getAssociation(original.mInputs[index]).mMaterial.mMaterial,
                        original.mInputs[index].stackSize);
                // fine wires
            } else if (BW_Util.checkStackAndPrefix(original.mInputs[index])
                    && GT_OreDictUnificator.getAssociation(original.mInputs[index]).mPrefix == OrePrefixes.wireFine) {
                in[index] = GT_OreDictUnificator.get(
                        OrePrefixes.wireGt04,
                        GT_OreDictUnificator.getAssociation(original.mInputs[index]).mMaterial.mMaterial,
                        original.mInputs[index].stackSize);
                if (in[index] == null) {
                    in[index] = GT_OreDictUnificator.get(
                            OrePrefixes.wireFine,
                            GT_OreDictUnificator.getAssociation(original.mInputs[index]).mMaterial.mMaterial,
                            original.mInputs[index].stackSize * 16);
                }
                // other components
            } else {
                in[index] = original.mInputs[index].copy();
                in[index].stackSize *= 16;
                if (in[index].stackSize > in[index].getItem().getItemStackLimit()
                        || in[index].stackSize > in[index].getMaxStackSize())
                    in[index].stackSize = in[index].getMaxStackSize();
            }
        }
    }

    private static void makeCircuitImprintRecipes() {
        removeOldRecipesFromRegistries();
        CircuitImprintLoader.recipeTagMap.keySet().forEach(e -> {
            makeAndAddSlicingRecipe(e);
            makeAndAddCraftingRecipes(e);
        });
    }

    private static boolean checkForBlacklistedComponents(ItemStack[] itemStacks) {
        for (ItemStack is : itemStacks) {
            for (ItemStack is2 : CircuitImprintLoader.blacklistSet) {
                if (GT_Utility.areStacksEqual(is, is2)) return true;
            }
        }
        return false;
    }

    private static void removeOldRecipesFromRegistries() {
        recipeWorldCache.forEach(CraftingManager.getInstance().getRecipeList()::remove);
        BWCoreStaticReplacementMethodes.clearRecentlyUsedRecipes();
        gtrecipeWorldCache.forEach(GT_Recipe.GT_Recipe_Map.sSlicerRecipes.mRecipeList::remove);
        recipeWorldCache.forEach(r -> {
            try {
                BW_Util.getGTBufferedRecipeList().remove(r);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        recipeWorldCache.clear();
        gtrecipeWorldCache.clear();
    }

    private static void makeAndAddSlicingRecipe(NBTTagCompound tag) {
        ItemStack stack = CircuitImprintLoader.getStackFromTag(tag);
        int eut = Integer.MAX_VALUE;

        for (GT_Recipe recipe : CircuitImprintLoader.recipeTagMap.get(tag)) {
            eut = Math.min(eut, recipe.mEUt);
        }

        eut = Math.min(
                eut,
                BW_Util.getMachineVoltageFromTier(BW_Util.getCircuitTierFromOreDictName(OreDictionary.getOreName(
                        (OreDictionary.getOreIDs(stack) != null && OreDictionary.getOreIDs(stack).length > 0)
                                ? OreDictionary.getOreIDs(stack)[0]
                                : -1))));
        GT_Recipe slicingRecipe = new BWRecipes.DynamicGTRecipe(
                true,
                new ItemStack[] {stack, ItemList.Shape_Slicer_Flat.get(0)},
                new ItemStack[] {BW_Meta_Items.getNEWCIRCUITS().getStackWithNBT(tag, 1, 1)},
                null,
                null,
                null,
                null,
                300,
                eut,
                BW_Util.CLEANROOM);
        gtrecipeWorldCache.add(slicingRecipe);
        GT_Recipe.GT_Recipe_Map.sSlicerRecipes.add(slicingRecipe);
    }

    private static void makeAndAddCraftingRecipes(NBTTagCompound tag) {
        ItemStack circuit = BW_Meta_Items.getNEWCIRCUITS().getStackWithNBT(tag, 0, 1);
        Object[] imprintRecipe = {
            " X ",
            "GPG",
            " X ",
            'P',
            BW_Meta_Items.getNEWCIRCUITS().getStackWithNBT(tag, 1, 1),
            'G',
            WerkstoffLoader.Prasiolite.get(OrePrefixes.gemExquisite, 1),
            'X',
            BW_Meta_Items.getNEWCIRCUITS().getStack(3)
        };

        IRecipe bwrecipe = new BWRecipes.BWNBTDependantCraftingRecipe(circuit, imprintRecipe);
        ShapedOreRecipe gtrecipe = BW_Util.createGTCraftingRecipe(
                circuit,
                GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.KEEPNBT
                        | GT_ModHandler.RecipeBits.BUFFERED,
                imprintRecipe);

        // Adds the actual recipe
        recipeWorldCache.add(bwrecipe);
        GameRegistry.addRecipe(bwrecipe);
        // Adds the NEI visual recipe
        recipeWorldCache.add(gtrecipe);
        GameRegistry.addRecipe(gtrecipe);
    }

    public static NBTTagCompound getTagFromStack(ItemStack stack) {
        if (GT_Utility.isStackValid(stack))
            return BW_Util.setStackSize(stack.copy(), 1).writeToNBT(new NBTTagCompound());
        return new NBTTagCompound();
    }

    public static ItemStack getStackFromTag(NBTTagCompound tagCompound) {
        return ItemStack.loadItemStackFromNBT(tagCompound);
    }

    private static void deleteCALRecipesAndTags() {
        BWRecipes.instance
                .getMappingsFor(BWRecipes.CIRCUITASSEMBLYLINE)
                .mRecipeList
                .clear();
        recipeTagMap.clear();
    }
}
