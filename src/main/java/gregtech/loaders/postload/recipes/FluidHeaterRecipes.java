package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.fluidHeaterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTModHandler;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class FluidHeaterRecipes implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.GrowthMediumRaw,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.GrowthMediumSterilized,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (1_000)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.BioMediumRaw,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(Materials.BioMediumSterilized.getFluid(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(Materials.Water.getFluid(6))
            .fluidOutputs(Materials.Steam.getGas(960))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(GTModHandler.getDistilledWater(6))
            .fluidOutputs(Materials.Steam.getGas(960))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SeedOil, Materials2FluidShapes.shapeFluidLiquid, (int) (16)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.FryingOilHot,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (16)))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.FishOil, Materials2FluidShapes.shapeFluidLiquid, (int) (16)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.FryingOilHot,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (16)))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidHeaterRecipes);
        // Ca(CH3COO)2 = CH3COCH3 + CaO + CO2

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CalciumAcetateSolution,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Acetone, Materials2FluidShapes.shapeFluidLiquid, (int) (1_000)))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidHeaterRecipes);
        // Fluid Sodium

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, 1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Sodium, Materials2FluidShapes.shapeFluidLiquid, (int) (1_000)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Acetone, Materials2FluidShapes.shapeFluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ethenone, Materials2FluidShapes.shapeFluidGas, (int) (1_000)))
            .duration(8 * SECONDS)
            .eut(160)
            .addTo(fluidHeaterRecipes);

    }
}
