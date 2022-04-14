package gtPlusPlus.nei;

import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class GT_NEI_FlotationCell extends GTPP_NEI_DefaultHandler {

	public GT_NEI_FlotationCell() {
		super(GTPP_Recipe_Map.sFlotationCellRecipes);
	}

	@Override
	public TemplateRecipeHandler newInstance() {
		return new GT_NEI_FlotationCell();
	}

	@Override
    public CachedDefaultRecipe createCachedRecipe(GT_Recipe aRecipe) {
    	return new FlotationCellDefaultRecipe(aRecipe);
    }

	public class FlotationCellDefaultRecipe extends CachedDefaultRecipe {

		public FlotationCellDefaultRecipe(final GT_Recipe aRecipe) {
			super(aRecipe);
		}

		@Override
		public void handleSlots() {
			int tStartIndex = 0;

			if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
				this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 12, 5));
			}
			tStartIndex++;
			if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
				this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 30, 5));
			}
			tStartIndex++;
			if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
				this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 48, 5));
			}
			tStartIndex++;
			if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
				this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 12, 23));
			}
			tStartIndex++;
			if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
				this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 30, 23));
			}
			tStartIndex++;
			if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
				this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 48, 23));
			}
			tStartIndex++;		
			
			if (mRecipe.mSpecialItems != null) {
				this.mInputs.add(new FixedPositionedStack(mRecipe.mSpecialItems, 120, 52));
			}
			tStartIndex = 0;
			
			//Four Output Slots
			boolean tUnificate = mRecipeMap.mNEIUnificateOutput;
			if (mRecipe.getOutput(tStartIndex) != null) {
				this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, 5, mRecipe.getOutputChance(tStartIndex), tUnificate));
			}
			tStartIndex++;
			if (mRecipe.getOutput(tStartIndex) != null) {
				this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 120, 5, mRecipe.getOutputChance(tStartIndex), tUnificate));
			}
			tStartIndex++;
			if (mRecipe.getOutput(tStartIndex) != null) {
				this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, 23, mRecipe.getOutputChance(tStartIndex), tUnificate));
			}
			tStartIndex++;
			if (mRecipe.getOutput(tStartIndex) != null) {
				this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 120, 23, mRecipe.getOutputChance(tStartIndex), tUnificate));
			}
			tStartIndex++;			
			
			if ((mRecipe.mFluidInputs.length > 0) && (mRecipe.mFluidInputs[0] != null) && (mRecipe.mFluidInputs[0].getFluid() != null)) {
				this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[0], true), 30, 52));
				if ((mRecipe.mFluidInputs.length > 1) && (mRecipe.mFluidInputs[1] != null) && (mRecipe.mFluidInputs[1].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[1], true), 48, 52));
				}
			}

			if (mRecipe.mFluidOutputs.length > 0) {
				if ((mRecipe.mFluidOutputs[0] != null) && (mRecipe.mFluidOutputs[0].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[0], true), 102, 52, tUnificate));
				}
				if ((mRecipe.mFluidOutputs.length > 1) && (mRecipe.mFluidOutputs[1] != null) && (mRecipe.mFluidOutputs[1].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[1], true), 120, 52, tUnificate));
				}
			}
		}
		
	}
}
