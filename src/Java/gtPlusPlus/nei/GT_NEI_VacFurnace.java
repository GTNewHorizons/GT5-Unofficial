package gtPlusPlus.nei;

import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class GT_NEI_VacFurnace extends GTPP_NEI_DefaultHandler {

	public GT_NEI_VacFurnace() {
		super(GTPP_Recipe_Map.sVacuumFurnaceRecipes);
	}

	@Override
	public TemplateRecipeHandler newInstance() {
		return new GT_NEI_VacFurnace();
	}

	@Override
    public CachedDefaultRecipe createCachedRecipe(GT_Recipe aRecipe) {
    	return new VacFurnaceDefaultRecipe(aRecipe);
    }

	public class VacFurnaceDefaultRecipe extends CachedDefaultRecipe {

		public VacFurnaceDefaultRecipe(final GT_Recipe aRecipe) {
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
			
			//9 Output Slots
			if (mRecipe.getOutput(tStartIndex) != null) {
				this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 101, 4, mRecipe.getOutputChance(tStartIndex)));
			}
			tStartIndex++;
			if (mRecipe.getOutput(tStartIndex) != null) {
				this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 119, 4, mRecipe.getOutputChance(tStartIndex)));
			}
			tStartIndex++;
			if (mRecipe.getOutput(tStartIndex) != null) {
				this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 137, 4, mRecipe.getOutputChance(tStartIndex)));
			}
			tStartIndex++;
			if (mRecipe.getOutput(tStartIndex) != null) {
				this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 101, 22, mRecipe.getOutputChance(tStartIndex)));
			}
			tStartIndex++;
			if (mRecipe.getOutput(tStartIndex) != null) {
				this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 119, 22, mRecipe.getOutputChance(tStartIndex)));
			}
			tStartIndex++;
			if (mRecipe.getOutput(tStartIndex) != null) {
				this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 137, 22, mRecipe.getOutputChance(tStartIndex)));
			}
			tStartIndex++;
			if (mRecipe.getOutput(tStartIndex) != null) {
				this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 101, 40, mRecipe.getOutputChance(tStartIndex)));
			}
			tStartIndex++;
			if (mRecipe.getOutput(tStartIndex) != null) {
				this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 119, 40, mRecipe.getOutputChance(tStartIndex)));
			}
			tStartIndex++;
			if (mRecipe.getOutput(tStartIndex) != null) {
				this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 137, 40, mRecipe.getOutputChance(tStartIndex)));
			}
			tStartIndex++;
			
			if ((mRecipe.mFluidInputs.length > 0) && (mRecipe.mFluidInputs[0] != null) && (mRecipe.mFluidInputs[0].getFluid() != null)) {
				this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[0], true), 12, 60));
				if ((mRecipe.mFluidInputs.length > 1) && (mRecipe.mFluidInputs[1] != null) && (mRecipe.mFluidInputs[1].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[1], true), 30, 60));
				}
			}

			if (mRecipe.mFluidOutputs.length > 0) {
				if ((mRecipe.mFluidOutputs[0] != null) && (mRecipe.mFluidOutputs[0].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[0], true), 101, 60));
				}
				if ((mRecipe.mFluidOutputs.length > 1) && (mRecipe.mFluidOutputs[1] != null) && (mRecipe.mFluidOutputs[1].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[1], true), 119, 60));
				}
			}
		}
	}
}
