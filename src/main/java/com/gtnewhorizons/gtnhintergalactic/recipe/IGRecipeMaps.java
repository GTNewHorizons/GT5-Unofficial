package com.gtnewhorizons.gtnhintergalactic.recipe;

import java.util.Comparator;

import com.gtnewhorizons.gtnhintergalactic.gui.IG_UITextures;
import com.gtnewhorizons.gtnhintergalactic.recipe.maps.SpaceAssemblerFrontend;
import com.gtnewhorizons.gtnhintergalactic.recipe.maps.SpaceMiningFrontend;
import com.gtnewhorizons.gtnhintergalactic.recipe.maps.SpaceResearchFrontend;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.util.GT_Recipe;

public class IGRecipeMaps {

    /** Recipe map for recipes in the Space Research Module */
    public static final RecipeMap<RecipeMapBackend> spaceResearchRecipes = RecipeMapBuilder
            .of("gt.recipe.spaceResearch").maxIO(7, 1, 3, 0).minInputs(1, 0).logo(IG_UITextures.PICTURE_ELEVATOR_LOGO)
            .logoSize(18, 18).neiTransferRect(110, 35, 18, 18).progressBarPos(110, 35)
            .frontend(SpaceResearchFrontend::new).build();
    /** Recipe map for recipes in the Space Assembler Module */
    public static final RecipeMap<RecipeMapBackend> spaceAssemblerRecipes = RecipeMapBuilder
            .of("gt.recipe.spaceAssembler").maxIO(16, 1, 4, 0).minInputs(1, 0).logo(IG_UITextures.PICTURE_ELEVATOR_LOGO)
            .logoSize(18, 18).neiTransferRect(124, 8, 16, 16)
            .neiRecipeComparator(
                    Comparator.<GT_Recipe, Integer>comparing(recipe -> recipe.mSpecialValue)
                            .thenComparing(GT_Recipe::compareTo))
            .neiTransferRect(88, 8, 18, 72).neiTransferRect(124, 8, 18, 72).useCustomFilterForNEI()
            .frontend(SpaceAssemblerFrontend::new).build();
    /** Recipe map for recipes in the Space Mining Module */
    public static final RecipeMap<RecipeMapBackend> spaceMiningRecipes = RecipeMapBuilder.of("gt.recipe.spaceMining")
            .maxIO(5, 16, 2, 0).minInputs(1, 0)
            .neiRecipeComparator(
                    Comparator.<GT_Recipe, Integer>comparing(recipe -> recipe.mSpecialValue)
                            .thenComparing(GT_Recipe::compareTo))
            .logo(IG_UITextures.PICTURE_ELEVATOR_LOGO).logoPos(151, 58).logoSize(18, 18)
            .neiRecipeBackgroundSize(170, 78).neiTransferRect(46, 6, 23, 63).dontUseProgressBar()
            .addSpecialTexture(46, 6, 23, 63, IG_UITextures.PROGRESSBAR_SPACE_MINING_MODULE_ARROW)
            .useCustomFilterForNEI().frontend(SpaceMiningFrontend::new).build();
}
