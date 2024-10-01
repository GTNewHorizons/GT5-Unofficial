package gtPlusPlus.core.handler.Recipes;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import gregtech.api.util.GTUtility;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class DecayableRecipe {

    public static final ArrayList<DecayableRecipe> mRecipes = new ArrayList<>();

    public final int mTime;
    public final ItemStack mInput;
    public final ItemStack mOutput;

    public DecayableRecipe(int time, ItemStack input, ItemStack output) {
        mTime = time;
        mInput = input;
        mOutput = output;
        mRecipes.add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DecayableRecipe i) {
            return i.mTime == this.mTime && GTUtility.areStacksEqual(mInput, i.mInput)
                && GTUtility.areStacksEqual(mOutput, i.mOutput);
        }
        return false;
    }

    public boolean isValid() {
        return (mTime > 0 && ItemUtils.checkForInvalidItems(mInput) && ItemUtils.checkForInvalidItems(mOutput));
    }
}
