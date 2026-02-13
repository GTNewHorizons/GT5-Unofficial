package gregtech.loaders.postload.recipes;

import static bartworks.system.material.WerkstoffLoader.CalciumChloride;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
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
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(2_000))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1L))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L))
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(2_000))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(2L))
            .circuit(3)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(Materials.Oxygen.getGas(1_000))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(2L))
            .circuit(4)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L))
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .fluidOutputs(Materials.Oxygen.getGas(1_000))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L), ItemList.Cell_Empty.get(2L))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Dye_Bonemeal.get(3L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L))
            .duration(4 * SECONDS + 18 * TICKS)
            .eut(26)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.sand, 8, 0))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3L))
            .duration(25 * SECONDS)
            .eut(25)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.sand, 8, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3L))
            .duration(25 * SECONDS)
            .eut(25)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 4))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(electrolyzerRecipes);
        // ZnS = Zn + S + 1 Ga(9.17%)

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sphalerite, 2))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gallium, 1))
            .outputChances(10000, 10000, 917)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);
        // NaOH = Na + O + H

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SodiumHydroxide.getDust(3), Materials.Empty.getCells(1))
            .itemOutputs(Materials.Sodium.getDust(1), Materials.Hydrogen.getCells(1))
            .outputChances(10000, 10000)
            .fluidOutputs(Materials.Oxygen.getGas(1_000))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);
        // CO2 = C + 2O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(Materials.Carbon.getDust(1))
            .fluidInputs(Materials.CarbonDioxide.getGas(1_000))
            .fluidOutputs(Materials.Oxygen.getGas(2_000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .circuit(11)
            .itemOutputs(Materials.Carbon.getDust(1), Materials.Oxygen.getCells(2))
            .fluidInputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);
        // CO = C + O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(Materials.Carbon.getDust(1))
            .fluidInputs(Materials.CarbonMonoxide.getGas(1_000))
            .fluidOutputs(Materials.Oxygen.getGas(1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .circuit(11)
            .itemOutputs(Materials.Carbon.getDust(1), Materials.Oxygen.getCells(1))
            .fluidInputs(Materials.CarbonMonoxide.getGas(1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.CarbonMonoxide.getCells(1))
            .itemOutputs(Materials.Carbon.getDust(1), Materials.Oxygen.getCells(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        // H2S = S + 2H

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Sulfur.getDust(1))
            .fluidInputs(Materials.HydricSulfide.getGas(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(2_000))
            .duration(3 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Sulfur.getDust(1), Materials.Hydrogen.getCells(2))
            .itemInputs(Materials.HydricSulfide.getCells(1), Materials.Empty.getCells(1))
            .duration(3 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        // SO2 = S + 2O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(Materials.Sulfur.getDust(1))
            .fluidInputs(Materials.SulfurDioxide.getGas(1_000))
            .fluidOutputs(Materials.Oxygen.getGas(2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .circuit(11)
            .itemOutputs(Materials.Sulfur.getDust(1), Materials.Oxygen.getCells(2))
            .fluidInputs(Materials.SulfurDioxide.getGas(1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);
        // NaCl = Na +Cl

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Salt.getDust(2))
            .itemOutputs(Materials.Sodium.getDust(1))
            .fluidOutputs(Materials.Chlorine.getGas(1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);
        // (NaCl·H2O)= NaOH + H

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .circuit(1)
            .itemOutputs(Materials.SodiumHydroxide.getDust(3), Materials.Hydrogen.getCells(1))
            .fluidInputs(Materials.SaltWater.getFluid(1_000))
            .fluidOutputs(Materials.Chlorine.getGas(1_000))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .circuit(11)
            .itemOutputs(Materials.SodiumHydroxide.getDust(3), Materials.Chlorine.getCells(1))
            .fluidInputs(Materials.SaltWater.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(1_000))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);
        // HCl = H + Cl

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .circuit(1)
            .itemOutputs(Materials.Hydrogen.getCells(1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
            .fluidOutputs(Materials.Chlorine.getGas(1_000))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .circuit(11)
            .itemOutputs(Materials.Chlorine.getCells(1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(1_000))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrochloricAcid.getCells(1))
            .circuit(1)
            .itemOutputs(Materials.Hydrogen.getCells(1))
            .fluidOutputs(Materials.Chlorine.getGas(1_000))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrochloricAcid.getCells(1))
            .circuit(11)
            .itemOutputs(Materials.Chlorine.getCells(1))
            .fluidOutputs(Materials.Hydrogen.getGas(1_000))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);
        // 2NaHSO4 = 2H + Na2S2O8

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SodiumBisulfate.getDust(14), Materials.Empty.getCells(2))
            .itemOutputs(Materials.Hydrogen.getCells(2))
            .fluidOutputs(Materials.SodiumPersulfate.getFluid(1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 4))
            .fluidInputs(new FluidStack(ItemList.sLeadZincSolution, 8000))
            .fluidOutputs(Materials.Water.getFluid(2_000))
            .duration(15 * SECONDS)
            .eut(192)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1))
            .fluidInputs(new FluidStack(ItemList.sBlueVitriol, 2000))
            .fluidOutputs(Materials.SulfuricAcid.getFluid(1_000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1))
            .fluidInputs(new FluidStack(ItemList.sNickelSulfate, 2000))
            .fluidOutputs(Materials.SulfuricAcid.getFluid(1_000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1))
            .fluidInputs(new FluidStack(ItemList.sGreenVitriol, 2000))
            .fluidOutputs(Materials.SulfuricAcid.getFluid(1_000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.PhosphoricAcid, 1L),
                ItemList.Cell_Empty.get(6L))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 3L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 1L),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 4L))
            .duration(27 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(CalciumChloride.getFluidOrGas(3_000))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(2_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

    }
}
