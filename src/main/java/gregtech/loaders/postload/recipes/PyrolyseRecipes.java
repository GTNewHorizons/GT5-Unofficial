package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMap.sPyrolyseRecipes;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;
import mods.railcraft.common.items.RailcraftToolItems;

public class PyrolyseRecipes implements Runnable {

    @Override
    public void run() {
        if (Railcraft.isModLoaded()) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(RailcraftToolItems.getCoalCoke(16))
                .fluidOutputs(Materials.Creosote.getFluid(8000))
                .duration(32 * SECONDS)
                .eut(64)
                .addTo(sPyrolyseRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(RailcraftToolItems.getCoalCoke(16))
                .fluidInputs(Materials.Nitrogen.getGas(1000))
                .fluidOutputs(Materials.Creosote.getFluid(8000))
                .duration(16 * SECONDS)
                .eut(96)
                .addTo(sPyrolyseRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 8),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(EnumCube.COKE_BLOCK.getItem(8))
                .fluidOutputs(Materials.Creosote.getFluid(32000))
                .duration(2 * MINUTES + 8 * SECONDS)
                .eut(64)
                .addTo(sPyrolyseRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 8),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(EnumCube.COKE_BLOCK.getItem(8))
                .fluidInputs(Materials.Nitrogen.getGas(1000))
                .fluidOutputs(Materials.Creosote.getFluid(32000))
                .duration(1 * MINUTES + 4 * SECONDS)
                .eut(96)
                .addTo(sPyrolyseRecipes);
        }

        if (!GregTech_API.mIC2Classic) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_ModHandler.getIC2Item("biochaff", 4), GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(Materials.Water.getFluid(4000))
                .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 5000))
                .duration(45 * SECONDS)
                .eut(10)
                .addTo(sPyrolyseRecipes);
        }

        if (Forestry.isModLoaded()) {
            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Forestry.ID, "fertilizerBio", 4), GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(Materials.Water.getFluid(4000))
                .fluidOutputs(Materials.Biomass.getFluid(5000))
                .duration(45 * SECONDS)
                .eut(10)
                .addTo(sPyrolyseRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Forestry.ID, "mulch", 32), GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(Materials.Water.getFluid(4000))
                .fluidOutputs(Materials.Biomass.getFluid(5000))
                .duration(45 * SECONDS)
                .eut(10)
                .addTo(sPyrolyseRecipes);
        }

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_ModHandler.getIC2Item("biochaff", 1), GT_Utility.getIntegratedCircuit(2))
            .fluidInputs(Materials.Water.getFluid(1500))
            .fluidOutputs(Materials.FermentedBiomass.getFluid(1500))
            .duration(10 * SECONDS)
            .eut(10)
            .addTo(sPyrolyseRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(2))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 1000))
            .fluidOutputs(Materials.FermentedBiomass.getFluid(1000))
            .duration(5 * SECONDS)
            .eut(10)
            .addTo(sPyrolyseRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(2))
            .fluidInputs(Materials.Biomass.getFluid(1000))
            .fluidOutputs(Materials.FermentedBiomass.getFluid(1000))
            .duration(5 * SECONDS)
            .eut(10)
            .addTo(sPyrolyseRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Sugar.getDust(23), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(Materials.Charcoal.getDust(12))
            .fluidOutputs(Materials.Water.getFluid(1500))
            .duration(16 * SECONDS)
            .eut(64)
            .addTo(sPyrolyseRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Sugar.getDust(23), GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(Materials.Charcoal.getDust(12))
            .fluidInputs(Materials.Nitrogen.getGas(500))
            .fluidOutputs(Materials.Water.getFluid(1500))
            .duration(8 * SECONDS)
            .eut(96)
            .addTo(sPyrolyseRecipes);
    }
}
