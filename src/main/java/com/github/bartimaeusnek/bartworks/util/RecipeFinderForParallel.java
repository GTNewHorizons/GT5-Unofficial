package com.github.bartimaeusnek.bartworks.util;

import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;

public class RecipeFinderForParallel {

    /**
        This method is used for mega multis which have extremely high parallel.<BR>
        Never use it for non parallel machines, it will have worse performance.<BR>
     */

    public static int handleParallelRecipe(GT_Recipe aRecipe, FluidStack[] aFluidInputs, ItemStack[] aItemStacks, int aMaxParallel) {
        HashMap<Integer, Integer> tCompressedFluidInput = compressFluid(aFluidInputs);
        HashMap<Integer, Integer> tCompressedItemInput = compressItem(aItemStacks);
        HashMap<Integer, Integer> tCompressedFluidRecipe = compressFluid(aRecipe.mFluidInputs);
        HashMap<Integer, Integer> tCompressedItemRecipe = compressItem(aRecipe.mInputs);
        int tCurrentPara = aMaxParallel;
        for (int tFluid : tCompressedFluidRecipe.keySet()) {
            if (tCompressedFluidInput.containsKey(tFluid) && tCompressedFluidRecipe.get(tFluid) != 0) {
                tCurrentPara = Math.min(tCurrentPara, tCompressedFluidInput.get(tFluid) / tCompressedFluidRecipe.get(tFluid));
            }
        }
        for (int tItem : tCompressedItemRecipe.keySet()) {
            if (tCompressedItemInput.containsKey(tItem) && tCompressedItemRecipe.get(tItem) != 0) {
                tCurrentPara = Math.min(tCurrentPara, tCompressedItemInput.get(tItem) / tCompressedItemRecipe.get(tItem));
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
                    tCompressedItemRecipe.remove(tFluid.getFluidID());
                }
                else {
                    tCompressedFluidRecipe.put(tFluid.getFluidID(), tCompressedFluidRecipe.get(tFluid.getFluidID()) - tFluid.amount);
                    tFluid.amount = 0;
                }
            }
        }

        for (ItemStack tItem : aItemStacks) {
            if (tItem != null && tCompressedItemRecipe.containsKey(GT_Utility.stackToInt(tItem))) {
                if (tItem.stackSize >= tCompressedItemRecipe.get(GT_Utility.stackToInt(tItem))) {
                    tItem.stackSize -= tCompressedItemRecipe.get(GT_Utility.stackToInt(tItem));
                    tCompressedItemRecipe.remove(GT_Utility.stackToInt(tItem));
                }
                else {
                    tCompressedItemRecipe.put(GT_Utility.stackToInt(tItem), tCompressedItemRecipe.get(GT_Utility.stackToInt(tItem)) - tItem.stackSize);
                    tItem.stackSize = 0;
                }
            }
        }
        return tCurrentPara;
    }

    public static HashMap<Integer, Integer> compressItem(ItemStack[] aItemStacks) {
        HashMap<Integer, Integer> tCompressed = new HashMap<>();
        for (ItemStack tItem : aItemStacks) {
            if (tItem != null) {
                int tItemID = GT_Utility.stackToInt(tItem);
                int tItemSize = tItem.stackSize;
                if (tCompressed.containsKey(tItemID)) {
                    int tOldSize = tCompressed.get(tItemID);
                    tCompressed.put(tItemID, tOldSize + tItemSize);
                }
                else {
                    tCompressed.put(tItemID, tItemSize);
                }
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
                if (tCompressed.containsKey(tFluidID)) {
                    int tOldSize = tCompressed.get(tFluidID);
                    tCompressed.put(tFluidID, tOldSize + tFluidSize);
                }
                else {
                    tCompressed.put(tFluidID, tFluidSize);
                }
            }
        }
        return tCompressed;
    }
}
