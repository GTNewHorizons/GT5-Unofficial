package gregtech.nei.formatter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FusionSpecialValueFormatter implements INEISpecialInfoFormatter {

    public static final FusionSpecialValueFormatter INSTANCE = new FusionSpecialValueFormatter();
    private static final long M = 1000000;

    @Override
    public List<String> format(RecipeDisplayInfo recipeInfo) {
        List<String> specialInfo = new ArrayList<>(3);

        int euToStart = recipeInfo.recipe.mSpecialValue;
        int voltage = recipeInfo.recipe.mEUt;

        int voltageTier = getFusionVoltageTier(voltage);
        int overclockTimes = overclock(euToStart);
        int startupTier = getFusionStartupTier(euToStart);

        specialInfo.add(StatCollector.translateToLocalFormatted("GT5U.nei.fusion_voltage", voltageTier));
        specialInfo.add(
            StatCollector
                .translateToLocalFormatted("GT5U.nei.start_eu", GT_Utility.formatNumbers(euToStart), startupTier));
        if (overclockTimes <= 0) {
            specialInfo.add(StatCollector.translateToLocalFormatted("GT5U.nei.fusion_no_overclock"));
        } else {
            specialInfo.add(StatCollector.translateToLocalFormatted("GT5U.nei.fusion_overclock", overclockTimes));
        }
        return specialInfo;
    }

    protected int overclock(int startEnergy) {
        if (startEnergy <= 160_000_000) {
            return 4;
        } else if (startEnergy <= 320_000_000) {
            return 3;
        } else if (startEnergy <= 640_000_000) {
            return 2;
        } else if (startEnergy <= 1_280_000_000) {
            return 1;
        } else {
            return 0;
        }
    }

    public static int getFusionStartupTier(long startupPower) {
        if (startupPower <= 10 * M * 16) {
            return 1;
        } else if (startupPower <= 20 * M * 16) {
            return 2;
        } else if (startupPower <= 40 * M * 16) {
            return 3;
        } else if (startupPower <= 320 * M * 16) {
            return 4;
        } else {
            return 5;
        }
    }

    public static int getFusionVoltageTier(long voltage) {
        if (voltage <= GT_Values.V[6]) {
            return 1;
        } else if (voltage <= GT_Values.V[7]) {
            return 2;
        } else if (voltage <= GT_Values.V[8]) {
            return 3;
        } else if (voltage <= GT_Values.V[9]) {
            return 4;
        } else {
            return 5;
        }
    }
}
