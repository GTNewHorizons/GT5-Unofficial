package gregtech.api.recipe.maps;

import static gregtech.api.util.GTRecipeConstants.EU_MULTIPLIER;
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
public class AdvancedChemFrontend extends RecipeMapFrontend {

    public AdvancedChemFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
                                           NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, 8, 8, 4,2);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int fluidOutputCount) {
        return UIHelper.getGridPositions(fluidOutputCount, 96, 8, 4,2);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(fluidInputCount, 8, 44, 4, 2);
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return UIHelper.getGridPositions(fluidOutputCount, 96, 44, 4,2);
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {
        // These look odd because recipeInfo.recipe.mEUt is actually the EU per litre of fluid processed, not
        // the EU/t.
        long multiplier = recipeInfo.recipe.getMetadataOrDefault();
        recipeInfo.drawText(
            GTUtility.trans("152", "Pressure: ")
                + formatNumbers(multiplier * recipeInfo.recipe.mDuration * recipeInfo.recipe.mEUt)
                + " EU");
    }
}
