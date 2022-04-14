package gtPlusPlus.nei;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.math.MathUtils;

public class GT_NEI_LFTR extends GTPP_NEI_DefaultHandler {

	public GT_NEI_LFTR() {
		super(GTPP_Recipe_Map.sLiquidFluorineThoriumReactorRecipes);
	}
	
	@Override
	public TemplateRecipeHandler newInstance() {
		return new GT_NEI_LFTR();
	}

	@Override
    public CachedDefaultRecipe createCachedRecipe(GT_Recipe aRecipe) {
    	return new LFTRDefaultRecipe(aRecipe);
    }
	
	@Override
	public void drawBackground(final int recipe) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(this.getGuiTexture());
		GuiDraw.drawTexturedModalRect(-4, -8, 1, 3, 174, 89);
	}

	@Override
	public void drawExtras(final int aRecipeIndex) {
		final long tEUt = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mSpecialValue;
		final int tDuration = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mDuration;
		drawText(10, 83, "Time: " + (tDuration < 20 ? "< 1" : MathUtils.formatNumbers(0.05d * tDuration)) + " secs", -16777216);
		drawText(10, 93, this.mRecipeMap.mNEISpecialValuePre + MathUtils.formatNumbers((((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mSpecialValue * this.mRecipeMap.mNEISpecialValueMultiplier)) + this.mRecipeMap.mNEISpecialValuePost, -16777216);
		drawText(10, 103, "Dynamo: " + MathUtils.formatNumbers((long) (tDuration * tEUt)) + " EU", -16777216);
		drawText(10, 113, "Total: " + MathUtils.formatNumbers((long) (tDuration * tEUt * 4)) + " EU", -16777216);
	}

	public class LFTRDefaultRecipe extends CachedDefaultRecipe {

		public LFTRDefaultRecipe(final GT_Recipe aRecipe) {
			super(aRecipe);
		}

		@Override
		public void handleSlots() {
			int tStartIndex = 0;
			if (mRecipe.mFluidInputs.length > 0) {
				if ((mRecipe.mFluidInputs[0] != null) && (mRecipe.mFluidInputs[0].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[0], true), 12, 5));
				}
				if ((mRecipe.mFluidInputs.length > 1) && (mRecipe.mFluidInputs[1] != null) && (mRecipe.mFluidInputs[1].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[1], true), 30, 5));
				}
				if ((mRecipe.mFluidInputs.length > 2) && (mRecipe.mFluidInputs[2] != null) && (mRecipe.mFluidInputs[2].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[2], true), 48, 5));
				}
				if ((mRecipe.mFluidInputs.length > 3) && (mRecipe.mFluidInputs[3] != null) && (mRecipe.mFluidInputs[3].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[3], true), 12, 23));
				}
				if ((mRecipe.mFluidInputs.length > 4) && (mRecipe.mFluidInputs[4] != null) && (mRecipe.mFluidInputs[4].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[4], true), 30, 23));
				}
				if ((mRecipe.mFluidInputs.length > 5) && (mRecipe.mFluidInputs[5] != null) && (mRecipe.mFluidInputs[5].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[5], true), 48, 23));
				}
				if ((mRecipe.mFluidInputs.length > 6) && (mRecipe.mFluidInputs[6] != null) && (mRecipe.mFluidInputs[6].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[6], true), 12, 41));
				}
				if ((mRecipe.mFluidInputs.length > 7) && (mRecipe.mFluidInputs[7] != null) && (mRecipe.mFluidInputs[7].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[7], true), 30, 41));
				}
				if ((mRecipe.mFluidInputs.length > 8) && (mRecipe.mFluidInputs[8] != null) && (mRecipe.mFluidInputs[8].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[8], true), 48, 41));
				}
			}

			tStartIndex = 0;
			boolean tUnificate = mRecipeMap.mNEIUnificateOutput;
			if (mRecipe.mFluidOutputs.length > 0) {
				if ((mRecipe.mFluidOutputs[0] != null) && (mRecipe.mFluidOutputs[0].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[0], true), 102, 5, mRecipe.getOutputChance(tStartIndex++), tUnificate));
				}
				if ((mRecipe.mFluidOutputs.length > 1) && (mRecipe.mFluidOutputs[1] != null) && (mRecipe.mFluidOutputs[1].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[1], true), 120, 5, mRecipe.getOutputChance(tStartIndex++), tUnificate));
				}
				if ((mRecipe.mFluidOutputs.length > 2) && (mRecipe.mFluidOutputs[2] != null) && (mRecipe.mFluidOutputs[2].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[2], true), 138, 5, mRecipe.getOutputChance(tStartIndex++), tUnificate));
				}
				if ((mRecipe.mFluidOutputs.length > 3) && (mRecipe.mFluidOutputs[3] != null) && (mRecipe.mFluidOutputs[3].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[3], true), 102, 23, mRecipe.getOutputChance(tStartIndex++), tUnificate));
				}
				if ((mRecipe.mFluidOutputs.length > 4) && (mRecipe.mFluidOutputs[4] != null) && (mRecipe.mFluidOutputs[4].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[4], true), 120, 23, mRecipe.getOutputChance(tStartIndex++), tUnificate));
				}
				if ((mRecipe.mFluidOutputs.length > 5) && (mRecipe.mFluidOutputs[5] != null) && (mRecipe.mFluidOutputs[5].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[5], true), 138, 23, mRecipe.getOutputChance(tStartIndex++), tUnificate));
				}
				if ((mRecipe.mFluidOutputs.length > 6) && (mRecipe.mFluidOutputs[6] != null) && (mRecipe.mFluidOutputs[6].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[6], true), 102, 41, mRecipe.getOutputChance(tStartIndex++), tUnificate));
				}
				if ((mRecipe.mFluidOutputs.length > 7) && (mRecipe.mFluidOutputs[7] != null) && (mRecipe.mFluidOutputs[7].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[7], true), 120, 41, mRecipe.getOutputChance(tStartIndex++), tUnificate));
				}
				if ((mRecipe.mFluidOutputs.length > 8) && (mRecipe.mFluidOutputs[8] != null) && (mRecipe.mFluidOutputs[8].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[8], true), 138, 41, mRecipe.getOutputChance(tStartIndex++), tUnificate));
				}
			}
			
		}
		
	}
}
