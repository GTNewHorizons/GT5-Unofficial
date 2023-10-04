package gregtech.nei.formatter;

import static gregtech.api.util.GT_Utility.trans;

import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.GT_Mod;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.NEIRecipeInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DefaultSpecialValueFormatter implements INEISpecialInfoFormatter {

    public static DefaultSpecialValueFormatter INSTANCE = new DefaultSpecialValueFormatter();

    @Override
    public List<String> format(NEIRecipeInfo recipeInfo) {
        int specialValue = recipeInfo.recipe.mSpecialValue;
        if (specialValue == -100 && GT_Mod.gregtechproxy.mLowGravProcessing) {
            return Collections.singletonList(trans("159", "Needs Low Gravity"));
        } else if (specialValue == -200 && GT_Mod.gregtechproxy.mEnableCleanroom) {
            return Collections.singletonList(trans("160", "Needs Cleanroom"));
        } else if (specialValue == -201) {
            return Collections.singletonList(trans("206", "Scan for Assembly Line"));
        } else if (specialValue == -300 && GT_Mod.gregtechproxy.mEnableCleanroom) {
            return Collections.singletonList(trans("160.1", "Needs Cleanroom & LowGrav"));
        } else if (specialValue == -400) {
            return Collections.singletonList(trans("216", "Deprecated Recipe"));
        }
        return Collections.emptyList();
    }
}
