package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.fluidHeaterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class FluidHeaterRecipes implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.GrowthMediumRaw.getFluid(1000))
            .fluidOutputs(Materials.GrowthMediumSterilized.getFluid(1000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.BioMediumRaw.getFluid(1000))
            .fluidOutputs(Materials.BioMediumSterilized.getFluid(1000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Water.getFluid(6))
            .fluidOutputs(Materials.Water.getGas(960))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(30)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(GTModHandler.getDistilledWater(6))
            .fluidOutputs(Materials.Water.getGas(960))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(30)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.SeedOil.getFluid(16))
            .fluidOutputs(Materials.FryingOilHot.getFluid(16))
            .duration(16 * TICKS)
            .eut(30)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.FishOil.getFluid(16))
            .fluidOutputs(Materials.FryingOilHot.getFluid(16))
            .duration(16 * TICKS)
            .eut(30)
            .addTo(fluidHeaterRecipes);
        // Ca(CH3COO)2 = CH3COCH3 + CaO + CO2

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.CalciumAcetateSolution.getFluid(1000))
            .fluidOutputs(Materials.Acetone.getFluid(1000))
            .duration(4 * SECONDS)
            .eut(30)
            .addTo(fluidHeaterRecipes);
        // Fluid Sodium

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Sodium.getDust(1))
            .fluidInputs(

            )
            .fluidOutputs(Materials.Sodium.getFluid(1000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Acetone.getFluid(1000))
            .fluidOutputs(Materials.Ethenone.getGas(1000))
            .duration(8 * SECONDS)
            .eut(160)
            .addTo(fluidHeaterRecipes);

    }
}
