package gregtech.nei.formatter;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.NEIRecipeInfo;

/**
 * Simple formatter for recipe's special value.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SimpleSpecialValueFormatter implements INEISpecialInfoFormatter {

    @Nullable
    private final String formatter;
    private final int multiplier;

    /**
     * @param formatter  Localization key to format
     * @param multiplier Number to multiply to special value for display
     */
    public SimpleSpecialValueFormatter(@Nullable String formatter, int multiplier) {
        this.formatter = formatter;
        this.multiplier = multiplier;
    }

    /**
     * @param formatter Localization key to format
     */
    public SimpleSpecialValueFormatter(@Nullable String formatter) {
        this(formatter, 1);
    }

    @Override
    public List<String> format(NEIRecipeInfo recipeInfo) {
        return Collections.singletonList(
            StatCollector.translateToLocalFormatted(formatter, recipeInfo.recipe.mSpecialValue * multiplier));
    }
}
