package gregtech.api.objects.overclockdescriber;

import static gregtech.api.enums.GTValues.V;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import com.google.common.primitives.Ints;

import gregtech.GTMod;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
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
    public OverclockCalculator createCalculator(OverclockCalculator template, GTRecipe recipe) {
        return template.setEUt(Ints.saturatedCast(V[tier] * amperage));
    }

    @Override
    protected void drawEnergyInfoImpl(RecipeDisplayInfo recipeInfo) {
        if (!wasOverclocked(recipeInfo.calculator)) {
            super.drawEnergyInfoImpl(recipeInfo);
            return;
        }

        recipeInfo.drawText(getEUtDisplay(recipeInfo.calculator));
        if (shouldShowAmperage(recipeInfo.calculator)) {
            recipeInfo.drawText(getVoltageString(recipeInfo.calculator));
        }
        if (GTMod.proxy.mNEIOriginalVoltage) {
            recipeInfo.drawText(getOriginalEUtDisplay(recipeInfo));
        }
        if (shouldShowAmperage(recipeInfo.calculator)) {
            recipeInfo.drawText(getAmperageString(recipeInfo.calculator));
        }
    }

    @Override
    protected String getVoltageString(OverclockCalculator calculator) {
        String voltageString = super.getVoltageString(calculator);
        if (wasOverclocked(calculator)) {
            voltageString += StatCollector.translateToLocal("GT5U.nei.display.overclock");
        }
        return voltageString;
    }

    /**
     * @return Whole original EU/t usage. Also displays voltage tier if it should be shown.
     */
    protected String getOriginalEUtDisplay(RecipeDisplayInfo recipeInfo) {
        OverclockCalculator originalPowerCalculator = OverclockCalculator.ofNoOverclock(recipeInfo.recipe)
            .calculate();
        String original_tier_displayed = shouldShowAmperage(originalPowerCalculator) ? ""
            : GTUtility.getTierNameWithParentheses(originalPowerCalculator.getConsumption());
        String original_voltage = GTUtility.formatNumbers(originalPowerCalculator.getConsumption());
        return StatCollector
            .translateToLocalFormatted("GT5U.nei.display.usage.original", original_voltage, original_tier_displayed);
    }

    protected boolean wasOverclocked(OverclockCalculator calculator) {
        return calculator.getPerformedOverclocks() > 0;
    }
}
