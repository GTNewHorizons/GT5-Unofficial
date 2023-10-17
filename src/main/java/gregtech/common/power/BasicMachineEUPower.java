package gregtech.common.power;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.util.GT_Utility.trans;

import gregtech.GT_Mod;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.nei.NEIRecipeInfo;

public class BasicMachineEUPower extends EUPower {

    protected static final String OC = " (OC)";
    protected boolean wasOverclocked;

    public BasicMachineEUPower(byte tier, int amperage) {
        super(tier, amperage);
    }

    @Override
    public void compute(GT_Recipe recipe) {
        super.compute(recipe);
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(recipe.mEUt)
            .setEUt(V[tier] * amperage)
            .setDuration(recipe.mDuration)
            .setOneTickDiscount(true)
            .calculate();
        recipeEuPerTick = (int) calculator.getConsumption();
        recipeDuration = calculator.getDuration();
        wasOverclocked = checkIfOverclocked();
    }

    @Override
    protected void drawNEIDescImpl(NEIRecipeInfo recipeInfo, RecipeMapFrontend frontend) {
        if (!wasOverclocked) {
            super.drawNEIDescImpl(recipeInfo, frontend);
            return;
        }

        frontend.drawNEIText(recipeInfo, trans("153", "Usage: ") + getEUtDisplay());
        if (shouldShowAmperage()) {
            frontend.drawNEIText(recipeInfo, trans("154", "Voltage: ") + getVoltageString());
        }
        if (GT_Mod.gregtechproxy.mNEIOriginalVoltage) {
            EUPower originalPower = new EUPower(tier, amperage);
            originalPower.compute(recipeInfo.recipe);
            frontend.drawNEIText(recipeInfo, trans("275", "Original usage: ") + originalPower.getEUtDisplay());
        }
        if (shouldShowAmperage()) {
            frontend.drawNEIText(recipeInfo, trans("155", "Amperage: ") + getAmperageString());
        }
    }

    @Override
    protected String getEUtWithoutTier() {
        return decorateWithOverclockLabel(super.getEUtWithoutTier());
    }

    @Override
    protected String getEUtWithTier() {
        return this.getEUtWithoutTier() + GT_Utility.getTierNameWithParentheses(recipeEuPerTick);
    }

    @Override
    protected String getVoltageString() {
        int voltage = computeVoltageForEURate(recipeEuPerTick);
        return decorateWithOverclockLabel(GT_Utility.formatNumbers(voltage) + " EU/t")
            + GT_Utility.getTierNameWithParentheses(voltage);
    }

    protected String decorateWithOverclockLabel(String s) {
        if (wasOverclocked) {
            s += OC;
        }
        return s;
    }

    protected boolean checkIfOverclocked() {
        return originalEUt != recipeEuPerTick;
    }
}
