package gregtech.api.recipe.maps;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.RecipeDisplayInfo;
import net.minecraft.util.StatCollector;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static gregtech.api.util.GTRecipeConstants.ECCF_PRESSURE;
import static gregtech.api.util.GTRecipeConstants.ECCF_TEMPERATURE;
import static gregtech.api.util.GTUtility.formatNumbers;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AdvancedChemFrontend extends RecipeMapFrontend {

    public AdvancedChemFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, 8, 8, 4, 2);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int fluidOutputCount) {
        return UIHelper.getGridPositions(fluidOutputCount, 96, 8, 4, 2);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(fluidInputCount, 8, 44, 4, 2);
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return UIHelper.getGridPositions(fluidOutputCount, 96, 44, 4, 2);
    }

    @Override
    protected void drawDurationInfo(RecipeDisplayInfo recipeInfo) {
        long pressure = recipeInfo.recipe.getMetadataOrDefault(ECCF_PRESSURE, 101000);
        long temperature = recipeInfo.recipe.getMetadataOrDefault(ECCF_TEMPERATURE, 300);
        double rangePressure = (int) (1.5 * Math.pow(pressure, 0.55));
        double rangeTemp = (int) (1.5 * Math.pow(temperature, 0.55));
        if (pressure < 10000) {
            recipeInfo.drawText(
                StatCollector.translateToLocalFormatted("GT5U.nei.pressure", formatNumbers(pressure + rangePressure) + " - " + formatNumbers(pressure - rangePressure) + " Pa"));
        }
        else {
            recipeInfo.drawText(
                StatCollector.translateToLocalFormatted("GT5U.nei.pressure", formatNumbers((int) ((pressure + rangePressure)/1000)) + " - " + formatNumbers((int)((pressure - rangePressure)/1000)) + " kPa"));
        }
        recipeInfo
            .drawText(StatCollector.translateToLocalFormatted("GT5U.nei.temperature", formatNumbers(temperature + rangeTemp) + " - " + formatNumbers(temperature - rangeTemp)));
    }
}
