package gregtech.api.recipe;

import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * Specifies behaviors for {@link RecipeCategory}.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public enum RecipeCategorySetting {

    /**
     * Show the category in separated NEI tab.
     */
    ENABLE,
    /**
     * The category is merged to default one for the recipemap.
     */
    MERGE,
    /**
     * Recipes belonging to the category are hidden from NEI.
     */
    HIDE;

    public static final RecipeCategorySetting[] VALUES = values();

    public static RecipeCategorySetting getDefault() {
        return ENABLE;
    }
}
