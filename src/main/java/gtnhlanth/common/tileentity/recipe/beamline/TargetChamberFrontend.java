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

        recipeInfo.drawText(getEUtDisplay(recipeInfo.calculator));
        recipeInfo.drawText(getVoltageString(recipeInfo.calculator));
        recipeInfo.drawText(getAmperageString(recipeInfo.calculator));

    }

    // todo: use an OverclockDescriber here
    private String getEUtDisplay(OverclockCalculator calculator) {
        return StatCollector
            .translateToLocalFormatted("GT5U.nei.display.usage", formatNumber(calculator.getConsumption()), "");
    }

    private String getVoltageString(OverclockCalculator calculator) {
        long voltage = computeVoltageForEURate(calculator.getConsumption());
        return StatCollector.translateToLocalFormatted(
            "GT5U.nei.display.voltage",
            formatNumber(voltage),
            GTUtility.getTierNameWithParentheses(voltage));
    }

    private long computeVoltageForEURate(long euPerTick) {
        return euPerTick;
    }

    private String getAmperageString(OverclockCalculator calculator) {
        return StatCollector.translateToLocalFormatted("GT5U.nei.display.amperage", formatNumber(1));
    }

}
