package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.common.loaders.FluidLoader;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;

public class PyrolyseOven implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addPyrolyseRecipe(
                Materials.Wood.getDust(10),
                new FluidStack(FluidLoader.Kerogen, 1000),
                10,
                null,
                Materials.Oil.getFluid(1000),
                105,
                (int) TierEU.RECIPE_HV);
    }
}
