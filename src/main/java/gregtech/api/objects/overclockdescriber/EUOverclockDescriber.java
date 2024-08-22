package gregtech.api.objects.overclockdescriber;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.util.GT_Utility.trans;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.primitives.Ints;

import gregtech.GT_Mod;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.api.util.OverclockCalculator;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EUOverclockDescriber extends EUNoOverclockDescriber {

    public EUOverclockDescriber(byte tier, int amperage) {
        super(tier, amperage);
    }

    @Override
    public OverclockCalculator createCalculator(OverclockCalculator template, GT_Recipe recipe) {
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
            OverclockCalculator originalPowerCalculator = OverclockCalculator.ofNoOverclock(recipeInfo.recipe)
                .calculate();
            recipeInfo
                .drawText(trans("275", "Original usage: ") + originalPower.getEUtDisplay(originalPowerCalculator));
        }
        if (shouldShowAmperage(recipeInfo.calculator)) {
            recipeInfo.drawText(trans("155", "Amperage: ") + getAmperageString(recipeInfo.calculator));
        }
    }

    @Override
    protected String getEUtWithoutTier(OverclockCalculator calculator) {
        return decorateWithOverclockLabel(super.getEUtWithoutTier(calculator), calculator);
    }

    @Override
    protected String getEUtWithTier(OverclockCalculator calculator) {
        return this.getEUtWithoutTier(calculator) + GT_Utility.getTierNameWithParentheses(calculator.getConsumption());
    }

    @Override
    protected String getVoltageString(OverclockCalculator calculator) {
        long voltage = computeVoltageForEURate(calculator.getConsumption());
        return decorateWithOverclockLabel(GT_Utility.formatNumbers(voltage) + " EU/t", calculator)
            + GT_Utility.getTierNameWithParentheses(voltage);
    }

    protected String decorateWithOverclockLabel(String s, OverclockCalculator calculator) {
        if (wasOverclocked(calculator)) {
            s += " (OC)";
        }
        return s;
    }

    protected boolean wasOverclocked(OverclockCalculator calculator) {
        return calculator.getPerformedOverclocks() > 0;
    }
}
