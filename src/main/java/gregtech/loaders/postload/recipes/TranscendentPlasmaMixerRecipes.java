package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.transcendentPlasmaMixerRecipes;

import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTRecipeConstants;

public class TranscendentPlasmaMixerRecipes implements Runnable {

    private static final int CRUDE_EU_PER_L = 14_514_983;
    private static final int PROSAIC_EU_PER_L = 66_768_460;
    private static final int RESPLENDENT_EU_PER_L = 269_326_451;
    private static final int EXOTIC_EU_PER_L = 1_073_007_393;
    private static final int STELLAR_EU_PER_HALF_L = 2_138_383_760;

    private static final int PRIMORDIAL_MATTER = 2_000_000_000;
    private static final int INACTIVE_COSMIC_SOLDER = 1_073_007_39;

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Iron, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Calcium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Niobium, FluidShapes.fluidPlasma, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.ExcitedDTCC, FluidShapes.fluidLiquid, 1_000))
            .duration(100)
            .eut(CRUDE_EU_PER_L)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Iron, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Calcium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Niobium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Nickel, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Boron, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Sulfur, FluidShapes.fluidPlasma, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.ExcitedDTPC, FluidShapes.fluidLiquid, 1_000))
            .duration(100)
            .eut(PROSAIC_EU_PER_L)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Iron, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Calcium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Niobium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Nickel, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Boron, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Sulfur, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Zinc, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Silver, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Titanium, FluidShapes.fluidPlasma, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.ExcitedDTRC, FluidShapes.fluidLiquid, 1_000))
            .duration(100)
            .eut(RESPLENDENT_EU_PER_L)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Iron, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Calcium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Niobium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Nickel, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Boron, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Sulfur, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Zinc, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Silver, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Titanium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Americium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Bismuth, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Tin, FluidShapes.fluidPlasma, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.ExcitedDTEC, FluidShapes.fluidLiquid, 1_000))
            .duration(100)
            .eut(EXOTIC_EU_PER_L)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(5)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Iron, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Calcium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Niobium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Nickel, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Boron, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Sulfur, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Zinc, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Silver, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Titanium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Americium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Bismuth, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Tin, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Lead, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Thorium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Naquadria, FluidShapes.fluidPlasma, 100),
                MaterialLibAPI.getFluidStack(Materials.RawStarMatter, FluidShapes.fluidLiquid, 25))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.ExcitedDTSC, FluidShapes.fluidLiquid, 1_000))
            .duration(100)
            .eut(STELLAR_EU_PER_HALF_L)
            .metadata(GTRecipeConstants.EU_MULTIPLIER, 20)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.RawStarMatter, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.SpaceTime, FluidShapes.fluidMolten, 1_000),
                MaterialLibAPI.getFluidStack(Materials.spatialFluid, FluidShapes.fluidMolten, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials.DimensionallyTranscendentResidue, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.PrimordialMatter, FluidShapes.fluidLiquid, 1_000))
            .duration(100)
            .eut(PRIMORDIAL_MATTER)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Fermium), 1_000),
                MaterialLibAPI.getFluidStack(Materials.Thorium, FluidShapes.fluidPlasma, 1_000),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.CelestialTungsten), 1_000),
                MaterialLibAPI.getFluidStack(Materials.Calcium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials.DimensionallyTranscendentResidue, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Creon, FluidShapes.fluidPlasma, 5_000))
            .duration(100)
            .eut(TierEU.RECIPE_UMV)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.DimensionallyTranscendentResidue, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Tin, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Antimony, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Lead, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.RawStarMatter, FluidShapes.fluidLiquid, 25))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.InactiveCosmicSolder, FluidShapes.fluidLiquid, 4_000))
            .duration(100)
            .eut(INACTIVE_COSMIC_SOLDER)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Infinity, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Neutronium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.FleroviumGT5U, FluidShapes.fluidPlasma, 1_000),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.ChromaticGlass), 1_000),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Hypogen), 1_000),
                MaterialLibAPI.getFluidStack(Materials.Ichorium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.SixPhasedCopper, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.DraconiumAwakened, FluidShapes.fluidPlasma, 1_000),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Dragonblood), 1_000),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Rhugnor), 1_000),
                MaterialLibAPI.getFluidStack(Materials.Draconium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Creon, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Tritanium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.CosmicNeutronium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Bedrockium, FluidShapes.fluidPlasma, 1_000),
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTCC, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTPC, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTRC, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTEC, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTSC, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.sgcrystalslurry, FluidShapes.fluidLiquid, 1_000))
            .duration(100 * GTRecipeBuilder.TICKS)
            // 1 Trillion EU/t
            .metadata(GTRecipeConstants.EU_MULTIPLIER, 1000)
            .eut(1_000_000_000L)
            .addTo(transcendentPlasmaMixerRecipes);
    }
}
