package gregtech.api.recipe.maps;

import static gregtech.api.util.GTRecipeConstants.DIMENSION_NAME;
import static gregtech.api.util.GTRecipeConstants.PLANET_TIER;
import static gregtech.api.util.GTUtility.formatNumbers;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import codechicken.nei.PositionedStack;
import gregtech.api.enums.DimensionConditions;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PlanetConditionsFrontend extends RecipeMapFrontend {

    public PlanetConditionsFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(1, 8, 8, 1, 1);
    }

    private void drawPlanetCondition(String label, String value, int y, float fontScale, int xOffset, int colonOffset) {
        drawNEIOverlayText(
            label,
            new PositionedStack(Materials.Acetone.getCells(1), 27 + xOffset, y, false),
            0x3f3f3f,
            fontScale,
            false,
            Alignment.CenterLeft);
        drawNEIOverlayText(
            ":",
            new PositionedStack(Materials.Acetone.getCells(1), 27 + 18 * 4 + xOffset + colonOffset, y, false),
            0x3f3f3f,
            fontScale,
            false,
            Alignment.CenterLeft);
        drawNEIOverlayText(
            value,
            new PositionedStack(Materials.Acetone.getCells(1), 27 + 18 * 4 + 6 + xOffset + colonOffset, y, false),
            0xffffff,
            fontScale,
            true,
            Alignment.CenterLeft);
    }

    private String formatPressure(int pressure) {
        if (pressure < 1e3) return formatNumbers(pressure) + " Pa";
        if (pressure < 1e6) return formatNumbers(pressure / 1e3) + " kPa";
        return formatNumbers(pressure / 1e6) + " MPa";
    }

    @Override
    protected void drawDurationInfo(RecipeDisplayInfo recipeInfo) {
        int yOffset = -6, yStep = 10;
        float fontScale = 1f;

        String displayName = recipeInfo.recipe.mInputs[0].getDisplayName();
        displayName = (displayName != null) ? displayName.replaceFirst("^T[1-9]0?:\\s*", "") : "";

        int planetTier = recipeInfo.recipe.getMetadataOrDefault(PLANET_TIER, 0);
        String dimName = recipeInfo.recipe.getMetadata(DIMENSION_NAME);

        var conditions = DimensionConditions.fromDimensionName(dimName);
        int temperature = conditions.getAmbientTemp();
        int pressure = conditions.getAmbientPressure();

        drawPlanetCondition(
            StatCollector.translateToLocalFormatted("GT5U.nei.bare_name"),
            displayName,
            yOffset,
            fontScale,
            0,
            -24);
        drawPlanetCondition(
            StatCollector.translateToLocalFormatted("GT5U.nei.bare_tier"),
            formatNumbers(planetTier),
            yOffset + yStep,
            fontScale,
            0,
            -24);
        drawPlanetCondition(
            StatCollector.translateToLocalFormatted("GT5U.nei.bare_pressure"),
            formatPressure(pressure),
            yOffset + 2 * yStep,
            fontScale,
            -24,
            0);
        drawPlanetCondition(
            StatCollector.translateToLocalFormatted("GT5U.nei.bare_temperature"),
            formatNumbers(temperature) + " K",
            yOffset + 3 * yStep,
            fontScale,
            -24,
            0);
    }
}
