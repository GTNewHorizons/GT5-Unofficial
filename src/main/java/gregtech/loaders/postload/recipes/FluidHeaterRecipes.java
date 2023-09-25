package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMap.sFluidHeaterRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;

public class FluidHeaterRecipes implements Runnable {

    @Override
    public void run() {

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(Materials.GrowthMediumRaw.getFluid(1000))
            .fluidOutputs(Materials.GrowthMediumSterilized.getFluid(1000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(sFluidHeaterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(Materials.BioMediumRaw.getFluid(1000))
            .fluidOutputs(Materials.BioMediumSterilized.getFluid(1000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(sFluidHeaterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Water.getFluid(6))
            .fluidOutputs(Materials.Water.getGas(960))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(30)
            .addTo(sFluidHeaterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(GT_ModHandler.getDistilledWater(6))
            .fluidOutputs(Materials.Water.getGas(960))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(30)
            .addTo(sFluidHeaterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(Materials.SeedOil.getFluid(16))
            .fluidOutputs(Materials.FryingOilHot.getFluid(16))
            .duration(16 * TICKS)
            .eut(30)
            .addTo(sFluidHeaterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(Materials.FishOil.getFluid(16))
            .fluidOutputs(Materials.FryingOilHot.getFluid(16))
            .duration(16 * TICKS)
            .eut(30)
            .addTo(sFluidHeaterRecipes);
        // Ca(CH3COO)2 = CH3COCH3 + CaO + CO2

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(Materials.CalciumAcetateSolution.getFluid(1000))
            .fluidOutputs(Materials.Acetone.getFluid(1000))
            .duration(4 * SECONDS)
            .eut(30)
            .addTo(sFluidHeaterRecipes);
        // Fluid Sodium

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Sodium.getDust(1))
            .fluidInputs(

            )
            .fluidOutputs(Materials.Sodium.getFluid(1000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sFluidHeaterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Acetone.getFluid(1000))
            .fluidOutputs(Materials.Ethenone.getGas(1000))
            .duration(8 * SECONDS)
            .eut(160)
            .addTo(sFluidHeaterRecipes);

    }
}
