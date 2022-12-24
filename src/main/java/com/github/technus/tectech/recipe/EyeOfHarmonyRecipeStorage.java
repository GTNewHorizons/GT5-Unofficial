package com.github.technus.tectech.recipe;

import static com.github.technus.tectech.recipe.TT_recipe.GT_Recipe_MapTT.sEyeofHarmonyRecipes;
import static java.lang.Math.pow;

import com.github.technus.tectech.util.ItemStackLong;
import com.google.common.math.LongMath;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import pers.gwyog.gtneioreplugin.plugin.block.BlockDimensionDisplay;
import pers.gwyog.gtneioreplugin.plugin.block.ModBlocks;
import pers.gwyog.gtneioreplugin.util.DimensionHelper;
import pers.gwyog.gtneioreplugin.util.GT5OreLayerHelper;
import pers.gwyog.gtneioreplugin.util.GT5OreSmallHelper;

public class EyeOfHarmonyRecipeStorage {

    public static final long BILLION = LongMath.pow(10, 9);

    // Map is unique so this is fine.
    HashMap<Block, String> blocksMapInverted = new HashMap<Block, String>() {
        {
            ModBlocks.blocks.forEach((dimString, dimBlock) -> {
                put(dimBlock, dimString);
            });
        }
    };

    private final HashMap<String, EyeOfHarmonyRecipe> recipeHashMap = new HashMap<String, EyeOfHarmonyRecipe>() {
        {
            for (String dimAbbreviation : DimensionHelper.DimNameDisplayed) {
                BlockDimensionDisplay blockDimensionDisplay =
                        (BlockDimensionDisplay) ModBlocks.blocks.get(dimAbbreviation);

                try {
                    put(
                            dimAbbreviation,
                            new EyeOfHarmonyRecipe(
                                    GT5OreLayerHelper.dimToOreWrapper.get(dimAbbreviation),
                                    GT5OreSmallHelper.dimToSmallOreWrapper.get(dimAbbreviation),
                                    blockDimensionDisplay,
                                    0.6 + blockDimensionDisplay.getDimensionRocketTier() / 10.0,
                                    //                                    100,
                                    //                                    100, // todo: DEBUG ONLY
                                    BILLION * (blockDimensionDisplay.getDimensionRocketTier() + 1),
                                    BILLION * (blockDimensionDisplay.getDimensionRocketTier() + 1),
                                    (long) (18_000L * pow(1.4, blockDimensionDisplay.getDimensionRocketTier())),
                                    //                                    2000L, // todo: debug only
                                    blockDimensionDisplay.getDimensionRocketTier(),
                                    1.0 - blockDimensionDisplay.getDimensionRocketTier() / 10.0));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(
                            dimAbbreviation + " dimension not found in dimToOreWrapper. Report error to GTNH team.");
                }
            }
        }
    };

    public EyeOfHarmonyRecipe recipeLookUp(ItemStack aStack) {
        String dimAbbreviation = blocksMapInverted.get(Block.getBlockFromItem(aStack.getItem()));
        return recipeHashMap.get(dimAbbreviation);
    }

    public EyeOfHarmonyRecipeStorage() {

        for (EyeOfHarmonyRecipe recipe : recipeHashMap.values()) {

            // todo sort items by fake long stack size.
            ArrayList<ItemStack> outputItems = new ArrayList<>();
            for (ItemStackLong itemStackLong : recipe.getOutputItems()) {
                outputItems.add(itemStackLong.itemStack);
            }

            ItemStack planetItem = recipe.getRecipeTriggerItem().copy();
            planetItem.stackSize = 0;

            sEyeofHarmonyRecipes.addRecipe(
                    false,
                    new ItemStack[] {planetItem},
                    outputItems.toArray(new ItemStack[0]),
                    recipe,
                    null,
                    null,
                    recipe.getOutputFluids(),
                    (int) recipe.getRecipeTimeInTicks(),
                    0,
                    0);
        }
    }
}
