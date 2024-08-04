package com.github.technus.tectech.loader.recipe;

import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.technus.tectech.thing.CustomItemList;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_OreDictUnificator;

public class CircuitAssembler implements Runnable {

    @Override
    public void run() {
        Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
            ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
            : FluidRegistry.getFluid("molten.solderingalloy");

        // Optical Processor
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Optically_Perfected_CPU.get(1L),
                ItemList.Optically_Compatible_Memory.get(2L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(16L),
                ItemList.Circuit_Parts_DiodeXSMD.get(16L),
                CustomItemList.DATApipe.get(4L),
                GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.EnrichedHolmium, 16))
            .itemOutputs(ItemList.Circuit_OpticalProcessor.get(1L))
            .fluidInputs(new FluidStack(solderUEV, 288))
            .requiresCleanRoom()
            .duration(20 * SECONDS)
            .eut(614400)
            .addTo(circuitAssemblerRecipes);

        // Parametrizer Memory Card
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 2),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gold, 4))
            .itemOutputs(CustomItemList.parametrizerMemory.get(1))
            .fluidInputs(Materials.Plastic.getMolten(72))
            .requiresCleanRoom()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(circuitAssemblerRecipes);
    }
}
