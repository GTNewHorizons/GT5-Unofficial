package com.gtnewhorizons.gtnhintergalactic.nei;

import com.gtnewhorizons.gtnhintergalactic.Tags;
import com.gtnewhorizons.gtnhintergalactic.gui.IG_UITextures;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import gregtech.api.util.GT_Recipe;

public class NEIConfig implements IConfigureNEI {

    public static boolean executed = false;

    @Override
    public void loadConfig() {
        API.registerRecipeHandler(new SpacePumpModuleRecipeHandler());
        API.registerUsageHandler(new SpacePumpModuleRecipeHandler());
        API.registerRecipeHandler(new GasSiphonRecipeHandler());
        API.registerUsageHandler(new GasSiphonRecipeHandler());
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
