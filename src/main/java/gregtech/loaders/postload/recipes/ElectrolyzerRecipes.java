package gregtech.loaders.postload.recipes;

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
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class ElectrolyzerRecipes implements Runnable {

    @Override
    public void run() {
        // H2O = 2H + O

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1L))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.cell, (int) (1)))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1L))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.cell, (int) (1)))
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(2L))
            .circuit(3)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, (int) (2)))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(2L))
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, (int) (2)))
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials2Materials.Water, 1L),
                ItemList.Cell_Empty.get(2L))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.cell, (int) (1)))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Dye_Bonemeal.get(3L))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Calcium, Materials2Shapes.dust, (int) (1)))
            .duration(4 * SECONDS + 18 * TICKS)
            .eut(26)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.sand, 8, 0))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.dust, (int) (3)))
            .duration(25 * SECONDS)
            .eut(25)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.sand, 8, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.dust, (int) (3)))
            .duration(25 * SECONDS)
            .eut(25)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Graphite, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (4)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(electrolyzerRecipes);
        // ZnS = Zn + S + 1 Ga(9.17%)

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sphalerite, Materials2Shapes.dust, (int) (2)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Gallium, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 10000, 917)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);
        // NaOH = Na + O + H

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SodiumHydroxideGT5U, Materials2Shapes.dust, 3),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials2Materials.Empty, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, (int) (1)))
            .outputChances(10000, 10000)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);
        // CH4 = C + 4H

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Carbon.getDust(1))
            .fluidInputs(Materials.Methane.getGas(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(4_000))
            .duration(4 * SECONDS)
            .eut(60)
            .addTo(electrolyzerRecipes);
        // CO2 = C + 2O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials2Materials.Empty, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.cell, (int) (2)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);
        // CO = C + O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials2Materials.Empty, 1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.cell, (int) (1)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.CarbonMonoxide, Materials2CellShapes.cell, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.cell, (int) (1)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        // C4H6 = 4C + 3H2

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Carbon.getDust(4))
            .fluidInputs(Materials.Butadiene.getGas(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(6_000))
            .duration(12 * SECONDS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // C4H10 = 4C + 5H2

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Carbon.getDust(4))
            .fluidInputs(Materials.Butane.getGas(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(10_000))
            .duration(11 * SECONDS + 4 * TICKS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // C4H8 = 4C + 4H2

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Carbon.getDust(4))
            .fluidInputs(Materials.Butene.getGas(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(8_000))
            .duration(9 * SECONDS + 12 * TICKS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // CH3Cl = C + 3H + Cl

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .itemOutputs(Materials.Carbon.getDust(1), Materials.Chlorine.getCells(1))
            .fluidInputs(Materials.Chloromethane.getGas(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(3_000))
            .duration(10 * SECONDS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // C2H7N = 2C + 7H + N

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .itemOutputs(Materials.Carbon.getDust(2), Materials.Nitrogen.getCells(1))
            .fluidInputs(Materials.Dimethylamine.getGas(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(7_000))
            .duration(8 * SECONDS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // N2O4 = N2 + 2O2

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .itemOutputs(Materials.Nitrogen.getCells(2))
            .fluidInputs(Materials.DinitrogenTetroxide.getGas(1_000))
            .fluidOutputs(Materials.Oxygen.getGas(4_000))
            .duration(16 * SECONDS + 16 * TICKS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // C2H6 = 2C + 3H2

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Carbon.getDust(2))
            .fluidInputs(Materials.Ethane.getGas(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(6_000))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // C2H2O = 2C + H2 + O

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .itemOutputs(Materials.Carbon.getDust(2), Materials.Oxygen.getCells(1))
            .fluidInputs(Materials.Ethenone.getGas(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(2_000))
            .duration(8 * SECONDS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // C2H4 = 2C + 2H2

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Carbon.getDust(2))
            .fluidInputs(Materials.Ethylene.getGas(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(4_000))
            .duration(4 * SECONDS + 16 * TICKS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // NO = N + O

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .itemOutputs(Materials.Oxygen.getCells(1))
            .fluidInputs(Materials.NitricOxide.getGas(1_000))
            .fluidOutputs(Materials.Nitrogen.getGas(1_000))
            .duration(5 * SECONDS + 12 * TICKS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // C3H8 = 3C + 4H2

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Carbon.getDust(3))
            .fluidInputs(Materials.Propane.getGas(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(8_000))
            .duration(8 * SECONDS + 16 * TICKS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // C3H6 = 3C + 3H2

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Carbon.getDust(3))
            .fluidInputs(Materials.Propene.getGas(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(6_000))
            .duration(7 * SECONDS + 4 * TICKS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // SO3 = S + 3/2 O2

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Sulfur.getDust(1))
            .fluidInputs(Materials.SulfurTrioxide.getGas(1_000))
            .fluidOutputs(Materials.Oxygen.getGas(3_000))
            .duration(16 * SECONDS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // C2F4 = 2C + 2F2

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Carbon.getDust(2))
            .fluidInputs(Materials.Tetrafluoroethylene.getGas(1_000))
            .fluidOutputs(Materials.Fluorine.getGas(4_000))
            .duration(19 * SECONDS + 4 * TICKS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // C2H3Cl = 2C + 3H + Cl

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .itemOutputs(Materials.Carbon.getDust(2), Materials.Chlorine.getCells(1))
            .fluidInputs(Materials.VinylChloride.getGas(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(3_000))
            .duration(12 * SECONDS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // SiH2Cl2 = Si + H2 + Cl2

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .itemOutputs(Materials.Silicon.getDust(1), Materials.Chlorine.getCells(2))
            .fluidInputs(Materials.Dichlorosilane.getGas(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(2_000))
            .duration(20 * SECONDS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // N2O = N2 + 1/2 O2

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .itemOutputs(Materials.Oxygen.getCells(1))
            .fluidInputs(Materials.NitrousOxide.getGas(1_000))
            .fluidOutputs(Materials.Nitrogen.getGas(2_000))
            .duration(8 * SECONDS + 8 * TICKS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // CH5N = C + 5H + N

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .itemOutputs(Materials.Carbon.getDust(1), Materials.Nitrogen.getCells(1))
            .fluidInputs(Materials.Methylamine.getGas(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(5_000))
            .duration(5 * SECONDS + 12 * TICKS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // C3H9N = 3C + 9H + N

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .itemOutputs(Materials.Carbon.getDust(3), Materials.Nitrogen.getCells(1))
            .fluidInputs(Materials.Trimethylamine.getGas(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(9_000))
            .duration(10 * SECONDS + 8 * TICKS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // C2H4O2 = 2C + 2H2 + O2

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .itemOutputs(Materials.Carbon.getDust(2), Materials.Oxygen.getCells(2))
            .fluidInputs(Materials.AceticAcid.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(4_000))
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // C3H6O = 3C + 3H2 + 1/2 O2

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .itemOutputs(Materials.Carbon.getDust(3), Materials.Oxygen.getCells(1))
            .fluidInputs(Materials.Acetone.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(6_000))
            .duration(12 * SECONDS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // C3H5Cl = 3C + 5H + Cl

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .itemOutputs(Materials.Carbon.getDust(3), Materials.Chlorine.getCells(1))
            .fluidInputs(Materials.AllylChloride.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(5_000))
            .duration(14 * SECONDS + 8 * TICKS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // C6H6 = 6C + 3H2

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Carbon.getDust(6))
            .fluidInputs(Materials.Benzene.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(6_000))
            .duration(14 * SECONDS + 8 * TICKS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // C6H5Cl = 6C + 5H + Cl

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .itemOutputs(Materials.Carbon.getDust(6), Materials.Chlorine.getCells(1))
            .fluidInputs(Materials.Chlorobenzene.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(5_000))
            .duration(19 * SECONDS + 4 * TICKS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // CaC4H6O4 = Ca + 4C + 3H2 + 2O2

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(4))
            .itemOutputs(Materials.Calcium.getDust(1), Materials.Carbon.getDust(4), Materials.Oxygen.getCells(4))
            .fluidInputs(Materials.CalciumAcetateSolution.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(6_000))
            .duration(30 * SECONDS)
            .eut(120)
            .addTo(electrolyzerRecipes);

        // NH2Cl = N + 2H + Cl

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .itemOutputs(Materials.Nitrogen.getCells(1), Materials.Chlorine.getCells(1))
            .fluidInputs(Materials.Chloramine.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(2_000))
            .duration(9 * SECONDS + 12 * TICKS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // CHCl3 = C + H + 3Cl

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .itemOutputs(Materials.Carbon.getDust(1), Materials.Hydrogen.getCells(1))
            .fluidInputs(Materials.Chloroform.getFluid(1_000))
            .fluidOutputs(Materials.Chlorine.getGas(3_000))
            .duration(22 * SECONDS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // C9H12 = 9C + 6H2

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Carbon.getDust(9))
            .fluidInputs(Materials.Cumene.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(12_000))
            .duration(25 * SECONDS + 4 * TICKS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // C6H4Cl2 = 6C + 2H2 + Cl2

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .itemOutputs(Materials.Carbon.getDust(6), Materials.Chlorine.getCells(2))
            .fluidInputs(Materials.Dichlorobenzene.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(4_000))
            .duration(28 * SECONDS + 16 * TICKS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // C2H8N2 = 2C + 4H2 + N2

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .itemOutputs(Materials.Carbon.getDust(2), Materials.Nitrogen.getCells(2))
            .fluidInputs(Materials.Dimethylhydrazine.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(8_000))
            .duration(9 * SECONDS + 12 * TICKS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // C2H6Cl2Si = 2C + Si + 3H2 + Cl2

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .itemOutputs(Materials.Carbon.getDust(2), Materials.Silicon.getDust(1), Materials.Chlorine.getCells(2))
            .fluidInputs(Materials.Dimethyldichlorosilane.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(6_000))
            .duration(26 * SECONDS + 8 * TICKS)
            .eut(120)
            .addTo(electrolyzerRecipes);

        // C3H8O3 = 3C + 4H2 + 3/2 O2

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(3))
            .itemOutputs(Materials.Carbon.getDust(3), Materials.Oxygen.getCells(3))
            .fluidInputs(Materials.Glycerol.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(8_000))
            .duration(16 * SECONDS + 16 * TICKS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // HF = H + F

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .itemOutputs(Materials.Fluorine.getCells(1))
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(1_000))
            .duration(4 * SECONDS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // HOCl = H + Cl + O

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .itemOutputs(Materials.Chlorine.getCells(1), Materials.Oxygen.getCells(1))
            .fluidInputs(Materials.HypochlorousAcid.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(1_000))
            .duration(9 * SECONDS + 12 * TICKS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // FeCl3 = Fe + 3/2 Cl2

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Iron.getDust(1))
            .fluidInputs(Materials.IronIIIChloride.getFluid(1_000))
            .fluidOutputs(Materials.Chlorine.getGas(3_000))
            .duration(30 * SECONDS + 8 * TICKS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // C5H8 = 5C + 4H2

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Carbon.getDust(5))
            .fluidInputs(Materials.Isoprene.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(8_000))
            .duration(10 * SECONDS + 8 * TICKS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // CH4O = C + 2H2 + 1/2 O2

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .itemOutputs(Materials.Carbon.getDust(1), Materials.Oxygen.getCells(1))
            .fluidInputs(Materials.Methanol.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(4_000))
            .duration(7 * SECONDS + 4 * TICKS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // C3H6O2 = 3C + 3H2 + O2

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .itemOutputs(Materials.Carbon.getDust(3), Materials.Oxygen.getCells(2))
            .fluidInputs(Materials.MethylAcetate.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(6_000))
            .duration(13 * SECONDS + 4 * TICKS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // HNO3 = H + N + 3/2 O2

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .itemOutputs(Materials.Hydrogen.getCells(1), Materials.Nitrogen.getCells(1))
            .fluidInputs(Materials.NitricAcid.getFluid(1_000))
            .fluidOutputs(Materials.Oxygen.getGas(3_000))
            .duration(12 * SECONDS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // C6H6O = 6C + 3H2 + 1/2 O2

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .itemOutputs(Materials.Carbon.getDust(6), Materials.Oxygen.getCells(1))
            .fluidInputs(Materials.Phenol.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(6_000))
            .duration(15 * SECONDS + 12 * TICKS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // C8H8 = 8C + 4H2

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Carbon.getDust(8))
            .fluidInputs(Materials.Styrene.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(8_000))
            .duration(19 * SECONDS + 4 * TICKS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // C(NO2)4 = C + 2N2 + 4O2

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(4))
            .itemOutputs(Materials.Carbon.getDust(1), Materials.Nitrogen.getCells(4))
            .fluidInputs(Materials.Tetranitromethane.getFluid(1_000))
            .fluidOutputs(Materials.Oxygen.getGas(8_000))
            .duration(36 * SECONDS + 8 * TICKS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // C4H6O2 = 4C + 3H2 + O2

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .itemOutputs(Materials.Carbon.getDust(4), Materials.Oxygen.getCells(2))
            .fluidInputs(Materials.VinylAcetate.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(6_000))
            .duration(14 * SECONDS + 8 * TICKS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // Si2Cl6 = 2Si + 3Cl2

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Silicon.getDust(2))
            .fluidInputs(Materials.Hexachlorodisilane.getFluid(1_000))
            .fluidOutputs(Materials.Chlorine.getGas(6_000))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // C8H10 = 8C + 5H2 (1,2-Dimethylbenzene)

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Carbon.getDust(8))
            .fluidInputs(Materials.Dimethylbenzene.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(10_000))
            .duration(21 * SECONDS + 12 * TICKS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // C4H8O = 4C + 4H2 + 1/2 O2

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .itemOutputs(Materials.Carbon.getDust(4), Materials.Oxygen.getCells(1))
            .fluidInputs(Materials.Isobutyraldehyde.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(8_000))
            .duration(15 * SECONDS + 12 * TICKS)
            .eut(90)
            .addTo(electrolyzerRecipes);

        // C8H10 = 8C + 5H2 (1,3-Dimethylbenzene)

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Carbon.getDust(8))
            .fluidInputs(Materials.IIIDimethylbenzene.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(10_000))
            .duration(21 * SECONDS + 12 * TICKS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // C8H10 = 8C + 5H2 (1,4-Dimethylbenzene)

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Carbon.getDust(8))
            .fluidInputs(Materials.IVDimethylbenzene.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(10_000))
            .duration(21 * SECONDS + 12 * TICKS)
            .eut(60)
            .addTo(electrolyzerRecipes);

        // H2S = S + 2H

        GTValues.RA.stdBuilder()
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, (int) (1)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydricSulfide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .duration(3 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, (int) (2)))
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.HydricSulfide, Materials2CellShapes.cell, (int) (1)),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials2Materials.Empty, 1))
            .duration(3 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        // SO2 = S + 2O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, (int) (1)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials2Materials.Empty, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.cell, (int) (2)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);
        // NaCl = Na +Cl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.dust, (int) (2)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.dust, (int) (1)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);
        // (NaCl·H2O)= NaOH + H

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials2Materials.Empty, 1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.SodiumHydroxideGT5U, Materials2Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, (int) (1)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SaltWater, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials2Materials.Empty, 1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.SodiumHydroxideGT5U, Materials2Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.cell, (int) (1)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SaltWater, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);
        // HCl = H + Cl

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials2Materials.Empty, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, (int) (1)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydrochloricAcidGT5U, Materials2FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials2Materials.Empty, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.cell, (int) (1)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydrochloricAcidGT5U, Materials2FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.HydrochloricAcidGT5U, Materials2CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, (int) (1)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.HydrochloricAcidGT5U, Materials2CellShapes.cell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.cell, (int) (1)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);
        // 2NaHSO4 = 2H + Na2S2O8

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SodiumBisulfate, Materials2Shapes.dust, (int) (14)),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials2Materials.Empty, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, (int) (2)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SodiumPersulfate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Lead, Materials2Shapes.dust, (int) (3)),
                MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, (int) (4)))
            .fluidInputs(new FluidStack(ItemList.sLeadZincSolution, 8000))
            .fluidOutputs(GTUtility.getWater(2_000))
            .duration(15 * SECONDS)
            .eut(192)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.cell, (int) (1)))
            .fluidInputs(new FluidStack(ItemList.sBlueVitriol, 2000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.cell, (int) (1)))
            .fluidInputs(new FluidStack(ItemList.sNickelSulfate, 2000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.cell, (int) (1)))
            .fluidInputs(new FluidStack(ItemList.sGreenVitriol, 2000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.PhosphoricAcidGT5U, Materials2CellShapes.cell, 1),
                ItemList.Cell_Empty.get(6L))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, (int) (3)),
                MaterialLibAPI.getStack(Materials2Materials.Phosphorus, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.cell, (int) (4)))
            .duration(27 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CalciumChloride,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (3_000)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Calcium, Materials2Shapes.dust, (int) (1)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

    }
}
