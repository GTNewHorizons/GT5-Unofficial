package net.glease.ggfab;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.HOURS;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeConstants.AssemblyLine;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_TIME;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

class ComponentRecipeLoader implements Runnable {

    @Override
    public void run() {
        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140");

        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Machine_Multi_Assemblyline.get(1L))
            .metadata(RESEARCH_TIME, 1 * HOURS + 6 * MINUTES)
            .itemInputs(
                ItemList.Machine_Multi_Assemblyline.get(1L),
                new Object[] { OrePrefixes.circuit.get(Materials.Master), 2 },
                new Object[] { OrePrefixes.circuit.get(Materials.Elite), 4 },
                new Object[] { OrePrefixes.circuit.get(Materials.Data), 8 },
                ItemList.Automation_ChestBuffer_LuV.get(1L))
            .fluidInputs(new FluidStack(solderIndalloy, 1296), Materials.Lubricant.getFluid(2000))
            .itemOutputs(GGItemList.AdvAssLine.get(1L))
            .eut(6_000)
            .duration(10 * MINUTES)
            .addTo(AssemblyLine);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_IV.get(1L),
                ItemList.Emitter_IV.get(1L),
                ItemList.Sensor_IV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Enderium, 1L),
                GT_Utility.getIntegratedCircuit(12))
            .itemOutputs(GGItemList.LinkedInputBus.get(1L))
            .fluidInputs(Materials.Polybenzimidazole.getMolten(144L))
            .duration(30 * SECONDS)
            .eut(GT_Values.VP[5])
            .addTo(assemblerRecipes);

    }
}
