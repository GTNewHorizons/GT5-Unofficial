package gtPlusPlus.api.recipe;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static gregtech.common.gui.modularui.UIHelper.getGridPositions;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AlgaePondFrontend extends RecipeMapFrontend {
    public AlgaePondFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder, NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder.progressBarPos(new Pos2d(68, 24)), neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return getGridPositions(itemOutputCount, 96, 6, 4);
    }

}


