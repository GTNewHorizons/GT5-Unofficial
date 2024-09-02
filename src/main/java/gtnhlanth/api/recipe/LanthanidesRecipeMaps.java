package gtnhlanth.api.recipe;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.nei.formatter.HeatingCoilSpecialValueFormatter;
import gregtech.nei.formatter.SimpleSpecialValueFormatter;

public class LanthanidesRecipeMaps {

    public static final RecipeMap<RecipeMapBackend> digesterRecipes = RecipeMapBuilder.of("gtnhlanth.recipe.digester")
        .maxIO(1, 1, 1, 1)
        .minInputs(1, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiSpecialInfoFormatter(HeatingCoilSpecialValueFormatter.INSTANCE)
        .build();
    public static final RecipeMap<RecipeMapBackend> dissolutionTankRecipes = RecipeMapBuilder
        .of("gtnhlanth.recipe.disstank")
        .maxIO(2, 3, 2, 1)
        .minInputs(1, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiSpecialInfoFormatter(new SimpleSpecialValueFormatter("value.disstank"))
        .build();
}
