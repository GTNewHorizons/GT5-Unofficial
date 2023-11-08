package gregtech.api.recipe;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.recipe.RecipeCategory.createIcon;

public final class RecipeCategories {

    public static final RecipeCategory arcFurnaceRecycling = new RecipeCategory(
        "gt.recipe.category.arc_furnace_recycling",
        RecipeMaps.arcFurnaceRecipes,
        builder -> builder.setDisplayImage(
            createIcon(GregTech.getResourcePath("textures", "gui", "picture", "arc_furnace_recycling.png"))));

    public static final RecipeCategory plasmaArcFurnaceRecycling = new RecipeCategory(
        "gt.recipe.category.plasma_arc_furnace_recycling",
        RecipeMaps.plasmaArcFurnaceRecipes,
        builder -> builder.setDisplayImage(
            createIcon(GregTech.getResourcePath("textures", "gui", "picture", "plasma_arc_furnace_recycling.png"))));

    public static final RecipeCategory maceratorRecycling = new RecipeCategory(
        "gt.recipe.category.macerator_recycling",
        RecipeMaps.maceratorRecipes,
        builder -> builder.setDisplayImage(
            createIcon(GregTech.getResourcePath("textures", "gui", "picture", "macerator_recycling.png"))));

    public static final RecipeCategory fluidExtractorRecycling = new RecipeCategory(
        "gt.recipe.category.fluid_extractor_recycling",
        RecipeMaps.fluidExtractionRecipes,
        builder -> builder.setDisplayImage(
            createIcon(GregTech.getResourcePath("textures", "gui", "picture", "fluid_extractor_recycling.png"))));

    public static final RecipeCategory alloySmelterRecycling = new RecipeCategory(
        "gt.recipe.category.alloy_smelter_recycling",
        RecipeMaps.alloySmelterRecipes,
        builder -> builder.setDisplayImage(
            createIcon(GregTech.getResourcePath("textures", "gui", "picture", "alloy_smelter_recycling.png"))));

    public static final RecipeCategory alloySmelterMolding = new RecipeCategory(
        "gt.recipe.category.alloy_smelter_molding",
        RecipeMaps.alloySmelterRecipes,
        builder -> builder.setDisplayImage(
            createIcon(GregTech.getResourcePath("textures", "gui", "picture", "alloy_smelter_molding.png"))));

    public static final RecipeCategory forgeHammerRecycling = new RecipeCategory(
        "gt.recipe.category.forge_hammer_recycling",
        RecipeMaps.hammerRecipes,
        builder -> builder.setDisplayImage(
            createIcon(GregTech.getResourcePath("textures", "gui", "picture", "forge_hammer_recycling.png"))));
}
