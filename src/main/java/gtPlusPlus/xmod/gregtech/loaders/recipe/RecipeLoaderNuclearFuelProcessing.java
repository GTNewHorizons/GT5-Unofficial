package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalPlantRecipes;
import static gregtech.api.recipe.RecipeMaps.coldTrapRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.fissionFuelProcessingRecipes;
import static gregtech.api.recipe.RecipeMaps.reactorProcessingUnitRecipes;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeLoaderNuclearFuelProcessing {

    public static void generate() {

        // Create Fuels

        // 7LiF - BeF2 - ZrF4 - U235 - 590C
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialUtils.legacyGtppFluid(Materials2Materials.LithiumFluoride, 550),
                MaterialUtils.legacyGtppFluid(Materials2Materials.BerylliumFluoride, 150),
                MaterialUtils.legacyGtppFluid(Materials2Materials.ZirconiumTetrafluoride, 60),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Uranium235, Materials2FluidShapes.fluidMolten, (int) (240)))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials2Materials.LFTRFuel1, 1000))
            .duration(30 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(fissionFuelProcessingRecipes);
        // 7LiF - BeF2 - ZrF4 - UF4 - 650C
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialUtils.legacyGtppFluid(Materials2Materials.LithiumFluoride, 600),
                MaterialUtils.legacyGtppFluid(Materials2Materials.BerylliumFluoride, 250),
                MaterialUtils.legacyGtppFluid(Materials2Materials.ZirconiumTetrafluoride, 80),
                MaterialUtils.legacyGtppFluid(Materials2Materials.UraniumTetrafluoride, 70))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials2Materials.LFTRFuel2, 1000))
            .duration(40 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(fissionFuelProcessingRecipes);
        // 7liF - BeF2 - ThF4 - UF4 - 566C
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialUtils.legacyGtppFluid(Materials2Materials.LithiumFluoride, 580),
                MaterialUtils.legacyGtppFluid(Materials2Materials.BerylliumFluoride, 270),
                MaterialUtils.legacyGtppFluid(Materials2Materials.ThoriumTetrafluoride, 80),
                MaterialUtils.legacyGtppFluid(Materials2Materials.UraniumTetrafluoride, 70))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials2Materials.LFTRFuel3, 1000))
            .duration(50 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(fissionFuelProcessingRecipes);

        // Reprocess Fuels

        // Reactor Blanket step 1 - Fluorination
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.cell(Materials2Materials.Fluorine, 6))
            .circuit(17)
            .itemOutputs(
                ItemList.Cell_Empty.get(5),
                MaterialParts.cell(Materials2Materials.LithiumFluoride, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1))
            .outputChances(10000, 10000, 500, 500, 500, 250, 250, 250)
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials2Materials.ThoriumDepletedMoltenSaltTSalt, 10000))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials2Materials.FluorinatedUraniumHexafluorideFUF6, 1500))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(reactorProcessingUnitRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.cell(Materials2Materials.Fluorine, 6))
            .circuit(18)
            .itemOutputs(
                ItemList.Cell_Empty.get(4),
                MaterialParts.cell(Materials2Materials.LithiumFluoride, 1),
                MaterialParts.cell(Materials2Materials.BerylliumFluoride, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1))
            .outputChances(10000, 10000, 10000, 1000, 1000, 1000, 500, 500, 500)
            .fluidInputs(
                MaterialUtils.legacyGtppFluid(Materials2Materials.ThoriumBerylliumDepletedMoltenSaltTBSalt, 10000))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials2Materials.FluorinatedUraniumHexafluorideFUF6, 3000))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(reactorProcessingUnitRecipes);
        // Reactor Blanket step 1 - Fluorination
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.cell(Materials2Materials.Fluorine, 6))
            .circuit(7)
            .itemOutputs(
                ItemList.Cell_Empty.get(4),
                MaterialParts.cell(Materials2Materials.LithiumFluoride, 2),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1))
            .outputChances(10000, 10000, 1000, 1000, 1000, 1000, 1000, 1000)
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials2Materials.FluorineSpargedTSalt, 10000))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials2Materials.FluorinatedUraniumHexafluorideFUF6, 3000))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(reactorProcessingUnitRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.cell(Materials2Materials.Fluorine, 6))
            .circuit(8)
            .itemOutputs(
                ItemList.Cell_Empty.get(2),
                MaterialParts.cell(Materials2Materials.LithiumFluoride, 2),
                MaterialParts.cell(Materials2Materials.BerylliumFluoride, 2),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Protactinium, Materials2Shapes.dust, 1))
            .outputChances(10000, 10000, 10000, 2000, 2000, 2000, 2000, 2000, 2000)
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials2Materials.FluorineSpargedTBSalt, 10000))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials2Materials.FluorinatedUraniumHexafluorideFUF6, 6000))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(reactorProcessingUnitRecipes);

        // Reactor Blanket step 2 - Sorption + Cold Trap
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.cell(Materials2Materials.SodiumFluoride, 4))
            .circuit(8)
            .itemOutputs(
                MaterialParts.cell(Materials2Materials.Fluorine, 2),
                MaterialParts.cell(Materials2Materials.UraniumHexafluoride, 2),
                MaterialLibAPI.getStack(Materials2Materials.Uranium233, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Uranium233, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Uranium233, Materials2Shapes.dust, 1))
            .outputChances(10000, 10000, 3000, 2000, 1000)
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials2Materials.FluorinatedUraniumHexafluorideFUF6, 3000))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials2Materials.SodiumFluoride, 2000))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(coldTrapRecipes);

        // LiBeF2UF4FP + F2 = LiFBeF2 & UF6F2FP
        // Reactor Core step 1 - Process Burnt Salt
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.cell(Materials2Materials.Fluorine, 1))
            .circuit(1)
            .itemOutputs(MaterialParts.cell(Materials2Materials.PhosphorousUraniumHexafluoridePUF6, 1))
            .outputChances(10000)
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials2Materials.UraniumDepletedMoltenSaltUSalt, 1000))
            .fluidOutputs(new FluidStack(GTPPFluids.ImpureMoltenSaltBase, 1000))
            .duration(2 * HOURS)
            .eut(TierEU.RECIPE_HV)
            .addTo(reactorProcessingUnitRecipes);
        // LiBeF2UF4FP + F2 = LiFBeF2 & UF6F2FP
        // Reactor Core step 1 - Process Burnt Salt
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.cell(Materials2Materials.Fluorine, 3))
            .circuit(1)
            .itemOutputs(
                ItemList.Cell_Empty.get(1),
                MaterialParts.cell(Materials2Materials.PhosphorousUraniumHexafluoridePUF6, 2))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials2Materials.HeliumSpargedUSalt, 1000))
            .fluidOutputs(new FluidStack(GTPPFluids.ImpureMoltenSaltBase, 2000))
            .duration(60 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(reactorProcessingUnitRecipes);

        // Reactor Core step 2A - Sorption + Cold Trap
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.cell(Materials2Materials.SodiumFluoride, 3))
            .circuit(8)
            .itemOutputs(
                MaterialParts.cell(Materials2Materials.Fluorine, 1),
                MaterialParts.cell(Materials2Materials.UraniumHexafluoride, 2),
                MaterialLibAPI.getStack(Materials2Materials.Phosphorus, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Phosphorus, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Phosphorus, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Phosphorus, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Phosphorus, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Phosphorus, Materials2Shapes.dust, 1))
            .outputChances(10000, 10000, 5000, 5000, 5000, 5000, 5000, 5000)
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials2Materials.PhosphorousUraniumHexafluoridePUF6, 2000))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials2Materials.SodiumFluoride, 2000))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(coldTrapRecipes);

        // Reactor Core step 2B - Distillation
        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(GTPPFluids.ImpureMoltenSaltBase, 1000))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials2Materials.StableMoltenSaltBase, 250))
            .duration(7 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(distillationTowerRecipes);

        // UF6 -> UF4 reduction
        // UF6 + LiFBeF2 + H2 -> LiFBeF2UF4 + HF
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.cell(Materials2Materials.UraniumHexafluoride, 1),
                MaterialParts.cell(Materials2Materials.StableMoltenSaltBase, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 2))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials2Materials.LFTRFuelBase, 3000))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 5400)
            .addTo(blastFurnaceRecipes);
        // Alternative recipe to the above, for chemplant, to not use cells

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialUtils.legacyGtppFluid(Materials2Materials.UraniumHexafluoride, 1000),
                MaterialUtils.legacyGtppFluid(Materials2Materials.StableMoltenSaltBase, 1000),
                MaterialUtils.legacyGtppFluid(Materials2Materials.Hydrogen, 2000))
            .fluidOutputs(
                MaterialUtils.legacyGtppFluid(Materials2Materials.LFTRFuelBase, 3000),
                new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 2000))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(CHEMPLANT_CASING_TIER, 4)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialUtils.legacyGtppFluid(Materials2Materials.ZirconiumTetrafluoride, 100),
                MaterialUtils.legacyGtppFluid(Materials2Materials.LFTRFuelBase, 900))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials2Materials.LFTRFuel2, 1000))
            .duration(12 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(fissionFuelProcessingRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialUtils.legacyGtppFluid(Materials2Materials.ThoriumTetrafluoride, 100),
                MaterialUtils.legacyGtppFluid(Materials2Materials.LFTRFuelBase, 900))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials2Materials.LFTRFuel3, 1000))
            .duration(15 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(fissionFuelProcessingRecipes);
    }
}
