package gregtech.loaders.postload.recipes;

import static bartworks.system.material.WerkstoffLoader.CalciumChloride;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class ElectrolyzerRecipes implements Runnable {

    @Override
    public void run() {
        // H2O = 2H + O

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1L))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, (int) (1)))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, (int) (2_000)))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1L))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, (int) (1)))
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, (int) (2_000)))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(2L))
            .circuit(3)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, (int) (2)))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, (int) (1_000)))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(2L))
            .circuit(4)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, (int) (2)))
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, (int) (1_000)))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L), ItemList.Cell_Empty.get(2L))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, (int) (1)))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Dye_Bonemeal.get(3L))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Calcium, Materials2Shapes.shapeDust, (int) (1)))
            .duration(4 * SECONDS + 18 * TICKS)
            .eut(26)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.sand, 8, 0))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, (int) (3)))
            .duration(25 * SECONDS)
            .eut(25)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.sand, 8, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, (int) (3)))
            .duration(25 * SECONDS)
            .eut(25)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Graphite, Materials2Shapes.shapeDust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, (int) (4)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(electrolyzerRecipes);
        // ZnS = Zn + S + 1 Ga(9.17%)

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sphalerite, Materials2Shapes.shapeDust, (int) (2)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Gallium, Materials2Shapes.shapeDust, (int) (1)))
            .outputChances(10000, 10000, 917)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);
        // NaOH = Na + O + H

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SodiumHydroxide.getDust(3), Materials.Empty.getCells(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, (int) (1)))
            .outputChances(10000, 10000)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, (int) (1_000)))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);
        // CO2 = C + 2O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, (int) (1)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CarbonDioxide,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, (int) (2_000)))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, (int) (2)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CarbonDioxide,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (1_000)))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);
        // CO = C + O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, (int) (1)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CarbonMonoxide,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, (int) (1_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, (int) (1)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CarbonMonoxide,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (1_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.CarbonMonoxide, Materials2CellShapes.shapeCell, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, (int) (1)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        // H2S = S + 2H

        GTValues.RA.stdBuilder()
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, (int) (1)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.HydricSulfide,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, (int) (2_000)))
            .duration(3 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, (int) (2)))
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.HydricSulfide, Materials2CellShapes.shapeCell, (int) (1)),
                Materials.Empty.getCells(1))
            .duration(3 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        // SO2 = S + 2O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, (int) (1)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfurDioxide,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, (int) (2_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, (int) (2)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfurDioxide,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (1_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);
        // NaCl = Na +Cl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.shapeDust, (int) (2)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, (int) (1)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, (int) (1_000)))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);
        // (NaCl·H2O)= NaOH + H

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .circuit(1)
            .itemOutputs(
                Materials.SodiumHydroxide.getDust(3),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, (int) (1)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SaltWater,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, (int) (1_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .circuit(11)
            .itemOutputs(
                Materials.SodiumHydroxide.getDust(3),
                MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, (int) (1)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SaltWater,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, (int) (1_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);
        // HCl = H + Cl

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, (int) (1)))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, (int) (1_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, (int) (1)))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, (int) (1_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrochloricAcid.getCells(1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, (int) (1)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, (int) (1_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrochloricAcid.getCells(1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, (int) (1)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, (int) (1_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);
        // 2NaHSO4 = 2H + Na2S2O8

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SodiumBisulfate, Materials2Shapes.shapeDust, (int) (14)),
                Materials.Empty.getCells(2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, (int) (2)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SodiumPersulfate,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (1_000)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Lead, Materials2Shapes.shapeDust, (int) (3)),
                MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, (int) (4)))
            .fluidInputs(new FluidStack(ItemList.sLeadZincSolution, 8000))
            .fluidOutputs(Materials.Water.getFluid(2_000))
            .duration(15 * SECONDS)
            .eut(192)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, (int) (1)))
            .fluidInputs(new FluidStack(ItemList.sBlueVitriol, 2000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (1_000)))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, (int) (1)))
            .fluidInputs(new FluidStack(ItemList.sNickelSulfate, 2000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (1_000)))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, (int) (1)))
            .fluidInputs(new FluidStack(ItemList.sGreenVitriol, 2000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (1_000)))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.PhosphoricAcid, 1L),
                ItemList.Cell_Empty.get(6L))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, (int) (3)),
                MaterialLibAPI.getStack(Materials2Materials.Phosphorus, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, (int) (4)))
            .duration(27 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(CalciumChloride.getFluidOrGas(3_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Calcium, Materials2Shapes.shapeDust, (int) (1)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, (int) (2_000)))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

    }
}
