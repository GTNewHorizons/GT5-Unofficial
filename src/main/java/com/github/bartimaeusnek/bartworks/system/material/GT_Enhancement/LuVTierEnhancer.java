/*
 * Copyright (c) 2018-2019 bartimaeusnek
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

package com.github.bartimaeusnek.bartworks.system.material.GT_Enhancement;

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.system.material.processingLoaders.AfterLuVTierEnhacement;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import cpw.mods.fml.common.Loader;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static gregtech.api.enums.OrePrefixes.*;

@SuppressWarnings("ALL")
public class LuVTierEnhancer implements Runnable {

    public void run() {

        List<IRecipe> bufferedRecipeList = null;

        try {
            bufferedRecipeList = (List<IRecipe>) FieldUtils.getDeclaredField(GT_ModHandler.class, "sBufferRecipeList", true).get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        HashSet<ItemStack> LuVMachines = new HashSet<>();
        LuVMachines.add(ItemRegistry.cal);
        OrePrefixes[] LuVMaterialsGenerated = {dust, ingot, plate, stick, stickLong, rotor, plateDouble, plateDense};

        Arrays.stream(ItemList.values())
                .filter(item -> item.toString().contains("LuV") && item.hasBeenSet())
                    .forEach(item -> LuVMachines.add(item.get(1)));

        if (Loader.isModLoaded("dreamcraft")) {
            addDreamcraftItemListItems(LuVMachines);
        }

        GT_ModHandler.addCraftingRecipe(ItemList.Casing_LuV.get(1),
                GT_ModHandler.RecipeBits.BUFFERED |
                         GT_ModHandler.RecipeBits.REVERSIBLE |
                         GT_ModHandler.RecipeBits.NOT_REMOVABLE |
                         GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_RECIPES,
                new Object[]{
                        "PPP",
                        "PwP",
                        "PPP",
                        'P', WerkstoffLoader.LuVTierMaterial.get(plate)
                });

        replaceAllRecipes(LuVMachines,LuVMaterialsGenerated,bufferedRecipeList);

        AfterLuVTierEnhacement.run();
    }

    private static void replaceAllRecipes(Collection<ItemStack> LuVMachines, OrePrefixes[] LuVMaterialsGenerated, List<IRecipe> bufferedRecipeList){
        LuVTierEnhancer.replaceOsmiridiumInLuVRecipes();
        LuVMachines.stream().forEach(stack -> {

                    Predicate recipeFilter = obj -> obj instanceof GT_Shaped_Recipe && GT_Utility.areStacksEqual(((GT_Shaped_Recipe) obj).getRecipeOutput(), stack, true);

                    GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes.forEach(
                            recipe -> rewriteAsslineRecipes(stack, LuVMaterialsGenerated, recipe));

                    GT_Recipe.GT_Recipe_Map.sMappings.forEach(
                            map -> map.mRecipeList.forEach(
                                    recipe -> rewriteMachineRecipes(stack, LuVMaterialsGenerated, recipe)));

                    rewriteCraftingRecipes(bufferedRecipeList, LuVMaterialsGenerated, recipeFilter);
                }
        );
    }

    private static void addDreamcraftItemListItems(Collection LuVMachines){
        try {
            Class customItemListClass = Class.forName("com.dreammaster.gthandler.CustomItemList");
            Method hasnotBeenSet = MethodUtils.getAccessibleMethod(customItemListClass, "hasBeenSet");
            Method get = MethodUtils.getAccessibleMethod(customItemListClass, "get", long.class, Object[].class);
            for (Enum customItemList : (Enum[]) FieldUtils.getField(customItemListClass, "$VALUES", true).get(null)) {
                if (customItemList.toString().contains("LuV") && (boolean) hasnotBeenSet.invoke(customItemList))
                    LuVMachines.add((ItemStack) get.invoke(customItemList, 1, new Object[0]));
            }
        } catch (IllegalAccessException | ClassNotFoundException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void rewriteCraftingRecipes(List<IRecipe> bufferedRecipeList, OrePrefixes[] LuVMaterialsGenerated, Predicate recipeFilter){
        for (OrePrefixes prefixes : LuVMaterialsGenerated) {

            Consumer recipeAction = obj -> LuVTierEnhancer.doStacksCointainAndReplace(((GT_Shaped_Recipe) obj).getInput(),
                    GT_OreDictUnificator.get(prefixes, Materials.Chrome, 1), true,
                    WerkstoffLoader.LuVTierMaterial.get(prefixes));

            CraftingManager.getInstance().getRecipeList().stream().filter(recipeFilter).forEach(recipeAction);
            bufferedRecipeList.stream().filter(recipeFilter).forEach(recipeAction);
        }
    }

    private static void rewriteMachineRecipes(ItemStack stack, OrePrefixes[] LuVMaterialsGenerated, GT_Recipe recipe){
            for (OrePrefixes prefixes : LuVMaterialsGenerated) {
                if (LuVTierEnhancer.doStacksCointainAndReplace(recipe.mInputs, stack, false)) {
                    LuVTierEnhancer.doStacksCointainAndReplace(recipe.mInputs, GT_OreDictUnificator.get(prefixes, Materials.Chrome, 1), true, WerkstoffLoader.LuVTierMaterial.get(prefixes));
                    LuVTierEnhancer.doStacksCointainAndReplace(recipe.mOutputs, GT_OreDictUnificator.get(prefixes, Materials.Chrome, 1), true, WerkstoffLoader.LuVTierMaterial.get(prefixes));
                }
                if (LuVTierEnhancer.doStacksCointainAndReplace(recipe.mOutputs, stack, false)) {
                    LuVTierEnhancer.doStacksCointainAndReplace(recipe.mInputs, GT_OreDictUnificator.get(prefixes, Materials.Chrome, 1), true, WerkstoffLoader.LuVTierMaterial.get(prefixes));
                    LuVTierEnhancer.doStacksCointainAndReplace(recipe.mOutputs, GT_OreDictUnificator.get(prefixes, Materials.Chrome, 1), true, WerkstoffLoader.LuVTierMaterial.get(prefixes));
                }
            }
            if (LuVTierEnhancer.doStacksCointainAndReplace(recipe.mInputs, stack, false)) {
                LuVTierEnhancer.doStacksCointainAndReplace(recipe.mFluidInputs, Materials.Chrome.getMolten(1), true, WerkstoffLoader.LuVTierMaterial.getMolten(1).getFluid());
                LuVTierEnhancer.doStacksCointainAndReplace(recipe.mFluidOutputs, Materials.Chrome.getMolten(1), true, WerkstoffLoader.LuVTierMaterial.getMolten(1).getFluid());
            }
            if (LuVTierEnhancer.doStacksCointainAndReplace(recipe.mOutputs, stack, false)) {
                LuVTierEnhancer.doStacksCointainAndReplace(recipe.mFluidInputs, Materials.Chrome.getMolten(1), true, WerkstoffLoader.LuVTierMaterial.getMolten(1).getFluid());
                LuVTierEnhancer.doStacksCointainAndReplace(recipe.mFluidOutputs, Materials.Chrome.getMolten(1), true, WerkstoffLoader.LuVTierMaterial.getMolten(1).getFluid());
            }
    }
    private static void rewriteAsslineRecipes(ItemStack stack, OrePrefixes[] LuVMaterialsGenerated, GT_Recipe.GT_Recipe_AssemblyLine recipe){
        for (OrePrefixes prefixes : LuVMaterialsGenerated) {
            if (LuVTierEnhancer.doStacksCointainAndReplace(recipe.mInputs, stack, false)) {
                LuVTierEnhancer.doStacksCointainAndReplace(recipe.mInputs, GT_OreDictUnificator.get(prefixes, Materials.Chrome, 1), true, WerkstoffLoader.LuVTierMaterial.get(prefixes));
                LuVTierEnhancer.doStacksCointainAndReplace(new Object[]{recipe.mOutput}, GT_OreDictUnificator.get(prefixes, Materials.Chrome, 1), true, WerkstoffLoader.LuVTierMaterial.get(prefixes));
            }
            if (LuVTierEnhancer.doStacksCointainAndReplace(new Object[]{recipe.mOutput}, stack, false)) {
                LuVTierEnhancer.doStacksCointainAndReplace(recipe.mInputs, GT_OreDictUnificator.get(prefixes, Materials.Chrome, 1), true, WerkstoffLoader.LuVTierMaterial.get(prefixes));
                LuVTierEnhancer.doStacksCointainAndReplace(new Object[]{recipe.mOutput}, GT_OreDictUnificator.get(prefixes, Materials.Chrome, 1), true, WerkstoffLoader.LuVTierMaterial.get(prefixes));
            }
        }
        if (LuVTierEnhancer.doStacksCointainAndReplace(recipe.mInputs, stack, false)) {
            LuVTierEnhancer.doStacksCointainAndReplace(recipe.mFluidInputs, Materials.Chrome.getMolten(1), true, WerkstoffLoader.LuVTierMaterial.getMolten(1).getFluid());
        }
        if (LuVTierEnhancer.doStacksCointainAndReplace(new Object[]{recipe.mOutput}, stack, false)) {
            LuVTierEnhancer.doStacksCointainAndReplace(recipe.mFluidInputs, Materials.Chrome.getMolten(1), true, WerkstoffLoader.LuVTierMaterial.getMolten(1).getFluid());
        }
    }

    private static void replaceOsmiridiumInLuVRecipes(){
        for (GT_Recipe.GT_Recipe_AssemblyLine recipe_assemblyLine : GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes){
            if (recipe_assemblyLine.mEUt > 6000)
                continue;
            for (int i = 0; i < recipe_assemblyLine.mInputs.length; i++) {
                ItemStack stack = recipe_assemblyLine.mInputs[i];
                if (!BW_Util.checkStackAndPrefix(stack))
                    continue;
                ItemData ass = GT_OreDictUnificator.getAssociation(stack);
                if (ass.mMaterial.mMaterial.equals(Materials.Osmiridium))
                    if (WerkstoffLoader.items.get(ass.mPrefix) != null)
                        recipe_assemblyLine.mInputs[i] = WerkstoffLoader.Ruridit.get(ass.mPrefix,stack.stackSize);
            }
        }
        for (GT_Recipe recipe_assemblyLine : GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.mRecipeList){
            if (recipe_assemblyLine.mEUt > BW_Util.getTierVoltage(6))
                continue;
            if (BW_Util.checkStackAndPrefix(recipe_assemblyLine.mOutputs[0]))
                continue;
            for (int i = 0; i < recipe_assemblyLine.mInputs.length; i++) {
                ItemStack stack = recipe_assemblyLine.mInputs[i];
                if (!BW_Util.checkStackAndPrefix(stack))
                    continue;
                ItemData ass = GT_OreDictUnificator.getAssociation(stack);
                if (ass.mMaterial.mMaterial.equals(Materials.Osmiridium))
                    if (WerkstoffLoader.items.get(ass.mPrefix) != null)
                        recipe_assemblyLine.mInputs[i] = WerkstoffLoader.Ruridit.get(ass.mPrefix, stack.stackSize);
            }
        }
        for (GT_Recipe recipe_assemblyLine : GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes.mRecipeList){
            if (recipe_assemblyLine.mEUt > 6000)
                continue;
            for (int i = 0; i < recipe_assemblyLine.mInputs.length; i++) {
                ItemStack stack = recipe_assemblyLine.mInputs[i];
                if (!BW_Util.checkStackAndPrefix(stack))
                    continue;
                ItemData ass = GT_OreDictUnificator.getAssociation(stack);
                if (ass.mMaterial.mMaterial.equals(Materials.Osmiridium))
                    if (WerkstoffLoader.items.get(ass.mPrefix) != null)
                        recipe_assemblyLine.mInputs[i] = WerkstoffLoader.Ruridit.get(ass.mPrefix,stack.stackSize);
            }
        }
    }

    private static boolean doStacksCointainAndReplace(FluidStack[] stacks, FluidStack stack, boolean replace, Fluid... replacement) {
        boolean replaced = false;
        for (int i = 0; i < stacks.length; i++) {
            if (GT_Utility.areFluidsEqual(stack, stacks[i]))
                if (!replace)
                    return true;
                else {
                    int amount = stacks[i].amount;
                    stacks[i] = new FluidStack(replacement[0], amount);
                    replaced = true;
                }
        }
        return replaced;
    }

    private static boolean doStacksCointainAndReplace(Object[] stacks, ItemStack stack, boolean replace, ItemStack... replacement) {
        boolean replaced = false;
        for (int i = 0; i < stacks.length; i++) {
            if (!GT_Utility.isStackValid(stacks[i])) {
                if (stacks[i] instanceof ArrayList && ((ArrayList)stacks[i]).size() > 0) {
                    if (GT_Utility.areStacksEqual(stack, (ItemStack) ((ArrayList)stacks[i]).get(0), true))
                        if (!replace)
                            return true;
                        else {
                            int amount = ((ItemStack) ((ArrayList)stacks[i]).get(0)).stackSize;
                            stacks[i] = new ArrayList<>();
                            ((ArrayList)stacks[i]).add(replacement[0].splitStack(amount));
                            replaced = true;
                        }

                } else
                    continue;
            } else if (GT_Utility.areStacksEqual(stack, (ItemStack) stacks[i], true))
                if (!replace)
                    return true;
                else {
                    int amount = ((ItemStack) stacks[i]).stackSize;
                    stacks[i] = replacement[0].splitStack(amount);
                    replaced = true;
                }
        }
        return replaced;
    }
}
