package gregtech.loaders.postload.recipes;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sTranscendentPlasmaMixerRecipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Utility;

public class TranscendentPlasmaMixerRecipes implements Runnable {

    private static final int CRUDE_EU_PER_L = 14_514_983;
    private static final int PROSAIC_EU_PER_L = 66_768_460;
    private static final int RESPLENDENT_EU_PER_L = 269_326_451;
    private static final int EXOTIC_EU_PER_L = 1_073_007_393;
    private static final int STELLAR_EU_PER_HALF_L = 2_138_383_760;

    @Deprecated
    private static void addTranscendentPlasmaMixerRecipe(final FluidStack fluidOutput, final int circuitNumber,
        final FluidStack[] fluidInput, final int EUPerL) {
        sTranscendentPlasmaMixerRecipes.addRecipe(
            false,
            new ItemStack[] { GT_Utility.getIntegratedCircuit(circuitNumber) },
            null,
            null,
            fluidInput,
            new FluidStack[] { fluidOutput },
            100,
            EUPerL, // NOT EU/T, I am simply using the field for this purpose.
            0);
    }

    @Override
    public void run() {

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .noItemOutputs()
            .fluidInputs(
                Materials.Helium.getPlasma(1000),
                Materials.Iron.getPlasma(1000),
                Materials.Calcium.getPlasma(1000),
                Materials.Niobium.getPlasma(1000))
            .fluidOutputs(Materials.ExcitedDTCC.getFluid(1000L))
            .duration(100)
            .eut(CRUDE_EU_PER_L)
            .noOptimize()
            .addTo(sTranscendentPlasmaMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(2))
            .noItemOutputs()
            .fluidInputs(
                Materials.Helium.getPlasma(1000),
                Materials.Iron.getPlasma(1000),
                Materials.Calcium.getPlasma(1000),
                Materials.Niobium.getPlasma(1000),
                Materials.Radon.getPlasma(1000),
                Materials.Nickel.getPlasma(1000),
                Materials.Boron.getPlasma(1000),
                Materials.Sulfur.getPlasma(1000))
            .fluidOutputs(Materials.ExcitedDTPC.getFluid(1000L))
            .duration(100)
            .eut(PROSAIC_EU_PER_L)
            .noOptimize()
            .addTo(sTranscendentPlasmaMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(3))
            .noItemOutputs()
            .fluidInputs(
                Materials.Helium.getPlasma(1000),
                Materials.Iron.getPlasma(1000),
                Materials.Calcium.getPlasma(1000),
                Materials.Niobium.getPlasma(1000),
                Materials.Radon.getPlasma(1000),
                Materials.Nickel.getPlasma(1000),
                Materials.Boron.getPlasma(1000),
                Materials.Sulfur.getPlasma(1000),
                Materials.Nitrogen.getPlasma(1000),
                Materials.Zinc.getPlasma(1000),
                Materials.Silver.getPlasma(1000),
                Materials.Titanium.getPlasma(1000))
            .fluidOutputs(Materials.ExcitedDTRC.getFluid(1000L))
            .duration(100)
            .eut(RESPLENDENT_EU_PER_L)
            .noOptimize()
            .addTo(sTranscendentPlasmaMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(4))
            .noItemOutputs()
            .fluidInputs(
                Materials.Helium.getPlasma(1000),
                Materials.Iron.getPlasma(1000),
                Materials.Calcium.getPlasma(1000),
                Materials.Niobium.getPlasma(1000),
                Materials.Radon.getPlasma(1000),
                Materials.Nickel.getPlasma(1000),
                Materials.Boron.getPlasma(1000),
                Materials.Sulfur.getPlasma(1000),
                Materials.Nitrogen.getPlasma(1000),
                Materials.Zinc.getPlasma(1000),
                Materials.Silver.getPlasma(1000),
                Materials.Titanium.getPlasma(1000),
                Materials.Americium.getPlasma(1000),
                Materials.Bismuth.getPlasma(1000),
                Materials.Oxygen.getPlasma(1000),
                Materials.Tin.getPlasma(1000))
            .fluidOutputs(Materials.ExcitedDTEC.getFluid(1000L))
            .duration(100)
            .eut(EXOTIC_EU_PER_L)
            .noOptimize()
            .addTo(sTranscendentPlasmaMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(5))
            .noItemOutputs()
            .fluidInputs(
                Materials.Helium.getPlasma(1000),
                Materials.Iron.getPlasma(1000),
                Materials.Calcium.getPlasma(1000),
                Materials.Niobium.getPlasma(1000),
                Materials.Radon.getPlasma(1000),
                Materials.Nickel.getPlasma(1000),
                Materials.Boron.getPlasma(1000),
                Materials.Sulfur.getPlasma(1000),
                Materials.Nitrogen.getPlasma(1000),
                Materials.Zinc.getPlasma(1000),
                Materials.Silver.getPlasma(1000),
                Materials.Titanium.getPlasma(1000),
                Materials.Americium.getPlasma(1000),
                Materials.Bismuth.getPlasma(1000),
                Materials.Oxygen.getPlasma(1000),
                Materials.Tin.getPlasma(1000),
                Materials.Lead.getPlasma(1000),
                Materials.Thorium.getPlasma(1000),
                Materials.Plutonium241.getPlasma(1000L),
                Materials.RawStarMatter.getFluid(25L))
            .fluidOutputs(Materials.ExcitedDTSC.getFluid(1000L))
            .duration(200)
            .eut(STELLAR_EU_PER_HALF_L)
            .noOptimize()
            .addTo(sTranscendentPlasmaMixerRecipes);

    }
}
