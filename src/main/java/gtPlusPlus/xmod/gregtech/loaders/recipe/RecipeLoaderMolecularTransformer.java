package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static advsolar.utils.MTRecipeManager.transformerRecipes;
import static gregtech.api.enums.Mods.AdvancedSolarPanel;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.molecularTransformerRecipes;

import advsolar.utils.MTRecipeRecord;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.math.MathUtils;

public class RecipeLoaderMolecularTransformer {

    public static void run() {

        for (MTRecipeRecord aRecipe : transformerRecipes) {
            int aEU = (int) TierEU.RECIPE_IV;
            Logger.INFO("=======================");
            Logger.INFO("Generating GT recipe for Molecular Transformer.");
            Logger.INFO(
                "Input: " + aRecipe.inputStack
                    .getDisplayName() + ", Output: " + aRecipe.outputStack.getDisplayName() + ", EU/t: " + aEU);
            float aTicks = (float) aRecipe.energyPerOperation / (float) aEU;
            Logger.INFO("Ticks: " + aTicks);
            int aTicksRoundedUp = MathUtils.roundToClosestInt(Math.ceil(aTicks));
            Logger.INFO("Ticks: " + aTicksRoundedUp);
            Logger.INFO("Total EU equal or greater? " + ((aTicksRoundedUp * aEU) >= aRecipe.energyPerOperation));
            GTValues.RA.stdBuilder()
                .itemInputs(aRecipe.inputStack)
                .itemOutputs(aRecipe.outputStack)
                .duration(aTicksRoundedUp)
                .eut(aEU)
                .addTo(molecularTransformerRecipes);

            Logger.INFO("=======================");
        }

        transformerRecipes.clear();
        if (AdvancedSolarPanel.isModLoaded() && GalaxySpace.isModLoaded()) {

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L))
                .itemOutputs(GTModHandler.getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 1L, 9))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(molecularTransformerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(GTModHandler.getModItem(GalaxySpace.ID, "item.GlowstoneDusts", 1L, 0))
                .itemOutputs(GTModHandler.getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 1L, 9))
                .duration(7 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_IV)
                .addTo(molecularTransformerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(GTModHandler.getModItem(GalaxySpace.ID, "item.GlowstoneDusts", 1L, 1))
                .itemOutputs(GTModHandler.getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 1L, 9))
                .duration(1 * SECONDS + 18 * TICKS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(molecularTransformerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(GTModHandler.getModItem(GalaxySpace.ID, "item.GlowstoneDusts", 1L, 2))
                .itemOutputs(GTModHandler.getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 1L, 9))
                .duration(10 * TICKS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(molecularTransformerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(GTModHandler.getModItem(GalaxySpace.ID, "item.GlowstoneDusts", 1L, 3))
                .itemOutputs(GTModHandler.getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 1L, 9))
                .duration(3 * TICKS)
                .eut(TierEU.RECIPE_UV)
                .addTo(molecularTransformerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(GTModHandler.getModItem(GalaxySpace.ID, "item.GlowstoneDusts", 1L, 4))
                .itemOutputs(GTModHandler.getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 1L, 9))
                .duration(1 * TICKS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(molecularTransformerRecipes);

        }
    }
}
