package gtPlusPlus.xmod.bop.blocks;

import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.loaders.oreprocessing.ProcessingLog.addPyrolyeOvenRecipes;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.xmod.bop.blocks.pine.LeavesPineTree;
import gtPlusPlus.xmod.bop.blocks.pine.LogPineTree;
import gtPlusPlus.xmod.bop.blocks.pine.SaplingPineTree;
import gtPlusPlus.xmod.bop.blocks.rainforest.LeavesRainforestTree;
import gtPlusPlus.xmod.bop.blocks.rainforest.LogRainforestTree;
import gtPlusPlus.xmod.bop.blocks.rainforest.SaplingRainforestTree;

public class BOPBlockRegistrator {

    public static Block log_Rainforest;
    public static Block leaves_Rainforest;
    public static Block sapling_Rainforest;
    public static Block log_Pine;
    public static Block leaves_Pine;
    public static Block sapling_Pine;

    // Runs Each tree Type separately
    public static void run() {
        registerTree_Rainforest();
        registerTree_Pine();
    }

    private static void registerTree_Rainforest() {
        log_Rainforest = new LogRainforestTree();
        leaves_Rainforest = new LeavesRainforestTree();
        sapling_Rainforest = new SaplingRainforestTree();
        ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(log_Rainforest), "logWood", true);
        ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(leaves_Rainforest), "treeLeaves", true);
        ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(sapling_Rainforest), "treeSapling", true);
    }

    private static void registerTree_Pine() {
        log_Pine = new LogPineTree();
        leaves_Pine = new LeavesPineTree();
        sapling_Pine = new SaplingPineTree();
        ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(log_Pine), "logWood", true);
        ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(leaves_Pine), "treeLeaves", true);
        ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(sapling_Pine), "treeSapling", true);
    }

    public static void recipes() {
        // Rainforest Oak
        addLogRecipes(ItemUtils.getSimpleStack(log_Rainforest));
        // Pine
        addLogRecipes(ItemUtils.getSimpleStack(log_Pine));
    }

    public static void addLogRecipes(final ItemStack aStack) {
        RecipeUtils.addShapelessGregtechRecipe(
            new ItemStack[] { aStack },
            ItemUtils
                .getSimpleStack(Item.getItemFromBlock(Blocks.planks), GTMod.gregtechproxy.mNerfedWoodPlank ? 2 : 4));
        RecipeUtils.recipeBuilder(
            CI.craftingToolSaw,
            null,
            null,
            aStack,
            null,
            null,
            null,
            null,
            null,
            ItemUtils.getSimpleStack(Item.getItemFromBlock(Blocks.planks), 4));
        GTModHandler.addCraftingRecipe(
            GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Wood, 2L),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "sLf", 'L', GTUtility.copyAmount(1L, aStack) });

        final short aMeta = (short) aStack.getItemDamage();
        if (GTUtility.areStacksEqual(
            GTModHandler.getSmeltingOutput(GTUtility.copyAmount(1L, aStack), false, null),
            new ItemStack(Items.coal, 1, 1))) {
            addPyrolyeOvenRecipes(aStack);
            GTModHandler.removeFurnaceSmelting(GTUtility.copyAmount(1L, aStack));
        }
        if (aMeta == 32767) {
            for (int i = 0; i < 32767; ++i) {
                if (GTUtility.areStacksEqual(
                    GTModHandler.getSmeltingOutput(new ItemStack(aStack.getItem(), 1, i), false, null),
                    new ItemStack(Items.coal, 1, 1))) {
                    addPyrolyeOvenRecipes(aStack);
                    GTModHandler.removeFurnaceSmelting(new ItemStack(aStack.getItem(), 1, i));
                }
                final ItemStack tStack = GTModHandler.getRecipeOutput(new ItemStack(aStack.getItem(), 1, i));
                if (tStack == null) {
                    if (i >= 16) {
                        break;
                    }
                } else {
                    final ItemStack tPlanks = GTUtility.copy(tStack);
                    tPlanks.stackSize = tPlanks.stackSize * 3 / 2;
                    GTValues.RA.stdBuilder()
                        .itemInputs(new ItemStack(aStack.getItem(), 1, i))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                GTMod.gregtechproxy.mNerfedWoodPlank ? ((long) tStack.stackSize)
                                    : (((long) tStack.stackSize) * 5L / 4),
                                tStack),
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
                        .duration(10 * SECONDS)
                        .eut(8)
                        .addTo(cutterRecipes);
                    GTModHandler.removeRecipe(new ItemStack(aStack.getItem(), 1, i));
                    GTModHandler.addCraftingRecipe(
                        GTUtility.copyAmount(
                            GTMod.gregtechproxy.mNerfedWoodPlank ? ((long) tStack.stackSize)
                                : (((long) tStack.stackSize) * 5L / 4),
                            tStack),
                        new Object[] { "s", "L", 'L', new ItemStack(aStack.getItem(), 1, i) });
                    GTModHandler.addShapelessCraftingRecipe(
                        GTUtility.copyAmount(tStack.stackSize / (GTMod.gregtechproxy.mNerfedWoodPlank ? 2 : 1), tStack),
                        new Object[] { new ItemStack(aStack.getItem(), 1, i) });
                }
            }
        } else {
            final ItemStack tStack2 = GTModHandler.getRecipeOutput(GTUtility.copyAmount(1L, aStack));
            if (tStack2 != null) {
                final ItemStack tPlanks2 = GTUtility.copy(tStack2);
                tPlanks2.stackSize = tPlanks2.stackSize * 3 / 2;
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1L, aStack))
                    .itemOutputs(
                        GTUtility.copyAmount(
                            GTMod.gregtechproxy.mNerfedWoodPlank ? ((long) tStack2.stackSize)
                                : (((long) tStack2.stackSize) * 5L / 4),
                            tStack2),
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
                    .duration(10 * SECONDS)
                    .eut(8)
                    .addTo(cutterRecipes);
                GTModHandler.removeRecipe(GTUtility.copyAmount(1L, aStack));
                GTModHandler.addCraftingRecipe(
                    GTUtility.copyAmount(
                        GTMod.gregtechproxy.mNerfedWoodPlank ? ((long) tStack2.stackSize)
                            : (((long) tStack2.stackSize) * 5L / 4),
                        tStack2),
                    new Object[] { "s", "L", 'L', GTUtility.copyAmount(1L, aStack) });
                GTModHandler.addShapelessCraftingRecipe(
                    GTUtility.copyAmount(tStack2.stackSize / (GTMod.gregtechproxy.mNerfedWoodPlank ? 2 : 1), tStack2),
                    new Object[] { GTUtility.copyAmount(1L, aStack) });
            }
        }
        if (GTUtility.areStacksEqual(
            GTModHandler.getSmeltingOutput(GTUtility.copyAmount(1L, aStack), false, null),
            new ItemStack(Items.coal, 1, 1))) {
            addPyrolyeOvenRecipes(aStack);
            GTModHandler.removeFurnaceSmelting(GTUtility.copyAmount(1L, aStack));
        }
    }
}
