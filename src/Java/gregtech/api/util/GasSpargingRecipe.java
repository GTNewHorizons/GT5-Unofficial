package gregtech.api.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GasSpargingRecipe implements Comparable<GasSpargingRecipe> {

	public final FluidStack mInputGas;
	public final int[] mMaxOutputQuantity;
	public final FluidStack[] mFluidInputs;
	public final FluidStack[] mFluidOutputs;
	public final ItemStack[] mInputs;
	public final ItemStack[] mOutputs;

	public GasSpargingRecipe(FluidStack aSpargeGas, FluidStack[] aOutputs, int[] aMaxOutputQuantity) {
		mInputGas = aSpargeGas;
		mFluidInputs = new FluidStack[] {mInputGas};
		mFluidOutputs = aOutputs;
		mMaxOutputQuantity = aMaxOutputQuantity;
		mInputs = new ItemStack[] {GT_Utility.getFluidDisplayStack(mFluidInputs[0], true)};
		mOutputs = new ItemStack[mFluidOutputs.length];
		for (int i=0; i<mFluidOutputs.length;i++) {
			mOutputs[i] = GT_Utility.getFluidDisplayStack(mFluidOutputs[i], true);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof GasSpargingRecipe) {
			GasSpargingRecipe i = (GasSpargingRecipe) o;
			this.mInputGas.equals(i.mInputGas);
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
