package gtPlusPlus.nei;

import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class GT_NEI_RFPP extends GTPP_NEI_DefaultHandler {

	public GT_NEI_RFPP() {
		super(GTPP_Recipe_Map.sFissionFuelProcessing);
	}

	@Override
	public TemplateRecipeHandler newInstance() {
		return new GT_NEI_RFPP();
	}

	@Override
    public CachedDefaultRecipe createCachedRecipe(GT_Recipe aRecipe) {
    	return new FFPPDefaultRecipe(aRecipe);
    }

	public class FFPPDefaultRecipe extends CachedDefaultRecipe {

		public FFPPDefaultRecipe(final GT_Recipe aRecipe) {
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
			if (mRecipe.getOutput(tStartIndex) != null) {
				this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, 5, mRecipe.getOutputChance(tStartIndex)));
			}
			tStartIndex++;
			if (mRecipe.getOutput(tStartIndex) != null) {
				this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 120, 5, mRecipe.getOutputChance(tStartIndex)));
			}
			tStartIndex++;
			if (mRecipe.getOutput(tStartIndex) != null) {
				this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, 23, mRecipe.getOutputChance(tStartIndex)));
			}
			tStartIndex++;
			if (mRecipe.getOutput(tStartIndex) != null) {
				this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 120, 23, mRecipe.getOutputChance(tStartIndex)));
			}
			tStartIndex++;			
			
			//New fluid display behaviour when 3 fluid inputs are detected. (Basically a mix of the code below for outputs an the code above for 9 input slots.)
			if (mRecipe.mFluidInputs.length > 2) {
				if ((mRecipe.mFluidInputs[0] != null) && (mRecipe.mFluidInputs[0].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[0], true), 12, 5));
				}
				if ((mRecipe.mFluidInputs[1] != null) && (mRecipe.mFluidInputs[1].getFluid() != null)) {
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
			//Returns to old behaviour if fluid inputs < 3
			else if ((mRecipe.mFluidInputs.length > 0) && (mRecipe.mFluidInputs[0] != null) && (mRecipe.mFluidInputs[0].getFluid() != null)) {
				this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[0], true), 48, 52));
				if ((mRecipe.mFluidInputs.length == 2) && (mRecipe.mFluidInputs[1] != null) && (mRecipe.mFluidInputs[1].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[1], true), 30, 52));
				}
			}

			if (mRecipe.mFluidOutputs.length > 1) {
				if ((mRecipe.mFluidOutputs[0] != null) && (mRecipe.mFluidOutputs[0].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[0], true), 102, 5));
				}
				if ((mRecipe.mFluidOutputs[1] != null) && (mRecipe.mFluidOutputs[1].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[1], true), 120, 5));
				}
				if ((mRecipe.mFluidOutputs.length > 2) && (mRecipe.mFluidOutputs[2] != null) && (mRecipe.mFluidOutputs[2].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[2], true), 138, 5));
				}
				if ((mRecipe.mFluidOutputs.length > 3) && (mRecipe.mFluidOutputs[3] != null) && (mRecipe.mFluidOutputs[3].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[3], true), 102, 23));
				}
				if ((mRecipe.mFluidOutputs.length > 4) && (mRecipe.mFluidOutputs[4] != null) && (mRecipe.mFluidOutputs[4].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[4], true), 120, 23));
				}
				if ((mRecipe.mFluidOutputs.length > 5) && (mRecipe.mFluidOutputs[5] != null) && (mRecipe.mFluidOutputs[5].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[5], true), 138, 23));
				}
				if ((mRecipe.mFluidOutputs.length > 6) && (mRecipe.mFluidOutputs[6] != null) && (mRecipe.mFluidOutputs[6].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[6], true), 102, 41));
				}
				if ((mRecipe.mFluidOutputs.length > 7) && (mRecipe.mFluidOutputs[7] != null) && (mRecipe.mFluidOutputs[7].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[7], true), 120, 41));
				}
				if ((mRecipe.mFluidOutputs.length > 8) && (mRecipe.mFluidOutputs[8] != null) && (mRecipe.mFluidOutputs[8].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[8], true), 138, 41));
				}
			} else if ((mRecipe.mFluidOutputs.length > 0) && (mRecipe.mFluidOutputs[0] != null) && (mRecipe.mFluidOutputs[0].getFluid() != null)) {
				this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[0], true), 102, 5));
			}
		}
	}
}
