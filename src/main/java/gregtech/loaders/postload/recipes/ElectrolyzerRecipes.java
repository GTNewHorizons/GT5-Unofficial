package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class ElectrolyzerRecipes implements Runnable {

    @Override
    public void run() {
        // H2O = 2H + O

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1), ItemList.Cell_Empty.get(1L))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L))
            .fluidInputs(Materials.Water.getFluid(1000L))
            .fluidOutputs(Materials.Hydrogen.getGas(2000L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(30)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(2), ItemList.Cell_Empty.get(1L))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L))
            .fluidInputs(GT_ModHandler.getDistilledWater(1000L))
            .fluidOutputs(Materials.Hydrogen.getGas(2000L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(30)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(3), ItemList.Cell_Empty.get(2L))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L))
            .fluidInputs(Materials.Water.getFluid(1000L))
            .fluidOutputs(Materials.Oxygen.getGas(1000L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(30)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(4), ItemList.Cell_Empty.get(2L))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L))
            .fluidInputs(GT_ModHandler.getDistilledWater(1000L))
            .fluidOutputs(Materials.Oxygen.getGas(1000L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(30)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_ModHandler.getIC2Item("electrolyzedWaterCell", 1L), ItemList.Cell_Empty.get(2L))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(30)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_ModHandler.getIC2Item("electrolyzedWaterCell", 1L), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L))
            .fluidOutputs(Materials.Hydrogen.getGas(2000L))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(30)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L))
            .itemOutputs(GT_ModHandler.getIC2Item("electrolyzedWaterCell", 1L))
            .duration(1 * MINUTES + 13 * SECONDS + 10 * TICKS)
            .eut(30)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Dye_Bonemeal.get(3L))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L))
            .duration(4 * SECONDS + 18 * TICKS)
            .eut(26)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.sand, 8, 0))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3L))
            .duration(25 * SECONDS)
            .eut(25)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.sand, 8, 1))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3L))
            .duration(25 * SECONDS)
            .eut(25)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 4))
            .duration(5 * SECONDS)
            .eut(64)
            .addTo(electrolyzerRecipes);
        // ZnS = Zn + S + 1 Ga(9.17%)

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sphalerite, 2))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gallium, 1))
            .outputChances(10000, 10000, 917)
            .duration(10 * SECONDS)
            .eut(30)
            .addTo(electrolyzerRecipes);
        // IC2 Fertilizer = H2O + CaCO3 + C

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Fertilizer.get(1L))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L))
            .fluidOutputs(Materials.Water.getFluid(1000L))
            .duration(5 * SECONDS)
            .eut(30)
            .addTo(electrolyzerRecipes);
        // NaOH = Na + O + H

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.SodiumHydroxide.getDust(3), Materials.Empty.getCells(1))
            .itemOutputs(Materials.Sodium.getDust(1), Materials.Hydrogen.getCells(1))
            .outputChances(10000, 10000)
            .fluidOutputs(Materials.Oxygen.getGas(1000))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);
        // CO2 = C + 2O

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(Materials.Carbon.getDust(1))
            .fluidInputs(Materials.CarbonDioxide.getGas(1000))
            .fluidOutputs(Materials.Oxygen.getGas(2000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(11), Materials.Empty.getCells(2))
            .itemOutputs(Materials.Carbon.getDust(1), Materials.Oxygen.getCells(2))
            .fluidInputs(Materials.CarbonDioxide.getGas(1000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);
        // CO = C + O

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(Materials.Carbon.getDust(1))
            .fluidInputs(Materials.CarbonMonoxide.getGas(1000))
            .fluidOutputs(Materials.Oxygen.getGas(1000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(11), Materials.Empty.getCells(1))
            .itemOutputs(Materials.Carbon.getDust(1), Materials.Oxygen.getCells(1))
            .fluidInputs(Materials.CarbonMonoxide.getGas(1000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.CarbonMonoxide.getCells(1))
            .itemOutputs(Materials.Carbon.getDust(1), Materials.Oxygen.getCells(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        // H2S = S + 2H

        GT_Values.RA.stdBuilder()
            .itemOutputs(Materials.Sulfur.getDust(1))
            .fluidInputs(Materials.HydricSulfide.getGas(1000))
            .fluidOutputs(Materials.Hydrogen.getGas(2000))
            .duration(3 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemOutputs(Materials.Sulfur.getDust(1), Materials.Hydrogen.getCells(2))
            .itemInputs(Materials.HydricSulfide.getCells(1), Materials.Empty.getCells(1))
            .duration(3 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        // SO2 = S + 2O

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(Materials.Sulfur.getDust(1))
            .fluidInputs(Materials.SulfurDioxide.getGas(1000))
            .fluidOutputs(Materials.Oxygen.getGas(2000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(11), Materials.Empty.getCells(2))
            .itemOutputs(Materials.Sulfur.getDust(1), Materials.Oxygen.getCells(2))
            .fluidInputs(Materials.SulfurDioxide.getGas(1000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);
        // NaCl = Na +Cl

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Salt.getDust(2))
            .itemOutputs(Materials.Sodium.getDust(1))
            .fluidOutputs(Materials.Chlorine.getGas(1000))
            .duration(16 * SECONDS)
            .eut(30)
            .addTo(electrolyzerRecipes);
        // (NaCl·H2O)= NaOH + H

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(Materials.SodiumHydroxide.getDust(3), Materials.Hydrogen.getCells(1))
            .fluidInputs(Materials.SaltWater.getFluid(1000))
            .fluidOutputs(Materials.Chlorine.getGas(1000))
            .duration(36 * SECONDS)
            .eut(30)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1), GT_Utility.getIntegratedCircuit(11))
            .itemOutputs(Materials.SodiumHydroxide.getDust(3), Materials.Chlorine.getCells(1))
            .fluidInputs(Materials.SaltWater.getFluid(1000))
            .fluidOutputs(Materials.Hydrogen.getGas(1000))
            .duration(36 * SECONDS)
            .eut(30)
            .addTo(electrolyzerRecipes);
        // HCl = H + Cl

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(Materials.Hydrogen.getCells(1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1000))
            .fluidOutputs(Materials.Chlorine.getGas(1000))
            .duration(36 * SECONDS)
            .eut(30)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1), GT_Utility.getIntegratedCircuit(11))
            .itemOutputs(Materials.Chlorine.getCells(1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1000))
            .fluidOutputs(Materials.Hydrogen.getGas(1000))
            .duration(36 * SECONDS)
            .eut(30)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.HydrochloricAcid.getCells(1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(Materials.Hydrogen.getCells(1))
            .fluidOutputs(Materials.Chlorine.getGas(1000))
            .duration(36 * SECONDS)
            .eut(30)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.HydrochloricAcid.getCells(1), GT_Utility.getIntegratedCircuit(11))
            .itemOutputs(Materials.Chlorine.getCells(1))
            .fluidOutputs(Materials.Hydrogen.getGas(1000))
            .duration(36 * SECONDS)
            .eut(30)
            .addTo(electrolyzerRecipes);
        // 2NaHSO4 = 2H + Na2S2O8

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.SodiumBisulfate.getDust(14), Materials.Empty.getCells(2))
            .itemOutputs(Materials.Hydrogen.getCells(2))
            .fluidOutputs(Materials.SodiumPersulfate.getFluid(1000))
            .duration(30 * SECONDS)
            .eut(30)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 4))
            .fluidInputs(new FluidStack(ItemList.sLeadZincSolution, 8000))
            .fluidOutputs(Materials.Water.getFluid(2000))
            .duration(15 * SECONDS)
            .eut(192)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1))
            .fluidInputs(new FluidStack(ItemList.sBlueVitriol, 2000))
            .fluidOutputs(Materials.SulfuricAcid.getFluid(1000))
            .duration(45 * SECONDS)
            .eut(30)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1))
            .fluidInputs(new FluidStack(ItemList.sNickelSulfate, 2000))
            .fluidOutputs(Materials.SulfuricAcid.getFluid(1000))
            .duration(45 * SECONDS)
            .eut(30)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1))
            .fluidInputs(new FluidStack(ItemList.sGreenVitriol, 2000))
            .fluidOutputs(Materials.SulfuricAcid.getFluid(1000))
            .duration(45 * SECONDS)
            .eut(30)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.PhosphoricAcid, 1L),
                ItemList.Cell_Empty.get(6L))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 1L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 4L))
            .duration(27 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

    }
}
