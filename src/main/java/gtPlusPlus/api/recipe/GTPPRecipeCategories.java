package gtPlusPlus.api.recipe;

import static gregtech.api.recipe.RecipeCategory.createIcon;

import gregtech.api.enums.Mods;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.RecipeCategoryHolder;

public class GTPPRecipeCategories {

    @RecipeCategoryHolder
    public static final RecipeCategory absNonAlloyRecipes = new RecipeCategory(
            "gtpp.recipe.category.abs_non_alloy_recipes",
            GTPPRecipeMaps.alloyBlastSmelterRecipes,
            builder -> builder.setDisplayImage(
                    createIcon(
                            Mods.GTPlusPlus
                                    .getResourcePath("textures", "gui", "picture", "abs_non_alloy_recipes.png"))));
}
