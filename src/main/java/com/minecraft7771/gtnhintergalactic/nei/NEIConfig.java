package com.minecraft7771.gtnhintergalactic.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

import com.minecraft7771.gtnhintergalactic.Tags;
import com.minecraft7771.gtnhintergalactic.gui.IG_UITextures;
import gregtech.api.util.GT_Recipe;

public class NEIConfig implements IConfigureNEI {

    public static boolean executed = false;

    @Override
    public void loadConfig() {
        API.registerRecipeHandler(new SpacePumpModuleRecipeHandler());
        API.registerUsageHandler(new SpacePumpModuleRecipeHandler());
        // Inject elevator logo into GT5U recipe map
        GT_Recipe.GT_Recipe_Map.sFakeSpaceProjectRecipes.setLogo(IG_UITextures.PICTURE_ELEVATOR_LOGO);
        executed = true;
    }

    @Override
    public String getName() {
        return "GTNH-Intergalactic NEI Plugin";
    }

    @Override
    public String getVersion() {
        return Tags.VERSION;
    }
}
