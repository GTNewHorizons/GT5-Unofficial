package gregtech.loaders.postload.recipes;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sTranscendentPlasmaMixerRecipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Utility;

public class TranscendentPlasmaMixerRecipes implements Runnable {

    private static final int CRUDE_EU_PER_L = 14_514_983;
    private static final int PROSAIC_EU_PER_L = 66_768_460;
    private static final int RESPLENDENT_EU_PER_L = 269_326_451;
    private static final int EXOTIC_EU_PER_L = 1_073_007_393;

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

        addTranscendentPlasmaMixerRecipe(
                Materials.ExcitedDTCC.getFluid(1000L),
                1,
                new FluidStack[] { Materials.Helium.getPlasma(1000), Materials.Iron.getPlasma(1000),
                        Materials.Calcium.getPlasma(1000), Materials.Niobium.getPlasma(1000) },
                CRUDE_EU_PER_L);

        addTranscendentPlasmaMixerRecipe(
                Materials.ExcitedDTPC.getFluid(1000L),
                2,
                new FluidStack[] { Materials.Helium.getPlasma(1000), Materials.Iron.getPlasma(1000),
                        Materials.Calcium.getPlasma(1000), Materials.Niobium.getPlasma(1000),

                        Materials.Nitrogen.getPlasma(1000), Materials.Zinc.getPlasma(1000),
                        Materials.Silver.getPlasma(1000), Materials.Titanium.getPlasma(1000), },
                PROSAIC_EU_PER_L);

        addTranscendentPlasmaMixerRecipe(
                Materials.ExcitedDTRC.getFluid(1000L),
                3,
                new FluidStack[] { Materials.Helium.getPlasma(1000), Materials.Iron.getPlasma(1000),
                        Materials.Calcium.getPlasma(1000), Materials.Niobium.getPlasma(1000),

                        Materials.Nitrogen.getPlasma(1000), Materials.Zinc.getPlasma(1000),
                        Materials.Silver.getPlasma(1000), Materials.Titanium.getPlasma(1000),

                        Materials.Radon.getPlasma(1000), Materials.Nickel.getPlasma(1000),
                        Materials.Boron.getPlasma(1000), Materials.Sulfur.getPlasma(1000), },
                RESPLENDENT_EU_PER_L);

        addTranscendentPlasmaMixerRecipe(
                Materials.ExcitedDTEC.getFluid(1000L),
                4,
                new FluidStack[] { Materials.Helium.getPlasma(1000), Materials.Iron.getPlasma(1000),
                        Materials.Calcium.getPlasma(1000), Materials.Niobium.getPlasma(1000),

                        Materials.Nitrogen.getPlasma(1000), Materials.Zinc.getPlasma(1000),
                        Materials.Silver.getPlasma(1000), Materials.Titanium.getPlasma(1000),

                        Materials.Radon.getPlasma(1000), Materials.Nickel.getPlasma(1000),
                        Materials.Boron.getPlasma(1000), Materials.Sulfur.getPlasma(1000),

                        Materials.Americium.getPlasma(1000), Materials.Bismuth.getPlasma(1000),
                        Materials.Oxygen.getPlasma(1000), Materials.Tin.getPlasma(1000), },
                EXOTIC_EU_PER_L);

    }
}
