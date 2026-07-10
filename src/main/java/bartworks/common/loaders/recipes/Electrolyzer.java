package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTOreDictUnificator;

public class Electrolyzer implements Runnable {

    // TODO: fix the chemical balance issues there are below
    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Forsterit.get(OrePrefixes.dust, 7))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Magnesium, Materials2Shapes.shapeDust, (int) (2L)),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, (int) (1L)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, (int) (2_000)))
            .duration(10 * SECONDS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.RedZircon.get(OrePrefixes.dust, 6))
            .itemOutputs(
                WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, (int) (1L)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, (int) (2_000)))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Fayalit.get(OrePrefixes.dust, 7))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.shapeDust, (int) (2L)),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, (int) (1L)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, (int) (2_000)))
            .duration(16 * SECONDS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Prasiolite.get(OrePrefixes.dust, 16))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, (int) (5L)),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.shapeDust, (int) (1L)))
            .duration(29 * SECONDS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Hedenbergit.get(OrePrefixes.dust, 10))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Calcium, Materials2Shapes.shapeDust, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.shapeDust, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, (int) (2L)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, (int) (2_000)))
            .duration(15 * SECONDS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.FuchsitAL.get(OrePrefixes.dust, 21), ItemList.Cell_Empty.get(2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Potassium, Materials2Shapes.shapeDust, (int) (1L)),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 3L),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, (int) (3L)),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, (int) (2)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, (int) (2_000)))
            .duration(19 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.FuchsitCR.get(OrePrefixes.dust, 21), ItemList.Cell_Empty.get(2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Potassium, Materials2Shapes.shapeDust, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.shapeDust, (int) (3L)),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, (int) (3L)),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, (int) (2)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, (int) (2_000)))
            .duration(23 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.VanadioOxyDravit.get(OrePrefixes.dust, 53), ItemList.Cell_Empty.get(3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.Vanadium, Materials2Shapes.shapeDust, (int) (3L)),
                MaterialLibAPI.getStack(Materials2Materials.Magnalium, Materials2Shapes.shapeDust, (int) (6L)),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, (int) (6)),
                MaterialLibAPI.getStack(Materials2Materials.Boron, Materials2Shapes.shapeDust, (int) (3)),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, (int) (3)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, (int) (19_000)))
            .duration(35 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.ChromoAluminoPovondrait.get(OrePrefixes.dust, 53), ItemList.Cell_Empty.get(3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.shapeDust, (int) (3L)),
                MaterialLibAPI.getStack(Materials2Materials.Magnalium, Materials2Shapes.shapeDust, (int) (6L)),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, (int) (6)),
                MaterialLibAPI.getStack(Materials2Materials.Boron, Materials2Shapes.shapeDust, (int) (3)),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, (int) (3)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, (int) (19_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.FluorBuergerit.get(OrePrefixes.dust, 50), ItemList.Cell_Empty.get(3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.shapeDust, (int) (3L)),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 6L),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, (int) (6)),
                MaterialLibAPI.getStack(Materials2Materials.Boron, Materials2Shapes.shapeDust, (int) (3)),
                MaterialLibAPI.getStack(Materials2Materials.Fluorine, Materials2CellShapes.shapeCell, (int) (3)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, (int) (6_000)))
            .duration(36 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Olenit.get(OrePrefixes.dust, 51), ItemList.Cell_Empty.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, (int) (1L)),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 9L),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, (int) (6L)),
                MaterialLibAPI.getStack(Materials2Materials.Boron, Materials2Shapes.shapeDust, (int) (3)),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, (int) (1)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, (int) (1_000)))
            .duration(39 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

    }
}
