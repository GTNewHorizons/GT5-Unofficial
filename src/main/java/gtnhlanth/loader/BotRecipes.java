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

import gregtech.api.enums.materials2.FluidShapes;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Shapes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.CellShapes;
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
                MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, 5),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CarbonDioxide, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.CalciumChloride, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // tungsten chain
        FluidStack sodiumTungsten = MaterialLibAPI
            .getFluidStack(Materials.SodiumTungstate, FluidShapes.fluidLiquid, (int) (1_000));
        ItemStack scheelite = MaterialLibAPI.getStack(Materials.Scheelite, Shapes.dust, 6);

        // Li2WO4 + 2Na = Na2WO4 + 2Li
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Tungstate, Shapes.dust, 7),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Lithium, Shapes.dust, 2))
            .fluidInputs(GTUtility.getWater(4_000))
            .fluidOutputs(sodiumTungsten)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(autoclaveRecipes);

        // MnWO4 + 2Na = Na2WO4 + Mn
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Huebnerite, Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Manganese, Shapes.dust, 1))
            .fluidInputs(GTUtility.getWater(4_000))
            .fluidOutputs(sodiumTungsten)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(autoclaveRecipes);

        // FeWO4 + 2Na = Na2WO4 + Fe
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Ferberite, Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1))
            .fluidInputs(GTUtility.getWater(4_000))
            .fluidOutputs(sodiumTungsten)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(autoclaveRecipes);

        // CaCl2 + Na2WO4 = 2NaCl + CaWO4
        ItemStack Ca2Cl = MaterialLibAPI.getStack(Materials.CalciumChloride, Shapes.dust, 3);
        GTValues.RA.stdBuilder()
            .itemInputs(Ca2Cl)
            .itemOutputs(scheelite, MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 4))
            .fluidInputs(sodiumTungsten)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        ItemStack H2WO4 = MaterialLibAPI.getStack(Materials.TungsticAcid, Shapes.dust, 7);
        // CaWO4 + 2HCl = H2WO4 + CaCl2
        GTValues.RA.stdBuilder()
            .itemInputs(scheelite)
            .itemOutputs(H2WO4, Ca2Cl)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 2_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        ItemStack WO3 = MaterialLibAPI.getStack(Materials.TungstenTrioxide, Shapes.dust, 4);
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
            .itemOutputs(MaterialLibAPI.getStack(Materials.Tungsten, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 6_000))
            .fluidOutputs(MaterialUtils.gas(Materials.Steam, 3_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1000)
            .addTo(blastFurnaceRecipes);

        // 2WO3 + 3C = 2W + 3CO2
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.TungstenTrioxide, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 3))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Tungsten, Shapes.ingotHot, 2))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 3_000))
            .duration(6 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 3000)
            .addTo(blastFurnaceRecipes);

        // rocket fuels
        // LMP103S
        // 2Cl + CO = COCl2

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CarbonMonoxide, CellShapes.cell, 1))
            .circuit(12)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Phosgene, CellShapes.cell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 2_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Chlorine, CellShapes.cell, 2))
            .circuit(12)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Phosgene, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 1_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CarbonMonoxide, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.Chlorine, CellShapes.cell, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Phosgene, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Chlorine, CellShapes.cell, 2))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Phosgene, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // H3PO4 = P + H2O
        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.PhosphoricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(GTUtility.getWater(500))
            .eut(TierEU.RECIPE_HV)
            .duration(1 * SECONDS)
            .addTo(distilleryRecipes);

        ItemStack cells = ItemList.Cell_Empty.get(1);
        cells.stackSize = 1;
        // NH4Cl = HCl + NH3
        GTValues.RA.stdBuilder()
            .itemInputs(cells)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ammonia, CellShapes.cell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.AmmoniumChloride,
                    FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .eut(TierEU.RECIPE_MV)
            .duration(2 * SECONDS + 10 * TICKS)
            .addTo(distilleryRecipes);

        // N2H4O3 + NaOH = NaNO3 + NH3 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.AmmoniumNitrate, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumNitrate, Shapes.dust, 5))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.AmmoniumNitrate, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumNitrate, Shapes.dust, 5))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 1_000),
                GTUtility.getWater(1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // N2H4O3 + NaOH + H =H2SO4= NH3 + HNO3 + Na + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.AmmoniumNitrate, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3))
            .circuit(24)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // 2HNO3 + C3H8 = 2CH3NO2 + 2H2O + C
        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Nitromethane, FluidShapes.fluidLiquid, (int) (2_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(crackingRecipes);
    }
}
