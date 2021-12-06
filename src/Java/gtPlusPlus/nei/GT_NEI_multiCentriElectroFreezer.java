package gtPlusPlus.nei;

import java.util.HashMap;

import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.data.Pair;

public class GT_NEI_multiCentriElectroFreezer extends GTPP_NEI_DefaultHandler {

	public GT_NEI_multiCentriElectroFreezer(GT_Recipe_Map aMap) {
		super(aMap);
	}

	@Override
	public TemplateRecipeHandler newInstance() {
		return new GT_NEI_multiCentriElectroFreezer(mRecipeMap);
	}

	@Override
    public CachedDefaultRecipe createCachedRecipe(GT_Recipe aRecipe) {
    	return new NoCellMultiDefaultRecipe(aRecipe);
    }
	
	private static final HashMap<Integer, Pair<Integer, Integer>> mInputSlotMap = new HashMap<Integer, Pair<Integer, Integer>>();
	private static final HashMap<Integer, Pair<Integer, Integer>> mOutputSlotMap = new HashMap<Integer, Pair<Integer, Integer>>();
	
	static {
		int aSlotX_1 = 12;
		int aSlotX_2 = 30;
		int aSlotX_3 = 48;
		int aSlotY_1 = 5;
		int aSlotY_2 = 23;
		int aSlotY_3 = 41;
		int aSlotY_10 = 65; // Only if 9 input items and a FLuid
		mInputSlotMap.put(0, new Pair<Integer, Integer>(aSlotX_1, aSlotY_1));
		mInputSlotMap.put(1, new Pair<Integer, Integer>(aSlotX_2, aSlotY_1));
		mInputSlotMap.put(2, new Pair<Integer, Integer>(aSlotX_3, aSlotY_1));
		mInputSlotMap.put(3, new Pair<Integer, Integer>(aSlotX_1, aSlotY_2));
		mInputSlotMap.put(4, new Pair<Integer, Integer>(aSlotX_2, aSlotY_2));
		mInputSlotMap.put(5, new Pair<Integer, Integer>(aSlotX_3, aSlotY_2));
		mInputSlotMap.put(6, new Pair<Integer, Integer>(aSlotX_1, aSlotY_3));
		mInputSlotMap.put(7, new Pair<Integer, Integer>(aSlotX_2, aSlotY_3));
		mInputSlotMap.put(8, new Pair<Integer, Integer>(aSlotX_3, aSlotY_3));
		mInputSlotMap.put(9, new Pair<Integer, Integer>(aSlotX_1, aSlotY_10));
		mInputSlotMap.put(10, new Pair<Integer, Integer>(aSlotX_2, aSlotY_10));
		mInputSlotMap.put(11, new Pair<Integer, Integer>(aSlotX_3, aSlotY_10));
		aSlotX_1 = 102;
		aSlotX_2 = 120;
		aSlotX_3 = 138;
		mOutputSlotMap.put(0, new Pair<Integer, Integer>(aSlotX_1, aSlotY_1));
		mOutputSlotMap.put(1, new Pair<Integer, Integer>(aSlotX_2, aSlotY_1));
		mOutputSlotMap.put(2, new Pair<Integer, Integer>(aSlotX_3, aSlotY_1));
		mOutputSlotMap.put(3, new Pair<Integer, Integer>(aSlotX_1, aSlotY_2));
		mOutputSlotMap.put(4, new Pair<Integer, Integer>(aSlotX_2, aSlotY_2));
		mOutputSlotMap.put(5, new Pair<Integer, Integer>(aSlotX_3, aSlotY_2));
		mOutputSlotMap.put(6, new Pair<Integer, Integer>(aSlotX_1, aSlotY_3));
		mOutputSlotMap.put(7, new Pair<Integer, Integer>(aSlotX_2, aSlotY_3));
		mOutputSlotMap.put(8, new Pair<Integer, Integer>(aSlotX_3, aSlotY_3));
		mOutputSlotMap.put(9, new Pair<Integer, Integer>(aSlotX_1, aSlotY_10));
		mOutputSlotMap.put(10, new Pair<Integer, Integer>(aSlotX_2, aSlotY_10));
		mOutputSlotMap.put(11, new Pair<Integer, Integer>(aSlotX_3, aSlotY_10));
	}

	public class NoCellMultiDefaultRecipe extends CachedDefaultRecipe {

		public NoCellMultiDefaultRecipe(final GT_Recipe aRecipe) {
			super(aRecipe);
			
		}

		@Override
		public void handleSlots() {

			int aInputItemsCount = this.mRecipe.mInputs.length;
			int aInputFluidsCount = this.mRecipe.mFluidInputs.length;			
			int aOutputItemsCount = this.mRecipe.mOutputs.length;
			int aOutputFluidsCount = this.mRecipe.mFluidOutputs.length;
			int aInputSlotsUsed = 0;
			int aOutputSlotsUsed = 0;			
			int aSlotToCheck = 0;	

			// Special Slot
			if (mRecipe.mSpecialItems != null) {
				this.mInputs.add(new FixedPositionedStack(mRecipe.mSpecialItems, 120, 52));
			}
			
			/*
			 * Items
			 */
			
			// Upto 9 Inputs Slots
			if (aInputItemsCount > 0) {				
				if (aInputItemsCount > 9) {
					aInputItemsCount = 9;
				}				
				for (int i=0;i<aInputItemsCount;i++) {
					int x = mInputSlotMap.get(aSlotToCheck).getKey();
					int y = mInputSlotMap.get(aSlotToCheck).getValue();
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(aSlotToCheck), x, y));	
					aSlotToCheck++;
					aInputSlotsUsed++;
				}
			}
			aSlotToCheck = 0;	
			// Upto 9 Output Slots
			if (aOutputItemsCount > 0) {			
				if (aOutputItemsCount > 9) {
					aOutputItemsCount = 9;
				}		
				for (int i=0;i<aOutputItemsCount;i++) {
					int x = mOutputSlotMap.get(aSlotToCheck).getKey();
					int y = mOutputSlotMap.get(aSlotToCheck).getValue();
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(aSlotToCheck), x, y, mRecipe.getOutputChance(aSlotToCheck)));	
					aSlotToCheck++;
					aOutputSlotsUsed++;
				}
			}					
			
			/*
			 * Fluids
			 */

			// Upto 9 Fluid Inputs Slots
			aSlotToCheck = aInputSlotsUsed;	
			if (aInputFluidsCount > 0) {				
				for (int i=0;i<aInputFluidsCount;i++) {
					int x = mInputSlotMap.get(aSlotToCheck).getKey();
					int y = mInputSlotMap.get(aSlotToCheck).getValue();
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[i], true), x, y));
					aSlotToCheck++;
					aInputSlotsUsed++;
				}
			}
			// Upto 9 Fluid Outputs Slots
			aSlotToCheck = aOutputSlotsUsed;	
			if (aOutputFluidsCount > 0) {				
				for (int i=0;i<aOutputFluidsCount;i++) {
					int x = mOutputSlotMap.get(aSlotToCheck).getKey();
					int y = mOutputSlotMap.get(aSlotToCheck).getValue();
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[i], true), x, y));
					aSlotToCheck++;
					aOutputSlotsUsed++;
				}
			}
		}
	}
}
