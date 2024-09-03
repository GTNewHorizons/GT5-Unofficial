package gtnhlanth.loader;

import static gregtech.api.enums.OrePrefixes.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import bartworks.system.material.Werkstoff;
import bartworks.util.BWUtil;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipe.RecipeAssemblyLine;
import gregtech.api.util.GTShapedRecipe;
import gregtech.api.util.GTUtility;
import gtnhlanth.common.register.WerkstoffMaterialPool;

public class ZPMRubberChanges implements Runnable {

    @SuppressWarnings("unchecked")
    public void run() {

        List<IRecipe> bufferedRecipeList = null;

        try {
            bufferedRecipeList = (List<IRecipe>) FieldUtils
                .getDeclaredField(GTModHandler.class, "sBufferRecipeList", true)
                .get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        HashSet<ItemStack> ZPMPlusComponents = new HashSet<>();
        OrePrefixes[] RubberGenerated = { plate };

        Arrays.stream(ItemList.values())
            .filter(
                item -> (item.toString()
                    .contains("ZPM")
                    || item.toString()
                        .contains("UV")
                    || item.toString()
                        .contains("UHV")
                    || item.toString()
                        .contains("UEV"))
                    && item.hasBeenSet())
            .forEach(item -> ZPMPlusComponents.add(item.get(1)));

        if (Mods.NewHorizonsCoreMod.isModLoaded()) {
            addDreamcraftItemListItems(ZPMPlusComponents);
        }

        for (ItemStack component : ZPMPlusComponents) {
            GTLog.out.print(component.getDisplayName() + " ");
        }

        replaceAllRecipes(ZPMPlusComponents, RubberGenerated, bufferedRecipeList);
    }

    private static void replaceAllRecipes(Collection<ItemStack> ZPMPlusComponents, OrePrefixes[] RubberGenerated,
        List<IRecipe> bufferedRecipeList) {

        for (RecipeAssemblyLine sAssemblylineRecipe : RecipeAssemblyLine.sAssemblylineRecipes) {
            for (ItemStack stack : ZPMPlusComponents) {
                rewriteAsslineRecipes(stack, RubberGenerated, sAssemblylineRecipe);
            }
        }

        for (RecipeMap<?> map : RecipeMap.ALL_RECIPE_MAPS.values()) {
            for (GTRecipe recipe : map.getAllRecipes()) {
                for (ItemStack stack : ZPMPlusComponents) {
                    rewriteMachineRecipes(stack, RubberGenerated, recipe);
                }
            }
        }

        for (ItemStack stack : ZPMPlusComponents) {
            Predicate recipeFilter = obj -> obj instanceof GTShapedRecipe
                && GTUtility.areStacksEqual(((GTShapedRecipe) obj).getRecipeOutput(), stack, true);
            rewriteCraftingRecipes(bufferedRecipeList, RubberGenerated, recipeFilter);
        }
        /*
         * for (ItemStack stack : LuVMachines) { Predicate recipeFilter = obj -> obj instanceof GT_Shaped_Recipe &&
         * GT_Utility.areStacksEqual(((GT_Shaped_Recipe) obj).getRecipeOutput(), stack, true);
         * rewriteCraftingRecipes(bufferedRecipeList, LuVMaterialsGenerated, recipeFilter); }
         */
    }

    private static void addDreamcraftItemListItems(Collection ZPMPlusComponents) {
        try {
            Class customItemListClass = Class.forName("com.dreammaster.gthandler.CustomItemList");
            Method hasnotBeenSet = MethodUtils.getAccessibleMethod(customItemListClass, "hasBeenSet");
            Method get = MethodUtils.getAccessibleMethod(customItemListClass, "get", long.class, Object[].class);
            for (Enum customItemList : (Enum[]) FieldUtils.getField(customItemListClass, "$VALUES", true)
                .get(null)) {
                if ((customItemList.toString()
                    .contains("ZPM")
                    || customItemList.toString()
                        .contains("UV")
                    || customItemList.toString()
                        .contains("UHV")
                    || customItemList.toString()
                        .contains("UEV"))
                    && (boolean) hasnotBeenSet.invoke(customItemList))
                    ZPMPlusComponents.add((ItemStack) get.invoke(customItemList, 1, new Object[0]));
            }
        } catch (IllegalAccessException | ClassNotFoundException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void rewriteCraftingRecipes(List<IRecipe> bufferedRecipeList, OrePrefixes[] RubberGenerated,
        Predicate recipeFilter) {
        for (OrePrefixes prefixes : RubberGenerated) {

            Consumer recipeAction = (obj) -> {
                ZPMRubberChanges.doStacksCointainAndReplace(
                    ((GTShapedRecipe) obj).getInput(),
                    GTOreDictUnificator.get(prefixes, Materials.Silicone, 1),
                    true,
                    WerkstoffMaterialPool.PTMEGElastomer.get(prefixes));
                ZPMRubberChanges.doStacksCointainAndReplace(
                    ((GTShapedRecipe) obj).getInput(),
                    GTOreDictUnificator.get(prefixes, Materials.StyreneButadieneRubber, 1),
                    true,
                    WerkstoffMaterialPool.PTMEGElastomer.get(prefixes));
            };

            /*
             * || ZPMRubberChanges.doStacksCointainAndReplace(((GT_Shaped_Recipe) obj).getInput(),
             * GTOreDictUnificator.get(prefixes, Materials.StyreneButadieneRubber, 1), true,
             * WerkstoffMaterialPool.PTMEGElastomer.get(prefixes));
             */

            CraftingManager.getInstance()
                .getRecipeList()
                .stream()
                .filter(recipeFilter)
                .forEach(recipeAction);
            bufferedRecipeList.stream()
                .filter(recipeFilter)
                .forEach(recipeAction);
        }
    }

    private static void rewriteMachineRecipes(ItemStack stack, OrePrefixes[] RubberGenerated, GTRecipe recipe) {
        if (ZPMRubberChanges.doStacksCointainAndReplace(recipe.mInputs, stack, false)) {
            for (OrePrefixes prefixes : RubberGenerated) {
                ZPMRubberChanges.doStacksCointainAndReplace(
                    recipe.mInputs,
                    GTOreDictUnificator.get(prefixes, Materials.Silicone, 1),
                    true,
                    WerkstoffMaterialPool.PTMEGElastomer.get(prefixes));
                ZPMRubberChanges.doStacksCointainAndReplace(
                    recipe.mOutputs,
                    GTOreDictUnificator.get(prefixes, Materials.Silicon, 1),
                    true,
                    WerkstoffMaterialPool.PTMEGElastomer.get(prefixes));

                ZPMRubberChanges.doStacksCointainAndReplace(
                    recipe.mInputs,
                    GTOreDictUnificator.get(prefixes, Materials.StyreneButadieneRubber, 1),
                    true,
                    WerkstoffMaterialPool.PTMEGElastomer.get(prefixes));
                ZPMRubberChanges.doStacksCointainAndReplace(
                    recipe.mOutputs,
                    GTOreDictUnificator.get(prefixes, Materials.StyreneButadieneRubber, 1),
                    true,
                    WerkstoffMaterialPool.PTMEGElastomer.get(prefixes));
            }
            ZPMRubberChanges.doStacksCointainAndReplace(
                recipe.mFluidInputs,
                Materials.Silicone.getMolten(1),
                true,
                WerkstoffMaterialPool.PTMEGElastomer.getMolten(1)
                    .getFluid());
            ZPMRubberChanges.doStacksCointainAndReplace(
                recipe.mFluidOutputs,
                Materials.Silicone.getMolten(1),
                true,
                WerkstoffMaterialPool.PTMEGElastomer.getMolten(1)
                    .getFluid());

            ZPMRubberChanges.doStacksCointainAndReplace(
                recipe.mFluidInputs,
                Materials.StyreneButadieneRubber.getMolten(1),
                true,
                WerkstoffMaterialPool.PTMEGElastomer.getMolten(1)
                    .getFluid());
            ZPMRubberChanges.doStacksCointainAndReplace(
                recipe.mFluidOutputs,
                Materials.StyreneButadieneRubber.getMolten(1),
                true,
                WerkstoffMaterialPool.PTMEGElastomer.getMolten(1)
                    .getFluid());
        }
        if (ZPMRubberChanges.doStacksCointainAndReplace(recipe.mOutputs, stack, false)) {
            for (OrePrefixes prefixes : RubberGenerated) {
                ZPMRubberChanges.doStacksCointainAndReplace(
                    recipe.mInputs,
                    GTOreDictUnificator.get(prefixes, Materials.Silicone, 1),
                    true,
                    WerkstoffMaterialPool.PTMEGElastomer.get(prefixes));
                ZPMRubberChanges.doStacksCointainAndReplace(
                    recipe.mOutputs,
                    GTOreDictUnificator.get(prefixes, Materials.Silicone, 1),
                    true,
                    WerkstoffMaterialPool.PTMEGElastomer.get(prefixes));

                ZPMRubberChanges.doStacksCointainAndReplace(
                    recipe.mInputs,
                    GTOreDictUnificator.get(prefixes, Materials.StyreneButadieneRubber, 1),
                    true,
                    WerkstoffMaterialPool.PTMEGElastomer.get(prefixes));
                ZPMRubberChanges.doStacksCointainAndReplace(
                    recipe.mOutputs,
                    GTOreDictUnificator.get(prefixes, Materials.StyreneButadieneRubber, 1),
                    true,
                    WerkstoffMaterialPool.PTMEGElastomer.get(prefixes));
            }
            ZPMRubberChanges.doStacksCointainAndReplace(
                recipe.mFluidInputs,
                Materials.Silicone.getMolten(1),
                true,
                WerkstoffMaterialPool.PTMEGElastomer.getMolten(1)
                    .getFluid());
            ZPMRubberChanges.doStacksCointainAndReplace(
                recipe.mFluidOutputs,
                Materials.Silicone.getMolten(1),
                true,
                WerkstoffMaterialPool.PTMEGElastomer.getMolten(1)
                    .getFluid());

            ZPMRubberChanges.doStacksCointainAndReplace(
                recipe.mFluidInputs,
                Materials.StyreneButadieneRubber.getMolten(1),
                true,
                WerkstoffMaterialPool.PTMEGElastomer.getMolten(1)
                    .getFluid());
            ZPMRubberChanges.doStacksCointainAndReplace(
                recipe.mFluidOutputs,
                Materials.StyreneButadieneRubber.getMolten(1),
                true,
                WerkstoffMaterialPool.PTMEGElastomer.getMolten(1)
                    .getFluid());
        }
    }

    private static void rewriteAsslineRecipes(ItemStack stack, OrePrefixes[] RubberGenerated,
        RecipeAssemblyLine recipe) {
        for (OrePrefixes prefixes : RubberGenerated) {
            if (ZPMRubberChanges.doStacksCointainAndReplace(recipe.mInputs, stack, false)) {

                GTLog.out.print(Arrays.toString(recipe.mInputs));

                ZPMRubberChanges.doStacksCointainAndReplace(
                    recipe.mInputs,
                    GTOreDictUnificator.get(prefixes, Materials.Silicone, 1),
                    true,
                    WerkstoffMaterialPool.PTMEGElastomer.get(prefixes));
                ZPMRubberChanges.doStacksCointainAndReplace(
                    new Object[] { recipe.mOutput },
                    GTOreDictUnificator.get(prefixes, Materials.Silicone, 1),
                    true,
                    WerkstoffMaterialPool.PTMEGElastomer.get(prefixes));

                ZPMRubberChanges.doStacksCointainAndReplace(
                    recipe.mInputs,
                    GTOreDictUnificator.get(prefixes, Materials.StyreneButadieneRubber, 1),
                    true,
                    WerkstoffMaterialPool.PTMEGElastomer.get(prefixes));
                ZPMRubberChanges.doStacksCointainAndReplace(
                    new Object[] { recipe.mOutput },
                    GTOreDictUnificator.get(prefixes, Materials.StyreneButadieneRubber, 1),
                    true,
                    WerkstoffMaterialPool.PTMEGElastomer.get(prefixes));
            }
            if (ZPMRubberChanges.doStacksCointainAndReplace(new Object[] { recipe.mOutput }, stack, false)) {
                ZPMRubberChanges.doStacksCointainAndReplace(
                    recipe.mInputs,
                    GTOreDictUnificator.get(prefixes, Materials.Silicone, 1),
                    true,
                    WerkstoffMaterialPool.PTMEGElastomer.get(prefixes));
                ZPMRubberChanges.doStacksCointainAndReplace(
                    new Object[] { recipe.mOutput },
                    GTOreDictUnificator.get(prefixes, Materials.Silicone, 1),
                    true,
                    WerkstoffMaterialPool.PTMEGElastomer.get(prefixes));

                ZPMRubberChanges.doStacksCointainAndReplace(
                    recipe.mInputs,
                    GTOreDictUnificator.get(prefixes, Materials.StyreneButadieneRubber, 1),
                    true,
                    WerkstoffMaterialPool.PTMEGElastomer.get(prefixes));
                ZPMRubberChanges.doStacksCointainAndReplace(
                    new Object[] { recipe.mOutput },
                    GTOreDictUnificator.get(prefixes, Materials.StyreneButadieneRubber, 1),
                    true,
                    WerkstoffMaterialPool.PTMEGElastomer.get(prefixes));
            }
        }
        if (ZPMRubberChanges.doStacksCointainAndReplace(recipe.mInputs, stack, false)) {
            ZPMRubberChanges.doStacksCointainAndReplace(
                recipe.mFluidInputs,
                Materials.Silicone.getMolten(1),
                true,
                WerkstoffMaterialPool.PTMEGElastomer.getMolten(1)
                    .getFluid());

            ZPMRubberChanges.doStacksCointainAndReplace(
                recipe.mFluidInputs,
                Materials.StyreneButadieneRubber.getMolten(1),
                true,
                WerkstoffMaterialPool.PTMEGElastomer.getMolten(1)
                    .getFluid());
        }
        if (ZPMRubberChanges.doStacksCointainAndReplace(new Object[] { recipe.mOutput }, stack, false)) {
            ZPMRubberChanges.doStacksCointainAndReplace(
                recipe.mFluidInputs,
                Materials.StyreneButadieneRubber.getMolten(1),
                true,
                WerkstoffMaterialPool.PTMEGElastomer.getMolten(1)
                    .getFluid());
        }
    }

    private static ItemStack[] replaceArrayWith(ItemStack[] stackArray, Materials source, Werkstoff target) {
        for (int i = 0; i < stackArray.length; i++) {
            ItemStack stack = stackArray[i];
            if (!BWUtil.checkStackAndPrefix(stack)) continue;
            stackArray[i] = replaceStackWith(stack, source, target);
        }
        return stackArray;
    }

    private static ItemStack replaceStackWith(ItemStack stack, Materials source, Werkstoff target) {
        ItemData ass = GTOreDictUnificator.getAssociation(stack);
        if (ass.mMaterial.mMaterial.equals(source))
            if (target.hasItemType(ass.mPrefix)) stack = target.get(ass.mPrefix, stack.stackSize);
        return stack;
    }

    private static boolean doStacksCointainAndReplace(FluidStack[] stacks, FluidStack stack, boolean replace,
        Fluid... replacement) {
        boolean replaced = false;
        for (int i = 0; i < stacks.length; i++) {
            if (GTUtility.areFluidsEqual(stack, stacks[i])) if (!replace) return true;
            else {
                int amount = stacks[i].amount;
                stacks[i] = new FluidStack(replacement[0], amount);
                replaced = true;
            }
        }
        return replaced;
    }

    private static boolean doStacksCointainAndReplace(Object[] stacks, ItemStack stack, boolean replace,
        ItemStack... replacement) {
        // GTLog.out.print("In doStacksCointainAndReplace!\n");
        boolean replaced = false;
        for (int i = 0; i < stacks.length; i++) {
            if (!GTUtility.isStackValid(stacks[i])) {
                if (stacks[i] instanceof ArrayList && ((ArrayList) stacks[i]).size() > 0) {
                    if (GTUtility.areStacksEqual(stack, (ItemStack) ((ArrayList) stacks[i]).get(0), true))
                        if (!replace) return true;
                        else {
                            int amount = ((ItemStack) ((ArrayList) stacks[i]).get(0)).stackSize;
                            stacks[i] = new ArrayList<>();
                            ((ArrayList) stacks[i]).add(BWUtil.setStackSize(replacement[0], amount));
                            replaced = true;

                            GTLog.out.print("Replaced recipe!: " + stack.getDisplayName() + " ");
                        }

                } else continue;
            } else if (GTUtility.areStacksEqual(stack, (ItemStack) stacks[i], true)) if (!replace) return true;
            else {
                int amount = ((ItemStack) stacks[i]).stackSize;
                stacks[i] = BWUtil.setStackSize(replacement[0], amount);
                replaced = true;
            }
        }
        return replaced;
    }
}
