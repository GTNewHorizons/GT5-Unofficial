package com.github.technus.tectech.recipe;

import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import pers.gwyog.gtneioreplugin.plugin.block.BlockDimensionDisplay;
import pers.gwyog.gtneioreplugin.plugin.block.ModBlocks;
import pers.gwyog.gtneioreplugin.util.DimensionHelper;
import pers.gwyog.gtneioreplugin.util.GT5OreLayerHelper;
import pers.gwyog.gtneioreplugin.util.GT5OreSmallHelper;

public class EyeOfHarmonyRecipeStorage {

    //    static final long MILLION = LongMath.pow(10, 6);
    //    static final long BILLION = LongMath.pow(10, 9);
    //    static final long TRILLION = LongMath.pow(10, 12);
    //    static final long QUADRILLION = LongMath.pow(10, 15);
    //    static final long QUINTILLION = LongMath.pow(10, 18);
    //    static final long SEXTILLION = LongMath.pow(10, 21);

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
                BlockDimensionDisplay a = (BlockDimensionDisplay) ModBlocks.blocks.get(dimAbbreviation);

                try {
                    put(
                            dimAbbreviation,
                            new EyeOfHarmonyRecipe(
                                    GT5OreLayerHelper.dimToOreWrapper.get(dimAbbreviation),
                                    GT5OreSmallHelper.dimToSmallOreWrapper.get(dimAbbreviation),
                                    1.0,
                                    100,
                                    100,
                                    36_000L,
                                    0,
                                    100 * 10,
                                    0.4));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(dimAbbreviation + " dimension not found in dimToOreWrapper");
                }
            }
        }
    };

    public EyeOfHarmonyRecipe recipeLookUp(ItemStack aStack) {
        String dimAbbreviation = blocksMapInverted.get(Block.getBlockFromItem(aStack.getItem()));
        return recipeHashMap.get(dimAbbreviation);
    }

    //    public EyeOfHarmonyRecipe overworld = new EyeOfHarmonyRecipe(dimToOreWrapper.get("Ow"),
    //        1.0,
    //        100,
    //        100,
    //        36_000L,
    //        0,
    //        100 * 10,
    //        0.4);
}
