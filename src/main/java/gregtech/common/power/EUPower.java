package gregtech.common.power;

import static gregtech.api.util.GT_Utility.trans;

import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.NEIRecipeInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EUPower extends Power {

    /**
     * Amperage of the recipemap.
     */
    protected final int amperage;

    public EUPower(byte tier, int amperage) {
        super(tier);
        if (amperage < 1) {
            throw new IllegalArgumentException("Amperage cannot be lower than 1");
        }
        this.amperage = amperage;
    }

    @Override
    public GT_OverclockCalculator createCalculator(GT_OverclockCalculator template, GT_Recipe recipe) {
        return GT_OverclockCalculator.ofNoOverclock(recipe);
    }

    @Override
    public String getTierString() {
        return GT_Utility.getColoredTierNameFromTier(tier);
    }

    @Override
    public final void drawEnergyInfo(NEIRecipeInfo recipeInfo, RecipeMapFrontend frontend) {
        if (recipeInfo.calculator.getConsumption() <= 0) return;

        frontend.drawNEIText(recipeInfo, trans("152", "Total: ") + getTotalPowerString(recipeInfo.calculator));
        drawEnergyInfoImpl(recipeInfo, frontend);
    }

    /**
     * Override this to draw custom info about the energy this power object can handle on NEI recipe GUI, minus total
     * power usage.
     */
    protected void drawEnergyInfoImpl(NEIRecipeInfo recipeInfo, RecipeMapFrontend frontend) {
        frontend.drawNEIText(recipeInfo, trans("153", "Usage: ") + getEUtDisplay(recipeInfo.calculator));
        if (shouldShowAmperage(recipeInfo.calculator)) {
            frontend.drawNEIText(recipeInfo, trans("154", "Voltage: ") + getVoltageString(recipeInfo.calculator));
            frontend.drawNEIText(recipeInfo, trans("155", "Amperage: ") + getAmperageString(recipeInfo.calculator));
        }
    }

    protected String getTotalPowerString(GT_OverclockCalculator calculator) {
        return GT_Utility.formatNumbers(calculator.getConsumption() * calculator.getDuration()) + " EU";
    }

    /**
     * @return If amperage should be shown on NEI.
     */
    protected boolean shouldShowAmperage(GT_OverclockCalculator calculator) {
        return amperage != 1;
    }

    /**
     * @return Whole EU/t usage, without tier display.
     */
    protected String getEUtWithoutTier(GT_OverclockCalculator calculator) {
        return GT_Utility.formatNumbers(calculator.getConsumption()) + " EU/t";
    }

    /**
     * @return Whole EU/t usage, with tier display.
     */
    protected String getEUtWithTier(GT_OverclockCalculator calculator) {
        return getEUtWithoutTier(calculator) + GT_Utility.getTierNameWithParentheses(calculator.getConsumption());
    }

    /**
     * @return Whole EU/t usage. Also displays voltage tier if it should be shown.
     */
    protected String getEUtDisplay(GT_OverclockCalculator calculator) {
        return shouldShowAmperage(calculator) ? getEUtWithoutTier(calculator) : getEUtWithTier(calculator);
    }

    /**
     * @return EU/t usage, divided by amperage. With tier display.
     */
    protected String getVoltageString(GT_OverclockCalculator calculator) {
        long voltage = computeVoltageForEURate(calculator.getConsumption());
        return GT_Utility.formatNumbers(voltage) + " EU/t" + GT_Utility.getTierNameWithParentheses(voltage);
    }

    protected String getAmperageString(GT_OverclockCalculator calculator) {
        return GT_Utility.formatNumbers(amperage);
    }

    protected long computeVoltageForEURate(long euPerTick) {
        return euPerTick / amperage;
    }
}
