package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.fluidHeaterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class FluidHeaterRecipes implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.GrowthMediumRaw,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.GrowthMediumSterilized,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.BioMediumRaw, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(MaterialUtils.fluid(Materials.BiohMediumSterilized, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(GTUtility.getWater(6))
            .fluidOutputs(MaterialUtils.gas(Materials.Steam, 960))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(GTModHandler.getDistilledWater(6))
            .fluidOutputs(MaterialUtils.gas(Materials.Steam, 960))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SeedOil, Materials2FluidShapes.fluidLiquid, (int) (16)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.FryingOilHot, Materials2FluidShapes.fluidLiquid, (int) (16)))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.FishOil, Materials2FluidShapes.fluidLiquid, (int) (16)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.FryingOilHot, Materials2FluidShapes.fluidLiquid, (int) (16)))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidHeaterRecipes);
        // Ca(CH3COO)2 = CH3COCH3 + CaO + CO2

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.CalciumAcetateSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Acetone, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidHeaterRecipes);
        // Fluid Sodium

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Sodium, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Acetone, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Ethenone, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(8 * SECONDS)
            .eut(160)
            .addTo(fluidHeaterRecipes);

    }
}
