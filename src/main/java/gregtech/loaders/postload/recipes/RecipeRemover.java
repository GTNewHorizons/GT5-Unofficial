package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.ExtraTrees;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.util.GT_ModHandler.getModItem;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import ic2.api.recipe.ILiquidHeatExchangerManager;
import ic2.api.recipe.Recipes;

public class RecipeRemover implements Runnable {

    @Override
    public void run() {
        GT_ModHandler.removeRecipeByOutput(ItemList.IC2_Fertilizer.get(1L));
        removeCrafting();
        removeSmelting();
        removeIC2Recipes();
    }

    public void removeCrafting() {
        GT_ModHandler.removeRecipe(new ItemStack(Items.lava_bucket), ItemList.Cell_Empty.get(1L));
        GT_ModHandler.removeRecipe(new ItemStack(Items.water_bucket), ItemList.Cell_Empty.get(1L));
    }

    public void removeIC2Recipes() {

        try {
            GT_Utility.removeSimpleIC2MachineRecipe(
                GT_Values.NI,
                Recipes.metalformerExtruding.getRecipes(),
                ItemList.Cell_Empty.get(3L));
            GT_Utility.removeSimpleIC2MachineRecipe(
                ItemList.IC2_Energium_Dust.get(1L),
                Recipes.compressor.getRecipes(),
                GT_Values.NI);
            GT_Utility.removeSimpleIC2MachineRecipe(
                new ItemStack(Items.gunpowder),
                Recipes.extractor.getRecipes(),
                GT_Values.NI);
            GT_Utility.removeSimpleIC2MachineRecipe(
                new ItemStack(Blocks.wool, 1, 32767),
                Recipes.extractor.getRecipes(),
                GT_Values.NI);
            GT_Utility.removeSimpleIC2MachineRecipe(
                new ItemStack(Blocks.gravel),
                Recipes.oreWashing.getRecipes(),
                GT_Values.NI);
        } catch (Throwable ignored) {}
        GT_Utility.removeIC2BottleRecipe(
            GT_ModHandler.getIC2Item("fuelRod", 1),
            GT_ModHandler.getIC2Item("UranFuel", 1),
            Recipes.cannerBottle.getRecipes(),
            GT_ModHandler.getIC2Item("reactorUraniumSimple", 1, 1));
        GT_Utility.removeIC2BottleRecipe(
            GT_ModHandler.getIC2Item("fuelRod", 1),
            GT_ModHandler.getIC2Item("MOXFuel", 1),
            Recipes.cannerBottle.getRecipes(),
            GT_ModHandler.getIC2Item("reactorMOXSimple", 1, 1));

        GT_Utility.removeSimpleIC2MachineRecipe(
            new ItemStack(Blocks.cobblestone),
            GT_ModHandler.getMaceratorRecipeList(),
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L));
        GT_Utility.removeSimpleIC2MachineRecipe(
            GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Lapis, 1L),
            GT_ModHandler.getMaceratorRecipeList(),
            ItemList.IC2_Plantball.get(1L));
        GT_Utility.removeSimpleIC2MachineRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
            GT_ModHandler.getMaceratorRecipeList(),
            ItemList.IC2_Plantball.get(1L));
        GT_Utility.removeSimpleIC2MachineRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L),
            GT_ModHandler.getMaceratorRecipeList(),
            ItemList.IC2_Plantball.get(1L));
        GT_Utility.removeSimpleIC2MachineRecipe(
            GT_Values.NI,
            GT_ModHandler.getMaceratorRecipeList(),
            getModItem(IndustrialCraft2.ID, "itemBiochaff", 1L));

        GT_Utility.removeSimpleIC2MachineRecipe(
            new ItemStack(Blocks.cactus, 8, 0),
            GT_ModHandler.getCompressorRecipeList(),
            getModItem(IndustrialCraft2.ID, "itemFuelPlantBall", 1L));
        GT_Utility.removeSimpleIC2MachineRecipe(
            getModItem(ExtraTrees.ID, "food", 8L, 24),
            GT_ModHandler.getCompressorRecipeList(),
            getModItem(IndustrialCraft2.ID, "itemFuelPlantBall", 1L));

        GT_Utility.removeSimpleIC2MachineRecipe(
            ItemList.Crop_Drop_BobsYerUncleRanks.get(1L),
            GT_ModHandler.getExtractorRecipeList(),
            null);
        GT_Utility.removeSimpleIC2MachineRecipe(
            ItemList.Crop_Drop_Ferru.get(1L),
            GT_ModHandler.getExtractorRecipeList(),
            null);
        GT_Utility.removeSimpleIC2MachineRecipe(
            ItemList.Crop_Drop_Aurelia.get(1L),
            GT_ModHandler.getExtractorRecipeList(),
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
        } catch (Throwable e) {
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
        } catch (Throwable e) {
            /* Do nothing */
        }

    }

    public void removeSmelting() {
        GT_ModHandler.removeFurnaceSmelting(GT_OreDictUnificator.get(OrePrefixes.ore, Materials.Graphite, 1L));
        GT_ModHandler
            .removeFurnaceSmelting(GT_OreDictUnificator.get(OrePrefixes.oreBlackgranite, Materials.Graphite, 1L));
        GT_ModHandler.removeFurnaceSmelting(GT_OreDictUnificator.get(OrePrefixes.oreEndstone, Materials.Graphite, 1L));
        GT_ModHandler
            .removeFurnaceSmelting(GT_OreDictUnificator.get(OrePrefixes.oreNetherrack, Materials.Graphite, 1L));
        GT_ModHandler
            .removeFurnaceSmelting(GT_OreDictUnificator.get(OrePrefixes.oreRedgranite, Materials.Graphite, 1L));
        GT_ModHandler.removeFurnaceSmelting(ItemList.IC2_Resin.get(1L));
    }
}
