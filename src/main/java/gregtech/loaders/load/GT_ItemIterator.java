package gregtech.loaders.load;

import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.RecipeBuilder.SECONDS;
import static gregtech.api.util.RecipeBuilder.WILDCARD;
import static gregtech.api.util.RecipeConstants.FUEL_TYPE;
import static gregtech.api.util.RecipeConstants.FUEL_VALUE;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import buildcraft.api.tools.IToolWrench;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.ItemGeneric;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.OreDictUnificator;
import gregtech.api.util.RecipeBuilder;
import gregtech.api.util.RecipeConstants;
import mods.railcraft.api.core.items.IToolCrowbar;

public class GT_ItemIterator implements Runnable {

    @Override
    public void run() {
        GT_Log.out.println("GT_Mod: Scanning for certain kinds of compatible Machineblocks.");
        ItemStack tStack2 = OreDictUnificator.get(OrePrefixes.ingot, Materials.Bronze, 1L);
        ItemStack tStack = GT_ModHandler
            .getRecipeOutput(tStack2, tStack2, tStack2, tStack2, null, tStack2, tStack2, tStack2, tStack2);

        if (null != tStack) {
            GT_Values.RA.stdBuilder()
                .itemInputs(tStack)
                .itemOutputs(OreDictUnificator.get(OrePrefixes.dust, Materials.Bronze, 8L))
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);

            GT_ModHandler.addSmeltingRecipe(tStack, OreDictUnificator.get(OrePrefixes.ingot, Materials.Bronze, 8L));
        }
        tStack2 = OreDictUnificator.get(OrePrefixes.plate, Materials.Bronze, 1L);
        tStack = GT_ModHandler
            .getRecipeOutput(tStack2, tStack2, tStack2, tStack2, null, tStack2, tStack2, tStack2, tStack2);

        if (null != tStack) {
            OreDictUnificator.registerOre(OreDictNames.craftingRawMachineTier00, tStack);

            GT_Values.RA.stdBuilder()
                .itemInputs(tStack)
                .itemOutputs(OreDictUnificator.get(OrePrefixes.dust, Materials.Bronze, 8L))
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);
            GT_ModHandler.addSmeltingRecipe(tStack, OreDictUnificator.get(OrePrefixes.ingot, Materials.Bronze, 8L));
        }

        ItemStack tStack3 = new ItemStack(Blocks.glass, 1, 0);
        tStack2 = OreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L);
        tStack = GT_ModHandler.getRecipeOutput(
            tStack2,
            tStack3,
            tStack2,
            tStack3,
            OreDictUnificator.get(OrePrefixes.ingot, Materials.Gold, 1L),
            tStack3,
            tStack2,
            tStack3,
            tStack2);

        if (null != (tStack)) {
            GT_Values.RA.stdBuilder()
                .itemInputs(tStack)
                .itemOutputs(
                    OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 4L),
                    OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1L))
                .outputChances(10000, 1000)
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);
        }

        tStack2 = OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L);
        tStack3 = new ItemStack(Blocks.glass, 1, 0);
        tStack = GT_ModHandler.getRecipeOutput(
            tStack2,
            tStack3,
            tStack2,
            tStack3,
            OreDictUnificator.get(OrePrefixes.ingot, Materials.Gold, 1L),
            tStack3,
            tStack2,
            tStack3,
            tStack2);
        if (null != tStack) {
            GT_Values.RA.stdBuilder()
                .itemInputs(tStack)
                .itemOutputs(
                    OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 4L),
                    OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1L))
                .outputChances(10000, 1000)
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);
        }
        GT_Log.out.println("GT_Mod: Registering various Tools to be usable on GregTech Machines");
        GregTech_API.registerScrewdriver(
            GT_ModHandler
                .getRecipeOutput(null, new ItemStack(Items.iron_ingot, 1), null, new ItemStack(Items.stick, 1)));
        GregTech_API.registerScrewdriver(
            GT_ModHandler
                .getRecipeOutput(new ItemStack(Items.iron_ingot, 1), null, null, null, new ItemStack(Items.stick, 1)));

        GT_Log.out.println(
            "GT_Mod: Adding Food Recipes to the Automatic Canning Machine. (also during the following Item Iteration)");
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.rotten_flesh, 2, WILDCARD), ItemList.IC2_Food_Can_Empty.get(1L))
            .itemOutputs(ItemList.IC2_Food_Can_Spoiled.get(1L))
            .duration(10 * SECONDS)
            .eut(1)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.spider_eye, 2, WILDCARD), ItemList.IC2_Food_Can_Empty.get(1L))
            .itemOutputs(ItemList.IC2_Food_Can_Spoiled.get(1L))
            .duration(5 * SECONDS)
            .eut(1)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Food_Poisonous_Potato.get(2L), ItemList.IC2_Food_Can_Empty.get(1L))
            .itemOutputs(ItemList.IC2_Food_Can_Spoiled.get(1L))
            .duration(5 * SECONDS)
            .eut(1)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.cake, 1, WILDCARD), ItemList.IC2_Food_Can_Empty.get(12L))
            .itemOutputs(ItemList.IC2_Food_Can_Filled.get(12L))
            .duration(30 * SECONDS)
            .eut(1)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.mushroom_stew, 1, WILDCARD), ItemList.IC2_Food_Can_Empty.get(6L))
            .itemOutputs(ItemList.IC2_Food_Can_Filled.get(6L), new ItemStack(Items.bowl, 1))
            .duration(15 * SECONDS)
            .eut(1)
            .addTo(cannerRecipes);

        GT_Log.out.println("GT_Mod: Scanning ItemList.");

        try {
            /* (tName.equals("tile.sedimentaryStone")) || **/
            for (Object o : Item.itemRegistry) {
                Object tObject;

                if (!(((tObject = o) instanceof Item) && (!(tObject instanceof ItemGeneric)))) {
                    continue;
                }

                Item tItem = (Item) tObject;
                String tName;
                if ((tName = tItem.getUnlocalizedName()) == null) {
                    continue;
                }

                if ((tItem instanceof IToolCrowbar)) {
                    if ((tItem.isDamageable()) || (GT_ModHandler.isElectricItem(new ItemStack(tItem, 1, 0)))) {
                        if (GregTech_API.registerCrowbar(new ItemStack(tItem, 1, WILDCARD))) {
                            GT_Log.out.println("GT_Mod: Registered valid RC Crowbar: " + tName);
                        }
                    }
                }
                if ((tItem instanceof IToolWrench)) {
                    if ((tItem.isDamageable()) || (GT_ModHandler.isElectricItem(new ItemStack(tItem, 1, 0)))) {
                        if (GregTech_API.registerWrench(new ItemStack(tItem, 1, WILDCARD))) {
                            GT_Log.out.println("GT_Mod: Registered valid BC Wrench: " + tName);
                        }
                    }
                }
                Block tBlock = GT_Utility.getBlockFromStack(new ItemStack(tItem, 1, 0));
                if (tBlock != null) {
                    if (tName.endsWith("beehives")) {
                        tBlock.setHarvestLevel("scoop", 0);
                        gregtech.common.tools.GT_Tool_Scoop.sBeeHiveMaterial = tBlock.getMaterial();
                    }
                    if (OrePrefixes.stone.mDefaultStackSize < tItem.getItemStackLimit(new ItemStack(tItem, 1, 0))) {
                        if ((tBlock.isReplaceableOreGen(GT_Values.DW, 0, 0, 0, Blocks.stone))
                            || (tBlock.isReplaceableOreGen(GT_Values.DW, 0, 0, 0, Blocks.netherrack))
                            || (tBlock.isReplaceableOreGen(GT_Values.DW, 0, 0, 0, Blocks.end_stone))) {
                            tItem.setMaxStackSize(OrePrefixes.stone.mDefaultStackSize);
                        }
                    }
                }
                if (((tItem instanceof ItemFood)) && (tItem != ItemList.IC2_Food_Can_Filled.getItem())
                    && (tItem != ItemList.IC2_Food_Can_Spoiled.getItem())) {
                    int tFoodValue = ((ItemFood) tItem).func_150905_g(new ItemStack(tItem, 1, 0));
                    if (tFoodValue > 0) {
                        RecipeBuilder recipeBuilder = GT_Values.RA.stdBuilder();
                        recipeBuilder
                            .itemInputs(new ItemStack(tItem, 1, WILDCARD), ItemList.IC2_Food_Can_Empty.get(tFoodValue));
                        if (GT_Utility.getContainerItem(new ItemStack(tItem, 1, 0), true) == null) {
                            recipeBuilder.itemOutputs(ItemList.IC2_Food_Can_Filled.get(tFoodValue));
                        } else {
                            recipeBuilder.itemOutputs(
                                ItemList.IC2_Food_Can_Filled.get(tFoodValue),
                                GT_Utility.getContainerItem(new ItemStack(tItem, 1, 0), true));
                        }
                        recipeBuilder.duration(tFoodValue * 5 * SECONDS)
                            .eut(1)
                            .addTo(cannerRecipes);
                    }
                }
                if ((tItem instanceof IFluidContainerItem)) {
                    OreDictUnificator.addToBlacklist(new ItemStack(tItem, 1, WILDCARD));
                }

                switch (tName) {
                    // twilight forest
                    case "item.fieryBlood", "item.fieryTears" -> GT_Values.RA.stdBuilder()
                        .itemInputs(new ItemStack(tItem, 1, 0))
                        .metadata(FUEL_VALUE, 2048)
                        .metadata(FUEL_TYPE, 5)
                        .addTo(RecipeConstants.Fuel);

                    // twilight forest
                    case "tile.TFRoots" -> {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(new ItemStack(tItem, 1, 0))
                            .itemOutputs(new ItemStack(Items.stick, 2), new ItemStack(Items.stick, 1))
                            .outputChances(10000, 3000)
                            .duration(20 * SECONDS)
                            .eut(2)
                            .addTo(maceratorRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(new ItemStack(tItem, 1, 1))
                            .itemOutputs(new ItemStack(Items.stick, 4))
                            .metadata(FUEL_VALUE, 32)
                            .metadata(FUEL_TYPE, 5)
                            .addTo(RecipeConstants.Fuel);
                    }

                    // tinker construct
                    case "item.tconstruct.manual" -> OreDictUnificator
                        .registerOre("bookTinkersManual", new ItemStack(tItem, 1, WILDCARD));

                    // buildcraft
                    case "item.blueprintItem" -> OreDictUnificator
                        .registerOre("paperBlueprint", new ItemStack(tItem, 1, WILDCARD));

                    // Thaumcraft
                    case "item.ItemThaumonomicon" -> OreDictUnificator
                        .registerOre("bookThaumonomicon", new ItemStack(tItem, 1, WILDCARD));

                    // BoP
                    case "tile.bop.redRocks" -> {
                        OreDictUnificator.registerOre(OrePrefixes.stone, Materials.Redrock, new ItemStack(tItem, 1, 0));
                        OreDictUnificator.registerOre(OrePrefixes.stone, Materials.Redrock, new ItemStack(tItem, 1, 1));
                        OreDictUnificator.registerOre(OrePrefixes.stone, Materials.Redrock, new ItemStack(tItem, 1, 2));
                    }

                    // Thaumcraft
                    case "tile.blockCosmeticSolid" -> {
                        OreDictUnificator
                            .registerOre(OrePrefixes.stone, Materials.Obsidian, new ItemStack(tItem, 1, 0));
                        OreDictUnificator
                            .registerOre(OrePrefixes.stone, Materials.Obsidian, new ItemStack(tItem, 1, 1));
                        OreDictUnificator
                            .registerOre(OrePrefixes.block, Materials.Thaumium, new ItemStack(tItem, 1, 4));
                    }

                    // minecraft
                    case "tile.enderchest" -> OreDictUnificator
                        .registerOre(OreDictNames.enderChest, new ItemStack(tItem, 1, WILDCARD));

                    // buildcraft
                    case "tile.autoWorkbenchBlock" -> OreDictUnificator
                        .registerOre(OreDictNames.craftingWorkBench, new ItemStack(tItem, 1, 0));

                    // buildcraft
                    case "tile.pumpBlock" -> OreDictUnificator
                        .registerOre(OreDictNames.craftingPump, new ItemStack(tItem, 1, 0));

                    // buildcraft
                    case "tile.tankBlock" -> OreDictUnificator
                        .registerOre(OreDictNames.craftingTank, new ItemStack(tItem, 1, 0));

                }
            }
        } catch (Throwable e) {
            /**/
        }
    }
}
