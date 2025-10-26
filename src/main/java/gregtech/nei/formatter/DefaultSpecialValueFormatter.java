package gregtech.nei.formatter;

import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.GTMod;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DefaultSpecialValueFormatter implements INEISpecialInfoFormatter {

    public static DefaultSpecialValueFormatter INSTANCE = new DefaultSpecialValueFormatter();

    @Override
    public List<String> format(RecipeDisplayInfo recipeInfo) {
        int specialValue = recipeInfo.recipe.mSpecialValue;
        if (specialValue == -100 && GTMod.proxy.mLowGravProcessing) {
            return Collections.singletonList(GTUtility.translate("gt.recipe.needs_low_gravity"));
        } else if (specialValue == -200 && GTMod.proxy.mEnableCleanroom) {
            return Collections.singletonList(GTUtility.translate("gt.recipe.needs_cleanroom"));
        } else if (specialValue == -201) {
            return Collections.singletonList(GTUtility.translate("gt.recipe.scan_ass_line"));
        } else if (specialValue == -300 && GTMod.proxy.mEnableCleanroom) {
            return Collections.singletonList(GTUtility.translate("gt.recipe.needs_cleanroom_and_low_gravity"));
        } else if (specialValue == -400) {
            return Collections.singletonList(GTUtility.translate("gt.recipe.deprecated_recipe"));
        }
        return Collections.emptyList();
    }
}
