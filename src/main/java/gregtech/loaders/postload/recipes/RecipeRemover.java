package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.ExtraTrees;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import ic2.api.recipe.ILiquidHeatExchangerManager;
import ic2.api.recipe.Recipes;

public class RecipeRemover implements Runnable {

    @Override
    public void run() {
        GTModHandler.removeRecipeByOutput(GTModHandler.getIC2Item("fertilizer", 1L));
        removeCrafting();
        removeSmelting();
        removeIC2Recipes();
    }

    public void removeCrafting() {
        GTModHandler.removeRecipe(new ItemStack(Items.lava_bucket), GTModHandler.getIC2Item("cell", 1L));
        GTModHandler.removeRecipe(new ItemStack(Items.water_bucket), GTModHandler.getIC2Item("cell", 1L));
    }

    public void removeIC2Recipes() {

        try {
            GTUtility.removeSimpleIC2MachineRecipe(
                GTValues.NI,
                Recipes.metalformerExtruding.getRecipes(),
                GTModHandler.getIC2Item("cell", 1L));
            GTUtility.removeSimpleIC2MachineRecipe(
                GTModHandler.getIC2Item("energiumDust", 1L),
                Recipes.compressor.getRecipes(),
                GTValues.NI);
            GTUtility.removeSimpleIC2MachineRecipe(
                new ItemStack(Items.gunpowder),
                Recipes.extractor.getRecipes(),
                GTValues.NI);
            GTUtility.removeSimpleIC2MachineRecipe(
                new ItemStack(Blocks.wool, 1, 32767),
                Recipes.extractor.getRecipes(),
                GTValues.NI);
            GTUtility.removeSimpleIC2MachineRecipe(
                new ItemStack(Blocks.gravel),
                Recipes.oreWashing.getRecipes(),
                GTValues.NI);
        } catch (Exception ignored) {}
        GTUtility.removeIC2BottleRecipe(
            ItemList.IC2_Fuel_Rod_Empty.get(1),
            ItemList.IC2_Uranium_Fuel.get(1),
            Recipes.cannerBottle.getRecipes(),
            GTModHandler.getIC2Item("reactorUraniumSimple", 1, 1));
        GTUtility.removeIC2BottleRecipe(
            ItemList.IC2_Fuel_Rod_Empty.get(1),
            ItemList.IC2_MOX_Fuel.get(1),
            Recipes.cannerBottle.getRecipes(),
            GTModHandler.getIC2Item("reactorMOXSimple", 1, 1));

        GTUtility.removeSimpleIC2MachineRecipe(
            new ItemStack(Blocks.cobblestone),
            GTModHandler.getMaceratorRecipeList(),
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L));
        GTUtility.removeSimpleIC2MachineRecipe(
            GTOreDictUnificator.get(OrePrefixes.gem, Materials.Lapis, 1L),
            GTModHandler.getMaceratorRecipeList(),
            GTModHandler.getIC2Item("plantBall", 1L));
        GTUtility.removeSimpleIC2MachineRecipe(
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
            GTModHandler.getMaceratorRecipeList(),
            GTModHandler.getIC2Item("plantBall", 1L));
        GTUtility.removeSimpleIC2MachineRecipe(
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L),
            GTModHandler.getMaceratorRecipeList(),
            GTModHandler.getIC2Item("plantBall", 1L));
        GTUtility.removeSimpleIC2MachineRecipe(
            GTValues.NI,
            GTModHandler.getMaceratorRecipeList(),
            GTModHandler.getIC2Item("biochaff", 1L));

        GTUtility.removeSimpleIC2MachineRecipe(
            new ItemStack(Blocks.cactus, 8, 0),
            GTModHandler.getCompressorRecipeList(),
            GTModHandler.getIC2Item("plantBall", 1L));
        GTUtility.removeSimpleIC2MachineRecipe(
            GTModHandler.getModItem(ExtraTrees.ID, "food", 8L, 24),
            GTModHandler.getCompressorRecipeList(),
            GTModHandler.getIC2Item("plantBall", 1L));

        GTUtility.removeSimpleIC2MachineRecipe(
            ItemList.Crop_Drop_BobsYerUncleRanks.get(1L),
            GTModHandler.getExtractorRecipeList(),
            null);
        GTUtility.removeSimpleIC2MachineRecipe(
            ItemList.Crop_Drop_Ferru.get(1L),
            GTModHandler.getExtractorRecipeList(),
            null);
        GTUtility.removeSimpleIC2MachineRecipe(
            ItemList.Crop_Drop_Aurelia.get(1L),
            GTModHandler.getExtractorRecipeList(),
            null);

        try {
            Map<String, ILiquidHeatExchangerManager.HeatExchangeProperty> tLiqExchange = Recipes.liquidCooldownManager
                .getHeatExchangeProperties();
            Iterator<Map.Entry<String, ILiquidHeatExchangerManager.HeatExchangeProperty>> tIterator = tLiqExchange
                .entrySet()
                .iterator();
            while (tIterator.hasNext()) {
                Map.Entry<String, ILiquidHeatExchangerManager.HeatExchangeProperty> tEntry = tIterator.next();
                if (tEntry.getKey()
                    .equals("ic2hotcoolant")) {
                    tIterator.remove();
                    Recipes.liquidCooldownManager.addFluid("ic2hotcoolant", "ic2coolant", 100);
                }
            }
        } catch (Exception e) {
            /* Do nothing */
        }

        try {
            Map<String, ILiquidHeatExchangerManager.HeatExchangeProperty> tLiqExchange = Recipes.liquidHeatupManager
                .getHeatExchangeProperties();
            Iterator<Map.Entry<String, ILiquidHeatExchangerManager.HeatExchangeProperty>> tIterator = tLiqExchange
                .entrySet()
                .iterator();
            while (tIterator.hasNext()) {
                Map.Entry<String, ILiquidHeatExchangerManager.HeatExchangeProperty> tEntry = tIterator.next();
                if (tEntry.getKey()
                    .equals("ic2coolant")) {
                    tIterator.remove();
                    Recipes.liquidHeatupManager.addFluid("ic2coolant", "ic2hotcoolant", 100);
                }
            }
        } catch (Exception e) {
            /* Do nothing */
        }

    }

    public void removeSmelting() {
        GTModHandler.removeFurnaceSmelting(GTOreDictUnificator.get(OrePrefixes.ore, Materials.Graphite, 1L));
        GTModHandler
            .removeFurnaceSmelting(GTOreDictUnificator.get(OrePrefixes.oreBlackgranite, Materials.Graphite, 1L));
        GTModHandler.removeFurnaceSmelting(GTOreDictUnificator.get(OrePrefixes.oreEndstone, Materials.Graphite, 1L));
        GTModHandler.removeFurnaceSmelting(GTOreDictUnificator.get(OrePrefixes.oreNetherrack, Materials.Graphite, 1L));
        GTModHandler.removeFurnaceSmelting(GTOreDictUnificator.get(OrePrefixes.oreRedgranite, Materials.Graphite, 1L));
        GTModHandler.removeFurnaceSmelting(ItemList.IC2_Resin.get(1L));
    }
}
