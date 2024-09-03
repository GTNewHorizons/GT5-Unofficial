package gregtech.nei.formatter;

import static gregtech.api.util.GTUtility.trans;

import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.GTMod;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DefaultSpecialValueFormatter implements INEISpecialInfoFormatter {

    public static DefaultSpecialValueFormatter INSTANCE = new DefaultSpecialValueFormatter();

    @Override
    public List<String> format(RecipeDisplayInfo recipeInfo) {
        int specialValue = recipeInfo.recipe.mSpecialValue;
        if (specialValue == -100 && GTMod.gregtechproxy.mLowGravProcessing) {
            return Collections.singletonList(trans("159", "Needs Low Gravity"));
        } else if (specialValue == -200 && GTMod.gregtechproxy.mEnableCleanroom) {
            return Collections.singletonList(trans("160", "Needs Cleanroom"));
        } else if (specialValue == -201) {
            return Collections.singletonList(trans("206", "Scan for Assembly Line"));
        } else if (specialValue == -300 && GTMod.gregtechproxy.mEnableCleanroom) {
            return Collections.singletonList(trans("160.1", "Needs Cleanroom & LowGrav"));
        } else if (specialValue == -400) {
            return Collections.singletonList(trans("216", "Deprecated Recipe"));
        }
        return Collections.emptyList();
    }
}
