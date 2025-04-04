package gregtech.nei.formatter;

import static gregtech.api.util.TemperatureUtils.getTemperatureAsCurrentUnit;

import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.util.TemperatureUtils;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HeatingCoilSpecialValueFormatter implements INEISpecialInfoFormatter {

    public static final HeatingCoilSpecialValueFormatter INSTANCE = new HeatingCoilSpecialValueFormatter();

    @Override
    public List<String> format(RecipeDisplayInfo recipeInfo) {
        int heat = recipeInfo.recipe.mSpecialValue;
        return Collections.singletonList(
            StatCollector.translateToLocal("GT5U.nei.heat_capacity") + " "
                + TemperatureUtils.getTemperatureAsCurrentUnit(heat)
                + " ("
                + HeatingCoilLevel.getDisplayNameFromHeat(heat, false)
                + ")");
    }
}
