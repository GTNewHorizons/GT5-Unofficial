package com.github.technus.tectech.loader;

import com.github.technus.tectech.compatibility.dreamcraft.DreamCraftRecipeLoader;
import com.github.technus.tectech.elementalMatter.definitions.complex.dAtomDefinition;
import cpw.mods.fml.common.Loader;

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
        else new BloodyRecipeLoader().run();//TODO init recipes for NON-GTNH version
    }
}
