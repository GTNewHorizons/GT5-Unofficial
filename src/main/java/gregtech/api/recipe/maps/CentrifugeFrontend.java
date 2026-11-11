package gregtech.api.recipe.maps;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.NEIScrollArea;

/**
 * The item outputs of the single-block centrifuge scroll when a recipe has more than its six output slots.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CentrifugeFrontend extends RecipeMapFrontend {

    private static final int OUTPUT_X = 106, OUTPUT_Y = 15, OUTPUT_COLUMNS = 3, OUTPUT_ROWS = 2;

    public CentrifugeFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        if (itemOutputCount <= OUTPUT_COLUMNS * OUTPUT_ROWS) return super.getItemOutputPositions(itemOutputCount);
        // Positions beyond the slots only make NEI create the stacks; the scroll area places them
        return UIHelper.getGridPositions(itemOutputCount, OUTPUT_X, OUTPUT_Y, OUTPUT_COLUMNS, Integer.MAX_VALUE);
    }

    @Override
    public void prepareRecipe(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        NEIScrollArea.scrollItemOutputs(neiCachedRecipe, OUTPUT_X, OUTPUT_Y, OUTPUT_COLUMNS, OUTPUT_ROWS);
    }
}
