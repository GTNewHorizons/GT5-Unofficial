package gregtech.nei;

import java.util.List;
import java.util.function.Function;

/**
 * Getter for description for {@link gregtech.api.util.GT_Recipe#mSpecialValue} etc. that will be drawn on NEI.
 */
@FunctionalInterface
public interface INEISpecialInfoFormatter {

    /**
     * @param recipeInfo           Recipe info to draw description. You can retrieve special value with
     *                             {@code recipeInfo.recipe.mSpecialValue}.
     * @param applyPrefixAndSuffix Function to apply
     *                             {@link gregtech.api.util.GT_Recipe.GT_Recipe_Map#formatSpecialValue}.
     * @return List of strings containing info for special value etc.
     */
    @SuppressWarnings("JavadocReference")
    List<String> format(NEIRecipeInfo recipeInfo, Function<Integer, String> applyPrefixAndSuffix);
}
