package gtnhintergalactic.recipe;

import java.util.Comparator;

import gregtech.api.enums.ItemList;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.recipe.metadata.SimpleRecipeMetadataKey;
import gregtech.api.util.GTRecipe;
import gtnhintergalactic.gui.IG_UITextures;
import gtnhintergalactic.recipe.maps.SpaceAssemblerFrontend;
import gtnhintergalactic.recipe.maps.SpaceMiningFrontend;
import gtnhintergalactic.recipe.maps.SpaceResearchFrontend;

public class IGRecipeMaps {

    public static final RecipeMetadataKey<Integer> MODULE_TIER = SimpleRecipeMetadataKey
        .create(Integer.class, "space_elevator_module_tier");
    public static final RecipeMetadataKey<String> SPACE_PROJECT = SimpleRecipeMetadataKey
        .create(String.class, "space_project");
    public static final RecipeMetadataKey<String> SPACE_LOCATION = SimpleRecipeMetadataKey
        .create(String.class, "space_project_location");
    public static final RecipeMetadataKey<SpaceMiningData> SPACE_MINING_DATA = SimpleRecipeMetadataKey
        .create(SpaceMiningData.class, "space_mining_data");

    /** Recipe map for recipes in the Space Research Module */
    public static final RecipeMap<RecipeMapBackend> spaceResearchRecipes = RecipeMapBuilder
        .of("gt.recipe.spaceResearch")
        .maxIO(7, 1, 3, 0)
        .minInputs(1, 0)
        .logo(IG_UITextures.PICTURE_ELEVATOR_LOGO)
        .logoSize(18, 18)
        .neiTransferRect(110, 35, 18, 18)
        .progressBarPos(110, 35)
        .frontend(SpaceResearchFrontend::new)
        .build();
    /** Recipe map for recipes in the Space Assembler Module */
    public static final RecipeMap<RecipeMapBackend> spaceAssemblerRecipes = RecipeMapBuilder
        .of("gt.recipe.spaceAssembler")
        .maxIO(16, 1, 4, 0)
        .minInputs(1, 0)
        .logo(IG_UITextures.PICTURE_ELEVATOR_LOGO)
        .logoSize(18, 18)
        .neiTransferRect(124, 8, 16, 16)
        .neiRecipeComparator(
            Comparator.<GTRecipe, Integer>comparing(recipe -> recipe.getMetadataOrDefault(MODULE_TIER, 1))
                .thenComparing(GTRecipe::compareTo))
        .neiTransferRect(88, 8, 18, 72)
        .neiTransferRect(124, 8, 18, 72)
        .useCustomFilterForNEI()
        .frontend(SpaceAssemblerFrontend::new)
        .build();
    /** Recipe map for recipes in the Space Mining Module */
    public static final RecipeMap<RecipeMapBackend> spaceMiningRecipes = RecipeMapBuilder.of("gt.recipe.spaceMining")
        .maxIO(5, 16, 2, 0)
        .minInputs(1, 0)
        .neiRecipeComparator(
            Comparator.<GTRecipe, Integer>comparing(recipe -> recipe.getMetadataOrDefault(MODULE_TIER, 1))
                .thenComparing(GTRecipe::compareTo))
        .logo(IG_UITextures.PICTURE_ELEVATOR_LOGO)
        .logoPos(151, 58)
        .logoSize(18, 18)
        .neiRecipeBackgroundSize(170, 78)
        .neiTransferRect(46, 6, 23, 63)
        .dontUseProgressBar()
        .addSpecialTexture(46, 6, 23, 63, IG_UITextures.PROGRESSBAR_SPACE_MINING_MODULE_ARROW)
        .useCustomFilterForNEI()
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(ItemList.SpaceElevatorModuleMinerT1.get(1))
                .setHeight(162)
                .setMaxRecipesPerPage(2))
        .frontend(SpaceMiningFrontend::new)
        .build();
}
