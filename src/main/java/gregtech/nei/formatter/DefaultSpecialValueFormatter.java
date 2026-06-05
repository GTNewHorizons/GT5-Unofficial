package gregtech.nei.formatter;

import static net.minecraft.util.StatCollector.translateToLocal;

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
        if (specialValue == -100 && GTMod.proxy.mLowGravProcessing) {
            return Collections.singletonList(translateToLocal("GT5U.nei.recipe.needs_low_gravity"));
        } else if (specialValue == -200 && GTMod.proxy.mEnableCleanroom) {
            return Collections.singletonList(translateToLocal("GT5U.nei.recipe.needs_cleanroom"));
        } else if (specialValue == -201) {
            return Collections.singletonList(translateToLocal("GT5U.nei.recipe.scan_assembly_line"));
        } else if (specialValue == -300 && GTMod.proxy.mEnableCleanroom) {
            return Collections.singletonList(translateToLocal("GT5U.nei.recipe.needs_cleanroom_low_gravity"));
        } else if (specialValue == -400) {
            return Collections.singletonList(translateToLocal("GT5U.nei.recipe.deprecated"));
        }
        return Collections.emptyList();
    }
}
