package gtPlusPlus.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gtPlusPlus.core.item.ModItems;
import java.util.List;
import net.minecraft.item.ItemStack;

public class GT_NEI_MultiTreeGrowthSimulator extends GT_NEI_MultiNoCell {

    public GT_NEI_MultiTreeGrowthSimulator() {
        super(GTPP_Recipe_Map.sTreeSimFakeRecipes);
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new GT_NEI_MultiTreeGrowthSimulator();
    }

    @Override
    public void drawExtras(final int aRecipeIndex) {
        if (ModItems.fluidFertBasic != null) {
            drawText(5, 90, "Chance of Sapling output if", -16777216);
            drawText(5, 100, "" + ModItems.fluidFertBasic.getLocalizedName() + " is provided.", -16777216);
            drawText(5, 110, "This is optional.", -16777216);
        }
    }

    @Override
    public List<String> handleItemTooltip(
            final GuiRecipe<?> gui, final ItemStack aStack, final List<String> currenttip, final int aRecipeIndex) {
        final TemplateRecipeHandler.CachedRecipe tObject = this.arecipes.get(aRecipeIndex);
        if (tObject instanceof CachedDefaultRecipe) {
            final CachedDefaultRecipe tRecipe = (CachedDefaultRecipe) tObject;
            for (final PositionedStack tStack : tRecipe.mOutputs) {
                if (aStack == tStack.item) {
                    if ((!(tStack instanceof FixedPositionedStack))
                            || (((FixedPositionedStack) tStack).mChance <= 0)
                            || (((FixedPositionedStack) tStack).mChance == 10000)) {
                        break;
                    }
                    if (ModItems.fluidFertBasic != null) {
                        currenttip.add(
                                "Chance output if " + ModItems.fluidFertBasic.getLocalizedName() + " is provided.");
                    }
                    break;
                }
            }
        }
        return currenttip;
    }
}
