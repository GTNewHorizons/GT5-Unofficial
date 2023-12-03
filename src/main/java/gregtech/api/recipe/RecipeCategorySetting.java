package gregtech.api.recipe;

import java.util.Locale;
import java.util.stream.Stream;

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
    public static final String[] NAMES = Stream.of(VALUES)
        .map(RecipeCategorySetting::toName)
        .toArray(String[]::new);

    public static RecipeCategorySetting getDefault() {
        return ENABLE;
    }

    public String toName() {
        return toString().toLowerCase(Locale.ENGLISH);
    }

    public static RecipeCategorySetting find(String name) {
        for (RecipeCategorySetting setting : VALUES) {
            if (setting.toName()
                .equals(name)) {
                return setting;
            }
        }
        return getDefault();
    }
}
