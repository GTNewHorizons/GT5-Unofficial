package bwcrossmod.galacticgreg;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.maps.ScrollableOutputFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

/**
 * NEI layout of the Void Miner ore lists, sharing the scrolling output grid of the Eye of Harmony.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class VoidMinerFrontend extends ScrollableOutputFrontend {

    private static final int ITEM_ROWS = 9;
    public static final int MAX_ITEM_OUTPUTS = ITEM_COLUMNS * ITEM_ROWS;

    public VoidMinerFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder.neiSpecialInfoFormatter(new WeightsFormatter()), ITEM_ROWS, 0);
    }

    // A miner's energy use and speed depend on the machine, not on the dimension.
    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {}

    @Override
    protected void drawDurationInfo(RecipeDisplayInfo recipeInfo) {}

    @Override
    protected List<String> handleNEIItemInputTooltip(List<String> currentTip,
        GTNEIDefaultHandler.FixedPositionedStack pStack) {
        VoidMinerRecipes.Weights weights = getWeights(pStack);
        if (weights != null) {
            currentTip.add(
                EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted(
                    "GT5U.nei.voidminer.total_weight",
                    formatNumber(weights.getTotalWeight())));
        }
        return currentTip;
    }

    @Override
    protected List<String> handleNEIItemOutputTooltip(List<String> currentTip,
        GTNEIDefaultHandler.FixedPositionedStack pStack) {
        VoidMinerRecipes.Weights weights = getWeights(pStack);
        if (weights != null) {
            ItemStack ore = pStack.item;
            currentTip.add(
                EnumChatFormatting.GRAY + StatCollector
                    .translateToLocalFormatted("GT5U.nei.voidminer.weight", formatNumber(weights.getWeight(ore))));
            currentTip.add(
                EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted(
                    "GT5U.nei.voidminer.chance",
                    formatNumber(100 * weights.getChance(ore))));
        }
        return currentTip;
    }

    private static VoidMinerRecipes.Weights getWeights(GTNEIDefaultHandler.FixedPositionedStack pStack) {
        return pStack.recipe.mRecipe.mSpecialItems instanceof VoidMinerRecipes.Weights weights ? weights : null;
    }

    private static class WeightsFormatter implements INEISpecialInfoFormatter {

        @Override
        public List<String> format(RecipeDisplayInfo recipeInfo) {
            List<String> result = new ArrayList<>();
            if (recipeInfo.recipe.mSpecialItems instanceof VoidMinerRecipes.Weights weights) {
                result.add(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.nei.voidminer.total_weight",
                        formatNumber(weights.getTotalWeight())));
            }
            return result;
        }
    }
}
