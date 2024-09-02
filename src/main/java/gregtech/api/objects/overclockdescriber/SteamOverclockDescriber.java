package gregtech.api.objects.overclockdescriber;

import static gregtech.api.util.GTUtility.trans;

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
        return StatCollector.translateToLocal("GT5U.steam_variant." + steamVariant.toString());
    }

    @Override
    public OverclockCalculator createCalculator(OverclockCalculator template, GTRecipe recipe) {
        return OverclockCalculator.ofNoOverclock(recipe)
            .setEUtDiscount(euPerTickMultiplier)
            .setSpeedBoost(durationMultiplier);
    }

    @Override
    public void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {
        if (recipeInfo.calculator.getConsumption() <= 0) return;

        recipeInfo.drawText(trans("152", "Total: ") + getTotalPowerString(recipeInfo.calculator));
        recipeInfo.drawText(trans("153", "Usage: ") + getSteamUsageString(recipeInfo.calculator));
    }

    private String getTotalPowerString(OverclockCalculator calculator) {
        return GTUtility.formatNumbers(convertEUToSteam(calculator.getConsumption() * calculator.getDuration()))
            + " Steam";
    }

    private String getSteamUsageString(OverclockCalculator calculator) {
        return GTUtility.formatNumbers(20 * convertEUToSteam(calculator.getConsumption())) + " L/s Steam";
    }

    private static long convertEUToSteam(long eu) {
        // 2L normal steam == 1EU
        return 2 * eu;
    }
}
