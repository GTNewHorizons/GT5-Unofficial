package gtPlusPlus.api.recipe;

import static gregtech.common.gui.modularui.UIHelper.getGridPositions;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AlgaePondFrontend extends RecipeMapFrontend {

    public AlgaePondFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder.progressBarPos(new Pos2d(68, 24)), neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return getGridPositions(itemOutputCount, 96, 6, 4);
    }

}
