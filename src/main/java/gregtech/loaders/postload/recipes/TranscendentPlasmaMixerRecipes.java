package gregtech.loaders.postload.recipes;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sTranscendentPlasmaMixerRecipes;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.util.GT_Utility;

public class TranscendentPlasmaMixerRecipes implements Runnable {

    private static final int CRUDE_EU_PER_L = 14_514_983;
    private static final int PROSAIC_EU_PER_L = 66_768_460;
    private static final int RESPLENDENT_EU_PER_L = 269_326_451;
    private static final int EXOTIC_EU_PER_L = 1_073_007_393;
    private static final int STELLAR_EU_PER_HALF_L = 2_138_383_760;

    private static final int PRIMORDIAL_MATTER = 2_000_000_000;
    private static final int ENHANCED_PERIODICIUM = 625_000_000;

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
            .fluidOutputs(MaterialsUEVplus.ExcitedDTCC.getFluid(1000L))
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
            .fluidOutputs(MaterialsUEVplus.ExcitedDTPC.getFluid(1000L))
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
            .fluidOutputs(MaterialsUEVplus.ExcitedDTRC.getFluid(1000L))
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
            .fluidOutputs(MaterialsUEVplus.ExcitedDTEC.getFluid(1000L))
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
                MaterialsUEVplus.RawStarMatter.getFluid(25L))
            .fluidOutputs(MaterialsUEVplus.ExcitedDTSC.getFluid(1000L))
            .duration(200)
            .eut(STELLAR_EU_PER_HALF_L)
            .noOptimize()
            .addTo(sTranscendentPlasmaMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(24))
            .noItemOutputs()
            .fluidInputs(
                MaterialsUEVplus.RawStarMatter.getFluid(1000L),
                MaterialsUEVplus.SpaceTime.getMolten(1000L),
                MaterialsUEVplus.Space.getMolten(1000L),
                MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(1000L))
            .fluidOutputs(MaterialsUEVplus.PrimordialMatter.getFluid(1000L))
            .duration(100)
            .eut(PRIMORDIAL_MATTER)
            .noOptimize()
            .addTo(sTranscendentPlasmaMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(4))
            .noItemOutputs()
            .fluidInputs(
                FluidRegistry.getFluidStack("molten.shirabon", 288),
                MaterialsUEVplus.SpaceTime.getMolten(576L),
                FluidRegistry.getFluidStack("molten.periodicium", 144),
                MaterialsUEVplus.ExcitedDTSC.getFluid(150L))
            .fluidOutputs(FluidRegistry.getFluidStack("molten.enhanced periodicium", 144))
            .duration(250)
            .eut(ENHANCED_PERIODICIUM)
            .noOptimize()
            .addTo(sTranscendentPlasmaMixerRecipes);
    }
}
