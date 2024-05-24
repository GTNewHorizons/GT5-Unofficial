package com.github.technus.tectech.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.CustomItemList;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_AssemblyLine;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_RecipeAdder;

public class TT_recipeAdder extends GT_RecipeAdder {

    public static final ItemStack[] nullItem = new ItemStack[0];
    public static final FluidStack[] nullFluid = new FluidStack[0];

    public static boolean addResearchableAssemblylineRecipe(ItemStack aResearchItem, int totalComputationRequired,
        int computationRequiredPerSec, int researchEUt, int researchAmperage, ItemStack[] aInputs,
        FluidStack[] aFluidInputs, ItemStack aOutput, int assDuration, int assEUt) {
        if (aInputs == null) {
            aInputs = nullItem;
        }
        if (aFluidInputs == null) {
            aFluidInputs = nullFluid;
        }
        if (aResearchItem == null || totalComputationRequired <= 0 || aOutput == null || aInputs.length > 16) {
            return false;
        }
        for (ItemStack tItem : aInputs) {
            if (tItem == null) {
                TecTech.LOGGER.error(
                    "addResearchableAssemblingLineRecipe " + aResearchItem.getDisplayName()
                        + " --> "
                        + aOutput.getUnlocalizedName()
                        + " there is some null item in that recipe");
            }
        }
        researchAmperage = GT_Utility.clamp(researchAmperage, 1, Short.MAX_VALUE);
        computationRequiredPerSec = GT_Utility.clamp(computationRequiredPerSec, 1, Short.MAX_VALUE);
        TecTechRecipeMaps.researchStationFakeRecipes.addFakeRecipe(
            false,
            new ItemStack[] { aResearchItem },
            new ItemStack[] { aOutput },
            new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Writes Research result") },
            null,
            null,
            totalComputationRequired,
            researchEUt,
            researchAmperage | computationRequiredPerSec << 16);
        RecipeMaps.assemblylineVisualRecipes.addFakeRecipe(
            false,
            aInputs,
            new ItemStack[] { aOutput },
            new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Reads Research result") },
            aFluidInputs,
            null,
            assDuration,
            assEUt,
            0,
            false);
        GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes.add(
            new GT_Recipe.GT_Recipe_AssemblyLine(
                CustomItemList.UnusedStuff.get(1),
                totalComputationRequired / computationRequiredPerSec,
                aInputs,
                aFluidInputs,
                aOutput,
                assDuration,
                assEUt));
        TecTechRecipeMaps.researchableALRecipeList.add(
            new GT_Recipe.GT_Recipe_AssemblyLine(
                aResearchItem,
                totalComputationRequired / computationRequiredPerSec,
                aInputs,
                aFluidInputs,
                aOutput,
                assDuration,
                assEUt));
        return true;
    }

    public static boolean addResearchableAssemblylineRecipe(ItemStack aResearchItem, int totalComputationRequired,
        int computationRequiredPerSec, int researchEUt, int researchAmperage, Object[] aInputs,
        FluidStack[] aFluidInputs, ItemStack aOutput, int assDuration, int assEUt) {
        if (aInputs == null) {
            aInputs = nullItem;
        }
        if (aFluidInputs == null) {
            aFluidInputs = nullFluid;
        }
        if (aResearchItem == null || totalComputationRequired <= 0
            || aOutput == null
            || aInputs.length > 16
            || aFluidInputs.length > 4
            || assDuration <= 0
            || assEUt <= 0) {
            return false;
        }

        ItemStack[] tInputs = new ItemStack[aInputs.length];
        ItemStack[][] tAlts = new ItemStack[aInputs.length][];
        int tPersistentHash = 1;
        for (int i = 0; i < aInputs.length; i++) {
            Object obj = aInputs[i];
            if (obj instanceof ItemStack) {
                tInputs[i] = (ItemStack) obj;
                tAlts[i] = null;
                tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(tInputs[i], true, false);
                continue;
            } else if (obj instanceof ItemStack[]aStacks) {
                if (aStacks.length > 0) {
                    tInputs[i] = aStacks[0];
                    tAlts[i] = Arrays.copyOf(aStacks, aStacks.length);
                    for (ItemStack tAlt : tAlts[i]) {
                        tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(tAlt, true, false);
                    }
                    tPersistentHash *= 31;
                    continue;
                }
            } else if (obj instanceof Object[]objs) {
                List<ItemStack> tList;
                if (objs.length >= 2 && !(tList = GT_OreDictUnificator.getOres(objs[0])).isEmpty()) {
                    try {
                        // sort the output, so the hash code is stable across launches
                        tList.sort(
                            Comparator
                                .<ItemStack, String>comparing(
                                    s -> GameRegistry.findUniqueIdentifierFor(s.getItem()).modId)
                                .thenComparing(s -> GameRegistry.findUniqueIdentifierFor(s.getItem()).modId)
                                .thenComparingInt(Items.feather::getDamage)
                                .thenComparingInt(s -> s.stackSize));
                        int tAmount = ((Number) objs[1]).intValue();
                        List<ItemStack> uList = new ArrayList<>();
                        for (ItemStack tStack : tList) {
                            ItemStack uStack = GT_Utility.copyAmount(tAmount, tStack);
                            if (GT_Utility.isStackValid(uStack)) {
                                uList.add(uStack);
                                if (tInputs[i] == null) tInputs[i] = uStack;
                            }
                        }
                        tAlts[i] = uList.toArray(new ItemStack[0]);
                        tPersistentHash = tPersistentHash * 31 + (objs[0] == null ? "" : objs[0].toString()).hashCode();
                        tPersistentHash = tPersistentHash * 31 + tAmount;
                        continue;
                    } catch (Exception t) {
                        TecTech.LOGGER.error(
                            "addAssemblingLineRecipe " + aResearchItem.getDisplayName()
                                + " --> there is some ... in that recipe");
                    }
                }
            }
            TecTech.LOGGER.error(
                "addAssemblingLineRecipe " + aResearchItem.getDisplayName()
                    + " --> "
                    + aOutput.getUnlocalizedName()
                    + " there is some null item in that recipe");
        }
        tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(aResearchItem, true, false);
        tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(aOutput, true, false);
        for (FluidStack tFluidInput : aFluidInputs) {
            if (tFluidInput == null) continue;
            tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(tFluidInput, true, false);
        }
        researchAmperage = GT_Utility.clamp(researchAmperage, 1, Short.MAX_VALUE);
        computationRequiredPerSec = GT_Utility.clamp(computationRequiredPerSec, 1, Short.MAX_VALUE);
        tPersistentHash = tPersistentHash * 31 + totalComputationRequired;
        tPersistentHash = tPersistentHash * 31 + computationRequiredPerSec;
        tPersistentHash = tPersistentHash * 31 + researchAmperage;
        tPersistentHash = tPersistentHash * 31 + researchEUt;
        tPersistentHash = tPersistentHash * 31 + assDuration;
        tPersistentHash = tPersistentHash * 31 + assEUt;
        TecTechRecipeMaps.researchStationFakeRecipes.addFakeRecipe(
            false,
            new ItemStack[] { aResearchItem },
            new ItemStack[] { aOutput },
            new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Writes Research result") },
            null,
            null,
            totalComputationRequired,
            researchEUt,
            researchAmperage | computationRequiredPerSec << 16);
        RecipeMaps.assemblylineVisualRecipes.addFakeRecipe(
            false,
            tInputs,
            new ItemStack[] { aOutput },
            new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Reads Research result") },
            aFluidInputs,
            null,
            assDuration,
            assEUt,
            0,
            tAlts,
            false);
        GT_Recipe_AssemblyLine recipeGT = new GT_Recipe_AssemblyLine(
            CustomItemList.UnusedStuff.get(1),
            totalComputationRequired / computationRequiredPerSec,
            tInputs,
            aFluidInputs,
            aOutput,
            assDuration,
            assEUt,
            tAlts);
        recipeGT.setPersistentHash(tPersistentHash);
        GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes.add(recipeGT);
        GT_Recipe_AssemblyLine recipeTT = new GT_Recipe_AssemblyLine(
            aResearchItem,
            totalComputationRequired / computationRequiredPerSec,
            tInputs,
            aFluidInputs,
            aOutput,
            assDuration,
            assEUt,
            tAlts);
        recipeTT.setPersistentHash(tPersistentHash);
        TecTechRecipeMaps.researchableALRecipeList.add(recipeTT);
        return true;
    }

    public static boolean addFOGPlasmaRecipe(ItemStack[] itemInputs, FluidStack[] fluidOutputs, int machineDuration,
        int machineEUt, boolean multiStep, int recipeTier) {
        if (itemInputs == null) {
            itemInputs = nullItem;
        }

        TecTechRecipeMaps.godforgePlasmaRecipes
            .addRecipe(false, itemInputs, null, multiStep, null, fluidOutputs, machineDuration, machineEUt, recipeTier);
        return true;
    }

    public static boolean addFOGPlasmaRecipe(FluidStack[] fluidInputs, FluidStack[] fluidOutputs, int machineDuration,
        int machineEUt, boolean multiStep, int recipeTier) {

        if (fluidInputs == null) {
            fluidInputs = nullFluid;
        }

        TecTechRecipeMaps.godforgePlasmaRecipes.addRecipe(
            false,
            null,
            null,
            multiStep,
            fluidInputs,
            fluidOutputs,
            machineDuration,
            machineEUt,
            recipeTier);
        return true;
    }

    public static boolean addFOGExoticFakeRecipe(ItemStack[] itemInputs, FluidStack[] fluidInputs,
        FluidStack[] fluidOutputs, int machineDuration, int machineEUt, int recipeTier) {

        TecTechRecipeMaps.godforgeExoticMatterRecipes.addFakeRecipe(
            false,
            itemInputs,
            null,
            null,
            fluidInputs,
            fluidOutputs,
            machineDuration,
            machineEUt,
            recipeTier,
            false);
        return true;
    }
}
