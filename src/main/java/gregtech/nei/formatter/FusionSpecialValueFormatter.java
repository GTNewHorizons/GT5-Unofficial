package gregtech.nei.formatter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
    public static final String ZERO_STRING = "0";
    public static final String[] UNITS = { "", "K", "M", "G", "T", "P", "E", "Z", "Y", "R", "Q" };
    public static final BigDecimal THOUSAND_DEC = BigDecimal.valueOf(1000);
    public static final DecimalFormat DF = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.US));

    public static String shortFormat(long value) {
        double number = value;
        int unitIndex = 0;
        while (Math.abs(number) >= 1000 && unitIndex < UNITS.length - 1) {
            number /= 1000;
            unitIndex++;
        }
        return DF.format(number) + UNITS[unitIndex];
    }

    public static String shortFormat(BigInteger value) {
        BigDecimal decimal = new BigDecimal(value);
        int unitIndex = 0;

        while (decimal.compareTo(THOUSAND_DEC) >= 0 && unitIndex < UNITS.length - 1) {
            decimal = decimal.divide(THOUSAND_DEC, 2, RoundingMode.HALF_UP);
            unitIndex++;
        }

        return DF.format(decimal) + UNITS[unitIndex];
    }

    @Override
    public List<String> format(RecipeDisplayInfo recipeInfo) {
        long euToStart = recipeInfo.recipe.getMetadataOrDefault(GTRecipeConstants.FUSION_THRESHOLD, 0L);
        int voltage = recipeInfo.recipe.mEUt;
        int tier = getFusionTier(euToStart, voltage);

        Test.test();

        return Collections
            .singletonList(StatCollector.translateToLocalFormatted("GT5U.nei.start_eu", shortFormat(euToStart), tier));
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
