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

package bartworks.system.material.CircuitGeneration;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import bartworks.API.recipe.BWNBTDependantCraftingRecipe;
import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.ASM.BWCoreStaticReplacementMethodes;
import bartworks.common.configs.ConfigHandler;
import bartworks.system.material.WerkstoffLoader;
import bartworks.util.BWUtil;
import bartworks.util.Pair;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;

public class CircuitImprintLoader {

    public static short reverseIDs = Short.MAX_VALUE - 1;

    public static final ArrayListMultimap<NBTTagCompound, GTRecipe> recipeTagMap = ArrayListMultimap.create();
    public static final HashBiMap<Short, ItemList> circuitIIconRefs = HashBiMap.create(20);
    public static final HashSet<ItemStack> blacklistSet = new HashSet<>();
    static final HashBiMap<CircuitData, ItemStack> bwCircuitTagMap = HashBiMap.create(20);
    private static final HashSet<IRecipe> recipeWorldCache = new HashSet<>();
    private static final HashSet<GTRecipe> gtrecipeWorldCache = new HashSet<>();
    private static final HashSet<GTRecipe> ORIGINAL_CAL_RECIPES = new HashSet<>();
    private static final HashSet<GTRecipe> MODIFIED_CAL_RECIPES = new HashSet<>();

    public static void run() {
        HashSet<GTRecipe> toRem = new HashSet<>();
        HashSet<GTRecipe> toAdd = new HashSet<>();

        deleteCALRecipesAndTags();
        rebuildCircuitAssemblerMap(toRem, toAdd);
        exchangeRecipesInList(toRem, toAdd);
        makeCircuitImprintRecipes();

        toRem = null;
        toAdd = null;
    }

    private static void reAddOriginalRecipes() {
        RecipeMaps.circuitAssemblerRecipes.getBackend()
            .removeRecipes(MODIFIED_CAL_RECIPES);
        ORIGINAL_CAL_RECIPES.forEach(RecipeMaps.circuitAssemblerRecipes::add);
        ORIGINAL_CAL_RECIPES.clear();
        MODIFIED_CAL_RECIPES.clear();
    }

    private static void rebuildCircuitAssemblerMap(HashSet<GTRecipe> toRem, HashSet<GTRecipe> toAdd) {
        reAddOriginalRecipes();
        RecipeMaps.circuitAssemblerRecipes.getAllRecipes()
            .forEach(e -> CircuitImprintLoader.handleCircuitRecipeRebuilding(e, toRem, toAdd));
    }

    private static void handleCircuitRecipeRebuilding(GTRecipe circuitRecipe, HashSet<GTRecipe> toRem,
        HashSet<GTRecipe> toAdd) {
        ItemStack[] outputs = circuitRecipe.mOutputs;
        boolean isOrePass = isCircuitOreDict(outputs[0]);
        String unlocalizedName = outputs[0].getUnlocalizedName();
        if (isOrePass || unlocalizedName.contains("Circuit") || unlocalizedName.contains("circuit")) {

            CircuitImprintLoader.recipeTagMap
                .put(CircuitImprintLoader.getTagFromStack(outputs[0]), circuitRecipe.copy());

            Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
                ? FluidRegistry.getFluid("molten.indalloy140")
                : FluidRegistry.getFluid("molten.solderingalloy");

            Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
                ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
                : FluidRegistry.getFluid("molten.solderingalloy");

            if (circuitRecipe.mFluidInputs[0].isFluidEqual(Materials.SolderingAlloy.getMolten(0))
                || circuitRecipe.mFluidInputs[0].isFluidEqual(new FluidStack(solderIndalloy, 0))
                || circuitRecipe.mFluidInputs[0].isFluidEqual(new FluidStack(solderUEV, 0))) {
                GTRecipe newRecipe = CircuitImprintLoader.reBuildRecipe(circuitRecipe);
                if (newRecipe != null) BartWorksRecipeMaps.circuitAssemblyLineRecipes.addRecipe(newRecipe);
                addCutoffRecipeToSets(toRem, toAdd, circuitRecipe);
            } else if (circuitRecipe.mEUt > BWUtil.getTierVoltage(ConfigHandler.cutoffTier)) toRem.add(circuitRecipe);
        }
    }

    private static boolean isCircuitOreDict(ItemStack item) {
        return BWUtil.isTieredCircuit(item) || BWUtil.getOreNames(item)
            .stream()
            .anyMatch(s -> "circuitPrimitiveArray".equals(s));
    }

    private static void exchangeRecipesInList(HashSet<GTRecipe> toRem, HashSet<GTRecipe> toAdd) {
        toAdd.forEach(RecipeMaps.circuitAssemblerRecipes::add);
        RecipeMaps.circuitAssemblerRecipes.getBackend()
            .removeRecipes(toRem);
        ORIGINAL_CAL_RECIPES.addAll(toRem);
        MODIFIED_CAL_RECIPES.addAll(toAdd);
    }

    private static void addCutoffRecipeToSets(HashSet<GTRecipe> toRem, HashSet<GTRecipe> toAdd,
        GTRecipe circuitRecipe) {
        if (circuitRecipe.mEUt > BWUtil.getTierVoltage(ConfigHandler.cutoffTier)) {
            toRem.add(circuitRecipe);
            toAdd.add(CircuitImprintLoader.makeMoreExpensive(circuitRecipe));
        }
    }

    @SuppressWarnings("deprecation")
    public static GTRecipe makeMoreExpensive(GTRecipe original) {
        GTRecipe newRecipe = original.copy();
        for (ItemStack is : newRecipe.mInputs) {
            if (!BWUtil.isTieredCircuit(is)) {
                is.stackSize = Math.min(is.stackSize * 6, 64);
                if (is.stackSize > is.getItem()
                    .getItemStackLimit() || is.stackSize > is.getMaxStackSize()) is.stackSize = is.getMaxStackSize();
            }
        }
        newRecipe.mFluidInputs[0].amount *= 4;
        newRecipe.mDuration *= 4;
        return newRecipe;
    }

    public static GTRecipe reBuildRecipe(GTRecipe original) {
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

        return new GTRecipe(
            false,
            in,
            new ItemStack[] { getOutputMultiplied(original) },
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(CircuitImprintLoader.getTagFromStack(original.mOutputs[0]), 0, 0),
            null,
            original.mFluidInputs,
            null,
            original.mDuration * 12,
            original.mEUt,
            0);
    }

    private static ItemStack getOutputMultiplied(GTRecipe original) {
        ItemStack out = original.copy()
            .getOutput(0);
        out.stackSize *= 16;
        return out;
    }

    private static void replaceCircuits(BiMap<ItemList, Short> inversed, GTRecipe original, ItemStack[] in, int index) {
        for (ItemList il : inversed.keySet()) {
            if (GTUtility.areStacksEqual(il.get(1), replaceCircuitParts(original.mInputs[index]))) {
                in[index] = BWMetaItems.getCircuitParts()
                    .getStack(inversed.get(il), original.mInputs[index].stackSize);
            }
        }
    }

    private static final List<Pair<ItemStack, ItemStack>> circuitPartsToReplace = Collections.unmodifiableList(
        Arrays.asList(
            new Pair<>(ItemList.Circuit_Parts_Resistor.get(1), ItemList.Circuit_Parts_ResistorSMD.get(1)),
            new Pair<>(ItemList.Circuit_Parts_Diode.get(1), ItemList.Circuit_Parts_DiodeSMD.get(1)),
            new Pair<>(ItemList.Circuit_Parts_Transistor.get(1), ItemList.Circuit_Parts_TransistorSMD.get(1)),
            new Pair<>(ItemList.Circuit_Parts_Capacitor.get(1), ItemList.Circuit_Parts_CapacitorSMD.get(1)),
            new Pair<>(ItemList.Circuit_Parts_Coil.get(1), ItemList.Circuit_Parts_InductorSMD.get(1))));

    private static ItemStack replaceCircuitParts(ItemStack stack) {
        for (Pair<ItemStack, ItemStack> pair : circuitPartsToReplace) {
            if (GTUtility.areStacksEqual(pair.getKey(), stack)) {
                ItemStack newStack = pair.getValue();
                newStack.stackSize = stack.stackSize;
                return newStack;
            }
        }
        return stack;
    }

    @SuppressWarnings("deprecation")
    private static void replaceComponents(ItemStack[] in, GTRecipe original, int index)
        throws ArrayIndexOutOfBoundsException {
        if (original.mInputs[index] != null && in[index] == null) {
            // big wires
            if (BWUtil.checkStackAndPrefix(original.mInputs[index])
                && GTOreDictUnificator.getAssociation(original.mInputs[index]).mPrefix == OrePrefixes.wireGt01) {
                in[index] = GTOreDictUnificator.get(
                    OrePrefixes.wireGt16,
                    GTOreDictUnificator.getAssociation(original.mInputs[index]).mMaterial.mMaterial,
                    original.mInputs[index].stackSize);
                // fine wires
            } else if (BWUtil.checkStackAndPrefix(original.mInputs[index])
                && GTOreDictUnificator.getAssociation(original.mInputs[index]).mPrefix == OrePrefixes.wireFine) {
                    in[index] = GTOreDictUnificator.get(
                        OrePrefixes.wireGt04,
                        GTOreDictUnificator.getAssociation(original.mInputs[index]).mMaterial.mMaterial,
                        original.mInputs[index].stackSize);
                    if (in[index] == null) {
                        in[index] = GTOreDictUnificator.get(
                            OrePrefixes.wireFine,
                            GTOreDictUnificator.getAssociation(original.mInputs[index]).mMaterial.mMaterial,
                            original.mInputs[index].stackSize * 16);
                    }
                    // other components
                } else {
                    in[index] = original.mInputs[index].copy();
                    in[index].stackSize *= 16;
                    if (in[index].stackSize > in[index].getItem()
                        .getItemStackLimit() || in[index].stackSize > in[index].getMaxStackSize())
                        in[index].stackSize = in[index].getMaxStackSize();
                }
        }
    }

    private static void makeCircuitImprintRecipes() {
        removeOldRecipesFromRegistries();
        CircuitImprintLoader.recipeTagMap.keySet()
            .forEach(e -> {
                makeAndAddSlicingRecipe(e);
                makeAndAddCraftingRecipes(e);
            });
    }

    private static boolean checkForBlacklistedComponents(ItemStack[] itemStacks) {
        for (ItemStack is : itemStacks) {
            for (ItemStack is2 : CircuitImprintLoader.blacklistSet) {
                if (GTUtility.areStacksEqual(is, is2)) return true;
            }
        }
        return false;
    }

    private static void removeOldRecipesFromRegistries() {
        recipeWorldCache.forEach(
            CraftingManager.getInstance()
                .getRecipeList()::remove);
        BWCoreStaticReplacementMethodes.clearRecentlyUsedRecipes();
        RecipeMaps.slicerRecipes.getBackend()
            .removeRecipes(gtrecipeWorldCache);
        recipeWorldCache.forEach(r -> {
            try {
                BWUtil.getGTBufferedRecipeList()
                    .remove(r);
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

        for (GTRecipe recipe : CircuitImprintLoader.recipeTagMap.get(tag)) {
            eut = Math.min(eut, recipe.mEUt);
        }

        eut = Math.min(
            eut,
            BWUtil.getMachineVoltageFromTier(
                BWUtil.getCircuitTierFromOreDictName(
                    OreDictionary.getOreName(
                        OreDictionary.getOreIDs(stack) != null && OreDictionary.getOreIDs(stack).length > 0
                            ? OreDictionary.getOreIDs(stack)[0]
                            : -1))));
        GTRecipe slicingRecipe = new GTRecipe(
            true,
            new ItemStack[] { stack, ItemList.Shape_Slicer_Flat.get(0) },
            new ItemStack[] { BWMetaItems.getCircuitParts()
                .getStackWithNBT(tag, 1, 1) },
            null,
            null,
            null,
            null,
            300,
            eut,
            BWUtil.CLEANROOM);
        gtrecipeWorldCache.add(slicingRecipe);
        RecipeMaps.slicerRecipes.add(slicingRecipe);
    }

    private static void makeAndAddCraftingRecipes(NBTTagCompound tag) {
        ItemStack circuit = BWMetaItems.getCircuitParts()
            .getStackWithNBT(tag, 0, 1);
        Object[] imprintRecipe = { " X ", "GPG", " X ", 'P', BWMetaItems.getCircuitParts()
            .getStackWithNBT(tag, 1, 1), 'G', WerkstoffLoader.Prasiolite.get(OrePrefixes.gemExquisite, 1), 'X',
            BWMetaItems.getCircuitParts()
                .getStack(3) };

        IRecipe bwrecipe = new BWNBTDependantCraftingRecipe(circuit, imprintRecipe);
        ShapedOreRecipe gtrecipe = BWUtil.createGTCraftingRecipe(
            circuit,
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.KEEPNBT
                | GTModHandler.RecipeBits.BUFFERED,
            imprintRecipe);

        // Adds the actual recipe
        recipeWorldCache.add(bwrecipe);
        GameRegistry.addRecipe(bwrecipe);
        // Adds the NEI visual recipe
        recipeWorldCache.add(gtrecipe);
        GameRegistry.addRecipe(gtrecipe);
    }

    public static NBTTagCompound getTagFromStack(ItemStack stack) {
        if (GTUtility.isStackValid(stack)) return BWUtil.setStackSize(stack.copy(), 1)
            .writeToNBT(new NBTTagCompound());
        return new NBTTagCompound();
    }

    public static ItemStack getStackFromTag(NBTTagCompound tagCompound) {
        return ItemStack.loadItemStackFromNBT(tagCompound);
    }

    private static void deleteCALRecipesAndTags() {
        BartWorksRecipeMaps.circuitAssemblyLineRecipes.getBackend()
            .clearRecipes();
        recipeTagMap.clear();
    }
}
