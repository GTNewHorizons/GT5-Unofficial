package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.ModIDs.Forestry;
import static gregtech.api.enums.ModIDs.Railcraft;
import static gregtech.api.util.GT_ModHandler.getModItem;

import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;
import mods.railcraft.common.items.RailcraftToolItems;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.loaders.postload.GT_MachineRecipeLoader;

public class PyrolyseRecipes implements Runnable {

    @Override
    public void run() {
        if (Railcraft.isModLoaded()) {
            GT_Values.RA.addPyrolyseRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16),
                    GT_Values.NF,
                    1,
                    RailcraftToolItems.getCoalCoke(16),
                    Materials.Creosote.getFluid(8000),
                    640,
                    64);
            GT_Values.RA.addPyrolyseRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16),
                    Materials.Nitrogen.getGas(1000),
                    2,
                    RailcraftToolItems.getCoalCoke(16),
                    Materials.Creosote.getFluid(8000),
                    320,
                    96);
            GT_Values.RA.addPyrolyseRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 8),
                    GT_Values.NF,
                    1,
                    EnumCube.COKE_BLOCK.getItem(8),
                    Materials.Creosote.getFluid(32000),
                    2560,
                    64);
            GT_Values.RA.addPyrolyseRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 8),
                    Materials.Nitrogen.getGas(1000),
                    2,
                    EnumCube.COKE_BLOCK.getItem(8),
                    Materials.Creosote.getFluid(32000),
                    1280,
                    96);
        }

        if (!GregTech_API.mIC2Classic) {
            GT_Values.RA.addPyrolyseRecipe(
                GT_ModHandler.getIC2Item("biochaff", 4L),
                Materials.Water.getFluid(4000),
                1,
                GT_Values.NI,
                new FluidStack(FluidRegistry.getFluid("ic2biomass"), 5000),
                900,
                10);
        }

        if (Forestry.isModLoaded()) {
            GT_Values.RA.addPyrolyseRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextForestry, "fertilizerBio", 4L),
                    Materials.Water.getFluid(4000),
                    1,
                    GT_Values.NI,
                    Materials.Biomass.getFluid(5000),
                    900,
                    10);
            GT_Values.RA.addPyrolyseRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextForestry, "mulch", 32L),
                    Materials.Water.getFluid(4000),
                    1,
                    GT_Values.NI,
                    Materials.Biomass.getFluid(5000),
                    900,
                    10);
        }

        GT_Values.RA.addPyrolyseRecipe(
                GT_ModHandler.getIC2Item("biochaff", 1),
                Materials.Water.getFluid(1500),
                2,
                GT_Values.NI,
                Materials.FermentedBiomass.getFluid(1500),
                200,
                10);
        GT_Values.RA.addPyrolyseRecipe(
                GT_Values.NI,
                new FluidStack(FluidRegistry.getFluid("ic2biomass"), 1000),
                2,
                GT_Values.NI,
                Materials.FermentedBiomass.getFluid(1000),
                100,
                10);
        GT_Values.RA.addPyrolyseRecipe(
                GT_Values.NI,
                Materials.Biomass.getFluid(1000),
                2,
                GT_Values.NI,
                Materials.FermentedBiomass.getFluid(1000),
                100,
                10);

        GT_Values.RA.addPyrolyseRecipe(
                Materials.Sugar.getDust(23),
                GT_Values.NF,
                1,
                Materials.Charcoal.getDust(12),
                Materials.Water.getFluid(1500),
                320,
                64);
        GT_Values.RA.addPyrolyseRecipe(
                Materials.Sugar.getDust(23),
                Materials.Nitrogen.getGas(500),
                2,
                Materials.Charcoal.getDust(12),
                Materials.Water.getFluid(1500),
                160,
                96);
    }
}
