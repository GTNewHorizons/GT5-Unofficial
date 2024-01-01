package gtPlusPlus.api.recipe;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.maps.LargeNEIFrontend;
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
        return UIHelper.getGridPositions(
                fluidInputCount,
                xOrigin + tileSize * (maxInputs - fluidInputCount),
                yOrigin,
                maxInputs);
    }

    private static class ThermalBoilerSpecialValueFormatter implements INEISpecialInfoFormatter {

        @Override
        public List<String> format(RecipeDisplayInfo recipeInfo) {
            // TODO: Translation.
            List<String> result = new ArrayList<>();
            result.add("Steam output shown");
            result.add("at maximum efficiency.");

            if (recipeInfo.recipe.mSpecialValue == -1) {
                result.add("Without a Lava Filter,");
                result.add("only Obsidian is produced.");
            }

            return result;
        }
    }
}
