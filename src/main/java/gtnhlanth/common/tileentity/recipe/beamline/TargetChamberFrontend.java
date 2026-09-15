package gtnhlanth.common.tileentity.recipe.beamline;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.util.StatCollector;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTUtility;
import gregtech.api.util.OverclockCalculator;
import gregtech.nei.RecipeDisplayInfo;

public class TargetChamberFrontend extends RecipeMapFrontend {

    public TargetChamberFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public void drawDescription(RecipeDisplayInfo recipeInfo) {
        drawEnergyInfo(recipeInfo);
        // drawDurationInfo(recipeInfo);
        drawSpecialInfo(recipeInfo);
        drawMetadataInfo(recipeInfo);
        drawRecipeOwnerInfo(recipeInfo);
    }

    @Override
    public void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {
        if (recipeInfo.calculator.getConsumption() <= 0) return;

        // recipeInfo.drawText(trans("152", "Total: ") + getTotalPowerString(recipeInfo.calculator));

        int amperage = recipeInfo.recipeMap.getAmperage();
        recipeInfo.drawText(getEUtDisplay(recipeInfo.calculator, amperage));
        if (amperage != 1) {
            recipeInfo.drawText(getVoltageString(recipeInfo.calculator, amperage));
        }
        recipeInfo.drawText(getAmperageString(amperage));

    }

    // todo: use an OverclockDescriber here
    private String getEUtDisplay(OverclockCalculator calculator, int amperage) {
        String tier = amperage == 1
            ? GTUtility.getTierNameWithParentheses(computeVoltageForEURate(calculator.getConsumption(), amperage))
            : "";
        return StatCollector
            .translateToLocalFormatted("GT5U.nei.display.usage", formatNumber(calculator.getConsumption()), tier);
    }

    private String getVoltageString(OverclockCalculator calculator, int amperage) {
        long voltage = computeVoltageForEURate(calculator.getConsumption(), amperage);
        return StatCollector.translateToLocalFormatted(
            "GT5U.nei.display.voltage",
            formatNumber(voltage),
            GTUtility.getTierNameWithParentheses(voltage));
    }

    private long computeVoltageForEURate(long euPerTick, int amperage) {
        return euPerTick / amperage;
    }

    private String getAmperageString(int amperage) {
        return StatCollector.translateToLocalFormatted("GT5U.nei.display.amperage", formatNumber(amperage));
    }

}
