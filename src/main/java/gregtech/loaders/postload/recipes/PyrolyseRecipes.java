package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.pyrolyseRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;
import mods.railcraft.common.items.RailcraftToolItems;

public class PyrolyseRecipes implements Runnable {

    @Override
    public void run() {
        if (Railcraft.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16),
                    GTUtility.getIntegratedCircuit(1))
                .itemOutputs(RailcraftToolItems.getCoalCoke(16))
                .fluidOutputs(Materials.Creosote.getFluid(8000))
                .duration(32 * SECONDS)
                .eut(64)
                .addTo(pyrolyseRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16),
                    GTUtility.getIntegratedCircuit(2))
                .itemOutputs(RailcraftToolItems.getCoalCoke(16))
                .fluidInputs(Materials.Nitrogen.getGas(1000))
                .fluidOutputs(Materials.Creosote.getFluid(8000))
                .duration(16 * SECONDS)
                .eut(96)
                .addTo(pyrolyseRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 8),
                    GTUtility.getIntegratedCircuit(1))
                .itemOutputs(EnumCube.COKE_BLOCK.getItem(8))
                .fluidOutputs(Materials.Creosote.getFluid(32000))
                .duration(2 * MINUTES + 8 * SECONDS)
                .eut(64)
                .addTo(pyrolyseRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 8),
                    GTUtility.getIntegratedCircuit(2))
                .itemOutputs(EnumCube.COKE_BLOCK.getItem(8))
                .fluidInputs(Materials.Nitrogen.getGas(1000))
                .fluidOutputs(Materials.Creosote.getFluid(32000))
                .duration(1 * MINUTES + 4 * SECONDS)
                .eut(96)
                .addTo(pyrolyseRecipes);
        }

        if (Forestry.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Forestry.ID, "fertilizerBio", 4), GTUtility.getIntegratedCircuit(1))
                .fluidInputs(Materials.Water.getFluid(4000))
                .fluidOutputs(Materials.Biomass.getFluid(5000))
                .duration(45 * SECONDS)
                .eut(10)
                .addTo(pyrolyseRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Forestry.ID, "mulch", 32), GTUtility.getIntegratedCircuit(1))
                .fluidInputs(Materials.Water.getFluid(4000))
                .fluidOutputs(Materials.Biomass.getFluid(5000))
                .duration(45 * SECONDS)
                .eut(10)
                .addTo(pyrolyseRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("biochaff", 4), GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Water.getFluid(4000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 5000))
            .duration(45 * SECONDS)
            .eut(10)
            .addTo(pyrolyseRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("biochaff", 1), GTUtility.getIntegratedCircuit(2))
            .fluidInputs(Materials.Water.getFluid(1500))
            .fluidOutputs(Materials.FermentedBiomass.getFluid(1500))
            .duration(10 * SECONDS)
            .eut(10)
            .addTo(pyrolyseRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 1000))
            .fluidOutputs(Materials.FermentedBiomass.getFluid(1000))
            .duration(5 * SECONDS)
            .eut(10)
            .addTo(pyrolyseRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .fluidInputs(Materials.Biomass.getFluid(1000))
            .fluidOutputs(Materials.FermentedBiomass.getFluid(1000))
            .duration(5 * SECONDS)
            .eut(10)
            .addTo(pyrolyseRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Sugar.getDust(23), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(Materials.Charcoal.getDust(12))
            .fluidOutputs(Materials.Water.getFluid(1500))
            .duration(16 * SECONDS)
            .eut(64)
            .addTo(pyrolyseRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Sugar.getDust(23), GTUtility.getIntegratedCircuit(2))
            .itemOutputs(Materials.Charcoal.getDust(12))
            .fluidInputs(Materials.Nitrogen.getGas(500))
            .fluidOutputs(Materials.Water.getFluid(1500))
            .duration(8 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes);
    }
}
