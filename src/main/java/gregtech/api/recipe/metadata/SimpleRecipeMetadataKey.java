package gregtech.api.recipe.metadata;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

/**
 * Simple metadata key that does not draw anything on NEI.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SimpleRecipeMetadataKey<T> extends RecipeMetadataKey<T> {

    private SimpleRecipeMetadataKey(Class<T> clazz, String identifier) {
        super(clazz, identifier);
    }

    public static <T> RecipeMetadataKey<T> create(Class<T> clazz, String identifier) {
        return new SimpleRecipeMetadataKey<>(clazz, identifier);
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {}
}
