package gtPlusPlus.core.handler.Recipes;

import net.minecraft.item.ItemStack;

import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class DecayableRecipe {

    public static final AutoMap<DecayableRecipe> mRecipes = new AutoMap<>();

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
        if (o instanceof DecayableRecipe i) {
            if (i.mTime == this.mTime && GT_Utility.areStacksEqual(mInput, i.mInput)
                    && GT_Utility.areStacksEqual(mOutput, i.mOutput)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValid() {
        return (mTime > 0 && ItemUtils.checkForInvalidItems(mInput) && ItemUtils.checkForInvalidItems(mOutput));
    }
}
