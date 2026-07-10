package gtnhlanth.loader;

import static gregtech.api.enums.OrePrefixes.*;
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
import static gtnhlanth.common.register.BotWerkstoffMaterialPool.*;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gtnhlanth.common.register.BotWerkstoffMaterialPool;

/*
 * Originally authored by botn365 under the MIT License. See BotdustriesLICENSE
 */

public class BotRecipes {

    public static void addGTRecipe() {

        // CaCO3 + 2HCl = H2O + CO2 + CaCl2
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.shapeDust, 5),
                Materials.Empty.getCells(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.CarbonDioxide, Materials2CellShapes.shapeCell, 1),
                WerkstoffLoader.CalciumChloride.get(dust, 3))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(2_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // tungsten chain
        FluidStack sodiumTungsten = SodiumTungstate.getFluidOrGas(1_000);
        ItemStack scheelite = MaterialLibAPI.getStack(Materials2Materials.Scheelite, Materials2Shapes.shapeDust, 6);

        // Li2WO4 + 2Na = Na2WO4 + 2Li
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Tungstate, Materials2Shapes.shapeDust, 7),
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Lithium, Materials2Shapes.shapeDust, 2))
            .fluidInputs(Materials.Water.getFluid(4_000))
            .fluidOutputs(sodiumTungsten)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(autoclaveRecipes);

        // MnWO4 + 2Na = Na2WO4 + Mn
        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffLoader.Huebnerit.get(dust, 6),
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Manganese, Materials2Shapes.shapeDust, 1))
            .fluidInputs(Materials.Water.getFluid(4_000))
            .fluidOutputs(sodiumTungsten)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(autoclaveRecipes);

        // FeWO4 + 2Na = Na2WO4 + Fe
        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffLoader.Ferberite.get(dust, 6),
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.shapeDust, 1))
            .fluidInputs(Materials.Water.getFluid(4_000))
            .fluidOutputs(sodiumTungsten)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(autoclaveRecipes);

        // CaCl2 + Na2WO4 = 2NaCl + CaWO4
        ItemStack Ca2Cl = WerkstoffLoader.CalciumChloride.get(dust, 3);
        GTValues.RA.stdBuilder()
            .itemInputs(Ca2Cl)
            .itemOutputs(scheelite, MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.shapeDust, 4))
            .fluidInputs(sodiumTungsten)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        ItemStack H2WO4 = TungsticAcid.get(dust, 7);
        // CaWO4 + 2HCl = H2WO4 + CaCl2
        GTValues.RA.stdBuilder()
            .itemInputs(scheelite)
            .itemOutputs(H2WO4, Ca2Cl)
            .fluidInputs(Materials.HydrochloricAcid.getFluid(2_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        ItemStack WO3 = TungstenTrioxide.get(dust, 4);
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
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Tungsten, Materials2Shapes.shapeDust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 6_000))
            .fluidOutputs(Materials.Steam.getGas(3_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1000)
            .addTo(blastFurnaceRecipes);

        // 2WO3 + 3C = 2W + 3CO2
        GTValues.RA.stdBuilder()
            .itemInputs(
                TungstenTrioxide.get(dust, 8),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, 3))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Tungsten, Materials2Shapes.shapeIngotHot, 2))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 3_000))
            .duration(6 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 3000)
            .addTo(blastFurnaceRecipes);

        // rocket fuels
        // LMP103S
        // 2Cl + CO = COCl2

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.CarbonMonoxide, Materials2CellShapes.shapeCell, 1))
            .circuit(12)
            .itemOutputs(Phosgene.get(cell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 2_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 2))
            .circuit(12)
            .itemOutputs(Phosgene.get(cell, 1), Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.CarbonMonoxide, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 2))
            .itemOutputs(Phosgene.get(cell, 1), Materials.Empty.getCells(2))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 2))
            .circuit(2)
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(BotWerkstoffMaterialPool.Phosgene.getFluidOrGas(1_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // H3PO4 = P + H2O
        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Phosphorus, Materials2Shapes.shapeDust, 1))
            .fluidInputs(Materials.PhosphoricAcid.getFluid(1_000))
            .fluidOutputs(Materials.Water.getFluid(500))
            .eut(TierEU.RECIPE_HV)
            .duration(1 * SECONDS)
            .addTo(distilleryRecipes);

        ItemStack cells = ItemList.Cell_Empty.get(1);
        cells.stackSize = 1;
        // NH4Cl = HCl + NH3
        GTValues.RA.stdBuilder()
            .itemInputs(cells)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ammonia, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(WerkstoffLoader.AmmoniumChloride.getFluidOrGas(1_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1_000))
            .eut(TierEU.RECIPE_MV)
            .duration(2 * SECONDS + 10 * TICKS)
            .addTo(distilleryRecipes);

        // N2H4O3 + NaOH = NaNO3 + NH3 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(AmmoniumNitrate.get(dust, 9), Materials.SodiumHydroxide.getDust(3))
            .itemOutputs(WerkstoffLoader.SodiumNitrate.get(dust, 5))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(AmmoniumNitrate.get(dust, 9), Materials.SodiumHydroxide.getDust(3))
            .circuit(2)
            .itemOutputs(WerkstoffLoader.SodiumNitrate.get(dust, 5))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 1_000),
                Materials.Water.getFluid(1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // N2H4O3 + NaOH + H =H2SO4= NH3 + HNO3 + Na + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(AmmoniumNitrate.get(dust, 9), Materials.SodiumHydroxide.getDust(3))
            .circuit(24)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // 2HNO3 + C3H8 = 2CH3NO2 + 2H2O + C
        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(Nitromethane.getFluidOrGas(2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(crackingRecipes);
    }
}
