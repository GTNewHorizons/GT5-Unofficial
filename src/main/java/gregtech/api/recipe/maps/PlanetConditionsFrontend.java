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
import gregtech.api.enums.Materials;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.common.tileentities.machines.multi.MTEEnvironmentallyControlledChemicalFacility;
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

    private void drawPlanetConditions(String text, String text_value, int height, float fontScale, int xOffset,
        int colonOffset) {
        drawNEIOverlayText(
            text,
            new PositionedStack(Materials.Acetone.getCells(1), 27 + xOffset, height, false),
            0x3f3f3f,
            fontScale,
            false,
            Alignment.CenterLeft);

        drawNEIOverlayText(
            ":",
            new PositionedStack(Materials.Acetone.getCells(1), 27 + 18 * 4 + xOffset + colonOffset, height, false),
            0x3f3f3f,
            fontScale,
            false,
            Alignment.CenterLeft);

        drawNEIOverlayText(
            text_value,
            new PositionedStack(Materials.Acetone.getCells(1), 27 + 18 * 4 + 6 + xOffset + colonOffset, height, false),
            0xffffff,
            fontScale,
            true,
            Alignment.CenterLeft);
    }

    @Override
    protected void drawDurationInfo(RecipeDisplayInfo recipeInfo) {
        int yOffset = -6;
        int yStepOffset = 10;
        float fontScale = 1f;
        String displayName = recipeInfo.recipe.mInputs[0].getDisplayName();
        displayName = (displayName != null) ? displayName.replaceFirst("^T[1-9]0?:\\s*", "") : "";
        int planetTier = recipeInfo.recipe.getMetadataOrDefault(PLANET_TIER, 0);
        String dimName = recipeInfo.recipe.getMetadata(DIMENSION_NAME);
        int temperature = MTEEnvironmentallyControlledChemicalFacility.DimensionConditions.fromDimensionName(dimName)
            .getAmbientTemp();
        int pressure = MTEEnvironmentallyControlledChemicalFacility.DimensionConditions.fromDimensionName(dimName)
            .getAmbientPressure();

        drawPlanetConditions(
            StatCollector.translateToLocalFormatted("GT5U.nei.bare_name"),
            displayName,
            yOffset,
            fontScale,
            0,
            -24);

        drawPlanetConditions(
            StatCollector.translateToLocalFormatted("GT5U.nei.bare_tier"),
            formatNumbers(planetTier),
            yOffset + yStepOffset,
            fontScale,
            0,
            -24);

        if (pressure < 1e3) {
            drawPlanetConditions(
                StatCollector.translateToLocalFormatted("GT5U.nei.bare_pressure"),
                formatNumbers(pressure) + " Pa",
                yOffset + 2 * yStepOffset,
                fontScale,
                -24,
                0);
        } else if (pressure < 1e6) {
            drawPlanetConditions(
                StatCollector.translateToLocalFormatted("GT5U.nei.bare_pressure"),
                formatNumbers(pressure / 1e3) + " kPa",
                yOffset + 2 * yStepOffset,
                fontScale,
                -24,
                0);
        } else {
            drawPlanetConditions(
                StatCollector.translateToLocalFormatted("GT5U.nei.bare_pressure"),
                formatNumbers(pressure / 1e6) + " MPa",
                yOffset + 2 * yStepOffset,
                fontScale,
                -24,
                0);
        }

        drawPlanetConditions(
            StatCollector.translateToLocalFormatted("GT5U.nei.bare_temperature"),
            formatNumbers(temperature) + " K",
            yOffset + 3 * yStepOffset,
            fontScale,
            -24,
            0);
    }
}
