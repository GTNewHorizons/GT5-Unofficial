package gregtech.api.recipe.maps;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ThermalBoilerFrontend extends LargeNEIFrontend {

    private static final int tileSize = 18;
    private static final int xOrigin = 16;
    private static final int yOrigin = 8 + tileSize; // Aligned with second row of output items.
    private static final int maxInputs = 3;

    public ThermalBoilerFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder,
            neiPropertiesBuilder.neiSpecialInfoFormatter(new ThermalBoilerSpecialValueFormatter()));
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper
            .getGridPositions(fluidInputCount, xOrigin + tileSize * (maxInputs - fluidInputCount), yOrigin, maxInputs);
    }

    private static class ThermalBoilerSpecialValueFormatter implements INEISpecialInfoFormatter {

        @Override
        public List<String> format(RecipeDisplayInfo recipeInfo) {
            List<String> result = new ArrayList<>();
            result.add(StatCollector.translateToLocal("GT5U.nei.thermal_boiler.steam_output"));

            if (recipeInfo.recipe.mOutputs != null && recipeInfo.recipe.mOutputs.length > 0) {
                result.add(StatCollector.translateToLocal("GT5U.nei.thermal_boiler.without_filter"));
            }

            return result;
        }
    }
}
