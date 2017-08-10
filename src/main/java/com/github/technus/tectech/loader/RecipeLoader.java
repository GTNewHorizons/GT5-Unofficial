package com.github.technus.tectech.loader;

import com.github.technus.tectech.elementalMatter.definitions.dAtomDefinition;
import com.github.technus.tectech.recipe.TT_recipeAdder;
import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class RecipeLoader implements Runnable {
    public void run() {
        dAtomDefinition.setTransformation();

        // ===================================================================================================
        // Recipes init - common goes here rest goes into methods below
        // ===================================================================================================



        if (Loader.isModLoaded("dreamcraft")) new DreamCraftRecipeLoader().run();//TODO init recipes for GTNH version
        else runBloodRun();//TODO init recipes for NON-GTNH version
    }

    private void runBloodRun(){
        //Don't init things after UV!!! They don't conform to the standards.
        TT_recipeAdder.addResearchableAssemblylineRecipe(new ItemStack(Blocks.stone),100,new ItemStack[]{new ItemStack(Blocks.cobblestone)},null,new ItemStack(Blocks.stone),30,30);
    }
}
