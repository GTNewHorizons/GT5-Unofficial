package gregtech.api.util;

import net.minecraftforge.fluids.FluidStack;

import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.data.ArrayUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

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
        mEUt = MaterialUtils.getVoltageForTier(5);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GasSpargingRecipe i) {
            if (this.mInputGas.equals(i.mInputGas) && this.mInputSpentFuel.equals(i.mInputSpentFuel)) {
                return true;
            }
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
        if (mInputGas == null || mInputGas.amount <= 0
                || mInputSpentFuel == null
                || mInputSpentFuel.amount <= 0
                || mFluidOutputs == null
                || mFluidOutputs.length < 1
                || mMaxOutputQuantity == null
                || mMaxOutputQuantity.length < 1
                || mFluidOutputs.length != mMaxOutputQuantity.length) {
            return false;
        }
        return true;
    }

    public boolean containsInputs(FluidStack aSpargeGas, FluidStack aSpentFuel) {
        if (aSpargeGas != null && aSpargeGas.getFluid().equals(this.mInputGas.getFluid())) {
            if (aSpentFuel != null && aSpentFuel.getFluid().equals(this.mInputSpentFuel.getFluid())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(GasSpargingRecipe o) {
        if (o.mFluidOutputs.length > this.mFluidOutputs.length) {
            return 1;
        } else if (o.mFluidOutputs.length == this.mFluidOutputs.length) {
            return 0;
        } else {
            return -1;
        }
    }

    public String[] getRecipeInfo() {
        AutoMap<String> result = new AutoMap<>();
        result.put("Input " + ItemUtils.getArrayStackNames(mFluidInputs));
        result.put("Output " + ItemUtils.getArrayStackNames(mFluidOutputs));
        result.put("Duration: " + mDuration);
        result.put("EU/t: " + mEUt);
        String s[] = result.toArray();
        return s;
    }
}
