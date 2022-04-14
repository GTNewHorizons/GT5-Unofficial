package gtPlusPlus.nei;

import java.util.List;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.item.ItemStack;

public class GT_NEI_MillingMachine extends GTPP_NEI_DefaultHandler {

	public GT_NEI_MillingMachine() {
		super(GTPP_Recipe_Map.sOreMillRecipes);
	}

	@Override
	public TemplateRecipeHandler newInstance() {
		return new GT_NEI_MillingMachine();
	}

	@Override
    public CachedDefaultRecipe createCachedRecipe(GT_Recipe aRecipe) {
    	return new MillingDefaultRecipe(aRecipe);
    }
	
	@Override
	public List<String> handleItemTooltip(final GuiRecipe gui, final ItemStack aStack, final List<String> currenttip, final int aRecipeIndex) {
		final TemplateRecipeHandler.CachedRecipe tObject = this.arecipes.get(aRecipeIndex);
		if ((tObject instanceof CachedDefaultRecipe)) {
			final CachedDefaultRecipe tRecipe = (CachedDefaultRecipe) tObject;
			for (final PositionedStack tStack : tRecipe.mOutputs) {
				if (aStack == tStack.item) {
					if ((!(tStack instanceof FixedPositionedStack)) || (((FixedPositionedStack) tStack).mChance <= 0) || (((FixedPositionedStack) tStack).mChance == 10000)) {
						break;
					}
					currenttip.add("Chance: " + (((FixedPositionedStack) tStack).mChance / 100) + "." + ((((FixedPositionedStack) tStack).mChance % 100) < 10 ? "0" + (((FixedPositionedStack) tStack).mChance % 100) : Integer.valueOf(((FixedPositionedStack) tStack).mChance % 100)) + "%");
					break;
				}
			}
			for (final PositionedStack tStack : tRecipe.mInputs) {
				if (GT_Utility.areStacksEqual(aStack, tStack.item)) {
					if ((gregtech.api.enums.ItemList.Display_Fluid.isStackEqual(tStack.item, true, true)) ||
							(tStack.item.stackSize != 0)) {
						break;
					}
					if (ItemUtils.isMillingBall(aStack)) {
						currenttip.add("Does not always get consumed in the process");						
					}
					else if (ItemUtils.isControlCircuit(aStack)) {
						currenttip.add("Does not get consumed in the process");					
					}
					break;
				}
			}
		}
		return currenttip;
	}

	public class MillingDefaultRecipe extends CachedDefaultRecipe {

		public MillingDefaultRecipe(final GT_Recipe aRecipe) {
			super(aRecipe);			
		}
		
		@Override
		public void handleSlots() {
			int tStartIndex = 0;

			if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
				this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 12, 14));
			}
			tStartIndex++;
			if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
				this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 30, 14));
			}
			tStartIndex++;
			if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
				this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 48, 14));
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


			//New fluid display behaviour when 3 fluid inputs are detected. (Basically a mix of the code below for outputs an the code above for 9 input slots.)
			if (mRecipe.mFluidInputs.length >= 1) {
				if ((mRecipe.mFluidInputs[0] != null) && (mRecipe.mFluidInputs[0].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[0], true), 3, 31));
				}
				if ((mRecipe.mFluidInputs.length > 1) && (mRecipe.mFluidInputs[1] != null) && (mRecipe.mFluidInputs[1].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[1], true), 21, 31));
				}
				if ((mRecipe.mFluidInputs.length > 2) && (mRecipe.mFluidInputs[2] != null) && (mRecipe.mFluidInputs[2].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[2], true), 39, 31));
				}
				if ((mRecipe.mFluidInputs.length > 3) && (mRecipe.mFluidInputs[3] != null) && (mRecipe.mFluidInputs[3].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[3], true), 57, 31));
				}
			}

			if (mRecipe.mFluidOutputs.length > 0) {
				if ((mRecipe.mFluidOutputs[0] != null) && (mRecipe.mFluidOutputs[0].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[0], true), 138, 5, tUnificate));
				}
				if ((mRecipe.mFluidOutputs.length > 1) && (mRecipe.mFluidOutputs[1] != null) && (mRecipe.mFluidOutputs[1].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[1], true), 138, 23, tUnificate));
				}
			}
		}
	}
}
