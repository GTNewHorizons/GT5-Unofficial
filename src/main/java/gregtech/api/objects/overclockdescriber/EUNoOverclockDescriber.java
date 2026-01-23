package gregtech.api.objects.overclockdescriber;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.api.util.OverclockCalculator;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EUNoOverclockDescriber extends OverclockDescriber {

    /**
     * Amperage of the recipemap.
     */
    protected final int amperage;

    public EUNoOverclockDescriber(byte tier, int amperage) {
        super(tier);
        if (amperage < 1) {
            throw new IllegalArgumentException("Amperage cannot be lower than 1");
        }
        this.amperage = amperage;
    }

    @Override
    public OverclockCalculator createCalculator(OverclockCalculator template, GTRecipe recipe) {
        return OverclockCalculator.ofNoOverclock(recipe);
    }

    @Override
    public String getTierString() {
        return GTUtility.getColoredTierNameFromTier(tier);
    }

    @Override
    public final void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {
        if (recipeInfo.calculator.getDuration() > 0 && recipeInfo.calculator.getConsumption() > 0) {
            recipeInfo.drawText(getTotalPowerString(recipeInfo.calculator));
        }
        drawEnergyInfoImpl(recipeInfo);
    }

    /**
     * Override this to draw custom info about the energy this object can handle on NEI recipe GUI, minus total power
     * usage.
     */
    protected void drawEnergyInfoImpl(RecipeDisplayInfo recipeInfo) {
        if (recipeInfo.calculator.getConsumption() <= 0) {
            return;
        }
        recipeInfo.drawText(getEUtDisplay(recipeInfo.calculator));

        if (shouldShowAmperage(recipeInfo.calculator)) {
            recipeInfo.drawText(getVoltageString(recipeInfo.calculator));
            recipeInfo.drawText(getAmperageString(recipeInfo.calculator));
        }
    }

    protected String getTotalPowerString(OverclockCalculator calculator) {
        return StatCollector.translateToLocalFormatted(
            "GT5U.nei.display.total",
            formatNumber(calculator.getConsumption() * calculator.getDuration()));
    }

    /**
     * @return If amperage should be shown on NEI.
     */
    protected boolean shouldShowAmperage(OverclockCalculator calculator) {
        return amperage != 1;
    }

    /**
     * @return Whole EU/t usage. Also displays voltage tier if it should be shown.
     */
    protected String getEUtDisplay(OverclockCalculator calculator) {
        String tier_displayed = shouldShowAmperage(calculator) ? ""
            : getTierNameWithParentheses(calculator.getConsumption(), calculator);
        return StatCollector.translateToLocalFormatted(
            "GT5U.nei.display.usage",
            formatNumber(calculator.getConsumption()),
            tier_displayed);
    }

    /**
     * @return EU/t usage, divided by amperage. With tier display.
     */
    protected String getVoltageString(OverclockCalculator calculator) {
        long voltage = computeVoltageForEURate(calculator.getConsumption());
        return StatCollector.translateToLocalFormatted(
            "GT5U.nei.display.voltage",
            formatNumber(voltage),
            getTierNameWithParentheses(voltage, calculator));

    }

    protected String getTierNameWithParentheses(long voltage, OverclockCalculator calculator) {
        return GTUtility.getTierNameWithParentheses(voltage);
    }

    protected String getAmperageString(OverclockCalculator calculator) {
        return StatCollector.translateToLocalFormatted("GT5U.nei.display.amperage", formatNumber(amperage));
    }

    protected long computeVoltageForEURate(long euPerTick) {
        return euPerTick / amperage;
    }
}
