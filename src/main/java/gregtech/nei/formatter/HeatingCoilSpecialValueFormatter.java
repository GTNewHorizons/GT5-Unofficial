package gregtech.nei.formatter;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.Arrays;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

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
        return Arrays.asList(
            StatCollector.translateToLocalFormatted("GT5U.nei.heat_capacity", formatNumber(heat)),
            StatCollector.translateToLocalFormatted(
                "GT5U.nei.heat_capacity.material",
                HeatingCoilLevel.getDisplayNameFromHeat(heat, false)));
    }
}
