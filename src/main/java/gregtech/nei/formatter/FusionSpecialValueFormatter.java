package gregtech.nei.formatter;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import gregtech.api.enums.GTValues;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FusionSpecialValueFormatter implements INEISpecialInfoFormatter {

    public static final FusionSpecialValueFormatter INSTANCE = new FusionSpecialValueFormatter();
    private static final long M = 1000000;

    @Override
    public List<String> format(RecipeDisplayInfo recipeInfo) {
        long euToStart = recipeInfo.recipe.getMetadataOrDefault(GTRecipeConstants.FUSION_THRESHOLD, 0L);
        int voltage = recipeInfo.recipe.mEUt;
        int tier = getFusionTier(euToStart, voltage);

        return Collections
            .singletonList(StatCollector.translateToLocalFormatted("GT5U.nei.start_eu", formatNumber(euToStart), tier));
    }

    public static int getFusionTier(long startupPower, long voltage) {
        int tier;
        if (startupPower <= 10 * M * 16) {
            tier = 1;
        } else if (startupPower <= 20 * M * 16) {
            tier = 2;
        } else if (startupPower <= 40 * M * 16) {
            tier = 3;
        } else if (startupPower <= 320 * M * 16) {
            tier = 4;
        } else {
            tier = 5;
        }

        if (voltage <= GTValues.V[6]) {
            // no-op
        } else if (voltage <= GTValues.V[7]) {
            tier = Math.max(tier, 2);
        } else if (voltage <= GTValues.V[8]) {
            tier = Math.max(tier, 3);
        } else if (voltage <= GTValues.V[9]) {
            tier = Math.max(tier, 4);
        } else {
            tier = 5;
        }
        return tier;
    }
}
