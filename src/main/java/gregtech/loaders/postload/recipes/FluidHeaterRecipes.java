package gregtech.loaders.postload.recipes;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;

public class FluidHeaterRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addFluidHeaterRecipe(
                GT_Utility.getIntegratedCircuit(1),
                Materials.GrowthMediumRaw.getFluid(1000L),
                Materials.GrowthMediumSterilized.getFluid(1000L),
                200,
                7680);
        GT_Values.RA.addFluidHeaterRecipe(
                GT_Utility.getIntegratedCircuit(1),
                Materials.BioMediumRaw.getFluid(1000L),
                Materials.BioMediumSterilized.getFluid(1000L),
                200,
                30720);

        GT_Values.RA.addFluidHeaterRecipe(
                GT_Utility.getIntegratedCircuit(1),
                Materials.Water.getFluid(6L),
                Materials.Water.getGas(960L),
                30,
                30);
        GT_Values.RA.addFluidHeaterRecipe(
                GT_Utility.getIntegratedCircuit(1),
                GT_ModHandler.getDistilledWater(6L),
                Materials.Water.getGas(960L),
                30,
                30);
        GT_Values.RA.addFluidHeaterRecipe(
                GT_Utility.getIntegratedCircuit(1),
                Materials.SeedOil.getFluid(16L),
                Materials.FryingOilHot.getFluid(16L),
                16,
                30);
        GT_Values.RA.addFluidHeaterRecipe(
                GT_Utility.getIntegratedCircuit(1),
                Materials.FishOil.getFluid(16L),
                Materials.FryingOilHot.getFluid(16L),
                16,
                30);

        // Ca(CH3COO)2 = CH3COCH3 + CaO + CO2
        GT_Values.RA.addFluidHeaterRecipe(
                GT_Utility.getIntegratedCircuit(1),
                Materials.CalciumAcetateSolution.getFluid(1000),
                Materials.Acetone.getFluid(1000),
                80,
                30);

        // Fluid Sodium
        GT_Values.RA.addFluidHeaterRecipe(Materials.Sodium.getDust(1), Materials.Sodium.getFluid(1000), 200, 120);

        GT_Values.RA.addFluidHeaterRecipe(
                GT_Utility.getIntegratedCircuit(1),
                Materials.Acetone.getFluid(1000),
                Materials.Ethenone.getGas(1000),
                160,
                160);
    }
}
