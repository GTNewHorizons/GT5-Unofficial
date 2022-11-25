package gtPlusPlus.nei;

import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.core.util.math.MathUtils;

public class GT_NEI_MultiSolarTower extends GTPP_NEI_DefaultHandler {

    public GT_NEI_MultiSolarTower(GT_Recipe_Map aMap) {
        super(aMap);
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new GT_NEI_MultiSolarTower(mRecipeMap);
    }

    @Override
    public void drawExtras(final int aRecipeIndex) {
        final int tDuration = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mDuration;
        int y = getDescriptionYOffset();
        if (tDuration > 0) {
            drawText(
                    10,
                    y,
                    "Time: " + (tDuration < 20 ? "< 1" : MathUtils.formatNumbers(0.05d * tDuration)) + " secs",
                    -16777216);
            y += 10;
        }
        drawText(5, y, "Solar Heater rings boost tier", -16777216);
        y += 10;
        drawText(5, y, "R1:T1, R2:T2, R3:T4, R4:T8, R5:T16", -16777216);
        y += 10;
        drawText(5, y, "Input Amount = 1000 x T", -16777216);
    }
}
