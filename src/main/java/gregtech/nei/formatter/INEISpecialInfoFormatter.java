package gregtech.nei.formatter;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

/**
 * Getter for description for {@link gregtech.api.util.GT_Recipe#mSpecialValue} etc. that will be drawn on NEI.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@FunctionalInterface
public interface INEISpecialInfoFormatter {

    /**
     * @param recipeInfo Recipe info to draw description. You can retrieve special value with
     *                   {@code recipeInfo.recipe.mSpecialValue}.
     * @return List of strings containing info for special value etc.
     */
    List<String> format(RecipeDisplayInfo recipeInfo);
}
