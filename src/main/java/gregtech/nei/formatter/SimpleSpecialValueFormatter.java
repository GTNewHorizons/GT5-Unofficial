package gregtech.nei.formatter;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

/**
 * Simple formatter for recipe's special value.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SimpleSpecialValueFormatter implements INEISpecialInfoFormatter {

    @Nullable
    private final String translationKey;
    private final int multiplier;

    /**
     * @param translationKey Localization key to format
     * @param multiplier     Number to multiply to special value for display
     */
    public SimpleSpecialValueFormatter(@Nullable String translationKey, int multiplier) {
        this.translationKey = translationKey;
        this.multiplier = multiplier;
    }

    /**
     * @param translationKey Localization key to format
     */
    public SimpleSpecialValueFormatter(@Nullable String translationKey) {
        this(translationKey, 1);
    }

    @Override
    public List<String> format(RecipeDisplayInfo recipeInfo) {
        return Collections.singletonList(
            StatCollector.translateToLocalFormatted(
                translationKey,
                formatNumber((long) recipeInfo.recipe.mSpecialValue * multiplier)));
    }
}
