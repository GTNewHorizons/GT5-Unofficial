package gregtech.loaders.load;

import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import buildcraft.api.tools.IToolWrench;
import buildcraft.factory.BlockTank;
import cpw.mods.fml.common.ModAPIManager;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.GTGenericItem;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.common.tools.ToolScoop;
import mods.railcraft.api.core.items.IToolCrowbar;

public class GTItemIterator implements Runnable {

    @Override
    public void run() {
        GTLog.out.println("GTMod: Scanning for certain kinds of compatible Machineblocks.");
        ItemStack tStack2 = GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Bronze, 1L);
        ItemStack tStack = GTModHandler
            .getRecipeOutput(tStack2, tStack2, tStack2, tStack2, null, tStack2, tStack2, tStack2, tStack2);

        if (null != tStack) {
            GTValues.RA.stdBuilder()
                .itemInputs(tStack)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Bronze, 8L))
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);

            GTModHandler.addSmeltingRecipe(tStack, GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Bronze, 8L));
        }
        tStack2 = GTOreDictUnificator.get(OrePrefixes.plate, Materials.Bronze, 1L);
        tStack = GTModHandler
            .getRecipeOutput(tStack2, tStack2, tStack2, tStack2, null, tStack2, tStack2, tStack2, tStack2);

        if (null != tStack) {
            GTOreDictUnificator.registerOre(OreDictNames.craftingRawMachineTier00, tStack);

            GTValues.RA.stdBuilder()
                .itemInputs(tStack)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Bronze, 8L))
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);
            GTModHandler.addSmeltingRecipe(tStack, GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Bronze, 8L));
        }

        ItemStack tStack3 = new ItemStack(Blocks.glass, 1, 0);
        tStack2 = GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L);
        tStack = GTModHandler.getRecipeOutput(
            tStack2,
            tStack3,
            tStack2,
            tStack3,
            GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Gold, 1L),
            tStack3,
            tStack2,
            tStack3,
            tStack2);

        if (null != (tStack)) {
            GTValues.RA.stdBuilder()
                .itemInputs(tStack)
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 4L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1L))
                .outputChances(10000, 1000)
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);
        }

        tStack2 = GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L);
        tStack3 = new ItemStack(Blocks.glass, 1, 0);
        tStack = GTModHandler.getRecipeOutput(
            tStack2,
            tStack3,
            tStack2,
            tStack3,
            GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Gold, 1L),
            tStack3,
            tStack2,
            tStack3,
            tStack2);
        if (null != tStack) {
            GTValues.RA.stdBuilder()
                .itemInputs(tStack)
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 4L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1L))
                .outputChances(10000, 1000)
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);
        }
        GTLog.out.println("GTMod: Registering various Tools to be usable on GregTech Machines");
        GregTechAPI.registerScrewdriver(
            GTModHandler
                .getRecipeOutput(null, new ItemStack(Items.iron_ingot, 1), null, new ItemStack(Items.stick, 1)));
        GregTechAPI.registerScrewdriver(
            GTModHandler
                .getRecipeOutput(new ItemStack(Items.iron_ingot, 1), null, null, null, new ItemStack(Items.stick, 1)));

        GTLog.out.println(
            "GTMod: Adding Food Recipes to the Automatic Canning Machine. (also during the following Item Iteration)");
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.cake, 1, WILDCARD), ItemList.IC2_Food_Can_Empty.get(12L))
            .itemOutputs(ItemList.IC2_Food_Can_Filled.get(12L))
            .duration(30 * SECONDS)
            .eut(1)
            .addTo(cannerRecipes);

        GTLog.out.println("GTMod: Scanning ItemList.");

        final boolean rcItems = ModAPIManager.INSTANCE.hasAPI("RailcraftAPI|items");
        final boolean bcTools = ModAPIManager.INSTANCE.hasAPI("BuildCraftAPI|tools");

        try {
            /* (tName.equals("tile.sedimentaryStone")) || **/
            for (Object o : Item.itemRegistry) {
                Object tObject;

                if (!(((tObject = o) instanceof Item) && (!(tObject instanceof GTGenericItem)))) {
                    continue;
                }

                Item tItem = (Item) tObject;
                String tName;
                if ((tName = tItem.getUnlocalizedName()) == null) {
                    continue;
                }

                if (rcItems && tItem instanceof IToolCrowbar) {
                    if ((tItem.isDamageable()) || (GTModHandler.isElectricItem(new ItemStack(tItem, 1, 0)))) {
                        if (GregTechAPI.registerCrowbar(new ItemStack(tItem, 1, WILDCARD))) {
                            GTLog.out.println("GTMod: Registered valid RC Crowbar: " + tName);
                        }
                    }
                }
                if (bcTools && tItem instanceof IToolWrench) {
                    if ((tItem.isDamageable()) || (GTModHandler.isElectricItem(new ItemStack(tItem, 1, 0)))) {
                        if (GregTechAPI.registerWrench(new ItemStack(tItem, 1, WILDCARD))) {
                            GTLog.out.println("GTMod: Registered valid BC Wrench: " + tName);
                        }
                    }
                }
                Block tBlock = GTUtility.getBlockFromStack(new ItemStack(tItem, 1, 0));
                if (tBlock != null) {
                    if (tName.endsWith("beehives")) {
                        tBlock.setHarvestLevel("scoop", 0);
                        ToolScoop.sBeeHiveMaterial = tBlock.getMaterial();
                    }
                    if (OrePrefixes.stone.getDefaultStackSize() < tItem.getItemStackLimit(new ItemStack(tItem, 1, 0))) {
                        if ((tBlock.isReplaceableOreGen(GTValues.DW, 0, 0, 0, Blocks.stone))
                            || (tBlock.isReplaceableOreGen(GTValues.DW, 0, 0, 0, Blocks.netherrack))
                            || (tBlock.isReplaceableOreGen(GTValues.DW, 0, 0, 0, Blocks.end_stone))) {
                            tItem.setMaxStackSize(OrePrefixes.stone.getDefaultStackSize());
                        }
                    }
                    if (tName.equals("tile.tankBlock") && tBlock instanceof BlockTank) {
                        GTOreDictUnificator.registerOre(OreDictNames.craftingTank, new ItemStack(tItem, 1, 0));
                    }
                }
                if (((tItem instanceof ItemFood)) && (tItem != ItemList.IC2_Food_Can_Filled.getItem())
                    && (tItem != ItemList.IC2_Food_Can_Spoiled.getItem())) {
                    int tFoodValue = ((ItemFood) tItem).func_150905_g(new ItemStack(tItem, 1, 0));
                    if (tFoodValue > 0) {
                        GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
                        recipeBuilder
                            .itemInputs(new ItemStack(tItem, 1, WILDCARD), ItemList.IC2_Food_Can_Empty.get(tFoodValue));
                        if (GTUtility.getContainerItem(new ItemStack(tItem, 1, 0), true) == null) {
                            recipeBuilder.itemOutputs(ItemList.IC2_Food_Can_Filled.get(tFoodValue));
                        } else {
                            recipeBuilder.itemOutputs(
                                ItemList.IC2_Food_Can_Filled.get(tFoodValue),
                                GTUtility.getContainerItem(new ItemStack(tItem, 1, 0), true));
                        }
                        recipeBuilder.duration(tFoodValue * 5 * SECONDS)
                            .eut(1)
                            .addTo(cannerRecipes);
                    }
                }
                if ((tItem instanceof IFluidContainerItem)) {
                    GTOreDictUnificator.addToBlacklist(new ItemStack(tItem, 1, WILDCARD));
                }

                switch (tName) {
                    // twilight forest
                    case "item.fieryBlood", "item.fieryTears" -> GTValues.RA.stdBuilder()
                        .itemInputs(new ItemStack(tItem, 1, 0))
                        .metadata(FUEL_VALUE, 2048)
                        .metadata(FUEL_TYPE, 5)
                        .addTo(GTRecipeConstants.Fuel);

                    // twilight forest
                    case "tile.TFRoots" -> {
                        GTValues.RA.stdBuilder()
                            .itemInputs(new ItemStack(tItem, 1, 0))
                            .itemOutputs(new ItemStack(Items.stick, 2), new ItemStack(Items.stick, 1))
                            .outputChances(10000, 3000)
                            .duration(20 * SECONDS)
                            .eut(2)
                            .addTo(maceratorRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(new ItemStack(tItem, 1, 1))
                            .itemOutputs(new ItemStack(Items.stick, 4))
                            .metadata(FUEL_VALUE, 32)
                            .metadata(FUEL_TYPE, 5)
                            .addTo(GTRecipeConstants.Fuel);
                    }

                    // tinker construct
                    case "item.tconstruct.manual" -> GTOreDictUnificator
                        .registerOre("bookTinkersManual", new ItemStack(tItem, 1, WILDCARD));

                    // buildcraft
                    case "item.blueprintItem" -> GTOreDictUnificator
                        .registerOre("paperBlueprint", new ItemStack(tItem, 1, WILDCARD));

                    // Thaumcraft
                    case "item.ItemThaumonomicon" -> GTOreDictUnificator
                        .registerOre("bookThaumonomicon", new ItemStack(tItem, 1, WILDCARD));

                    // BoP
                    case "tile.bop.redRocks" -> {
                        GTOreDictUnificator
                            .registerOre(OrePrefixes.stone, Materials.Redrock, new ItemStack(tItem, 1, 0));
                        GTOreDictUnificator
                            .registerOre(OrePrefixes.stone, Materials.Redrock, new ItemStack(tItem, 1, 1));
                        GTOreDictUnificator
                            .registerOre(OrePrefixes.stone, Materials.Redrock, new ItemStack(tItem, 1, 2));
                    }

                    // Thaumcraft
                    case "tile.blockCosmeticSolid" -> {
                        GTOreDictUnificator
                            .registerOre(OrePrefixes.stone, Materials.Obsidian, new ItemStack(tItem, 1, 0));
                        GTOreDictUnificator
                            .registerOre(OrePrefixes.stone, Materials.Obsidian, new ItemStack(tItem, 1, 1));
                        GTOreDictUnificator
                            .registerOre(OrePrefixes.block, Materials.Thaumium, new ItemStack(tItem, 1, 4));
                    }

                    // minecraft
                    case "tile.enderchest" -> GTOreDictUnificator
                        .registerOre(OreDictNames.enderChest, new ItemStack(tItem, 1, WILDCARD));

                    // buildcraft
                    case "tile.autoWorkbenchBlock" -> GTOreDictUnificator
                        .registerOre(OreDictNames.craftingWorkBench, new ItemStack(tItem, 1, 0));

                    // buildcraft
                    case "tile.pumpBlock" -> GTOreDictUnificator
                        .registerOre(OreDictNames.craftingPump, new ItemStack(tItem, 1, 0));

                }
            }
        } catch (Exception e) {
            /**/
        }
    }
}
