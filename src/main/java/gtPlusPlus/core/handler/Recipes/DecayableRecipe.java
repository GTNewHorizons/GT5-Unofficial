package gtPlusPlus.core.handler.Recipes;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;

public class DecayableRecipe {

    public static final ArrayList<DecayableRecipe> mRecipes = new ArrayList<>();

    public final int mTime;
    public final ItemStack mInput;
    public final ItemStack mOutput;

    public DecayableRecipe(int time, ItemStack input, ItemStack output, GTRecipeConstants.DecayType decayType) {
        mTime = time;
        mInput = input;
        mOutput = output;
        mRecipes.add(this);

        GTValues.RA.stdBuilder()
            .itemInputs(input)
            .itemOutputs(output)
            .eut(1)
            .duration(1)
            .metadata(GTRecipeConstants.HALF_LIFE, time / 320d)
            .metadata(GTRecipeConstants.DECAY_TYPE, decayType)
            .addTo(RecipeMaps.isotopeDecay);
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
        return (mTime > 0 && mInput != null && mOutput != null);
    }
}
