package gregtech.api.recipe.maps;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

import codechicken.nei.PositionedStack;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.NEIScrollArea;

/**
 * NEI layout for recipes with a trigger item and a long list of item outputs, such as the Eye of Harmony and the Void
 * Miner: the item outputs scroll, an optional fluid output grid sits below them.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class ScrollableOutputFrontend extends RecipeMapFrontend {

    public static final int ITEM_COLUMNS = 8;
    public static final int FLUID_COLUMNS = 8;

    protected static final int ORIGIN_Y = 8;
    // The background is drawn this far inside the coordinates slots are placed in, which the centering has to include
    private static final int BACKGROUND_OFFSET = 3;
    private static final int BACKGROUND_WIDTH = 170;
    private static final int ITEM_GRID_X = centeredX(ITEM_COLUMNS * 18);
    private static final int FLUID_GRID_X = centeredX(FLUID_COLUMNS * 18);
    private static final int TRIGGER_X = centeredX(18);
    private static final int ITEM_GRID_Y = ORIGIN_Y + 36;

    private final int itemRows;
    private final int fluidRows;

    /**
     * @param itemRows visible rows of item outputs; the recipe map needs {@code ITEM_COLUMNS * itemRows} item output
     *                 slots
     */
    protected ScrollableOutputFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder, int itemRows, int fluidRows) {
        super(
            uiPropertiesBuilder.logoPos(new Pos2d(8, ORIGIN_Y)),
            // The space under the last slot is the same as the one above the trigger item
            neiPropertiesBuilder.recipeBackgroundSize(
                new Size(
                    BACKGROUND_WIDTH,
                    ITEM_GRID_Y + (itemRows + fluidRows) * 18 + ORIGIN_Y - 2 * BACKGROUND_OFFSET)));
        this.itemRows = itemRows;
        this.fluidRows = fluidRows;
    }

    private static int centeredX(int width) {
        return BACKGROUND_OFFSET + (BACKGROUND_WIDTH - width) / 2;
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, TRIGGER_X, ORIGIN_Y, 1, 1);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        // Positions beyond the visible slots only make NEI create the stacks; the scroll area places them
        return UIHelper.getGridPositions(itemOutputCount, ITEM_GRID_X, ITEM_GRID_Y, ITEM_COLUMNS, Integer.MAX_VALUE);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(fluidInputCount, 0, 0, 0, 0);
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return UIHelper
            .getGridPositions(fluidOutputCount, FLUID_GRID_X, ITEM_GRID_Y + itemRows * 18, FLUID_COLUMNS, fluidRows);
    }

    @Override
    public void prepareRecipe(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        List<PositionedStack> itemOutputs = new ArrayList<>();
        for (PositionedStack stack : neiCachedRecipe.mOutputs) {
            if (stack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed && !fixed.isFluid()) {
                itemOutputs.add(stack);
            }
        }
        neiCachedRecipe
            .addScrollArea(new NEIScrollArea(ITEM_GRID_X, ITEM_GRID_Y, ITEM_COLUMNS, itemRows).setStacks(itemOutputs));
    }
}
