package gregtech.common.power;

import static gregtech.api.util.GT_Utility.trans;

import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GT_Utility;
import gregtech.nei.NEIRecipeInfo;

public class EUPower extends Power {

    /**
     * Amperage of the recipemap.
     */
    protected final int amperage;
    protected int originalEUt;

    public EUPower(byte tier, int amperage) {
        super(tier);
        if (amperage < 1) {
            throw new IllegalArgumentException("Amperage cannot be lower than 1");
        }
        this.amperage = amperage;
    }

    @Override
    // This generic EU Power class has no overclock defined and does no special calculations.
    public void computePowerUsageAndDuration(int euPerTick, int duration) {
        originalEUt = euPerTick;
        recipeEuPerTick = euPerTick;
        recipeDuration = duration;
    }

    @Override
    public String getTierString() {
        return GT_Utility.getColoredTierNameFromTier(tier);
    }

    @Override
    protected String getTotalPowerString() {
        return GT_Utility.formatNumbers((long) recipeDuration * recipeEuPerTick) + " EU";
    }

    @Override
    protected void drawNEIDescImpl(NEIRecipeInfo recipeInfo, RecipeMapFrontend frontend) {
        frontend.drawNEIText(recipeInfo, trans("153", "Usage: ") + getEUtDisplay());
        if (shouldShowAmperage()) {
            frontend.drawNEIText(recipeInfo, trans("154", "Voltage: ") + getVoltageString());
            frontend.drawNEIText(recipeInfo, trans("155", "Amperage: ") + getAmperageString());
        }
    }

    /**
     * @return If amperage should be shown on NEI.
     */
    protected boolean shouldShowAmperage() {
        return amperage != 1;
    }

    /**
     * @return Whole EU/t usage, without tier display.
     */
    protected String getEUtWithoutTier() {
        return GT_Utility.formatNumbers(recipeEuPerTick) + " EU/t";
    }

    /**
     * @return Whole EU/t usage, with tier display.
     */
    protected String getEUtWithTier() {
        return getEUtWithoutTier() + GT_Utility.getTierNameWithParentheses(recipeEuPerTick);
    }

    /**
     * @return Whole EU/t usage. Also displays voltage tier if it should be shown.
     */
    protected String getEUtDisplay() {
        return shouldShowAmperage() ? getEUtWithoutTier() : getEUtWithTier();
    }

    /**
     * @return EU/t usage, divided by amperage. With tier display.
     */
    protected String getVoltageString() {
        int voltage = computeVoltageForEURate(recipeEuPerTick);
        return GT_Utility.formatNumbers(voltage) + " EU/t" + GT_Utility.getTierNameWithParentheses(voltage);
    }

    protected String getAmperageString() {
        return GT_Utility.formatNumbers(amperage);
    }

    protected int computeVoltageForEURate(int euPerTick) {
        return euPerTick / amperage;
    }
}
