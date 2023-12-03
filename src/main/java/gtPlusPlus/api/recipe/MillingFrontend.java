package gtPlusPlus.api.recipe;

import static net.minecraft.util.EnumChatFormatting.GRAY;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.GT_NEI_DefaultHandler;
import gtPlusPlus.core.util.minecraft.ItemUtils;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MillingFrontend extends RecipeMapFrontend {

    public MillingFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
            NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    protected List<String> handleNEIItemInputTooltip(List<String> currentTip,
            GT_NEI_DefaultHandler.FixedPositionedStack pStack) {
        if (ItemUtils.isMillingBall(pStack.item)) {
            currentTip.add(GRAY + StatCollector.translateToLocal("gtpp.nei.milling.not_consumed"));
        } else {
            super.handleNEIItemInputTooltip(currentTip, pStack);
        }
        return currentTip;
    }

    @Override
    protected void drawNEIOverlayForInput(GT_NEI_DefaultHandler.FixedPositionedStack stack) {
        if (ItemUtils.isMillingBall(stack.item)) {
            drawNEIOverlayText("NC*", stack);
        } else {
            super.drawNEIOverlayForInput(stack);
        }
    }
}
