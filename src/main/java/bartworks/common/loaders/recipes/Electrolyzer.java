package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.material.MaterialParts;
import gregtech.api.util.GTOreDictUnificator;

public class Electrolyzer implements Runnable {

    // TODO: fix the chemical balance issues there are below
    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.stack(Materials2Shapes.dust, Materials2Materials.Forsterite, 7))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Magnesium, Materials2Shapes.dust, (int) (2L)),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.dust, (int) (1L)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .duration(10 * SECONDS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.stack(Materials2Shapes.dust, Materials2Materials.RedZircon, 6))
            .itemOutputs(
                MaterialParts.stack(Materials2Shapes.dust, Materials2Materials.Zirconium, 1),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.dust, (int) (1L)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.stack(Materials2Shapes.dust, Materials2Materials.Fayalite, 7))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (2L)),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.dust, (int) (1L)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .duration(16 * SECONDS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.stack(Materials2Shapes.dust, Materials2Materials.Prasiolite, 16))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.dust, (int) (5L)),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (1L)))
            .duration(29 * SECONDS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.stack(Materials2Shapes.dust, Materials2Materials.Hedenbergite, 10))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Calcium, Materials2Shapes.dust, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.dust, (int) (2L)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .duration(15 * SECONDS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.stack(Materials2Shapes.dust, Materials2Materials.GreenFuchsite, 21),
                ItemList.Cell_Empty.get(2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Potassium, Materials2Shapes.dust, (int) (1L)),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials2Materials.Alumina, 3L),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.dust, (int) (3L)),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, (int) (2)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .duration(19 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.stack(Materials2Shapes.dust, Materials2Materials.RedFuchsite, 21),
                ItemList.Cell_Empty.get(2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Potassium, Materials2Shapes.dust, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.dust, (int) (3L)),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.dust, (int) (3L)),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, (int) (2)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .duration(23 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.stack(Materials2Shapes.dust, Materials2Materials.VanadioOxyDravite, 53),
                ItemList.Cell_Empty.get(3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.dust, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.Vanadium, Materials2Shapes.dust, (int) (3L)),
                MaterialLibAPI.getStack(Materials2Materials.Magnalium, Materials2Shapes.dust, (int) (6L)),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.dust, (int) (6)),
                MaterialLibAPI.getStack(Materials2Materials.Boron, Materials2Shapes.dust, (int) (3)),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, (int) (3)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (19_000)))
            .duration(35 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.stack(Materials2Shapes.dust, Materials2Materials.ChromoAluminoPovondraite, 53),
                ItemList.Cell_Empty.get(3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.dust, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.dust, (int) (3L)),
                MaterialLibAPI.getStack(Materials2Materials.Magnalium, Materials2Shapes.dust, (int) (6L)),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.dust, (int) (6)),
                MaterialLibAPI.getStack(Materials2Materials.Boron, Materials2Shapes.dust, (int) (3)),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, (int) (3)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (19_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.stack(Materials2Shapes.dust, Materials2Materials.FluorBuergerite, 50),
                ItemList.Cell_Empty.get(3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.dust, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (3L)),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials2Materials.Alumina, 6L),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.dust, (int) (6)),
                MaterialLibAPI.getStack(Materials2Materials.Boron, Materials2Shapes.dust, (int) (3)),
                MaterialLibAPI.getStack(Materials2Materials.Fluorine, Materials2CellShapes.cell, (int) (3)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (6_000)))
            .duration(36 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.stack(Materials2Shapes.dust, Materials2Materials.Olenite, 51),
                ItemList.Cell_Empty.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.dust, (int) (1L)),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials2Materials.Alumina, 9L),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.dust, (int) (6L)),
                MaterialLibAPI.getStack(Materials2Materials.Boron, Materials2Shapes.dust, (int) (3)),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, (int) (1)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(39 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

    }
}
