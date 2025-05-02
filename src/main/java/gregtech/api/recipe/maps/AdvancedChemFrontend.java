package gregtech.api.recipe.maps;

import static gregtech.api.util.GTRecipeConstants.*;
import static gregtech.api.util.GTUtility.formatNumbers;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.RecipeDisplayInfo;

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
        long pressure = recipeInfo.recipe.getMetadataOrDefault(ACR_PRESSURE, 101000);
        long temperature = recipeInfo.recipe.getMetadataOrDefault(ACR_TEMPERATURE, 300);
        if (pressure < 1000) {
            recipeInfo.drawText(
                StatCollector.translateToLocalFormatted("GT5U.nei.pressure", formatNumbers(pressure) + " Pa"));
        } else {
            recipeInfo.drawText(
                StatCollector.translateToLocalFormatted("GT5U.nei.pressure", formatNumbers(pressure / 1000) + " kPa"));
        }
        recipeInfo
            .drawText(StatCollector.translateToLocalFormatted("GT5U.nei.temperature", formatNumbers(temperature)));
    }
}
