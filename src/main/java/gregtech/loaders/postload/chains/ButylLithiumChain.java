package gregtech.loaders.postload.chains;

import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalDehydratorRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.util.GTModHandler.getDistilledWater;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.DISSOLUTION_TANK_RATIO;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gtnhlanth.api.recipe.LanthanidesRecipeMaps.dissolutionTankRecipes;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gtPlusPlus.core.fluids.GTPPFluids;

public class ButylLithiumChain {

    public static void run() {
        // n-BuOH + PCl5 -> n-BuCl + POCl3 + HCl
        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(FluidRegistry.getFluid("butanol"), 1000),
                Materials.PhosphorusPentachloride.getFluid(1000L))
            .fluidOutputs(Materials.nButylChlorideMixture.getFluid(1000L))
            .eut(TierEU.RECIPE_IV)
            .duration(1 * MINUTES)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("butanol"), 1000))
            .itemInputs(Materials.PhosphorusPentachloride.getCells(1))
            .fluidOutputs(Materials.nButylChlorideMixture.getFluid(1000L))
            .eut(TierEU.RECIPE_IV)
            .duration(1 * MINUTES)
            .addTo(chemicalReactorRecipes);

        // washing the mixture
        GTValues.RA.stdBuilder()
            .fluidInputs(getDistilledWater(1000L), Materials.nButylChlorideMixture.getFluid(1000L))
            .fluidOutputs(
                Materials.nButylChloridePurified.getFluid(1000L),
                Materials.PhosphoricAcidMixture.getFluid(1000L))
            .circuit(1)
            .eut(TierEU.RECIPE_HV)
            .duration(30 * SECONDS)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.NefariousOil.getFluid(5000L), Materials.nButylChlorideMixture.getFluid(1000L))
            .fluidOutputs(
                Materials.nButylChloridePurified.getFluid(4000L),
                Materials.PhosphoricAcidMixture.getFluid(2000L))
            .circuit(1)
            .eut(TierEU.RECIPE_LuV)
            .duration(15 * SECONDS)
            .addTo(chemicalBathRecipes);

        // dehydrate
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.nButylChloridePurified.getFluid(1000L))
            .fluidOutputs(Materials.nButylChloride.getFluid(1000L))
            .eut(TierEU.RECIPE_IV)
            .duration(1 * MINUTES)
            .addTo(chemicalDehydratorRecipes);

        // dissolve with cyclohexane
        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.nButylChloride.getFluid(5000L),
                new FluidStack(FluidRegistry.getFluid("cyclohexane"), 5000))
            .fluidOutputs(Materials.nButylChlorideSolution.getFluid(10000L))
            .circuit(1)
            .eut(TierEU.RECIPE_HV)
            .duration(15 * SECONDS)
            .metadata(DISSOLUTION_TANK_RATIO, 1)
            .addTo(dissolutionTankRecipes);

        // n-BuCl + Li -> LiCl + n-BuLi
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.nButylChlorideSolution.getFluid(2000L))
            .itemInputs(Materials.Lithium.getDust(2))
            .fluidOutputs(Materials.nButylLithiumSlurry.getFluid(2000L))
            .eut(TierEU.RECIPE_ZPM)
            .duration(30 * SECONDS)
            .addTo(UniversalChemical);

        // extracting LiCl
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.nButylLithiumSlurry.getFluid(1000L))
            .fluidOutputs(Materials.nButylLithiumSolutionMixture.getFluid(1000L))
            .itemOutputs(GGMaterial.lithiumChloride.get(OrePrefixes.dust, 1))
            .eut(TierEU.RECIPE_HV)
            .duration(15 * SECONDS)
            .addTo(centrifugeRecipes);

        // Concentration of n-BuLi
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.nButylLithiumSolutionMixture.getFluid(10000L))
            .fluidOutputs(
                Materials.nButylLithiumSolutionConcentrated.getFluid(6000L),
                Materials.Octane.getFluid(200L),
                new FluidStack(GTPPFluids.Cyclohexane, 5000))
            .eut(TierEU.RECIPE_LuV)
            .duration(20 * SECONDS)
            .addTo(distillationTowerRecipes);

        // postprocessing of phosphorus acid mixture
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.PhosphoricAcidMixture.getFluid(15000L))
            .fluidOutputs(
                Materials.PhosphoricAcid.getFluid(5000L),
                Materials.Water.getFluid(5000L),
                Materials.HydrochloricAcid.getFluid(5000L))
            .eut(TierEU.RECIPE_HV)
            .duration(15 * SECONDS)
            .addTo(distillationTowerRecipes);
    }
}
