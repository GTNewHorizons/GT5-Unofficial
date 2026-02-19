package gregtech.loaders.postload.chains;

import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.DISSOLUTION_TANK_RATIO;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gtnhlanth.api.recipe.LanthanidesRecipeMaps.dissolutionTankRecipes;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gtPlusPlus.core.material.MaterialsElements;

public class FranciumLineRecipes {

    public static void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Uranium.getDust(16))
            .fluidInputs(Materials.NitricAcid.getFluid(10000L), Materials.TerephthalicAcid.getFluid(400L))
            .itemOutputs(Materials.DepletedUraniumResidue.getDust(8))
            .fluidOutputs(Materials.UraniumInfusedAcidicSolution.getFluid(15000L))
            .duration(240 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(DISSOLUTION_TANK_RATIO, 25)
            .addTo(dissolutionTankRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Uranium235.getDust(8))
            .fluidInputs(Materials.NitricAcid.getFluid(10000L), Materials.TerephthalicAcid.getFluid(400L))
            .itemOutputs(Materials.DepletedUraniumResidue.getDust(8))
            .fluidOutputs(Materials.UraniumInfusedAcidicSolution.getFluid(30000L))
            .duration(160 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(DISSOLUTION_TANK_RATIO, 25)
            .addTo(dissolutionTankRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsElements.getInstance().URANIUM233.getDust(4))
            .fluidInputs(Materials.NitricAcid.getFluid(10000L), Materials.TerephthalicAcid.getFluid(400L))
            .itemOutputs(Materials.DepletedUraniumResidue.getDust(8))
            .fluidOutputs(Materials.UraniumInfusedAcidicSolution.getFluid(60000L))
            .duration(80 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(DISSOLUTION_TANK_RATIO, 25)
            .addTo(dissolutionTankRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.DepletedUraniumResidue.getDust(5))
            .itemOutputs(Materials.Lead.getDust(3), Materials.Bismuth.getDust(2))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.UraniumInfusedAcidicSolution.getFluid(6000L))
            .itemOutputs(Materials.Thorium.getDust(1), Materials.Lead.getDust(2), Materials.CrudeFrancium.getDust(1))
            .fluidOutputs(Materials.NitrogenDioxide.getFluid(1000L))
            .outputChances(9000, 8000, 6000)
            .duration(15 * SECONDS)
            .eut(1200)
            .addTo(sifterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.CrudeFrancium.getDust(10), GGMaterial.vanadiumPentoxide.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(4000L))
            .itemOutputs(Materials.Francium.getDust(1))
            .fluidOutputs(Materials.HypochlorousAcid.getFluid(4000L))
            .outputChances(3000)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);
        // Fr + H2O = FrOH + H
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Francium.getDust(1))
            .circuit(1)
            .itemOutputs(Materials.FranciumHydroxide.getDust(3))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(1_000))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(UniversalChemical);
    }
}
