package gtPlusPlus.api.recipe;

import static net.minecraft.util.EnumChatFormatting.GRAY;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GT_NEI_DefaultHandler;
import gtPlusPlus.core.util.minecraft.ItemUtils;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ChemicalPlantFrontend extends RecipeMapFrontend {

    public ChemicalPlantFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
            NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, 7, 6, itemInputCount, 1);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(itemOutputCount, 106, 15, 2);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(fluidInputCount, 7, 41, fluidInputCount, 1);
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return UIHelper.getGridPositions(fluidOutputCount, 142, 15, 1, fluidOutputCount);
    }

    @Override
    protected List<String> handleNEIItemInputTooltip(List<String> currentTip,
            GT_NEI_DefaultHandler.FixedPositionedStack pStack) {
        if (ItemUtils.isCatalyst(pStack.item)) {
            currentTip.add(GRAY + "Does not always get consumed in the process");
            currentTip.add(GRAY + "Higher tier pipe casings allow this item to last longer");
        } else {
            super.handleNEIItemInputTooltip(currentTip, pStack);
        }
        return currentTip;
    }

    @Override
    protected void drawNEIOverlayForInput(GT_NEI_DefaultHandler.FixedPositionedStack stack) {
        if (ItemUtils.isCatalyst(stack.item)) {
            drawNEIOverlayText("NC*", stack);
        } else {
            super.drawNEIOverlayForInput(stack);
        }
    }
}
