package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.FluidShapes;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.CellShapes;
import gregtech.api.enums.materials2.Shapes;
import gregtech.api.util.GTOreDictUnificator;

public class Electrolyzer implements Runnable {

    // TODO: fix the chemical balance issues there are below
    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Forsterite, Shapes.dust, 7))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, (int) (2L)),
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, (int) (1L)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (2_000)))
            .duration(10 * SECONDS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.RedZircon, Shapes.dust, 6))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, (int) (1L)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (2_000)))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Fayalite, Shapes.dust, 7))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (2L)),
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, (int) (1L)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (2_000)))
            .duration(16 * SECONDS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Prasiolite, Shapes.dust, 16))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, (int) (5L)),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (1L)))
            .duration(29 * SECONDS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Hedenbergite, Shapes.dust, 10))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, (int) (1L)),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (1L)),
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, (int) (2L)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (2_000)))
            .duration(15 * SECONDS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.GreenFuchsite, Shapes.dust, 21),
                ItemList.Cell_Empty.get(2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Potassium, Shapes.dust, (int) (1L)),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Alumina, 3L),
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, (int) (3L)),
                MaterialLibAPI.getStack(Materials.Hydrogen, CellShapes.cell, (int) (2)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (2_000)))
            .duration(19 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.RedFuchsite, Shapes.dust, 21),
                ItemList.Cell_Empty.get(2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Potassium, Shapes.dust, (int) (1L)),
                MaterialLibAPI.getStack(Materials.Chrome, Shapes.dust, (int) (3L)),
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, (int) (3L)),
                MaterialLibAPI.getStack(Materials.Hydrogen, CellShapes.cell, (int) (2)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (2_000)))
            .duration(23 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.VanadioOxyDravite, Shapes.dust, 53),
                ItemList.Cell_Empty.get(3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, (int) (1L)),
                MaterialLibAPI.getStack(Materials.Vanadium, Shapes.dust, (int) (3L)),
                MaterialLibAPI.getStack(Materials.Magnalium, Shapes.dust, (int) (6L)),
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, (int) (6)),
                MaterialLibAPI.getStack(Materials.Boron, Shapes.dust, (int) (3)),
                MaterialLibAPI.getStack(Materials.Hydrogen, CellShapes.cell, (int) (3)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (19_000)))
            .duration(35 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.ChromoAluminoPovondraite, Shapes.dust, 53),
                ItemList.Cell_Empty.get(3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, (int) (1L)),
                MaterialLibAPI.getStack(Materials.Chrome, Shapes.dust, (int) (3L)),
                MaterialLibAPI.getStack(Materials.Magnalium, Shapes.dust, (int) (6L)),
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, (int) (6)),
                MaterialLibAPI.getStack(Materials.Boron, Shapes.dust, (int) (3)),
                MaterialLibAPI.getStack(Materials.Hydrogen, CellShapes.cell, (int) (3)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (19_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.FluorBuergerite, Shapes.dust, 50),
                ItemList.Cell_Empty.get(3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, (int) (1L)),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (3L)),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Alumina, 6L),
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, (int) (6)),
                MaterialLibAPI.getStack(Materials.Boron, Shapes.dust, (int) (3)),
                MaterialLibAPI.getStack(Materials.Fluorine, CellShapes.cell, (int) (3)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (6_000)))
            .duration(36 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Olenite, Shapes.dust, 51),
                ItemList.Cell_Empty.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, (int) (1L)),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Alumina, 9L),
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, (int) (6L)),
                MaterialLibAPI.getStack(Materials.Boron, Shapes.dust, (int) (3)),
                MaterialLibAPI.getStack(Materials.Hydrogen, CellShapes.cell, (int) (1)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (1_000)))
            .duration(39 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

    }
}
