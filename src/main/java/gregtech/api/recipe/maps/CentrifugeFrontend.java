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
 * The item outputs of the single-block centrifuge scroll when a recipe has more than its output slots.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CentrifugeFrontend extends RecipeMapFrontend {

    private static final int OUTPUT_COLUMNS = 3;

    public CentrifugeFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    /** The position of the first output slot, which depends on how many there are. */
    private Pos2d getOutputOrigin() {
        return UIHelper.getItemOutputPositions(uiProperties.maxItemOutputs)
            .get(0);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        if (itemOutputCount <= uiProperties.maxItemOutputs) return super.getItemOutputPositions(itemOutputCount);
        // Positions beyond the slots only make NEI create the stacks; the scroll area places them
        Pos2d origin = getOutputOrigin();
        return UIHelper.getGridPositions(itemOutputCount, origin.x, origin.y, OUTPUT_COLUMNS, Integer.MAX_VALUE);
    }

    @Override
    public void prepareRecipe(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        Pos2d origin = getOutputOrigin();
        NEIScrollArea.scrollItemOutputs(
            neiCachedRecipe,
            origin.x,
            origin.y,
            OUTPUT_COLUMNS,
            uiProperties.maxItemOutputs / OUTPUT_COLUMNS);
    }
}
