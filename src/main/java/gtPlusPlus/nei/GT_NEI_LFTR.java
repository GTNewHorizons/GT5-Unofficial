package gtPlusPlus.nei;

import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gtPlusPlus.core.util.math.MathUtils;

public class GT_NEI_LFTR extends GT_NEI_MultiNoCell {

    public GT_NEI_LFTR() {
        super(GTPP_Recipe_Map.sLiquidFluorineThoriumReactorRecipes);
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new GT_NEI_LFTR();
    }

    @Override
    public void drawExtras(final int aRecipeIndex) {
        final long tEUt = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mSpecialValue;
        final int tDuration = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mDuration;
        drawText(
                10,
                83,
                "Time: " + (tDuration < 20 ? "< 1" : MathUtils.formatNumbers(0.05d * tDuration)) + " secs",
                -16777216);
        drawText(
                10,
                93,
                this.mRecipeMap.mNEISpecialValuePre
                        + MathUtils.formatNumbers(
                                (((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mSpecialValue
                                        * this.mRecipeMap.mNEISpecialValueMultiplier))
                        + this.mRecipeMap.mNEISpecialValuePost,
                -16777216);
        drawText(10, 103, "Dynamo: " + MathUtils.formatNumbers((long) (tDuration * tEUt)) + " EU", -16777216);
        drawText(10, 113, "Total: " + MathUtils.formatNumbers((long) (tDuration * tEUt * 4)) + " EU", -16777216);
    }
}
