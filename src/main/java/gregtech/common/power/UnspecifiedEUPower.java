package gregtech.common.power;

import static gregtech.api.util.GT_Utility.trans;

import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.nei.NEIRecipeInfo;

public class UnspecifiedEUPower extends EUPower {

    public UnspecifiedEUPower(byte tier, int amperage) {
        super(tier, amperage);
    }

    @Override
    protected void drawNEIDescImpl(NEIRecipeInfo recipeInfo, RecipeMapFrontend frontend) {
        frontend.drawNEIText(recipeInfo, trans("153", "Usage: ") + getVoltageString());
    }
}
