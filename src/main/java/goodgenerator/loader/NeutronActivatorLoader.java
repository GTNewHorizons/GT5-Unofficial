package goodgenerator.loader;

import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.neutronActivatorRecipes;
import static goodgenerator.util.MyRecipeAdder.computeRangeNKE;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.NKE_RANGE;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTUtility;

public class NeutronActivatorLoader {

    public static void NARecipeLoad() {
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ThoriumBasedLiquidFuelExcitedState,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (200)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ThoriumBasedLiquidFuelDepleted,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (200)))
            .duration(8 * MINUTES + 20 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(700, 500))
            .addTo(neutronActivatorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility
                    .copyAmount(0, MaterialLibAPI.getStack(Materials.Tungsten, Materials2Shapes.plate, 1)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.UraniumBasedLiquidFuel,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (100)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.UraniumBasedLiquidFuelExcitedState,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (100)))
            .duration(4 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(550, 450))
            .addTo(neutronActivatorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility
                    .copyAmount(0, MaterialLibAPI.getStack(Materials.Tritanium, Materials2Shapes.plate, 1)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.PlutoniumBasedLiquidFuel,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (100)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.PlutoniumBasedLiquidFuelExcitedState,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (100)))
            .duration(4 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(600, 500))
            .addTo(neutronActivatorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Tesseract.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NaquadahBasedLiquidFuelMkV,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (64)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NaquadahBasedLiquidFuelMkVDepleted,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (64)))
            .itemOutputs(ItemList.EnergisedTesseract.get(1))
            .duration(16400 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(1100, 1050))
            .addTo(neutronActivatorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Tesseract.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NaquadahBasedLiquidFuelMkVI,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (64)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NaquadahBasedLiquidFuelMkVIDepleted,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (64)))
            .itemOutputs(ItemList.EnergisedTesseract.get(1))
            .duration(24600 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(1100, 1075))
            .addTo(neutronActivatorRecipes);

    }
}
