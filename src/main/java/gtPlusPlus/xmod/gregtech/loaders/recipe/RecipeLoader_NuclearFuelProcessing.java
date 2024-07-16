package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.HOURS;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeConstants.CHEMPLANT_CASING_TIER;
import static gregtech.api.util.GT_RecipeConstants.COIL_HEAT;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.coldTrapRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.fissionFuelProcessingRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.reactorProcessingUnitRecipes;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gtPlusPlus.core.item.chemistry.NuclearChem;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.nuclear.NUCLIDE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeLoader_NuclearFuelProcessing {

    public static void generate() {

        // Create Fuels

        final FluidStack aLithiumFluoride = FLUORIDES.LITHIUM_FLUORIDE.getFluidStack(100);
        final FluidStack aBerylliumFluoride = FLUORIDES.BERYLLIUM_FLUORIDE.getFluidStack(100);
        final FluidStack aThoriumFluoride = FLUORIDES.THORIUM_TETRAFLUORIDE.getFluidStack(100);
        final FluidStack aZirconiumFluoride = FLUORIDES.ZIRCONIUM_TETRAFLUORIDE.getFluidStack(100);
        final FluidStack aUraniumTetraFluoride = FLUORIDES.URANIUM_TETRAFLUORIDE.getFluidStack(100);
        final FluidStack aUranium235 = ELEMENT.getInstance().URANIUM235.getFluidStack(1000);
        final FluidStack aLiFBeF2ZrF4U235 = NUCLIDE.LiFBeF2ZrF4U235.getFluidStack(100);
        final FluidStack aLiFBeF2ZrF4UF4 = NUCLIDE.LiFBeF2ZrF4UF4.getFluidStack(100);
        final FluidStack aLiFBeF2ThF4UF4 = NUCLIDE.LiFBeF2ThF4UF4.getFluidStack(100);

        // 7LiF - BeF2 - ZrF4 - U235 - 590C
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                FluidUtils.getFluidStack(aLithiumFluoride, 550),
                FluidUtils.getFluidStack(aBerylliumFluoride, 150),
                FluidUtils.getFluidStack(aZirconiumFluoride, 60),
                FluidUtils.getFluidStack(aUranium235, 240))
            .fluidOutputs(FluidUtils.getFluidStack(aLiFBeF2ZrF4U235, 1000))
            .duration(30 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(fissionFuelProcessingRecipes);
        // 7LiF - BeF2 - ZrF4 - UF4 - 650C
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                FluidUtils.getFluidStack(aLithiumFluoride, 600),
                FluidUtils.getFluidStack(aBerylliumFluoride, 250),
                FluidUtils.getFluidStack(aZirconiumFluoride, 80),
                FluidUtils.getFluidStack(aUraniumTetraFluoride, 70))
            .fluidOutputs(FluidUtils.getFluidStack(aLiFBeF2ZrF4UF4, 1000))
            .duration(40 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(fissionFuelProcessingRecipes);
        // 7liF - BeF2 - ThF4 - UF4 - 566C
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                FluidUtils.getFluidStack(aLithiumFluoride, 580),
                FluidUtils.getFluidStack(aBerylliumFluoride, 270),
                FluidUtils.getFluidStack(aThoriumFluoride, 80),
                FluidUtils.getFluidStack(aUraniumTetraFluoride, 70))
            .fluidOutputs(FluidUtils.getFluidStack(aLiFBeF2ThF4UF4, 1000))
            .duration(50 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(fissionFuelProcessingRecipes);

        // Reprocess Fuels

        // Reactor Blanket step 1 - Fluorination
        GT_Values.RA.stdBuilder()
            .itemInputs(CI.getNumberedAdvancedCircuit(17), ELEMENT.getInstance().FLUORINE.getCell(6))
            .itemOutputs(
                CI.emptyCells(5),
                FLUORIDES.LITHIUM_FLUORIDE.getCell(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1))
            .outputChances(10000, 10000, 500, 500, 500, 250, 250, 250)
            .fluidInputs(NUCLIDE.LiFThF4.getFluidStack(10000))
            .fluidOutputs(NUCLIDE.UF6F2.getFluidStack(1500))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(reactorProcessingUnitRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(CI.getNumberedAdvancedCircuit(18), ELEMENT.getInstance().FLUORINE.getCell(6))
            .itemOutputs(
                CI.emptyCells(4),
                FLUORIDES.LITHIUM_FLUORIDE.getCell(1),
                FLUORIDES.BERYLLIUM_FLUORIDE.getCell(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1))
            .outputChances(10000, 10000, 10000, 1000, 1000, 1000, 500, 500, 500)
            .fluidInputs(NUCLIDE.LiFBeF2ThF4.getFluidStack(10000))
            .fluidOutputs(NUCLIDE.UF6F2.getFluidStack(3000))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(reactorProcessingUnitRecipes);
        // Reactor Blanket step 1 - Fluorination
        GT_Values.RA.stdBuilder()
            .itemInputs(CI.getNumberedAdvancedCircuit(7), ELEMENT.getInstance().FLUORINE.getCell(6))
            .itemOutputs(
                CI.emptyCells(4),
                FLUORIDES.LITHIUM_FLUORIDE.getCell(2),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1))
            .outputChances(10000, 10000, 1000, 1000, 1000, 1000, 1000, 1000)
            .fluidInputs(NUCLIDE.Sparged_LiFThF4.getFluidStack(10000))
            .fluidOutputs(NUCLIDE.UF6F2.getFluidStack(3000))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(reactorProcessingUnitRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(CI.getNumberedAdvancedCircuit(8), ELEMENT.getInstance().FLUORINE.getCell(6))
            .itemOutputs(
                CI.emptyCells(2),
                FLUORIDES.LITHIUM_FLUORIDE.getCell(2),
                FLUORIDES.BERYLLIUM_FLUORIDE.getCell(2),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                ELEMENT.getInstance().PROTACTINIUM.getDust(1))
            .outputChances(10000, 10000, 10000, 2000, 2000, 2000, 2000, 2000, 2000)
            .fluidInputs(NUCLIDE.Sparged_LiFBeF2ThF4.getFluidStack(10000))
            .fluidOutputs(NUCLIDE.UF6F2.getFluidStack(6000))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(reactorProcessingUnitRecipes);

        // Reactor Blanket step 2 - Sorption + Cold Trap
        GT_Values.RA.stdBuilder()
            .itemInputs(CI.getNumberedAdvancedCircuit(8), FLUORIDES.SODIUM_FLUORIDE.getCell(4))
            .itemOutputs(
                ELEMENT.getInstance().FLUORINE.getCell(2),
                FLUORIDES.URANIUM_HEXAFLUORIDE.getCell(2),
                ELEMENT.getInstance().URANIUM233.getDust(1),
                ELEMENT.getInstance().URANIUM233.getDust(1),
                ELEMENT.getInstance().URANIUM233.getDust(1))
            .outputChances(10000, 10000, 3000, 2000, 1000)
            .fluidInputs(NUCLIDE.UF6F2.getFluidStack(3000))
            .fluidOutputs(FLUORIDES.SODIUM_FLUORIDE.getFluidStack(2000))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(coldTrapRecipes);

        // LiBeF2UF4FP + F2 = LiFBeF2 & UF6F2FP
        // Reactor Core step 1 - Process Burnt Salt
        GT_Values.RA.stdBuilder()
            .itemInputs(CI.getNumberedAdvancedCircuit(1), ELEMENT.getInstance().FLUORINE.getCell(1))
            .itemOutputs(NUCLIDE.UF6F2FP.getCell(1))
            .outputChances(10000)
            .fluidInputs(NUCLIDE.LiFBeF2UF4FP.getFluidStack(1000))
            .fluidOutputs(FluidUtils.getFluidStack(NuclearChem.Impure_LiFBeF2, 1000))
            .duration(2 * HOURS)
            .eut(TierEU.RECIPE_HV)
            .addTo(reactorProcessingUnitRecipes);
        // LiBeF2UF4FP + F2 = LiFBeF2 & UF6F2FP
        // Reactor Core step 1 - Process Burnt Salt
        GT_Values.RA.stdBuilder()
            .itemInputs(CI.getNumberedAdvancedCircuit(1), ELEMENT.getInstance().FLUORINE.getCell(3))
            .itemOutputs(CI.emptyCells(1), NUCLIDE.UF6F2FP.getCell(2))
            .fluidInputs(NUCLIDE.Sparged_LiFBeF2UF4FP.getFluidStack(1000))
            .fluidOutputs(FluidUtils.getFluidStack(NuclearChem.Impure_LiFBeF2, 2000))
            .duration(60 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(reactorProcessingUnitRecipes);

        // Reactor Core step 2A - Sorption + Cold Trap
        GT_Values.RA.stdBuilder()
            .itemInputs(CI.getNumberedAdvancedCircuit(8), FLUORIDES.SODIUM_FLUORIDE.getCell(3))
            .itemOutputs(
                ELEMENT.getInstance().FLUORINE.getCell(1),
                FLUORIDES.URANIUM_HEXAFLUORIDE.getCell(2),
                ELEMENT.getInstance().PHOSPHORUS.getDust(1),
                ELEMENT.getInstance().PHOSPHORUS.getDust(1),
                ELEMENT.getInstance().PHOSPHORUS.getDust(1),
                ELEMENT.getInstance().PHOSPHORUS.getDust(1),
                ELEMENT.getInstance().PHOSPHORUS.getDust(1),
                ELEMENT.getInstance().PHOSPHORUS.getDust(1))
            .outputChances(10000, 10000, 5000, 5000, 5000, 5000, 5000, 5000)
            .fluidInputs(NUCLIDE.UF6F2FP.getFluidStack(2000))
            .fluidOutputs(FLUORIDES.SODIUM_FLUORIDE.getFluidStack(2000))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(coldTrapRecipes);

        // Reactor Core step 2B - Distillation
        GT_Values.RA.stdBuilder()
            .fluidInputs(FluidUtils.getFluidStack(NuclearChem.Impure_LiFBeF2, 1000))
            .fluidOutputs(NUCLIDE.LiFBeF2.getFluidStack(250))
            .duration(7 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(distillationTowerRecipes);

        // UF6 -> UF4 reduction
        // UF6 + LiFBeF2 + H2 -> LiFBeF2UF4 + HF
        GT_Values.RA.stdBuilder()
            .itemInputs(FLUORIDES.URANIUM_HEXAFLUORIDE.getCell(1), NUCLIDE.LiFBeF2.getCell(1))
            .fluidInputs(Materials.Hydrogen.getGas(2000L))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 2))
            .fluidOutputs(NUCLIDE.LiFBeF2UF4.getFluidStack(3000))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 5400)
            .addTo(blastFurnaceRecipes);
        // Alternative recipe to the above, for chemplant, to not use cells

        GT_Values.RA.stdBuilder()
            .fluidInputs(
                FLUORIDES.URANIUM_HEXAFLUORIDE.getFluidStack(1000),
                NUCLIDE.LiFBeF2.getFluidStack(1000),
                ELEMENT.getInstance().HYDROGEN.getFluidStack(2000))
            .fluidOutputs(NUCLIDE.LiFBeF2UF4.getFluidStack(3000), FluidUtils.getFluidStack("hydrofluoricacid", 2000))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(CHEMPLANT_CASING_TIER,4)
            .addTo(chemicalPlantRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(FLUORIDES.ZIRCONIUM_TETRAFLUORIDE.getFluidStack(100), NUCLIDE.LiFBeF2UF4.getFluidStack(900))
            .fluidOutputs(NUCLIDE.LiFBeF2ZrF4UF4.getFluidStack(1000))
            .duration(12 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(fissionFuelProcessingRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(FLUORIDES.THORIUM_TETRAFLUORIDE.getFluidStack(100), NUCLIDE.LiFBeF2UF4.getFluidStack(900))
            .fluidOutputs(NUCLIDE.LiFBeF2ThF4UF4.getFluidStack(1000))
            .duration(15 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(fissionFuelProcessingRecipes);
    }
}
