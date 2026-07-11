package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static gregtech.api.enums.Mods.AdvancedSolarPanel;
import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.PamsHarvestCraft;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.molecularTransformerRecipes;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import advsolar.utils.MTRecipeManager;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTModHandler;

public class RecipeLoaderMolecularTransformer {

    public static void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.NetherQuartz, Materials2Shapes.shapeDust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.CertusQuartz, Materials2Shapes.shapeDust, (int) (1)))
            .duration(1 * SECONDS + 13 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .addTo(molecularTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.CertusQuartz, Materials2Shapes.shapeDust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.NetherQuartz, Materials2Shapes.shapeDust, (int) (1)))
            .duration(1 * SECONDS + 13 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .addTo(molecularTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Redstone, Materials2Shapes.shapeDust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ruby, Materials2Shapes.shapeDust, (int) (1)))
            .duration(16 * SECONDS + 6 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .addTo(molecularTransformerRecipes);
        // GT Cheese(and pams cheese) -> GalactiCraft Cheese
        if (GalacticraftCore.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Food_Cheese.get(1L))
                .itemOutputs(GTModHandler.getModItem(GalacticraftCore.ID, "item.cheeseCurd", 1L))
                .duration(16 * SECONDS + 6 * TICKS)
                .eut(TierEU.RECIPE_IV)
                .addTo(molecularTransformerRecipes);
            if (PamsHarvestCraft.isModLoaded()) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTModHandler.getModItem(PamsHarvestCraft.ID, "cheeseItem", 1L))
                    .itemOutputs(GTModHandler.getModItem(GalacticraftCore.ID, "item.cheeseCurd", 1L))
                    .duration(16 * SECONDS + 6 * TICKS)
                    .eut(TierEU.RECIPE_IV)
                    .addTo(molecularTransformerRecipes);
            }
        }

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.shapeDust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.shapeDust, (int) (1)))
            .duration(16 * SECONDS + 6 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .addTo(molecularTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.shapeDust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.shapeDust, (int) (1)))
            .duration(16 * SECONDS + 6 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .addTo(molecularTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.GarnetRed, Materials2Shapes.shapeDust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.GarnetYellow, Materials2Shapes.shapeDust, (int) (1)))
            .duration(16 * SECONDS + 6 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .addTo(molecularTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.GarnetYellow, Materials2Shapes.shapeDust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.GarnetRed, Materials2Shapes.shapeDust, (int) (1)))
            .duration(16 * SECONDS + 6 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .addTo(molecularTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.shapeDust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.shapeDust, (int) (1)))
            .duration(32 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .addTo(molecularTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Graphene, Materials2Shapes.shapeDust, (int) (1)))
            .duration(32 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .addTo(molecularTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.shapeDust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Platinum, Materials2Shapes.shapeDust, (int) (1)))
            .duration(4 * MINUTES + 20 * SECONDS + 9 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .addTo(molecularTransformerRecipes);

        if (AdvancedSolarPanel.isModLoaded()) {
            MTRecipeManager.transformerRecipes.clear();
            if (GalaxySpace.isModLoaded()) {

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        MaterialLibAPI.getStack(Materials2Materials.Glowstone, Materials2Shapes.shapeDust, (int) (1)))
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
}
