package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static advsolar.utils.MTRecipeManager.transformerRecipes;
import static gregtech.api.enums.Mods.AdvancedSolarPanel;
import static gregtech.api.enums.Mods.GalaxySpace;

import advsolar.utils.MTRecipeRecord;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public class RecipeLoader_MolecularTransformer {

    public static void run() {

        for (MTRecipeRecord aRecipe : transformerRecipes) {
            int aEU = MaterialUtils.getVoltageForTier(5);
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
            CORE.RA.addMolecularTransformerRecipe(aRecipe.inputStack, aRecipe.outputStack, aTicksRoundedUp, aEU, 2);
            Logger.INFO("=======================");
        }

        transformerRecipes.clear();
        if (AdvancedSolarPanel.isModLoaded() && GalaxySpace.isModLoaded()) {

            CORE.RA.addMolecularTransformerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L),
                    GT_ModHandler.getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 1L, 9),
                    600,
                    1920,
                    2);
            CORE.RA.addMolecularTransformerRecipe(
                    GT_ModHandler.getModItem(GalaxySpace.ID, "item.GlowstoneDusts", 1L, 0),
                    GT_ModHandler.getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 1L, 9),
                    150,
                    7680,
                    2);
            CORE.RA.addMolecularTransformerRecipe(
                    GT_ModHandler.getModItem(GalaxySpace.ID, "item.GlowstoneDusts", 1L, 1),
                    GT_ModHandler.getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 1L, 9),
                    38,
                    30720,
                    2);
            CORE.RA.addMolecularTransformerRecipe(
                    GT_ModHandler.getModItem(GalaxySpace.ID, "item.GlowstoneDusts", 1L, 2),
                    GT_ModHandler.getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 1L, 9),
                    10,
                    122880,
                    2);
            CORE.RA.addMolecularTransformerRecipe(
                    GT_ModHandler.getModItem(GalaxySpace.ID, "item.GlowstoneDusts", 1L, 3),
                    GT_ModHandler.getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 1L, 9),
                    3,
                    491520,
                    2);
            CORE.RA.addMolecularTransformerRecipe(
                    GT_ModHandler.getModItem(GalaxySpace.ID, "item.GlowstoneDusts", 1L, 4),
                    GT_ModHandler.getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 1L, 9),
                    1,
                    1996080,
                    2);
        }
    }
}
