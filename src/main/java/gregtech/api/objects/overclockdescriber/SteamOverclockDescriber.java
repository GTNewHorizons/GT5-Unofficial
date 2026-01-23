package gregtech.api.objects.overclockdescriber;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import gregtech.api.enums.SteamVariant;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.api.util.OverclockCalculator;
import gregtech.nei.RecipeDisplayInfo;

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
        return StatCollector.translateToLocal("GT5U.nei.display.steam_variant." + steamVariant.toString());
    }

    @Override
    public OverclockCalculator createCalculator(OverclockCalculator template, GTRecipe recipe) {
        return OverclockCalculator.ofNoOverclock(recipe)
            .setEUtDiscount(euPerTickMultiplier)
            .setDurationModifier(durationMultiplier);
    }

    @Override
    public void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {
        if (recipeInfo.calculator.getConsumption() <= 0) return;

        recipeInfo.drawText(getTotalPowerString(recipeInfo.calculator));
        recipeInfo.drawText(getSteamUsageString(recipeInfo.calculator));
    }

    private String getTotalPowerString(OverclockCalculator calculator) {
        long steamTotal = convertEUToSteam(calculator.getConsumption() * calculator.getDuration());
        return StatCollector
            .translateToLocalFormatted("GT5U.nei.display.total.steam", formatNumber(steamTotal));
    }

    private String getSteamUsageString(OverclockCalculator calculator) {
        long steamUsage = 20 * convertEUToSteam(calculator.getConsumption());
        return StatCollector
            .translateToLocalFormatted("GT5U.nei.display.usage.steam", formatNumber(steamUsage));
    }

    private static long convertEUToSteam(long eu) {
        // 2L normal steam == 1EU
        return 2 * eu;
    }
}
