package gtPlusPlus.xmod.gregtech.loaders.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gtPlusPlus.core.item.chemistry.NuclearChem;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.nuclear.NUCLIDE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

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
        CORE.RA.addFissionFuel(
                FluidUtils.getFluidStack(aLithiumFluoride, 550),
                FluidUtils.getFluidStack(aBerylliumFluoride, 150),
                FluidUtils.getFluidStack(aZirconiumFluoride, 60),
                FluidUtils.getFluidStack(aUranium235, 240),
                null,
                null,
                null,
                null,
                null, // Extra 5 inputs
                FluidUtils.getFluidStack(aLiFBeF2ZrF4U235, 1000),
                null,
                30 * 60 * 20, // Duration
                MaterialUtils.getVoltageForTier(4));

        // 7LiF - BeF2 - ZrF4 - UF4 - 650C
        CORE.RA.addFissionFuel(
                FluidUtils.getFluidStack(aLithiumFluoride, 600),
                FluidUtils.getFluidStack(aBerylliumFluoride, 250),
                FluidUtils.getFluidStack(aZirconiumFluoride, 80),
                FluidUtils.getFluidStack(aUraniumTetraFluoride, 70),
                null,
                null,
                null,
                null,
                null, // Extra 5 inputs
                FluidUtils.getFluidStack(aLiFBeF2ZrF4UF4, 1000),
                null,
                40 * 60 * 20,
                MaterialUtils.getVoltageForTier(5));

        // 7liF - BeF2 - ThF4 - UF4 - 566C
        CORE.RA.addFissionFuel(
                FluidUtils.getFluidStack(aLithiumFluoride, 580),
                FluidUtils.getFluidStack(aBerylliumFluoride, 270),
                FluidUtils.getFluidStack(aThoriumFluoride, 80),
                FluidUtils.getFluidStack(aUraniumTetraFluoride, 70),
                null,
                null,
                null,
                null,
                null, // Extra 5 inputs
                FluidUtils.getFluidStack(aLiFBeF2ThF4UF4, 1000),
                null,
                50 * 60 * 20, // Duration
                MaterialUtils.getVoltageForTier(5));

        // Reprocess Fuels

        // Reactor Blanket step 1 - Fluorination
        CORE.RA.addReactorProcessingUnitRecipe(
                CI.getNumberedAdvancedCircuit(17),
                ELEMENT.getInstance().FLUORINE.getCell(6),
                NUCLIDE.LiFThF4.getFluidStack(10000),
                new ItemStack[] { CI.emptyCells(5), FLUORIDES.LITHIUM_FLUORIDE.getCell(1),
                        ELEMENT.getInstance().PROTACTINIUM.getDust(1), ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                        ELEMENT.getInstance().PROTACTINIUM.getDust(1), ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                        ELEMENT.getInstance().PROTACTINIUM.getDust(1), ELEMENT.getInstance().PROTACTINIUM.getDust(1) },
                new int[] { 10000, 10000, 500, 500, 500, 250, 250, 250 },
                NUCLIDE.UF6F2.getFluidStack(1500),
                20 * 60 * 10,
                MaterialUtils.getVoltageForTier(5));
        CORE.RA.addReactorProcessingUnitRecipe(
                CI.getNumberedAdvancedCircuit(18),
                ELEMENT.getInstance().FLUORINE.getCell(6),
                NUCLIDE.LiFBeF2ThF4.getFluidStack(10000),
                new ItemStack[] { CI.emptyCells(4), FLUORIDES.LITHIUM_FLUORIDE.getCell(1),
                        FLUORIDES.BERYLLIUM_FLUORIDE.getCell(1), ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                        ELEMENT.getInstance().PROTACTINIUM.getDust(1), ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                        ELEMENT.getInstance().PROTACTINIUM.getDust(1), ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                        ELEMENT.getInstance().PROTACTINIUM.getDust(1) },
                new int[] { 10000, 10000, 10000, 1000, 1000, 1000, 500, 500, 500 },
                NUCLIDE.UF6F2.getFluidStack(3000),
                20 * 60 * 10,
                MaterialUtils.getVoltageForTier(5));

        // Reactor Blanket step 1 - Fluorination
        CORE.RA.addReactorProcessingUnitRecipe(
                CI.getNumberedAdvancedCircuit(7),
                ELEMENT.getInstance().FLUORINE.getCell(6),
                NUCLIDE.Sparged_LiFThF4.getFluidStack(10000),
                new ItemStack[] { CI.emptyCells(4), FLUORIDES.LITHIUM_FLUORIDE.getCell(2),
                        ELEMENT.getInstance().PROTACTINIUM.getDust(1), ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                        ELEMENT.getInstance().PROTACTINIUM.getDust(1), ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                        ELEMENT.getInstance().PROTACTINIUM.getDust(1), ELEMENT.getInstance().PROTACTINIUM.getDust(1) },
                new int[] { 10000, 10000, 1000, 1000, 1000, 1000, 1000, 1000 },
                NUCLIDE.UF6F2.getFluidStack(3000),
                20 * 60 * 5,
                MaterialUtils.getVoltageForTier(5));
        CORE.RA.addReactorProcessingUnitRecipe(
                CI.getNumberedAdvancedCircuit(8),
                ELEMENT.getInstance().FLUORINE.getCell(6),
                NUCLIDE.Sparged_LiFBeF2ThF4.getFluidStack(10000),
                new ItemStack[] { CI.emptyCells(2), FLUORIDES.LITHIUM_FLUORIDE.getCell(2),
                        FLUORIDES.BERYLLIUM_FLUORIDE.getCell(2), ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                        ELEMENT.getInstance().PROTACTINIUM.getDust(1), ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                        ELEMENT.getInstance().PROTACTINIUM.getDust(1), ELEMENT.getInstance().PROTACTINIUM.getDust(1),
                        ELEMENT.getInstance().PROTACTINIUM.getDust(1) },
                new int[] { 10000, 10000, 10000, 2000, 2000, 2000, 2000, 2000, 2000 },
                NUCLIDE.UF6F2.getFluidStack(6000),
                20 * 60 * 5,
                MaterialUtils.getVoltageForTier(5));

        // Reactor Blanket step 2 - Sorption + Cold Trap
        CORE.RA.addColdTrapRecipe(
                8,
                FLUORIDES.SODIUM_FLUORIDE.getCell(4),
                NUCLIDE.UF6F2.getFluidStack(3000),
                new ItemStack[] { ELEMENT.getInstance().FLUORINE.getCell(2), FLUORIDES.URANIUM_HEXAFLUORIDE.getCell(2),
                        ELEMENT.getInstance().URANIUM233.getDust(1), ELEMENT.getInstance().URANIUM233.getDust(1),
                        ELEMENT.getInstance().URANIUM233.getDust(1) },
                new int[] { 10000, 10000, 3000, 2000, 1000 },
                FLUORIDES.SODIUM_FLUORIDE.getFluidStack(2000),
                20 * 60 * 10,
                MaterialUtils.getVoltageForTier(3));

        // Reactor Core step 0 - Process Burnt Salt
        // Tier 1 Fuel - Gives back FLIBE and breeds U233
        /*
         * CORE.RA.addReactorProcessingUnitRecipe( CI.getNumberedAdvancedCircuit(1), CI.emptyCells(2), new
         * FluidStack(NuclearChem.Burnt_LiFBeF2ZrF4U235, 4000), new ItemStack[] { FLUORIDES.LITHIUM_FLUORIDE.getCell(1),
         * ELEMENT.getInstance().URANIUM233.getCell(1) }, new int[] {10000, 10000}, NUCLIDE.LiFBeF2.getFluidStack(2000),
         * 20 * 60 * 60, MaterialUtils.getVoltageForTier(3));
         */

        // LiBeF2UF4FP + F2 = LiFBeF2 & UF6F2FP
        // Reactor Core step 1 - Process Burnt Salt
        CORE.RA.addReactorProcessingUnitRecipe(
                CI.getNumberedAdvancedCircuit(1),
                ELEMENT.getInstance().FLUORINE.getCell(1),
                NUCLIDE.LiFBeF2UF4FP.getFluidStack(1000),
                new ItemStack[] { NUCLIDE.UF6F2FP.getCell(1) },
                new int[] { 10000 },
                FluidUtils.getFluidStack(NuclearChem.Impure_LiFBeF2, 1000),
                20 * 60 * 120,
                MaterialUtils.getVoltageForTier(3));

        // LiBeF2UF4FP + F2 = LiFBeF2 & UF6F2FP
        // Reactor Core step 1 - Process Burnt Salt
        CORE.RA.addReactorProcessingUnitRecipe(
                CI.getNumberedAdvancedCircuit(1),
                ELEMENT.getInstance().FLUORINE.getCell(3),
                NUCLIDE.Sparged_LiFBeF2UF4FP.getFluidStack(1000),
                new ItemStack[] { CI.emptyCells(1), NUCLIDE.UF6F2FP.getCell(2) },
                new int[] { 10000 },
                FluidUtils.getFluidStack(NuclearChem.Impure_LiFBeF2, 2000),
                20 * 60 * 60,
                MaterialUtils.getVoltageForTier(3));

        // Reactor Core step 2A - Sorption + Cold Trap
        CORE.RA.addColdTrapRecipe(
                8,
                FLUORIDES.SODIUM_FLUORIDE.getCell(3),
                NUCLIDE.UF6F2FP.getFluidStack(2000),
                new ItemStack[] { ELEMENT.getInstance().FLUORINE.getCell(1), FLUORIDES.URANIUM_HEXAFLUORIDE.getCell(2),
                        ELEMENT.getInstance().PHOSPHORUS.getDust(1), ELEMENT.getInstance().PHOSPHORUS.getDust(1),
                        ELEMENT.getInstance().PHOSPHORUS.getDust(1), ELEMENT.getInstance().PHOSPHORUS.getDust(1),
                        ELEMENT.getInstance().PHOSPHORUS.getDust(1), ELEMENT.getInstance().PHOSPHORUS.getDust(1) },
                new int[] { 10000, 10000, 5000, 5000, 5000, 5000, 5000, 5000 },
                FLUORIDES.SODIUM_FLUORIDE.getFluidStack(2000),
                20 * 60 * 10,
                MaterialUtils.getVoltageForTier(4));

        // Reactor Core step 2B - Distillation
        GT_Values.RA.addDistillationTowerRecipe(
                FluidUtils.getFluidStack(NuclearChem.Impure_LiFBeF2, 1000),
                new FluidStack[] { NUCLIDE.LiFBeF2.getFluidStack(250) },
                null,
                450 * 20,
                MaterialUtils.getVoltageForTier(5));

        // UF6 -> UF4 reduction
        // UF6 + LiFBeF2 + H2 -> LiFBeF2UF4 + HF
        CORE.RA.addBlastRecipe(
                new ItemStack[] { FLUORIDES.URANIUM_HEXAFLUORIDE.getCell(1), NUCLIDE.LiFBeF2.getCell(1) },
                new FluidStack[] { ELEMENT.getInstance().HYDROGEN.getFluidStack(2000) },
                new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 2) },
                new FluidStack[] { NUCLIDE.LiFBeF2UF4.getFluidStack(3000) },
                300 * 10,
                MaterialUtils.getVoltageForTier(5),
                5400);

        // LiFBeF2ZrF4U235 - We can't add both ZrF4 and U235 here, so best we leave this disabled.
        /*
         * CORE.RA.addReactorProcessingUnitRecipe( CI.getNumberedAdvancedCircuit(8), NUCLIDE.LiFBeF2UF4.getCell(9),
         * ELEMENT.getInstance().URANIUM235.getFluidStack(1000), new ItemStack[] { CI.emptyCells(9) }, new int[]
         * {10000}, NUCLIDE.LiFBeF2ZrF4U235.getFluidStack(10000), 20 * 60 * 5, MaterialUtils.getVoltageForTier(4));
         */

        // LiFBeF2ZrF4UF4
        /*
         * CORE.RA.addReactorProcessingUnitRecipe( CI.getNumberedAdvancedCircuit(9), NUCLIDE.LiFBeF2UF4.getCell(9),
         * FLUORIDES.ZIRCONIUM_TETRAFLUORIDE.getFluidStack(1000), new ItemStack[] { CI.emptyCells(9) }, new int[]
         * {10000}, NUCLIDE.LiFBeF2ZrF4UF4.getFluidStack(10000), 20 * 60 * 5, MaterialUtils.getVoltageForTier(5));
         * CORE.RA.addReactorProcessingUnitRecipe( CI.getNumberedAdvancedCircuit(9),
         * FLUORIDES.ZIRCONIUM_TETRAFLUORIDE.getCell(1), NUCLIDE.LiFBeF2UF4.getFluidStack(9000), new ItemStack[] {
         * CI.emptyCells(1) }, new int[] {10000}, NUCLIDE.LiFBeF2ZrF4UF4.getFluidStack(10000), 20 * 60 * 5,
         * MaterialUtils.getVoltageForTier(5));
         */

        CORE.RA.addFissionFuel(
                FLUORIDES.ZIRCONIUM_TETRAFLUORIDE.getFluidStack(1000),
                NUCLIDE.LiFBeF2UF4.getFluidStack(9000),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                NUCLIDE.LiFBeF2ZrF4UF4.getFluidStack(10000),
                null,
                20 * 60 * 120, // Duration
                MaterialUtils.getVoltageForTier(5));

        // LiFBeF2ThF4UF4
        /*
         * CORE.RA.addReactorProcessingUnitRecipe( CI.getNumberedAdvancedCircuit(10), NUCLIDE.LiFBeF2UF4.getCell(9),
         * FLUORIDES.THORIUM_TETRAFLUORIDE.getFluidStack(1000), new ItemStack[] { CI.emptyCells(9) }, new int[] {10000},
         * NUCLIDE.LiFBeF2ThF4UF4.getFluidStack(10000), 20 * 60 * 5, MaterialUtils.getVoltageForTier(5));
         * CORE.RA.addReactorProcessingUnitRecipe( CI.getNumberedAdvancedCircuit(10),
         * FLUORIDES.THORIUM_TETRAFLUORIDE.getCell(1), NUCLIDE.LiFBeF2UF4.getFluidStack(9000), new ItemStack[] {
         * CI.emptyCells(1) }, new int[] {10000}, NUCLIDE.LiFBeF2ThF4UF4.getFluidStack(10000), 20 * 60 * 5,
         * MaterialUtils.getVoltageForTier(5));
         */

        CORE.RA.addFissionFuel(
                FLUORIDES.THORIUM_TETRAFLUORIDE.getFluidStack(1000),
                NUCLIDE.LiFBeF2UF4.getFluidStack(9000),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                NUCLIDE.LiFBeF2ThF4UF4.getFluidStack(10000),
                null,
                20 * 60 * 150, // Duration
                MaterialUtils.getVoltageForTier(5));
    }
}
