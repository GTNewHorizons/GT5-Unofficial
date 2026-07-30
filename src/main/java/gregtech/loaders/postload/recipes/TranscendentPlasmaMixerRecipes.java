package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.transcendentPlasmaMixerRecipes;

import gregtech.api.enums.materials2.Materials;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
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
                MaterialLibAPI
                    .getFluidStack(Materials.Helium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Iron, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Calcium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Niobium, Materials2FluidShapes.fluidPlasma, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.ExcitedDTCC, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(100)
            .eut(CRUDE_EU_PER_L)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Helium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Iron, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Calcium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Niobium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Radon, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Nickel, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Boron, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Sulfur, Materials2FluidShapes.fluidPlasma, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.ExcitedDTPC, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(100)
            .eut(PROSAIC_EU_PER_L)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Helium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Iron, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Calcium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Niobium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Radon, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Nickel, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Boron, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Sulfur, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Nitrogen, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Zinc, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Silver, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Titanium, Materials2FluidShapes.fluidPlasma, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.ExcitedDTRC, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(100)
            .eut(RESPLENDENT_EU_PER_L)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Helium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Iron, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Calcium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Niobium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Radon, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Nickel, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Boron, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Sulfur, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Nitrogen, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Zinc, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Silver, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Titanium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Americium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Bismuth, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.Tin, Materials2FluidShapes.fluidPlasma, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.ExcitedDTEC, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(100)
            .eut(EXOTIC_EU_PER_L)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(5)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Helium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Iron, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Calcium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Niobium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Radon, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Nickel, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Boron, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Sulfur, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Nitrogen, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Zinc, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Silver, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Titanium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Americium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Bismuth, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.Tin, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Lead, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Thorium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Naquadria, Materials2FluidShapes.fluidPlasma, (int) (100L)),
                MaterialLibAPI
                    .getFluidStack(Materials.RawStarMatter, Materials2FluidShapes.fluidLiquid, (int) (25L)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.ExcitedDTSC, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(100)
            .eut(STELLAR_EU_PER_HALF_L)
            .metadata(GTRecipeConstants.EU_MULTIPLIER, 20)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.RawStarMatter, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.SpaceTime, Materials2FluidShapes.fluidMolten, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.spatialFluid, Materials2FluidShapes.fluidMolten, (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.DimensionallyTranscendentResidue,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.PrimordialMatter,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(100)
            .eut(PRIMORDIAL_MATTER)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Fermium), 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials.Thorium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.CelestialTungsten), 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials.Calcium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.DimensionallyTranscendentResidue,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Creon, Materials2FluidShapes.fluidPlasma, (int) (5_000)))
            .duration(100)
            .eut(TierEU.RECIPE_UMV)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DimensionallyTranscendentResidue,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.Tin, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Antimony, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Lead, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.RawStarMatter, Materials2FluidShapes.fluidLiquid, (int) (25)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.InactiveCosmicSolder,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (4_000)))
            .duration(100)
            .eut(INACTIVE_COSMIC_SOLDER)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Infinity, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Neutronium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.FleroviumGT5U, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.ChromaticGlass), 1_000),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Hypogen), 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials.Ichorium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.SixPhasedCopper,
                    Materials2FluidShapes.fluidPlasma,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.DraconiumAwakened,
                    Materials2FluidShapes.fluidPlasma,
                    (int) (1_000)),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Dragonblood), 1_000),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Rhugnor), 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials.Draconium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Creon, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Tritanium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.CosmicNeutronium,
                    Materials2FluidShapes.fluidPlasma,
                    (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Bedrockium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.ExcitedDTCC, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.ExcitedDTPC, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.ExcitedDTRC, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.ExcitedDTEC, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.ExcitedDTSC, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.sgcrystalslurry,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(100 * GTRecipeBuilder.TICKS)
            // 1 Trillion EU/t
            .metadata(GTRecipeConstants.EU_MULTIPLIER, 1000)
            .eut(1_000_000_000L)
            .addTo(transcendentPlasmaMixerRecipes);
    }
}
