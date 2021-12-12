package gtPlusPlus.core.handler.Recipes;

import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.item.ItemStack;

public class DecayableRecipe {

	public static final AutoMap<DecayableRecipe> mRecipes = new AutoMap<DecayableRecipe>();
	
	
	public final int mTime;
	public final ItemStack mInput;
	public final ItemStack mOutput;

	public DecayableRecipe(int time, ItemStack input, ItemStack output) {
		mTime = time;
		mInput = input;
		mOutput = output;
		mRecipes.put(this);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof DecayableRecipe) {
			DecayableRecipe i = (DecayableRecipe) o;
			if (i.mTime == this.mTime && GT_Utility.areStacksEqual(mInput, i.mInput) && GT_Utility.areStacksEqual(mOutput, i.mOutput)) {
				return true;
			}
		}
		return false;
	}

	public boolean isValid() {
		return (mTime > 0 && ItemUtils.checkForInvalidItems(mInput) && ItemUtils.checkForInvalidItems(mOutput));
	}

}
