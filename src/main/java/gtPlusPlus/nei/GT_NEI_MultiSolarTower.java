package gtPlusPlus.nei;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.core.util.math.MathUtils;

public class GT_NEI_MultiSolarTower extends GT_NEI_MultiNoCell {

	public GT_NEI_MultiSolarTower(GT_Recipe_Map aMap) {
		super(aMap);
	}

	@Override
	public TemplateRecipeHandler newInstance() {
		return new GT_NEI_MultiSolarTower(mRecipeMap);
	}
	
	@Override
	public String getGuiTexture() {
		return RES_PATH_GUI + "basicmachines/FissionFuel.png";
	}

	@Override
	public void drawExtras(final int aRecipeIndex) {
		final int tDuration = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mDuration;
		if (tDuration > 0) {
			drawText(10, 90, "Time: " + (tDuration < 20 ? "< 1" : MathUtils.formatNumbers(Integer.valueOf(tDuration / 20))) + " secs", -16777216);
		}
		drawText(5, 100, "Solar Heater rings boost tier", -16777216);
		drawText(5, 110, "R1:T1, R2:T2, R3:T4, R4:T8, R5:T16", -16777216);
		drawText(5, 120, "Input Amount = 1000 x T", -16777216);		
	}
	
}
