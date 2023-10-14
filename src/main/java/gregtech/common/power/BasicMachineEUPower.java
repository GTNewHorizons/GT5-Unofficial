package gregtech.common.power;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.util.GT_Utility.trans;

import gregtech.GT_Mod;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GT_Utility;
import gregtech.nei.NEIRecipeInfo;

public class BasicMachineEUPower extends EUPower {

    protected static final String OC = " (OC)";
    protected boolean wasOverclocked;

    public BasicMachineEUPower(byte tier, int amperage) {
        super(tier, amperage);
    }

    @Override
    public void computePowerUsageAndDuration(int euPerTick, int duration) {
        super.computePowerUsageAndDuration(euPerTick, duration);
        if (tier == 0) {
            // Long time calculation
            long xMaxProgresstime = ((long) duration) << 1;
            if (xMaxProgresstime > Integer.MAX_VALUE - 1) {
                // make impossible if too long
                recipeEuPerTick = Integer.MAX_VALUE - 1;
                recipeDuration = Integer.MAX_VALUE - 1;
            } else {
                recipeEuPerTick = euPerTick >> 2;
                recipeDuration = (int) xMaxProgresstime;
            }
        } else {
            // Long EUt calculation
            long xEUt = euPerTick;
            // Isnt too low EUt check?
            long tempEUt = Math.max(xEUt, V[1]);

            recipeDuration = duration;

            while (tempEUt <= V[tier - 1] * (long) amperage) {
                tempEUt <<= 2; // this actually controls overclocking
                // xEUt *= 4;//this is effect of everclocking
                recipeDuration >>= 1; // this is effect of overclocking
                xEUt = recipeDuration == 0 ? xEUt >> 1 : xEUt << 2; // U know, if the time is less than 1 tick make the
                                                                    // machine use 2x less power
            }
            if (xEUt > Integer.MAX_VALUE - 1) {
                recipeEuPerTick = Integer.MAX_VALUE - 1;
                recipeDuration = Integer.MAX_VALUE - 1;
            } else {
                recipeEuPerTick = (int) xEUt;
                if (recipeEuPerTick == 0) recipeEuPerTick = 1;
                if (recipeDuration == 0) recipeDuration = 1; // set time to 1 tick
            }
        }
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
            originalPower.computePowerUsageAndDuration(recipeInfo.recipe.mEUt, recipeInfo.recipe.mDuration);
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
