package com.github.technus.tectech.loader;

import com.github.technus.tectech.recipe.TT_recipeAdder;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

/**
 * Created by Tec on 06.08.2017.
 */
public class BloodyRecipeLoader implements Runnable {
    @Override
    public void run() {
        TT_recipeAdder.addResearchableAssemblylineRecipe(new ItemStack(Blocks.cobblestone),100,new ItemStack[]{new ItemStack(Blocks.stone)},null,new ItemStack(Blocks.stone_button),30,30);
    }
}
