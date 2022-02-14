package gtPlusPlus.nei;

import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.core.util.math.MathUtils;

public class GT_NEI_MolecularTransformer extends GTPP_NEI_DefaultHandler {

	public GT_NEI_MolecularTransformer() {
		super(GTPP_Recipe_Map.sMolecularTransformerRecipes);
	}
	
	@Override
	public TemplateRecipeHandler newInstance() {
		return new GT_NEI_MolecularTransformer();
	}

	@Override
	public void drawExtras(final int aRecipeIndex) {
		GT_Recipe aRecipe = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe;
		final long tEUt = aRecipe.mEUt;
		final long tDuration = aRecipe.mDuration;
		if (tEUt != 0) {
			drawText(10, 73, "Total: " + MathUtils.formatNumbers((long) (tDuration * tEUt)) + " EU", -16777216);
			drawText(10, 83, "Usage: " + MathUtils.formatNumbers(tEUt * aRecipe.mSpecialValue) + " EU/t", -16777216);
			drawText(10, 93, "Voltage: " + MathUtils.formatNumbers(tEUt) + " EU", -16777216);
			drawText(10, 103, "Amperage: " + aRecipe.mSpecialValue, -16777216);			
		}
		if (tDuration > 0) {
			drawText(10, 113, "Time: " + (tDuration < 20 ? "< 1" : MathUtils.formatNumbers(0.05d * tDuration)) + " secs", -16777216);
		}		
	}

}
