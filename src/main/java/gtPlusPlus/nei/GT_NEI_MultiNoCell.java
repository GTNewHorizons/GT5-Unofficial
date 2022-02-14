package gtPlusPlus.nei;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.math.MathUtils;

public class GT_NEI_MultiNoCell extends GTPP_NEI_DefaultHandler {

	public GT_NEI_MultiNoCell(GT_Recipe_Map aMap) {
		super(aMap);
	}

	@Override
	public TemplateRecipeHandler newInstance() {
		return new GT_NEI_MultiNoCell(mRecipeMap);
	}

	@Override
    public CachedDefaultRecipe createCachedRecipe(GT_Recipe aRecipe) {
    	return new NoCellMultiDefaultRecipe(aRecipe);
    }

	@Override
	public void drawBackground(final int recipe) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(this.getGuiTexture());
		GuiDraw.drawTexturedModalRect(-4, -8, 1, 3, 174, 89);
	}
	
	@Override
	public String getGuiTexture() {
		return RES_PATH_GUI + "basicmachines/FissionFuel.png";
	}

	@Override
	public void drawExtras(final int aRecipeIndex) {
		final long tEUt = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mEUt;
		final int tDuration = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mDuration;
		if (tEUt != 0) {
			drawText(10, 90, "Total: " + MathUtils.formatNumbers((long) (tDuration * tEUt)) + " EU", -16777216);
			drawText(10, 100, "Usage: " + MathUtils.formatNumbers(tEUt) + " EU/t", -16777216);
		}
		if (tDuration > 0) {
			drawText(10, 110, "Time: " + (tDuration < 20 ? "< 1" : MathUtils.formatNumbers(0.05d * tDuration)) + " secs", -16777216);
		}
		if ((GT_Utility.isStringValid(this.mRecipeMap.mNEISpecialValuePre)) || (GT_Utility.isStringValid(this.mRecipeMap.mNEISpecialValuePost))) {
			drawText(10, 120, this.mRecipeMap.mNEISpecialValuePre + MathUtils.formatNumbers((((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mSpecialValue * this.mRecipeMap.mNEISpecialValueMultiplier)) + this.mRecipeMap.mNEISpecialValuePost, -16777216);
		}
	}
	
}
