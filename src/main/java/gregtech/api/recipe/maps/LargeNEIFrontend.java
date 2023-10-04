package gregtech.api.recipe.maps;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;

/**
 * Nicely display NEI with many items and fluids. Remember to call
 * If row count >= 6, it doesn't fit in 2 recipes per page, so change it via IMC.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LargeNEIFrontend extends RecipeMapFrontend {

    private static final int xDirMaxCount = 3;
    private static final int yOrigin = 8;

    private final int itemRowCount;
    private final int fluidRowCount;

    public LargeNEIFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder.logoPos(new Pos2d(80, 62)), neiPropertiesBuilder);
        this.itemRowCount = getItemRowCount();
        this.fluidRowCount = getFluidRowCount();
        neiProperties.neiBackgroundSize = new Size(172, 82 + (Math.max(itemRowCount + fluidRowCount - 4, 0)) * 18);
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, 16, yOrigin, xDirMaxCount);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(itemOutputCount, 106, yOrigin, xDirMaxCount);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(fluidInputCount, 16, yOrigin + itemRowCount * 18, xDirMaxCount);
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return UIHelper.getGridPositions(fluidOutputCount, 106, yOrigin + itemRowCount * 18, xDirMaxCount);
    }

    private int getItemRowCount() {
        return (Math.max(uiProperties.maxItemInputs, uiProperties.maxItemOutputs) - 1) / xDirMaxCount + 1;
    }

    private int getFluidRowCount() {
        return (Math.max(uiProperties.maxFluidInputs, uiProperties.maxFluidOutputs) - 1) / xDirMaxCount + 1;
    }
}
