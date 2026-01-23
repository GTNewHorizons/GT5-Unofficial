package gtnhintergalactic.recipe.maps;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;
import gtnhintergalactic.recipe.IGRecipeMaps;
import gtnhintergalactic.recipe.SpaceMiningData;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpaceMiningFrontend extends RecipeMapFrontend {

    public SpaceMiningFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder,
            neiPropertiesBuilder.neiSpecialInfoFormatter(new SpaceMiningSpecialValueFormatter()));
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        List<Pos2d> results = new ArrayList<>();
        // assume drones are present on first input
        results.add(new Pos2d(143, 15));
        results.addAll(UIHelper.getGridPositions(itemInputCount - 1, 10, 6, 2));
        return results;
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(itemOutputCount, 69, 6, 4);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(fluidInputCount, 10, 51, fluidInputCount);
    }

    private static class SpaceMiningSpecialValueFormatter implements INEISpecialInfoFormatter {

        @Override
        public List<String> format(RecipeDisplayInfo recipeInfo) {
            List<String> result = new ArrayList<>();
            int recipeTier = recipeInfo.recipe.getMetadataOrDefault(IGRecipeMaps.MODULE_TIER, 1);
            result.add(GTUtility.translate("ig.nei.module", recipeTier));

            SpaceMiningData data = recipeInfo.recipe.getMetadata(IGRecipeMaps.SPACE_MINING_DATA);
            if (data != null) {
                result.add(
                    GTUtility.translate("ig.nei.spacemining.distance") + " "
                        + data.minDistance
                        + "-"
                        + data.maxDistance);
                result.add(GTUtility.translate("ig.nei.spacemining.size") + " " + data.minSize + "-" + data.maxSize);
                result.add(GTUtility.translate("tt.nei.research.min_computation", formatNumber(data.computation)));
                result.add(GTUtility.translate("ig.nei.spacemining.weight") + " " + data.recipeWeight);
            }
            return result;
        }
    }

}
