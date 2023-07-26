package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.GalacticraftMars;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sImplosionRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_RecipeConstants;

public class ImplosionCompressorRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Compressed_Coal_Chunk.get(1L))
            .itemOutputs(
                ItemList.IC2_Industrial_Diamond.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 4L))
            .noFluidInputs()
            .noFluidOutputs()
            .metadata(GT_RecipeConstants.ADDITIVE_AMOUNT, 8)
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sImplosionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Ingot_IridiumAlloy.get(1L))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.plateAlloy, Materials.Iridium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 4L))
            .noFluidInputs()
            .noFluidOutputs()
            .metadata(GT_RecipeConstants.ADDITIVE_AMOUNT, 8)
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sImplosionRecipes);

        if (GalacticraftMars.isModLoaded()) {

            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Ingot_Heavy1.get(1L))
                .itemOutputs(
                    getModItem(GalacticraftCore.ID, "item.heavyPlating", 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.StainlessSteel, 1L))
                .noFluidInputs()
                .noFluidOutputs()
                .metadata(GT_RecipeConstants.ADDITIVE_AMOUNT, 8)
                .duration(1 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sImplosionRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Ingot_Heavy2.get(1L))
                .itemOutputs(
                    getModItem(GalacticraftMars.ID, "item.null", 1L, 3),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.TungstenSteel, 2L))
                .noFluidInputs()
                .noFluidOutputs()
                .metadata(GT_RecipeConstants.ADDITIVE_AMOUNT, 16)
                .duration(1 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sImplosionRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Ingot_Heavy3.get(1L))
                .itemOutputs(
                    getModItem(GalacticraftMars.ID, "item.itemBasicAsteroids", 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Platinum, 3L))
                .noFluidInputs()
                .noFluidOutputs()
                .metadata(GT_RecipeConstants.ADDITIVE_AMOUNT, 24)
                .duration(1 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sImplosionRecipes);
        }
    }

    @Deprecated
    public void addImplosionRecipe(ItemStack input, int explosiveAmount, ItemStack[] outputs) {
        GT_Values.RA.stdBuilder()
            .itemInputs(input)
            .itemOutputs(outputs)
            .noFluidInputs()
            .noFluidOutputs()
            .metadata(GT_RecipeConstants.ADDITIVE_AMOUNT, explosiveAmount)
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sImplosionRecipes);
    }
}
