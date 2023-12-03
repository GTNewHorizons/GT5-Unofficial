package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.fluidHeaterRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.common.loaders.FluidLoader;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_Utility;

public class FluidHeater implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(10))
                .fluidInputs(new FluidStack(FluidLoader.fulvicAcid, 1000))
                .fluidOutputs(new FluidStack(FluidLoader.heatedfulvicAcid, 1000)).duration(4 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_MV).addTo(fluidHeaterRecipes);
    }
}
