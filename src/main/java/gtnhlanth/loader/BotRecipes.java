package gtnhlanth.loader;

import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.crackingRecipes;
import static gregtech.api.recipe.RecipeMaps.distilleryRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;

import gregtech.api.enums.materials2.Materials;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

/*
 * Originally authored by botn365 under the MIT License. See BotdustriesLICENSE
 */

public class BotRecipes {

    public static void addGTRecipe() {

        // CaCO3 + 2HCl = H2O + CO2 + CaCl2
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Calcite, Materials2Shapes.dust, 5),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CarbonDioxide, Materials2CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.CalciumChloride, Materials2Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HydrochloricAcidGT5U, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // tungsten chain
        FluidStack sodiumTungsten = MaterialLibAPI
            .getFluidStack(Materials.SodiumTungstate, Materials2FluidShapes.fluidLiquid, (int) (1_000));
        ItemStack scheelite = MaterialLibAPI.getStack(Materials.Scheelite, Materials2Shapes.dust, 6);

        // Li2WO4 + 2Na = Na2WO4 + 2Li
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Tungstate, Materials2Shapes.dust, 7),
                MaterialLibAPI.getStack(Materials.Sodium, Materials2Shapes.dust, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Lithium, Materials2Shapes.dust, 2))
            .fluidInputs(GTUtility.getWater(4_000))
            .fluidOutputs(sodiumTungsten)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(autoclaveRecipes);

        // MnWO4 + 2Na = Na2WO4 + Mn
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Huebnerite, Materials2Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.Sodium, Materials2Shapes.dust, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Manganese, Materials2Shapes.dust, 1))
            .fluidInputs(GTUtility.getWater(4_000))
            .fluidOutputs(sodiumTungsten)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(autoclaveRecipes);

        // FeWO4 + 2Na = Na2WO4 + Fe
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Ferberite, Materials2Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.Sodium, Materials2Shapes.dust, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Iron, Materials2Shapes.dust, 1))
            .fluidInputs(GTUtility.getWater(4_000))
            .fluidOutputs(sodiumTungsten)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(autoclaveRecipes);

        // CaCl2 + Na2WO4 = 2NaCl + CaWO4
        ItemStack Ca2Cl = MaterialLibAPI.getStack(Materials.CalciumChloride, Materials2Shapes.dust, 3);
        GTValues.RA.stdBuilder()
            .itemInputs(Ca2Cl)
            .itemOutputs(scheelite, MaterialLibAPI.getStack(Materials.Salt, Materials2Shapes.dust, 4))
            .fluidInputs(sodiumTungsten)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        ItemStack H2WO4 = MaterialLibAPI.getStack(Materials.TungsticAcid, Materials2Shapes.dust, 7);
        // CaWO4 + 2HCl = H2WO4 + CaCl2
        GTValues.RA.stdBuilder()
            .itemInputs(scheelite)
            .itemOutputs(H2WO4, Ca2Cl)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HydrochloricAcidGT5U, Materials2FluidShapes.fluidLiquid, 2_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        ItemStack WO3 = MaterialLibAPI.getStack(Materials.TungstenTrioxide, Materials2Shapes.dust, 4);
        // H2WO4 = WO3 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(H2WO4)
            .itemOutputs(WO3)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        // WO3 + 6H = W + 3H2O
        GTValues.RA.stdBuilder()
            .itemInputs(WO3)
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Tungsten, Materials2Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, Materials2FluidShapes.fluidGas, 6_000))
            .fluidOutputs(MaterialUtils.gas(Materials.Steam, 3_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1000)
            .addTo(blastFurnaceRecipes);

        // 2WO3 + 3C = 2W + 3CO2
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.TungstenTrioxide, Materials2Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Carbon, Materials2Shapes.dust, 3))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Tungsten, Materials2Shapes.ingotHot, 2))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, 3_000))
            .duration(6 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 3000)
            .addTo(blastFurnaceRecipes);

        // rocket fuels
        // LMP103S
        // 2Cl + CO = COCl2

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CarbonMonoxide, Materials2CellShapes.cell, 1))
            .circuit(12)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Phosgene, Materials2CellShapes.cell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 2_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Chlorine, Materials2CellShapes.cell, 2))
            .circuit(12)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Phosgene, Materials2CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, Materials2FluidShapes.fluidGas, 1_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CarbonMonoxide, Materials2CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.Chlorine, Materials2CellShapes.cell, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Phosgene, Materials2CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Chlorine, Materials2CellShapes.cell, 2))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, Materials2FluidShapes.fluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Phosgene, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // H3PO4 = P + H2O
        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Phosphorus, Materials2Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.PhosphoricAcidGT5U, Materials2FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(GTUtility.getWater(500))
            .eut(TierEU.RECIPE_HV)
            .duration(1 * SECONDS)
            .addTo(distilleryRecipes);

        ItemStack cells = ItemList.Cell_Empty.get(1);
        cells.stackSize = 1;
        // NH4Cl = HCl + NH3
        GTValues.RA.stdBuilder()
            .itemInputs(cells)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ammonia, Materials2CellShapes.cell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.AmmoniumChloride,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HydrochloricAcidGT5U, Materials2FluidShapes.fluidLiquid, 1_000))
            .eut(TierEU.RECIPE_MV)
            .duration(2 * SECONDS + 10 * TICKS)
            .addTo(distilleryRecipes);

        // N2H4O3 + NaOH = NaNO3 + NH3 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.AmmoniumNitrate, Materials2Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Materials2Shapes.dust, 3))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumNitrate, Materials2Shapes.dust, 5))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Ammonia, Materials2FluidShapes.fluidGas, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.AmmoniumNitrate, Materials2Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Materials2Shapes.dust, 3))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumNitrate, Materials2Shapes.dust, 5))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Ammonia, Materials2FluidShapes.fluidGas, 1_000),
                GTUtility.getWater(1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // N2H4O3 + NaOH + H =H2SO4= NH3 + HNO3 + Na + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.AmmoniumNitrate, Materials2Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Materials2Shapes.dust, 3))
            .circuit(24)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Sodium, Materials2Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, Materials2FluidShapes.fluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Ammonia, Materials2FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials.DilutedSulfuricAcid, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // 2HNO3 + C3H8 = 2CH3NO2 + 2H2O + C
        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Propane, Materials2FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Nitromethane, Materials2FluidShapes.fluidLiquid, (int) (2_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(crackingRecipes);
    }
}
