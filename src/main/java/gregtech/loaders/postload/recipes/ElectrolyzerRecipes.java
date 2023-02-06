package gregtech.loaders.postload.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class ElectrolyzerRecipes implements Runnable {

    @Override
    public void run() {
        // H2O = 2H + O
        GT_Values.RA.addElectrolyzerRecipe(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Cell_Empty.get(1L),
                Materials.Water.getFluid(1000L),
                Materials.Hydrogen.getGas(2000L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                2000,
                30);
        GT_Values.RA.addElectrolyzerRecipe(
                GT_Utility.getIntegratedCircuit(2),
                ItemList.Cell_Empty.get(1L),
                GT_ModHandler.getDistilledWater(1000L),
                Materials.Hydrogen.getGas(2000L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                2000,
                30);
        GT_Values.RA.addElectrolyzerRecipe(
                GT_Utility.getIntegratedCircuit(3),
                ItemList.Cell_Empty.get(2L),
                Materials.Water.getFluid(1000L),
                Materials.Oxygen.getGas(1000L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                2000,
                30);
        GT_Values.RA.addElectrolyzerRecipe(
                GT_Utility.getIntegratedCircuit(4),
                ItemList.Cell_Empty.get(2L),
                GT_ModHandler.getDistilledWater(1000L),
                Materials.Oxygen.getGas(1000L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                2000,
                30);
        GT_Values.RA.addElectrolyzerRecipe(
                GT_ModHandler.getIC2Item("electrolyzedWaterCell", 1L),
                ItemList.Cell_Empty.get(2L),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                30,
                30);
        GT_Values.RA.addElectrolyzerRecipe(
                GT_ModHandler.getIC2Item("electrolyzedWaterCell", 1L),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                Materials.Hydrogen.getGas(2000L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                30,
                30);
        GT_Values.RA.addElectrolyzerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L),
                0,
                GT_ModHandler.getIC2Item("electrolyzedWaterCell", 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                1470,
                30);

        GT_Values.RA.addElectrolyzerRecipe(
                ItemList.Dye_Bonemeal.get(3L),
                0,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                98,
                26);
        GT_Values.RA.addElectrolyzerRecipe(
                new ItemStack(Blocks.sand, 8, 0),
                0,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                500,
                25);
        GT_Values.RA.addElectrolyzerRecipe(
                new ItemStack(Blocks.sand, 8, 1),
                0,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                500,
                25);

        GT_Values.RA.addElectrolyzerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1),
                0,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 4),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                100,
                64);
        // ZnS = Zn + S + 1 Ga(9.17%)
        GT_Values.RA.addElectrolyzerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sphalerite, 2),
                GT_Values.NI,
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gallium, 1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                new int[] { 10000, 10000, 917, 0, 0, 0 },
                200,
                30);
        // IC2 Fertilizer = H2O + CaCO3 + C
        GT_Values.RA.addElectrolyzerRecipe(
                ItemList.IC2_Fertilizer.get(1L),
                GT_Values.NI,
                GT_Values.NF,
                Materials.Water.getFluid(1000L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                100,
                30);

        // NaOH = Na + O + H
        GT_Values.RA.addElectrolyzerRecipe(
                Materials.SodiumHydroxide.getDust(3),
                Materials.Empty.getCells(1),
                GT_Values.NF,
                Materials.Oxygen.getGas(1000),
                Materials.Sodium.getDust(1),
                Materials.Hydrogen.getCells(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                new int[] { 10000, 10000 },
                1000,
                60);

        GT_Values.RA.addElectrolyzerRecipe(
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NI,
                Materials.CarbonDioxide.getGas(1000),
                Materials.Oxygen.getGas(2000),
                Materials.Carbon.getDust(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                300,
                120);
        GT_Values.RA.addElectrolyzerRecipe(
                GT_Utility.getIntegratedCircuit(11),
                Materials.Empty.getCells(2),
                Materials.CarbonDioxide.getGas(1000),
                GT_Values.NF,
                Materials.Carbon.getDust(1),
                Materials.Oxygen.getCells(2),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                300,
                120);
        // SO2 = S + 2O
        GT_Values.RA.addElectrolyzerRecipe(
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NI,
                Materials.SulfurDioxide.getGas(1000),
                Materials.Oxygen.getGas(2000),
                Materials.Sulfur.getDust(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                300,
                120);
        GT_Values.RA.addElectrolyzerRecipe(
                GT_Utility.getIntegratedCircuit(11),
                Materials.Empty.getCells(2),
                Materials.SulfurDioxide.getGas(1000),
                GT_Values.NF,
                Materials.Sulfur.getDust(1),
                Materials.Oxygen.getCells(2),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                300,
                120);
        // NaCl = Na +Cl
        GT_Values.RA.addElectrolyzerRecipe(
                Materials.Salt.getDust(2),
                GT_Values.NI,
                GT_Values.NF,
                Materials.Chlorine.getGas(1000),
                Materials.Sodium.getDust(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                320,
                30);

        // (NaCl·H2O)= NaOH + H
        GT_Values.RA.addElectrolyzerRecipe(
                Materials.Empty.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.SaltWater.getFluid(1000),
                Materials.Chlorine.getGas(1000),
                Materials.SodiumHydroxide.getDust(3),
                Materials.Hydrogen.getCells(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                720,
                30);
        GT_Values.RA.addElectrolyzerRecipe(
                Materials.Empty.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.SaltWater.getFluid(1000),
                Materials.Hydrogen.getGas(1000),
                Materials.SodiumHydroxide.getDust(3),
                Materials.Chlorine.getCells(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                720,
                30);
        // HCl = H + Cl
        GT_Values.RA.addElectrolyzerRecipe(
                Materials.Empty.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.Chlorine.getGas(1000),
                Materials.Hydrogen.getCells(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                720,
                30);
        GT_Values.RA.addElectrolyzerRecipe(
                Materials.Empty.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.Hydrogen.getGas(1000),
                Materials.Chlorine.getCells(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                720,
                30);

        GT_Values.RA.addElectrolyzerRecipe(
                Materials.HydrochloricAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                Materials.Chlorine.getGas(1000),
                Materials.Hydrogen.getCells(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                720,
                30);
        GT_Values.RA.addElectrolyzerRecipe(
                Materials.HydrochloricAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                GT_Values.NF,
                Materials.Hydrogen.getGas(1000),
                Materials.Chlorine.getCells(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                720,
                30);

        // 2NaHSO4 = 2H + Na2S2O8
        GT_Values.RA.addElectrolyzerRecipe(
                Materials.SodiumBisulfate.getDust(14),
                Materials.Empty.getCells(2),
                null,
                Materials.SodiumPersulfate.getFluid(1000),
                Materials.Hydrogen.getCells(2),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                600,
                30);

        GT_Values.RA.addElectrolyzerRecipe(
                GT_Values.NI,
                GT_Values.NI,
                new FluidStack(ItemList.sLeadZincSolution, 8000),
                Materials.Water.getFluid(2000),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 4),
                null,
                null,
                null,
                300,
                192);

        GT_Values.RA.addElectrolyzerRecipe(
                ItemList.Cell_Empty.get(1),
                null,
                new FluidStack(ItemList.sBlueVitriol, 2000),
                Materials.SulfuricAcid.getFluid(1000),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                900,
                30);
        GT_Values.RA.addElectrolyzerRecipe(
                ItemList.Cell_Empty.get(1),
                null,
                new FluidStack(ItemList.sNickelSulfate, 2000),
                Materials.SulfuricAcid.getFluid(1000),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                900,
                30);
        GT_Values.RA.addElectrolyzerRecipe(
                ItemList.Cell_Empty.get(1),
                null,
                new FluidStack(ItemList.sGreenVitriol, 2000),
                Materials.SulfuricAcid.getFluid(1000),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                900,
                30);
    }
}
