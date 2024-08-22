package gregtech.api.objects.overclockdescriber;

import static gregtech.api.util.GT_Utility.trans;

import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
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
    public OverclockCalculator createCalculator(OverclockCalculator template, GT_Recipe recipe) {
        return OverclockCalculator.ofNoOverclock(recipe);
    }

    @Override
    public String getTierString() {
        return GT_Utility.getColoredTierNameFromTier(tier);
    }

    @Override
    public final void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {
        if (recipeInfo.calculator.getDuration() > 0 && recipeInfo.calculator.getConsumption() > 0) {
            recipeInfo.drawText(trans("152", "Total: ") + getTotalPowerString(recipeInfo.calculator));
        }
        drawEnergyInfoImpl(recipeInfo);
    }

    /**
     * Override this to draw custom info about the energy this object can handle on NEI recipe GUI, minus total
     * power usage.
     */
    protected void drawEnergyInfoImpl(RecipeDisplayInfo recipeInfo) {
        if (recipeInfo.calculator.getConsumption() <= 0) {
            return;
        }
        recipeInfo.drawText(trans("153", "Usage: ") + getEUtDisplay(recipeInfo.calculator));
        if (shouldShowAmperage(recipeInfo.calculator)) {
            recipeInfo.drawText(trans("154", "Voltage: ") + getVoltageString(recipeInfo.calculator));
            recipeInfo.drawText(trans("155", "Amperage: ") + getAmperageString(recipeInfo.calculator));
        }
    }

    protected String getTotalPowerString(OverclockCalculator calculator) {
        return GT_Utility.formatNumbers(calculator.getConsumption() * calculator.getDuration()) + " EU";
    }

    /**
     * @return If amperage should be shown on NEI.
     */
    protected boolean shouldShowAmperage(OverclockCalculator calculator) {
        return amperage != 1;
    }

    /**
     * @return Whole EU/t usage, without tier display.
     */
    protected String getEUtWithoutTier(OverclockCalculator calculator) {
        return GT_Utility.formatNumbers(calculator.getConsumption()) + " EU/t";
    }

    /**
     * @return Whole EU/t usage, with tier display.
     */
    protected String getEUtWithTier(OverclockCalculator calculator) {
        return getEUtWithoutTier(calculator) + GT_Utility.getTierNameWithParentheses(calculator.getConsumption());
    }

    /**
     * @return Whole EU/t usage. Also displays voltage tier if it should be shown.
     */
    protected String getEUtDisplay(OverclockCalculator calculator) {
        return shouldShowAmperage(calculator) ? getEUtWithoutTier(calculator) : getEUtWithTier(calculator);
    }

    /**
     * @return EU/t usage, divided by amperage. With tier display.
     */
    protected String getVoltageString(OverclockCalculator calculator) {
        long voltage = computeVoltageForEURate(calculator.getConsumption());
        return GT_Utility.formatNumbers(voltage) + " EU/t" + GT_Utility.getTierNameWithParentheses(voltage);
    }

    protected String getAmperageString(OverclockCalculator calculator) {
        return GT_Utility.formatNumbers(amperage);
    }

    protected long computeVoltageForEURate(long euPerTick) {
        return euPerTick / amperage;
    }
}
