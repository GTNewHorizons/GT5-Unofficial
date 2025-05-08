package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.transcendentPlasmaMixerRecipes;
import static gregtech.api.util.GTRecipeBuilder.BUCKETS;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsElements;

public class TranscendentPlasmaMixerRecipes implements Runnable {

    private static final int CRUDE_EU_PER_L = 14_514_983;
    private static final int PROSAIC_EU_PER_L = 66_768_460;
    private static final int RESPLENDENT_EU_PER_L = 269_326_451;
    private static final int EXOTIC_EU_PER_L = 1_073_007_393;
    private static final int STELLAR_EU_PER_HALF_L = 2_138_383_760;

    private static final int PRIMORDIAL_MATTER = 2_000_000_000;

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(
                Materials.Helium.getPlasma(1 * BUCKETS),
                Materials.Iron.getPlasma(1 * BUCKETS),
                Materials.Calcium.getPlasma(1 * BUCKETS),
                Materials.Niobium.getPlasma(1 * BUCKETS))
            .fluidOutputs(MaterialsUEVplus.ExcitedDTCC.getFluid(1 * BUCKETS))
            .duration(100)
            .eut(CRUDE_EU_PER_L)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .fluidInputs(
                Materials.Helium.getPlasma(1 * BUCKETS),
                Materials.Iron.getPlasma(1 * BUCKETS),
                Materials.Calcium.getPlasma(1 * BUCKETS),
                Materials.Niobium.getPlasma(1 * BUCKETS),
                Materials.Radon.getPlasma(1 * BUCKETS),
                Materials.Nickel.getPlasma(1 * BUCKETS),
                Materials.Boron.getPlasma(1 * BUCKETS),
                Materials.Sulfur.getPlasma(1 * BUCKETS))
            .fluidOutputs(MaterialsUEVplus.ExcitedDTPC.getFluid(1 * BUCKETS))
            .duration(100)
            .eut(PROSAIC_EU_PER_L)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(3))
            .fluidInputs(
                Materials.Helium.getPlasma(1 * BUCKETS),
                Materials.Iron.getPlasma(1 * BUCKETS),
                Materials.Calcium.getPlasma(1 * BUCKETS),
                Materials.Niobium.getPlasma(1 * BUCKETS),
                Materials.Radon.getPlasma(1 * BUCKETS),
                Materials.Nickel.getPlasma(1 * BUCKETS),
                Materials.Boron.getPlasma(1 * BUCKETS),
                Materials.Sulfur.getPlasma(1 * BUCKETS),
                Materials.Nitrogen.getPlasma(1 * BUCKETS),
                Materials.Zinc.getPlasma(1 * BUCKETS),
                Materials.Silver.getPlasma(1 * BUCKETS),
                Materials.Titanium.getPlasma(1 * BUCKETS))
            .fluidOutputs(MaterialsUEVplus.ExcitedDTRC.getFluid(1 * BUCKETS))
            .duration(100)
            .eut(RESPLENDENT_EU_PER_L)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(4))
            .fluidInputs(
                Materials.Helium.getPlasma(1 * BUCKETS),
                Materials.Iron.getPlasma(1 * BUCKETS),
                Materials.Calcium.getPlasma(1 * BUCKETS),
                Materials.Niobium.getPlasma(1 * BUCKETS),
                Materials.Radon.getPlasma(1 * BUCKETS),
                Materials.Nickel.getPlasma(1 * BUCKETS),
                Materials.Boron.getPlasma(1 * BUCKETS),
                Materials.Sulfur.getPlasma(1 * BUCKETS),
                Materials.Nitrogen.getPlasma(1 * BUCKETS),
                Materials.Zinc.getPlasma(1 * BUCKETS),
                Materials.Silver.getPlasma(1 * BUCKETS),
                Materials.Titanium.getPlasma(1 * BUCKETS),
                Materials.Americium.getPlasma(1 * BUCKETS),
                Materials.Bismuth.getPlasma(1 * BUCKETS),
                Materials.Oxygen.getPlasma(1 * BUCKETS),
                Materials.Tin.getPlasma(1 * BUCKETS))
            .fluidOutputs(MaterialsUEVplus.ExcitedDTEC.getFluid(1 * BUCKETS))
            .duration(100)
            .eut(EXOTIC_EU_PER_L)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(5))
            .fluidInputs(
                Materials.Helium.getPlasma(1 * BUCKETS),
                Materials.Iron.getPlasma(1 * BUCKETS),
                Materials.Calcium.getPlasma(1 * BUCKETS),
                Materials.Niobium.getPlasma(1 * BUCKETS),
                Materials.Radon.getPlasma(1 * BUCKETS),
                Materials.Nickel.getPlasma(1 * BUCKETS),
                Materials.Boron.getPlasma(1 * BUCKETS),
                Materials.Sulfur.getPlasma(1 * BUCKETS),
                Materials.Nitrogen.getPlasma(1 * BUCKETS),
                Materials.Zinc.getPlasma(1 * BUCKETS),
                Materials.Silver.getPlasma(1 * BUCKETS),
                Materials.Titanium.getPlasma(1 * BUCKETS),
                Materials.Americium.getPlasma(1 * BUCKETS),
                Materials.Bismuth.getPlasma(1 * BUCKETS),
                Materials.Oxygen.getPlasma(1 * BUCKETS),
                Materials.Tin.getPlasma(1 * BUCKETS),
                Materials.Lead.getPlasma(1 * BUCKETS),
                Materials.Thorium.getPlasma(1 * BUCKETS),
                Materials.Naquadria.getPlasma(100L),
                MaterialsUEVplus.RawStarMatter.getFluid(25L))
            .fluidOutputs(MaterialsUEVplus.ExcitedDTSC.getFluid(1 * BUCKETS))
            .duration(100)
            .eut(STELLAR_EU_PER_HALF_L)
            .metadata(GTRecipeConstants.EU_MULTIPLIER, 20)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .fluidInputs(
                MaterialsUEVplus.RawStarMatter.getFluid(1 * BUCKETS),
                MaterialsUEVplus.SpaceTime.getMolten(1 * BUCKETS),
                MaterialsUEVplus.Space.getMolten(1 * BUCKETS),
                MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(1 * BUCKETS))
            .fluidOutputs(MaterialsUEVplus.PrimordialMatter.getFluid(1 * BUCKETS))
            .duration(100)
            .eut(PRIMORDIAL_MATTER)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .fluidInputs(
                new FluidStack(MaterialsElements.getInstance().FERMIUM.getPlasma(), 1 * BUCKETS),
                Materials.Thorium.getPlasma(1 * BUCKETS),
                new FluidStack(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getPlasma(), 1 * BUCKETS),
                Materials.Calcium.getPlasma(1 * BUCKETS),
                MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(1 * BUCKETS))
            .fluidOutputs(MaterialsUEVplus.Creon.getPlasma(5 * BUCKETS))
            .duration(100)
            .eut(TierEU.RECIPE_UMV)
            .addTo(transcendentPlasmaMixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .fluidInputs(
                Materials.Infinity.getPlasma(1 * BUCKETS),
                Materials.Neutronium.getPlasma(1 * BUCKETS),
                Materials.Flerovium.getPlasma(1 * BUCKETS),
                new FluidStack(MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getPlasma(), 1 * BUCKETS),
                new FluidStack(MaterialsElements.STANDALONE.HYPOGEN.getPlasma(), 1 * BUCKETS),
                Materials.Ichorium.getPlasma(1 * BUCKETS),
                MaterialsUEVplus.SixPhasedCopper.getPlasma(1 * BUCKETS),
                Materials.DraconiumAwakened.getPlasma(1 * BUCKETS),
                new FluidStack(MaterialsElements.STANDALONE.DRAGON_METAL.getPlasma(), 1 * BUCKETS),
                new FluidStack(MaterialsElements.STANDALONE.RHUGNOR.getPlasma(), 1 * BUCKETS),
                Materials.Draconium.getPlasma(1 * BUCKETS),
                MaterialsUEVplus.Creon.getPlasma(1 * BUCKETS),
                Materials.Tritanium.getPlasma(1 * BUCKETS),
                Materials.CosmicNeutronium.getPlasma(1 * BUCKETS),
                Materials.Bedrockium.getPlasma(1 * BUCKETS),
                MaterialsUEVplus.ExcitedDTCC.getFluid(1 * BUCKETS),
                MaterialsUEVplus.ExcitedDTPC.getFluid(1 * BUCKETS),
                MaterialsUEVplus.ExcitedDTRC.getFluid(1 * BUCKETS),
                MaterialsUEVplus.ExcitedDTEC.getFluid(1 * BUCKETS),
                MaterialsUEVplus.ExcitedDTSC.getFluid(1 * BUCKETS))
            .fluidOutputs(MaterialsUEVplus.StargateCrystalSlurry.getFluid(1 * BUCKETS))
            .duration(100 * GTRecipeBuilder.TICKS)
            // 1 Trillion EU/t
            .metadata(GTRecipeConstants.EU_MULTIPLIER, 1000)
            .eut(1_000_000_000L)
            .addTo(transcendentPlasmaMixerRecipes);
    }
}
