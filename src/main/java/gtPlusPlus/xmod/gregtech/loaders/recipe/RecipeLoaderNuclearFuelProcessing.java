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
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialParts;
import gtPlusPlus.core.fluids.GTPPFluids;

public class RecipeLoaderNuclearFuelProcessing {

    public static void generate() {

        // Create Fuels

        // 7LiF - BeF2 - ZrF4 - U235 - 590C
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.LithiumFluoride, FluidShapes.fluidLiquid, 550),
                MaterialLibAPI.getFluidStack(Materials.BerylliumFluoride, FluidShapes.fluidLiquid, 150),
                MaterialLibAPI.getFluidStack(Materials.ZirconiumTetrafluoride, FluidShapes.fluidLiquid, 60),
                MaterialLibAPI.getFluidStack(Materials.Uranium235, FluidShapes.fluidMolten, 240))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.LFTRFuel1, FluidShapes.fluidLiquid, 1000))
            .duration(30 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(fissionFuelProcessingRecipes);
        // 7LiF - BeF2 - ZrF4 - UF4 - 650C
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.LithiumFluoride, FluidShapes.fluidLiquid, 600),
                MaterialLibAPI.getFluidStack(Materials.BerylliumFluoride, FluidShapes.fluidLiquid, 250),
                MaterialLibAPI.getFluidStack(Materials.ZirconiumTetrafluoride, FluidShapes.fluidLiquid, 80),
                MaterialLibAPI.getFluidStack(Materials.UraniumTetrafluoride, FluidShapes.fluidLiquid, 70))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.LFTRFuel2, FluidShapes.fluidLiquid, 1000))
            .duration(40 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(fissionFuelProcessingRecipes);
        // 7liF - BeF2 - ThF4 - UF4 - 566C
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.LithiumFluoride, FluidShapes.fluidLiquid, 580),
                MaterialLibAPI.getFluidStack(Materials.BerylliumFluoride, FluidShapes.fluidLiquid, 270),
                MaterialLibAPI.getFluidStack(Materials.ThoriumTetrafluoride, FluidShapes.fluidLiquid, 80),
                MaterialLibAPI.getFluidStack(Materials.UraniumTetrafluoride, FluidShapes.fluidLiquid, 70))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.LFTRFuel3, FluidShapes.fluidLiquid, 1000))
            .duration(50 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(fissionFuelProcessingRecipes);

        // Reprocess Fuels

        // Reactor Blanket step 1 - Fluorination
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Fluorine, 6))
            .circuit(17)
            .itemOutputs(
                ItemList.Cell_Empty.get(5),
                MaterialLibAPI.getStack(Materials.LithiumFluoride, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1))
            .outputChances(10000, 10000, 500, 500, 500, 250, 250, 250)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.ThoriumDepletedMoltenSaltTSalt, FluidShapes.fluidLiquid, 10000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.FluorinatedUraniumHexafluorideFUF6, FluidShapes.fluidLiquid, 1500))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(reactorProcessingUnitRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Fluorine, 6))
            .circuit(18)
            .itemOutputs(
                ItemList.Cell_Empty.get(4),
                MaterialLibAPI.getStack(Materials.LithiumFluoride, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.BerylliumFluoride, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1))
            .outputChances(10000, 10000, 10000, 1000, 1000, 1000, 500, 500, 500)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.ThoriumBerylliumDepletedMoltenSaltTBSalt, FluidShapes.fluidLiquid, 10000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.FluorinatedUraniumHexafluorideFUF6, FluidShapes.fluidLiquid, 3000))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(reactorProcessingUnitRecipes);
        // Reactor Blanket step 1 - Fluorination
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Fluorine, 6))
            .circuit(7)
            .itemOutputs(
                ItemList.Cell_Empty.get(4),
                MaterialLibAPI.getStack(Materials.LithiumFluoride, CellShapes.cell, 2),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1))
            .outputChances(10000, 10000, 1000, 1000, 1000, 1000, 1000, 1000)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FluorineSpargedTSalt, FluidShapes.fluidLiquid, 10000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.FluorinatedUraniumHexafluorideFUF6, FluidShapes.fluidLiquid, 3000))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(reactorProcessingUnitRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Fluorine, 6))
            .circuit(8)
            .itemOutputs(
                ItemList.Cell_Empty.get(2),
                MaterialLibAPI.getStack(Materials.LithiumFluoride, CellShapes.cell, 2),
                MaterialLibAPI.getStack(Materials.BerylliumFluoride, CellShapes.cell, 2),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Protactinium, Shapes.dust, 1))
            .outputChances(10000, 10000, 10000, 2000, 2000, 2000, 2000, 2000, 2000)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FluorineSpargedTBSalt, FluidShapes.fluidLiquid, 10000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.FluorinatedUraniumHexafluorideFUF6, FluidShapes.fluidLiquid, 6000))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(reactorProcessingUnitRecipes);

        // Reactor Blanket step 2 - Sorption + Cold Trap
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumFluoride, CellShapes.cell, 4))
            .circuit(8)
            .itemOutputs(
                MaterialParts.requireCell(Materials.Fluorine, 2),
                MaterialLibAPI.getStack(Materials.UraniumHexafluoride, CellShapes.cell, 2),
                MaterialLibAPI.getStack(Materials.Uranium233, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Uranium233, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Uranium233, Shapes.dust, 1))
            .outputChances(10000, 10000, 3000, 2000, 1000)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.FluorinatedUraniumHexafluorideFUF6, FluidShapes.fluidLiquid, 3000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SodiumFluoride, FluidShapes.fluidLiquid, 2000))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(coldTrapRecipes);

        // LiBeF2UF4FP + F2 = LiFBeF2 & UF6F2FP
        // Reactor Core step 1 - Process Burnt Salt
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Fluorine, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.PhosphorousUraniumHexafluoridePUF6, CellShapes.cell, 1))
            .outputChances(10000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.UraniumDepletedMoltenSaltUSalt, FluidShapes.fluidLiquid, 1000))
            .fluidOutputs(new FluidStack(GTPPFluids.ImpureMoltenSaltBase, 1000))
            .duration(2 * HOURS)
            .eut(TierEU.RECIPE_HV)
            .addTo(reactorProcessingUnitRecipes);
        // LiBeF2UF4FP + F2 = LiFBeF2 & UF6F2FP
        // Reactor Core step 1 - Process Burnt Salt
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Fluorine, 3))
            .circuit(1)
            .itemOutputs(
                ItemList.Cell_Empty.get(1),
                MaterialLibAPI.getStack(Materials.PhosphorousUraniumHexafluoridePUF6, CellShapes.cell, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HeliumSpargedUSalt, FluidShapes.fluidLiquid, 1000))
            .fluidOutputs(new FluidStack(GTPPFluids.ImpureMoltenSaltBase, 2000))
            .duration(60 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(reactorProcessingUnitRecipes);

        // Reactor Core step 2A - Sorption + Cold Trap
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumFluoride, CellShapes.cell, 3))
            .circuit(8)
            .itemOutputs(
                MaterialParts.requireCell(Materials.Fluorine, 1),
                MaterialLibAPI.getStack(Materials.UraniumHexafluoride, CellShapes.cell, 2),
                MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dust, 1))
            .outputChances(10000, 10000, 5000, 5000, 5000, 5000, 5000, 5000)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.PhosphorousUraniumHexafluoridePUF6, FluidShapes.fluidLiquid, 2000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SodiumFluoride, FluidShapes.fluidLiquid, 2000))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(coldTrapRecipes);

        // Reactor Core step 2B - Distillation
        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(GTPPFluids.ImpureMoltenSaltBase, 1000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.StableMoltenSaltBase, FluidShapes.fluidLiquid, 250))
            .duration(7 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(distillationTowerRecipes);

        // UF6 -> UF4 reduction
        // UF6 + LiFBeF2 + H2 -> LiFBeF2UF4 + HF
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.UraniumHexafluoride, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.StableMoltenSaltBase, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2_000))
            .itemOutputs(MaterialParts.requireCell(Materials.IndustrialStrengthHydrofluoricAcid, 2))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.LFTRFuelBase, FluidShapes.fluidLiquid, 3000))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 5400)
            .addTo(blastFurnaceRecipes);
        // Alternative recipe to the above, for chemplant, to not use cells

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.UraniumHexafluoride, FluidShapes.fluidLiquid, 1000),
                MaterialLibAPI.getFluidStack(Materials.StableMoltenSaltBase, FluidShapes.fluidLiquid, 1000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.LFTRFuelBase, FluidShapes.fluidLiquid, 3000),
                MaterialLibAPI
                    .getFluidStack(Materials.IndustrialStrengthHydrofluoricAcid, FluidShapes.fluidLiquid, 2000))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(CHEMPLANT_CASING_TIER, 4)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.ZirconiumTetrafluoride, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.LFTRFuelBase, FluidShapes.fluidLiquid, 900))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.LFTRFuel2, FluidShapes.fluidLiquid, 1000))
            .duration(12 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(fissionFuelProcessingRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.ThoriumTetrafluoride, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.LFTRFuelBase, FluidShapes.fluidLiquid, 900))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.LFTRFuel3, FluidShapes.fluidLiquid, 1000))
            .duration(15 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(fissionFuelProcessingRecipes);
    }
}
