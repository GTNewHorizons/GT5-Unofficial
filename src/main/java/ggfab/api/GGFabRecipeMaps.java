package ggfab.api;

import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;

public class GGFabRecipeMaps {

    public static final RecipeMap<RecipeMapBackend> toolCastRecipes = RecipeMapBuilder.of("ggfab.recipe.toolcast")
        .maxIO(1, 4, 1, 0)
        .minInputs(1, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
        .build();
}
