package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.GalacticraftMars;
import static gregtech.api.recipe.RecipeMaps.implosionRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class ImplosionCompressorRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Compressed_Coal_Chunk.get(1L))
            .itemOutputs(
                ItemList.IC2_Industrial_Diamond.get(1L),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.AshDark, 4L))
            .metadata(GTRecipeConstants.ADDITIVE_AMOUNT, 8)
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(implosionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Ingot_IridiumAlloy.get(1L))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.plateAlloy, Materials.Iridium, 1L),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.AshDark, 4L))
            .metadata(GTRecipeConstants.ADDITIVE_AMOUNT, 8)
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(implosionRecipes);

        if (GalacticraftMars.isModLoaded()) {

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Ingot_Heavy1.get(1L))
                .itemOutputs(
                    getModItem(GalacticraftCore.ID, "item.heavyPlating", 1L),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.StainlessSteel, 1L))
                .metadata(GTRecipeConstants.ADDITIVE_AMOUNT, 8)
                .duration(1 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(implosionRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Ingot_Heavy2.get(1L))
                .itemOutputs(
                    getModItem(GalacticraftMars.ID, "item.null", 1L, 3),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.TungstenSteel, 2L))
                .metadata(GTRecipeConstants.ADDITIVE_AMOUNT, 16)
                .duration(1 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(implosionRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Ingot_Heavy3.get(1L))
                .itemOutputs(
                    getModItem(GalacticraftMars.ID, "item.itemBasicAsteroids", 1L),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Platinum, 3L))
                .metadata(GTRecipeConstants.ADDITIVE_AMOUNT, 24)
                .duration(1 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(implosionRecipes);
        }
    }
}
