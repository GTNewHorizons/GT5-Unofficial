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
    protected String getTierNameWithParentheses(long voltage, OverclockCalculator calculator) {
        String nameString = super.getTierNameWithParentheses(voltage, calculator);
        if (wasOverclocked(calculator)) {
            nameString = StatCollector.translateToLocal("GT5U.nei.display.overclock") + " " + nameString;
        }
        return nameString;
    }

    /**
     * @return Whole original EU/t usage. Also displays voltage tier if it should be shown.
     */
    // TODO: when porting frontends to mui2, use mui2 Color, not EnumChatFormatting for customization
    protected String getOriginalEUtDisplay(RecipeDisplayInfo recipeInfo) {
        OverclockCalculator originalPowerCalculator = OverclockCalculator.ofNoOverclock(recipeInfo.recipe)
            .calculate();
        String original_tier_displayed = shouldShowAmperage(originalPowerCalculator) ? ""
            : GTUtility.getTierNameWithParentheses(originalPowerCalculator.getConsumption());
        String original_voltage = formatNumber(originalPowerCalculator.getConsumption());
        return StatCollector
            .translateToLocalFormatted("GT5U.nei.display.usage.original", original_voltage, original_tier_displayed);
    }

    protected boolean wasOverclocked(OverclockCalculator calculator) {
        return calculator.getPerformedOverclocks() > 0;
    }
}
