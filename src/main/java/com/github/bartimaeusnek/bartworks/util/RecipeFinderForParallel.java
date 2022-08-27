package com.github.bartimaeusnek.bartworks.util;

import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Handle the parallel more efficient.
 *
 * @author GlodBlock
 */
public class RecipeFinderForParallel {

    /**
     * This method is used for mega multis which have extremely high parallel.
     * Never use it for non parallel machines, it will have worse performance.
     * It will auto consume the inputs.
     *
     * @param   aRecipe         The target recipe that you want calculate the parallel
     * @param   aFluidInputs    The input fluid from machine
     * @param   aItemStacks     The input item from machine
     * @param   aMaxParallel    The max parallel that it can reach
     * @return  The parallel that it can reach
     */
    public static int handleParallelRecipe(
            GT_Recipe aRecipe, FluidStack[] aFluidInputs, ItemStack[] aItemStacks, int aMaxParallel) {
        if (aFluidInputs == null) aFluidInputs = new FluidStack[0];
        if (aItemStacks == null) aItemStacks = new ItemStack[0];
        HashMap<Integer, Integer> tCompressedFluidInput = compressFluid(aFluidInputs);
        HashMap<Integer, Integer> tCompressedItemInput = compressItem(aItemStacks);
        HashMap<Integer, Integer> tCompressedFluidRecipe = compressFluid(aRecipe.mFluidInputs);
        HashMap<Integer, Integer> tCompressedItemRecipe = compressItem(aRecipe.mInputs);
        int tCurrentPara = aMaxParallel;
        for (int tFluid : tCompressedFluidRecipe.keySet()) {
            if (tCompressedFluidInput.containsKey(tFluid) && tCompressedFluidRecipe.get(tFluid) != 0) {
                tCurrentPara =
                        Math.min(tCurrentPara, tCompressedFluidInput.get(tFluid) / tCompressedFluidRecipe.get(tFluid));
            }
        }
        for (int tItem : tCompressedItemRecipe.keySet()) {
            /*Wildcard Stuff*/
            if (tItem >> 16 == Short.MAX_VALUE) {
                int tCountWildcard = 0;
                for (int tInputItem : tCompressedItemInput.keySet()) {
                    if ((tInputItem & 0xffff) == (tItem & 0xffff)) {
                        tCountWildcard += tCompressedItemInput.get(tInputItem);
                    }
                }
                tCurrentPara = Math.min(tCurrentPara, tCountWildcard / tCompressedItemRecipe.get(tItem));
            } else if (tCompressedItemRecipe.get(tItem) != 0) {
                /*OreDict Stuff*/
                int tCountOre = 0;
                ItemStack tRealRecipe = GT_Utility.intToStack(tItem);
                for (int tInputItem : tCompressedItemInput.keySet()) {
                    ItemStack tRealStack = GT_Utility.intToStack(tInputItem);
                    if (GT_OreDictUnificator.isInputStackEqual(tRealStack, tRealRecipe)) {
                        tCountOre += tCompressedItemInput.get(tInputItem);
                    }
                }
                tCurrentPara = Math.min(tCurrentPara, tCountOre / tCompressedItemRecipe.get(tItem));
            }
        }

        for (int tFluid : tCompressedFluidRecipe.keySet()) {
            int tOldSize = tCompressedFluidRecipe.get(tFluid);
            tCompressedFluidRecipe.put(tFluid, tOldSize * tCurrentPara);
        }
        for (int tItem : tCompressedItemRecipe.keySet()) {
            int tOldSize = tCompressedItemRecipe.get(tItem);
            tCompressedItemRecipe.put(tItem, tOldSize * tCurrentPara);
        }

        for (FluidStack tFluid : aFluidInputs) {
            if (tFluid != null && tCompressedFluidRecipe.containsKey(tFluid.getFluidID())) {
                if (tFluid.amount >= tCompressedFluidRecipe.get(tFluid.getFluidID())) {
                    tFluid.amount -= tCompressedFluidRecipe.get(tFluid.getFluidID());
                    tCompressedFluidRecipe.remove(tFluid.getFluidID());
                } else {
                    tCompressedFluidRecipe.put(
                            tFluid.getFluidID(), tCompressedFluidRecipe.get(tFluid.getFluidID()) - tFluid.amount);
                    tFluid.amount = 0;
                }
            }
        }

        /*OreDict Stuff*/
        /*Wildcard Stuff*/
        for (Iterator<Integer> i = tCompressedItemRecipe.keySet().iterator(); i.hasNext(); ) {
            int tItem = i.next();
            if (tItem >> 16 == Short.MAX_VALUE) {
                for (ItemStack tInputItem : aItemStacks) {
                    int InputID = GT_Utility.stackToInt(tInputItem);
                    if ((InputID & 0xffff) == (tItem & 0xffff)) {
                        if (tInputItem.stackSize >= tCompressedItemRecipe.get(tItem)) {
                            tInputItem.stackSize -= tCompressedItemRecipe.get(tItem);
                            i.remove();
                            break;
                        } else {
                            tCompressedItemRecipe.put(tItem, tCompressedItemRecipe.get(tItem) - tInputItem.stackSize);
                            tInputItem.stackSize = 0;
                        }
                    }
                }
            } else {
                ItemStack tRealRecipe = GT_Utility.intToStack(tItem);
                int tTargetAmount = tCompressedItemRecipe.get(tItem);
                for (ItemStack input : aItemStacks) {
                    if (GT_OreDictUnificator.isInputStackEqual(input, tRealRecipe)) {
                        int d = Math.min(tTargetAmount, input.stackSize);
                        tTargetAmount -= d;
                        tCompressedItemRecipe.put(tItem, tTargetAmount);
                        input.stackSize -= d;
                    }
                    if (tTargetAmount == 0) {
                        i.remove();
                        break;
                    }
                }
            }
        }

        return tCurrentPara;
    }

    /**
     * Get the proper packed output stacks
     *
     * @param aRecipe   The target recipe
     * @param aPall     The parallel it has
     * @return  A pair of the output fluid and item stack, the first value is fluid, the second is item.
     */
    public static Pair<ArrayList<FluidStack>, ArrayList<ItemStack>> getMultiOutput(GT_Recipe aRecipe, int aPall) {
        ArrayList<FluidStack> tFluidList = new ArrayList<>();
        ArrayList<ItemStack> tItemList = new ArrayList<>();
        if (aRecipe == null) return new Pair<>(tFluidList, tItemList);
        if (aRecipe.mFluidOutputs != null && aRecipe.mFluidOutputs.length > 0) {
            for (FluidStack tFluid : aRecipe.mFluidOutputs) {
                if (tFluid != null && tFluid.amount > 0) {
                    tFluidList.add(new FluidStack(tFluid.getFluid(), tFluid.amount * aPall));
                }
            }
        }
        if (aRecipe.mOutputs != null && aRecipe.mOutputs.length > 0) {
            for (ItemStack tItem : aRecipe.mOutputs) {
                if (tItem != null && tItem.stackSize > 0) {
                    int tAmount = tItem.stackSize * aPall;
                    while (tAmount > tItem.getMaxStackSize()) {
                        tItemList.add(GT_Utility.copyAmount(tItem.getMaxStackSize(), tItem));
                        tAmount -= tItem.getMaxStackSize();
                    }
                    tItemList.add(GT_Utility.copyAmount(tAmount, tItem));
                }
            }
        }
        return new Pair<>(tFluidList, tItemList);
    }

    public static HashMap<Integer, Integer> compressItem(ItemStack[] aItemStacks) {
        HashMap<Integer, Integer> tCompressed = new HashMap<>();
        for (ItemStack tItem : aItemStacks) {
            if (tItem != null) {
                int tItemID = GT_Utility.stackToInt(tItem);
                int tItemSize = tItem.stackSize;
                tCompressed.merge(tItemID, tItemSize, Integer::sum);
            }
        }
        return tCompressed;
    }

    public static HashMap<Integer, Integer> compressFluid(FluidStack[] aFluidStack) {
        HashMap<Integer, Integer> tCompressed = new HashMap<>();
        for (FluidStack tFluid : aFluidStack) {
            if (tFluid != null) {
                int tFluidID = tFluid.getFluidID();
                int tFluidSize = tFluid.amount;
                tCompressed.merge(tFluidID, tFluidSize, Integer::sum);
            }
        }
        return tCompressed;
    }
}
