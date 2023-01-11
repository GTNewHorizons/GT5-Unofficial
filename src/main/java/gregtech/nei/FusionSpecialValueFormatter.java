package gregtech.nei;

import gregtech.api.enums.GT_Values;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class FusionSpecialValueFormatter implements INEISpecialInfoFormatter {

    public static final FusionSpecialValueFormatter INSTANCE = new FusionSpecialValueFormatter();
    private static final int M = 1000000;

    @Override
    public List<String> format(NEIRecipeInfo recipeInfo, Function<Integer, String> applyPrefixAndSuffix) {
        int euToStart = recipeInfo.recipe.mSpecialValue;
        int voltage = recipeInfo.recipe.mEUt;
        int tier;

        if (euToStart <= 10 * M * 16) {
            tier = 1;
        } else if (euToStart <= 20 * M * 16) {
            tier = 2;
        } else if (euToStart <= 40 * M * 16) {
            tier = 3;
        } else {
            tier = 4;
        }

        if (voltage <= GT_Values.V[6]) {
            // no-op
        } else if (voltage <= GT_Values.V[7]) {
            tier = Math.max(tier, 2);
        } else if (voltage <= GT_Values.V[8]) {
            tier = Math.max(tier, 3);
        } else {
            tier = 4;
        }

        return Collections.singletonList(applyPrefixAndSuffix.apply(euToStart) + " (MK " + tier + ")");
    }
}
