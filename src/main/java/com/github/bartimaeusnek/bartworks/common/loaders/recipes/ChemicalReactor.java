package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.common.loaders.FluidLoader;
import com.github.bartimaeusnek.bartworks.util.BW_Util;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;

public class ChemicalReactor implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addChemicalRecipe(
                GT_Utility.getIntegratedCircuit(10),
                null,
                new FluidStack(FluidLoader.heatedfulvicAcid, 1000),
                new FluidStack(FluidLoader.Kerogen, 1000),
                null,
                75,
                BW_Util.getMachineVoltageFromTier(2));
    }
}
