package gregtech.api.util;

import gtPlusPlus.core.util.minecraft.MaterialUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GasSpargingRecipe implements Comparable<GasSpargingRecipe> {

	public final FluidStack mInputGas;
	public final FluidStack mInputSpentFuel;
	public final int[] mMaxOutputQuantity;
	public final FluidStack[] mFluidInputs;
	public final FluidStack[] mFluidOutputs;
	public final ItemStack[] mInputs;
	public final ItemStack[] mOutputs;
	public final int mDuration;
	public final int mEUt;

	public GasSpargingRecipe(FluidStack aSpargeGas, FluidStack aSpentFuel, FluidStack[] aOutputs, int[] aMaxOutputQuantity) {
		mInputGas = aSpargeGas;
		mInputSpentFuel = aSpentFuel;
		mFluidInputs = new FluidStack[] {mInputGas, mInputSpentFuel};
		mFluidOutputs = aOutputs;
		mMaxOutputQuantity = aMaxOutputQuantity;
		mInputs = new ItemStack[] {GT_Utility.getFluidDisplayStack(mFluidInputs[0], true), GT_Utility.getFluidDisplayStack(mFluidInputs[1], true)};
		mOutputs = new ItemStack[mFluidOutputs.length];
		for (int i=0; i<mFluidOutputs.length;i++) {
			mOutputs[i] = GT_Utility.getFluidDisplayStack(mFluidOutputs[i], true);
		}
		mDuration = 20 * 60 * 5;
		mEUt = MaterialUtils.getVoltageForTier(3);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof GasSpargingRecipe) {
			GasSpargingRecipe i = (GasSpargingRecipe) o;
			if (this.mInputGas.equals(i.mInputGas) && this.mInputSpentFuel.equals(i.mInputSpentFuel)) {
				return true;
			}
		}
		return false;
	}

	public int getMaxOutput(final int aIndex) {
		if ((aIndex < 0) || (aIndex >= this.mMaxOutputQuantity.length)) {
			return 10000;
		}
		return this.mMaxOutputQuantity[aIndex];
	}

	public boolean isValid() {
		if (mInputGas == null || mInputGas.amount <= 0 ||
				mInputSpentFuel == null || mInputSpentFuel.amount <= 0 ||
				mFluidOutputs == null || mFluidOutputs.length < 1 ||
				mMaxOutputQuantity == null || mMaxOutputQuantity.length < 1 ||
				mFluidOutputs.length != mMaxOutputQuantity.length) {
			return false;
		}
		return true;
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

}
