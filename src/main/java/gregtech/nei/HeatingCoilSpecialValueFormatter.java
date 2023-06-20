package gregtech.nei;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import gregtech.api.enums.HeatingCoilLevel;

public class HeatingCoilSpecialValueFormatter implements INEISpecialInfoFormatter {

    public static final HeatingCoilSpecialValueFormatter INSTANCE = new HeatingCoilSpecialValueFormatter();

    @Override
    public List<String> format(NEIRecipeInfo recipeInfo, Function<Integer, String> applyPrefixAndSuffix) {
        int heat = recipeInfo.recipe.mSpecialValue;

        List<String> result = new ArrayList<>();
        result.add(applyPrefixAndSuffix.apply(heat));
        result.add(" (" + HeatingCoilLevel.getDisplayNameFromHeat(heat, false) + ")");
        return result;
    }
}
