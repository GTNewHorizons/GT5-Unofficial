package com.github.bartimaeusnek.bartworks.API.recipe;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.MainMod;

import gregtech.api.util.GT_Recipe;

public class DynamicGTRecipe extends GT_Recipe {

    public DynamicGTRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems,
            int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
            int aSpecialValue, GT_Recipe originalRecipe) {
        super(
                aOptimize,
                aInputs,
                aOutputs,
                aSpecialItems,
                aChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue);
        if (originalRecipe != null) {
            this.owners = new ArrayList<>(originalRecipe.owners);
            this.stackTraces = new ArrayList<>(originalRecipe.stackTraces);
            this.setOwner(MainMod.MOD_ID);
        }
    }
}
