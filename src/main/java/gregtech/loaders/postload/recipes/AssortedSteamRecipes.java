package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.metadata.CompressionTierKey;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

public class AssortedSteamRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .fluidInputs(GTModHandler.getSteam(100000))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.CompressedSteam, 1))
            .duration(30 * SECONDS)
            .eut(512)
            .metadata(CompressionTierKey.INSTANCE, 2)
            .addTo(compressorRecipes);
    }
}
