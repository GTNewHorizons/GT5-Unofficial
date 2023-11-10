package gregtech.api.objects.overclockdescriber;

import static gregtech.api.util.GT_Utility.trans;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import gregtech.api.enums.SteamVariant;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.NEIRecipeInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SteamOverclockDescriber extends OverclockDescriber {

    private final SteamVariant steamVariant;
    private final int euPerTickMultiplier;
    private final int durationMultiplier;

    public SteamOverclockDescriber(SteamVariant steamVariant, int euPerTickMultiplier, int durationMultiplier) {
        super((byte) 1); // recipe tier is always LV
        this.steamVariant = steamVariant;
        this.euPerTickMultiplier = euPerTickMultiplier;
        this.durationMultiplier = durationMultiplier;
    }

    @Override
    public String getTierString() {
        return StatCollector.translateToLocal("GT5U.steam_variant." + steamVariant.toString());
    }

    @Override
    public GT_OverclockCalculator createCalculator(GT_OverclockCalculator template, GT_Recipe recipe) {
        return GT_OverclockCalculator.ofNoOverclock(recipe)
            .setEUtDiscount(euPerTickMultiplier)
            .setSpeedBoost(durationMultiplier);
    }

    @Override
    public void drawEnergyInfo(NEIRecipeInfo recipeInfo) {
        if (recipeInfo.calculator.getConsumption() <= 0) return;

        recipeInfo.drawNEIText(trans("152", "Total: ") + getTotalPowerString(recipeInfo.calculator));
        recipeInfo.drawNEIText(trans("153", "Usage: ") + getSteamUsageString(recipeInfo.calculator));
    }

    private String getTotalPowerString(GT_OverclockCalculator calculator) {
        return GT_Utility.formatNumbers(convertEUToSteam(calculator.getConsumption() * calculator.getDuration()))
            + " Steam";
    }

    private String getSteamUsageString(GT_OverclockCalculator calculator) {
        return GT_Utility.formatNumbers(20 * convertEUToSteam(calculator.getConsumption())) + " L/s Steam";
    }

    private static long convertEUToSteam(long eu) {
        // 2L normal steam == 1EU
        return 2 * eu;
    }
}
