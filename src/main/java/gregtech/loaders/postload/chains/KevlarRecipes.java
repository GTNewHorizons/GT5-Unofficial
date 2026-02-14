package gregtech.loaders.postload.chains;

import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;

import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhlanth.common.register.BotWerkstoffMaterialPool;

public class KevlarRecipes {

    public static void run() {

        // TPA, Precursor to TLC
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.BlueMetalCatalyst.get(0))
            .fluidInputs(Materials.Dimethylbenzene.getFluid(1000), Materials.Oxygen.getGas(6000))
            .fluidOutputs(Materials.TerephthalicAcid.getFluid(1000), Materials.Water.getFluid(2000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

        // Precursor to TLC
        // GTValues.RA.stdBuilder()
        // .fluidInputs(
        // Materials.Chlorine.getGas(12000),
        // Materials.Dimethylbenzene.getFluid(1000))
        // .fluidOutputs()

        // TLC, Precursor to LCK

        // Precursor to PPD
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Ammonia.getGas(2000), Materials.Nitrochlorobenzene.getFluid(1000))
            .fluidOutputs(Materials.Nitroaniline.getFluid(1000), WerkstoffLoader.AmmoniumChloride.getFluidOrGas(1000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // PPD, Precursor to LCK
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.PinkMetalCatalyst.get(0), Materials.Iron.getDust(1))
            .fluidInputs(Materials.Hydrogen.getGas(6000), Materials.Nitroaniline.getFluid(1000))
            .fluidOutputs(Materials.ParaPhenylenediamine.getFluid(1000), Materials.Water.getFluid(2000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

        // LCK, First taste of Kevlar

        // MDA, Precursor to MDI
        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(GTPPFluids.Nitrobenzene, 2000),
                new FluidStack(GTPPFluids.Formaldehyde, 1000),
                Materials.Hydrogen.getGas(12000),
                Materials.HydrochloricAcid.getFluid(4000))
            .fluidOutputs(
                Materials.Methylenedianiline.getFluid(1000),
                Materials.Water.getFluid(6000),
                Materials.DilutedHydrochloricAcid.getFluid(4000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // MDI, Precursor to Polyurethane
        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.Methylenedianiline.getFluid(1000),
                BotWerkstoffMaterialPool.Phosgene.getFluidOrGas(2000))
            .itemOutputs(Materials.MethyleneDiphenylDiisocyanate.getDust(1))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(4000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // Polyurethane Resin, Precursor for Kevlar Plates
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.MethyleneDiphenylDiisocyanate.getDust(1))
            .fluidInputs(Materials.Ethyleneglycol.getFluid(1000), Materials.Glycerol.getFluid(1000))
            .fluidOutputs(Materials.PolyurethaneResin.getFluid(1000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);
    }
}
