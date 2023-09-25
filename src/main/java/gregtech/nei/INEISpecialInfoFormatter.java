package gregtech.nei;

import java.util.List;
import java.util.function.Function;

import gregtech.api.recipe.RecipeMap;

/**
 * Getter for description for {@link gregtech.api.util.GT_Recipe#mSpecialValue} etc. that will be drawn on NEI.
 */
@FunctionalInterface
public interface INEISpecialInfoFormatter {

    /**
     * @param recipeInfo           Recipe info to draw description. You can retrieve special value with
     *                             {@code recipeInfo.recipe.mSpecialValue}.
     * @param applyPrefixAndSuffix Function to apply
     *                             {@link RecipeMap#formatSpecialValue}.
     * @return List of strings containing info for special value etc.
     */
    @SuppressWarnings("JavadocReference")
    List<String> format(NEIRecipeInfo recipeInfo, Function<Integer, String> applyPrefixAndSuffix);
}
