package gregtech.api.recipe.maps;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BECCreationFrontend extends RecipeMapFrontend {

    public BECCreationFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    protected void drawDurationInfo(RecipeDisplayInfo recipeInfo) {
        recipeInfo.drawText(
            GTUtility.translate(
                "GT5U.gui.text.entanglement-duration",
                NumberFormatUtil.formatNumber(recipeInfo.recipe.mDuration)));
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {
        recipeInfo.drawText(
            GTUtility
                .translate("GT5U.gui.text.bec-quota-required", NumberFormatUtil.formatEnergy(recipeInfo.recipe.mEUt)));
    }
}
