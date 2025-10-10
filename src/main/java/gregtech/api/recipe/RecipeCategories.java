package gregtech.api.recipe;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.recipe.RecipeCategory.createIcon;

import gregtech.api.enums.Mods;
import gregtech.common.config.Client;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;

@SuppressWarnings("unused")
public final class RecipeCategories {

    public static final RecipeCategory arcFurnaceRecycling = new RecipeCategory(
        "gt.recipe.category.arc_furnace_recycling",
        RecipeMaps.arcFurnaceRecipes,
        () -> Client.nei.recipeCategories.arcFurnaceRecycling,
        builder -> builder
            .setDisplayImage(createIcon(GregTech.getResourcePath("textures/gui/picture/arc_furnace_recycling.png"))));

    public static final RecipeCategory maceratorRecycling = new RecipeCategory(
        "gt.recipe.category.macerator_recycling",
        RecipeMaps.maceratorRecipes,
        () -> Client.nei.recipeCategories.maceratorRecycling,
        builder -> builder
            .setDisplayImage(createIcon(GregTech.getResourcePath("textures/gui/picture/macerator_recycling.png"))));

    public static final RecipeCategory fluidExtractorRecycling = new RecipeCategory(
        "gt.recipe.category.fluid_extractor_recycling",
        RecipeMaps.fluidExtractionRecipes,
        () -> Client.nei.recipeCategories.fluidExtractorRecycling,
        builder -> builder.setDisplayImage(
            createIcon(GregTech.getResourcePath("textures/gui/picture/fluid_extractor_recycling.png"))));

    public static final RecipeCategory alloySmelterRecycling = new RecipeCategory(
        "gt.recipe.category.alloy_smelter_recycling",
        RecipeMaps.alloySmelterRecipes,
        () -> Client.nei.recipeCategories.alloySmelterRecycling,
        builder -> builder
            .setDisplayImage(createIcon(GregTech.getResourcePath("textures/gui/picture/alloy_smelter_recycling.png"))));

    public static final RecipeCategory alloySmelterMolding = new RecipeCategory(
        "gt.recipe.category.alloy_smelter_molding",
        RecipeMaps.alloySmelterRecipes,
        () -> Client.nei.recipeCategories.alloySmelterMolding,
        builder -> builder
            .setDisplayImage(createIcon(GregTech.getResourcePath("textures/gui/picture/alloy_smelter_molding.png"))));

    public static final RecipeCategory forgeHammerRecycling = new RecipeCategory(
        "gt.recipe.category.forge_hammer_recycling",
        RecipeMaps.hammerRecipes,
        () -> Client.nei.recipeCategories.forgeHammerRecycling,
        builder -> builder
            .setDisplayImage(createIcon(GregTech.getResourcePath("textures/gui/picture/forge_hammer_recycling.png"))));

    public static final RecipeCategory ticPartExtruding = new RecipeCategory(
        "gt.recipe.category.tic_part_extruding",
        RecipeMaps.extruderRecipes,
        () -> Client.nei.recipeCategories.ticPartExtruding,
        builder -> builder
            .setDisplayImage(createIcon(GregTech.getResourcePath("textures/gui/picture/tic_part_extruding.png"))));

    public static final RecipeCategory ticBoltMolding = new RecipeCategory(
        "gt.recipe.category.tic_bolt_molding",
        RecipeMaps.fluidSolidifierRecipes,
        () -> Client.nei.recipeCategories.ticBoltMolding,
        builder -> builder
            .setDisplayImage(createIcon(GregTech.getResourcePath("textures/gui/picture/tic_bolt_molding.png"))));

    public static final RecipeCategory absNonAlloyRecipes = new RecipeCategory(
        "gtpp.recipe.category.abs_non_alloy_recipes",
        GTPPRecipeMaps.alloyBlastSmelterRecipes,
        () -> Client.nei.recipeCategories.absNonAlloyRecipes,
        builder -> builder.setDisplayImage(
            createIcon(Mods.GTPlusPlus.getResourcePath("textures/gui/picture/abs_non_alloy_recipes.png"))));
}
