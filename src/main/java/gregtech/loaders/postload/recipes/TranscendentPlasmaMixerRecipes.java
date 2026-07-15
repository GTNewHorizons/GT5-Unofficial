package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.transcendentPlasmaMixerRecipes;

import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTRecipeConstants;
import gtPlusPlus.core.material.MaterialsElements;

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
                    .getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Iron, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Calcium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Niobium, Materials2FluidShapes.fluidPlasma, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.ExcitedDTCC, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(100)
            .eut(CRUDE_EU_PER_L)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Iron, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Calcium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Niobium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Radon, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Nickel, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Boron, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Sulfur, Materials2FluidShapes.fluidPlasma, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.ExcitedDTPC, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(100)
            .eut(PROSAIC_EU_PER_L)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Iron, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Calcium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Niobium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Radon, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Nickel, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Boron, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Sulfur, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Zinc, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Silver, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Titanium, Materials2FluidShapes.fluidPlasma, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.ExcitedDTRC, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(100)
            .eut(RESPLENDENT_EU_PER_L)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Iron, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Calcium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Niobium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Radon, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Nickel, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Boron, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Sulfur, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Zinc, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Silver, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Titanium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Americium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Bismuth, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Tin, Materials2FluidShapes.fluidPlasma, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.ExcitedDTEC, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(100)
            .eut(EXOTIC_EU_PER_L)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(5)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Iron, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Calcium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Niobium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Radon, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Nickel, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Boron, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Sulfur, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Zinc, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Silver, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Titanium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Americium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Bismuth, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Tin, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lead, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Thorium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.fluidPlasma, (int) (100L)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.RawStarMatter, Materials2FluidShapes.fluidLiquid, (int) (25L)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.ExcitedDTSC, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(100)
            .eut(STELLAR_EU_PER_HALF_L)
            .metadata(GTRecipeConstants.EU_MULTIPLIER, 20)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.RawStarMatter, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SpaceTime, Materials2FluidShapes.fluidMolten, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.spatialFluid, Materials2FluidShapes.fluidMolten, (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DimensionallyTranscendentResidue,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.PrimordialMatter,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(100)
            .eut(PRIMORDIAL_MATTER)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                new FluidStack(MaterialsElements.getInstance().FERMIUM.getPlasma(), 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Thorium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                new FluidStack(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getPlasma(), 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Calcium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DimensionallyTranscendentResidue,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creon, Materials2FluidShapes.fluidPlasma, (int) (5_000)))
            .duration(100)
            .eut(TierEU.RECIPE_UMV)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DimensionallyTranscendentResidue,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Tin, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Antimony, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lead, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.RawStarMatter, Materials2FluidShapes.fluidLiquid, (int) (25)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.InactiveCosmicSolder,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (4_000)))
            .duration(100)
            .eut(INACTIVE_COSMIC_SOLDER)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Infinity, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Neutronium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.FleroviumGT5U, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                new FluidStack(MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getPlasma(), 1_000),
                new FluidStack(MaterialsElements.STANDALONE.HYPOGEN.getPlasma(), 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ichorium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SixPhasedCopper,
                    Materials2FluidShapes.fluidPlasma,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DraconiumAwakened,
                    Materials2FluidShapes.fluidPlasma,
                    (int) (1_000)),
                new FluidStack(MaterialsElements.STANDALONE.DRAGON_METAL.getPlasma(), 1_000),
                new FluidStack(MaterialsElements.STANDALONE.RHUGNOR.getPlasma(), 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Draconium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creon, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Tritanium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CosmicNeutronium,
                    Materials2FluidShapes.fluidPlasma,
                    (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Bedrockium, Materials2FluidShapes.fluidPlasma, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.ExcitedDTCC, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.ExcitedDTPC, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.ExcitedDTRC, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.ExcitedDTEC, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.ExcitedDTSC, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.sgcrystalslurry,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(100 * GTRecipeBuilder.TICKS)
            // 1 Trillion EU/t
            .metadata(GTRecipeConstants.EU_MULTIPLIER, 1000)
            .eut(1_000_000_000L)
            .addTo(transcendentPlasmaMixerRecipes);
    }
}
