package gregtech.api.util;

import java.util.ArrayList;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.TierEU;
import gtPlusPlus.core.util.data.ArrayUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class GasSpargingRecipe implements Comparable<GasSpargingRecipe> {

    public final FluidStack mInputGas;
    public final FluidStack mInputSpentFuel;
    public final FluidStack mOutputSpargedFuel;
    public final int[] mMaxOutputQuantity;
    public final FluidStack[] mFluidInputs;
    public final FluidStack[] mFluidOutputs;
    public final int mDuration;
    public final int mEUt;

    public GasSpargingRecipe(FluidStack aSpargeGas, FluidStack aSpentFuel, FluidStack aSpargedFuel,
        FluidStack[] aOutputs, int[] aMaxOutputQuantity) {
        mInputGas = aSpargeGas;
        mInputSpentFuel = aSpentFuel;
        mOutputSpargedFuel = aSpargedFuel;
        mFluidInputs = new FluidStack[] { mInputGas, mInputSpentFuel };
        aOutputs = ArrayUtils.insertElementAtIndex(aOutputs, 0, aSpargeGas);
        aOutputs = ArrayUtils.insertElementAtIndex(aOutputs, 1, aSpargedFuel);
        mFluidOutputs = aOutputs;
        mMaxOutputQuantity = aMaxOutputQuantity;
        mDuration = 500;
        mEUt = (int) TierEU.RECIPE_IV;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GasSpargingRecipe i) {
            return this.mInputGas.equals(i.mInputGas) && this.mInputSpentFuel.equals(i.mInputSpentFuel);
        }
        return false;
    }

    public int getMaxOutput(int aIndex) {
        if (aIndex == 0) {
            return mInputGas.amount * 100;
        } else if (aIndex == 1) {
            return mOutputSpargedFuel.amount * 100;
        }
        aIndex -= 2;
        if ((aIndex < 0) || (aIndex >= this.mMaxOutputQuantity.length)) {
            return 10000;
        }
        return this.mMaxOutputQuantity[aIndex];
    }

    public boolean isValid() {
        return mInputGas != null && mInputGas.amount > 0
            && mInputSpentFuel != null
            && mInputSpentFuel.amount > 0
            && mFluidOutputs != null
            && mFluidOutputs.length >= 1
            && mMaxOutputQuantity != null
            && mMaxOutputQuantity.length >= 1
            && mFluidOutputs.length == mMaxOutputQuantity.length;
    }

    public boolean containsInputs(FluidStack aSpargeGas, FluidStack aSpentFuel) {
        if (aSpargeGas != null && aSpargeGas.getFluid()
            .equals(this.mInputGas.getFluid())) {
            return aSpentFuel != null && aSpentFuel.getFluid()
                .equals(this.mInputSpentFuel.getFluid());
        }
        return false;
    }

    @Override
    public int compareTo(GasSpargingRecipe o) {
        return Integer.compare(o.mFluidOutputs.length, this.mFluidOutputs.length);
    }

    public String[] getRecipeInfo() {
        ArrayList<String> result = new ArrayList<>();
        result.add("Input " + ItemUtils.getArrayStackNames(mFluidInputs));
        result.add("Output " + ItemUtils.getArrayStackNames(mFluidOutputs));
        result.add("Duration: " + mDuration);
        result.add("EU/t: " + mEUt);
        return result.toArray(new String[] {});
    }
}
