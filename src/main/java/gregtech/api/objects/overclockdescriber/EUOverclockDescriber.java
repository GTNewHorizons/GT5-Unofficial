package gregtech.api.objects.overclockdescriber;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.util.GT_Utility.trans;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.primitives.Ints;

import gregtech.GT_Mod;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EUOverclockDescriber extends EUNoOverclockDescriber {

    public EUOverclockDescriber(byte tier, int amperage) {
        super(tier, amperage);
    }

    @Override
    public GT_OverclockCalculator createCalculator(GT_OverclockCalculator template, GT_Recipe recipe) {
        return template.setEUt(Ints.saturatedCast(V[tier] * amperage));
    }

    @Override
    protected void drawEnergyInfoImpl(RecipeDisplayInfo recipeInfo) {
        if (!wasOverclocked(recipeInfo.calculator)) {
            super.drawEnergyInfoImpl(recipeInfo);
            return;
        }

        recipeInfo.drawText(trans("153", "Usage: ") + getEUtDisplay(recipeInfo.calculator));
        if (shouldShowAmperage(recipeInfo.calculator)) {
            recipeInfo.drawText(trans("154", "Voltage: ") + getVoltageString(recipeInfo.calculator));
        }
        if (GT_Mod.gregtechproxy.mNEIOriginalVoltage) {
            EUNoOverclockDescriber originalPower = new EUNoOverclockDescriber(tier, amperage);
            GT_OverclockCalculator originalPowerCalculator = GT_OverclockCalculator.ofNoOverclock(recipeInfo.recipe)
                .calculate();
            recipeInfo
                .drawText(trans("275", "Original usage: ") + originalPower.getEUtDisplay(originalPowerCalculator));
        }
        if (shouldShowAmperage(recipeInfo.calculator)) {
            recipeInfo.drawText(trans("155", "Amperage: ") + getAmperageString(recipeInfo.calculator));
        }
    }

    @Override
    protected String getEUtWithoutTier(GT_OverclockCalculator calculator) {
        return decorateWithOverclockLabel(super.getEUtWithoutTier(calculator), calculator);
    }

    @Override
    protected String getEUtWithTier(GT_OverclockCalculator calculator) {
        return this.getEUtWithoutTier(calculator) + GT_Utility.getTierNameWithParentheses(calculator.getConsumption());
    }

    @Override
    protected String getVoltageString(GT_OverclockCalculator calculator) {
        long voltage = computeVoltageForEURate(calculator.getConsumption());
        return decorateWithOverclockLabel(GT_Utility.formatNumbers(voltage) + " EU/t", calculator)
            + GT_Utility.getTierNameWithParentheses(voltage);
    }

    protected String decorateWithOverclockLabel(String s, GT_OverclockCalculator calculator) {
        if (wasOverclocked(calculator)) {
            s += " (OC)";
        }
        return s;
    }

    protected boolean wasOverclocked(GT_OverclockCalculator calculator) {
        return calculator.getPerformedOverclocks() > 0;
    }
}
