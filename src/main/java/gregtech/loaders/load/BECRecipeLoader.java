package gregtech.loaders.load;

import gregtech.api.enums.CondensateType;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.NaniteTier;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import tectech.recipe.TecTechRecipeMaps;

public class BECRecipeLoader {

    public static void run() {
        loadAssemblyRecipes();
    }

    private static void loadAssemblyRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 2L),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 2L),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 2L),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 2L),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 2L),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 2L),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 2L),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 2L))
            .fluidInputs(CondensateType.Quantium.getEntangled(1_000_000))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 2L))
            .metadata(
                GTRecipeConstants.NANITE_TIERS,
                new NaniteTier[] { NaniteTier.Carbon, NaniteTier.Carbon, NaniteTier.Carbon, NaniteTier.Carbon,
                    NaniteTier.Neutronium, NaniteTier.Silver, NaniteTier.Gold, NaniteTier.Neutronium, })
            .eut(144)
            .duration(200)
            .addTo(TecTechRecipeMaps.condensateAssemblingRecipes);
    }
}
