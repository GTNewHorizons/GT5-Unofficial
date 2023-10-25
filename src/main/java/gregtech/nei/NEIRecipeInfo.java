package gregtech.nei;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.common.power.Power;

/**
 * Holds info used for drawing descriptions on NEI.
 */
public class NEIRecipeInfo {

    /**
     * Recipe to show description.
     */
    public final GT_Recipe recipe;

    /**
     * RecipeMap the recipe belongs to.
     */
    public final RecipeMap<?> recipeMap;

    /**
     * Recipe object for NEI.
     */
    public final GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe;

    /**
     * When user looks up usage for machine, NEI will show all the recipes that the machine can process, taking tier of
     * the machine into consideration. This object can be used to show info around overclocked EU/t and duration.
     */
    public final Power power;

    /**
     * Pre-built overclock calculator, used for drawing OC information. Do not calculate it again.
     */
    public final GT_OverclockCalculator calculator;

    /**
     * Current Y position for drawing description.
     */
    public int yPos;

    NEIRecipeInfo(GT_Recipe recipe, RecipeMap<?> recipeMap, GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe,
        Power power, GT_OverclockCalculator calculator, int descriptionYOffset) {
        this.recipe = recipe;
        this.recipeMap = recipeMap;
        this.neiCachedRecipe = neiCachedRecipe;
        this.power = power;
        this.calculator = calculator;
        this.yPos = descriptionYOffset;
    }
}
