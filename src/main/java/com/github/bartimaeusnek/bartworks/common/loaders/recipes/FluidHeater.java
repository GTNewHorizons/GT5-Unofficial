package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.common.loaders.FluidLoader;
import com.github.bartimaeusnek.bartworks.util.BW_Util;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;

public class FluidHeater implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addFluidHeaterRecipe(
                GT_Utility.getIntegratedCircuit(10),
                new FluidStack(FluidLoader.fulvicAcid, 1000),
                new FluidStack(FluidLoader.heatedfulvicAcid, 1000),
                90,
                BW_Util.getMachineVoltageFromTier(2));
    }
}
