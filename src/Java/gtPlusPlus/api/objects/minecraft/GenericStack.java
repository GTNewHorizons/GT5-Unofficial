package gtPlusPlus.api.objects.minecraft;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.FluidStack;

public class GenericStack {

	private ItemStack mItemStack;
	private FluidStack mFluidStack;
	
	public GenericStack(ItemStack s){
		this.mItemStack = s;
		this.mFluidStack = null;
	}
	
	public GenericStack(FluidStack f){
		this.mItemStack = null;
		this.mFluidStack = f;
	}
	
	public GenericStack() {
		this.mItemStack = null;
		this.mFluidStack = null;
	}

	public synchronized final FluidStack getFluidStack() {
		return mFluidStack;
	}

	public synchronized final ItemStack getItemStack() {
		return mItemStack;
	}

	public synchronized final void setItemStack(ItemStack mItemStack) {
		this.mItemStack = mItemStack;
	}

	public synchronized final void setFluidStack(FluidStack mFluidStack) {
		this.mFluidStack = mFluidStack;
	}
}
