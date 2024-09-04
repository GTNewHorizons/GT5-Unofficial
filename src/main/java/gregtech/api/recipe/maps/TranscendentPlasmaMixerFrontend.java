package gregtech.api.recipe.maps;

import static gregtech.api.util.GTUtility.formatNumbers;

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

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TranscendentPlasmaMixerFrontend extends RecipeMapFrontend {

    public TranscendentPlasmaMixerFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, 60, 8, 1);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(fluidInputCount, 6, 26, 4, 5);
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return UIHelper.getGridPositions(fluidOutputCount, 114, 44, 1);
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {
        // These look odd because recipeInfo.recipe.mEUt is actually the EU per litre of fluid processed, not
        // the EU/t.
        recipeInfo.drawText(
            GTUtility.trans("152", "Total: ")
                + formatNumbers(1000L * recipeInfo.recipe.mDuration / 100L * recipeInfo.recipe.mEUt)
                + " EU");
        // 1000 / (20 ticks * 5 seconds) = 10L/t. 10L/t * x EU/L = 10 * x EU/t.
        long averageUsage = 10L * recipeInfo.recipe.mEUt;
        recipeInfo.drawText(
            "Average: " + formatNumbers(averageUsage) + " EU/t" + GTUtility.getTierNameWithParentheses(averageUsage));
    }
}
