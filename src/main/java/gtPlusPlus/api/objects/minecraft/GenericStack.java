package gtPlusPlus.api.objects.minecraft;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GenericStack {

    private ItemStack mItemStack;
    private FluidStack mFluidStack;

    public GenericStack(ItemStack s) {
        this.mItemStack = s;
        this.mFluidStack = null;
    }

    public GenericStack(FluidStack f) {
        this.mItemStack = null;
        this.mFluidStack = f;
    }

    public GenericStack() {
        this.mItemStack = null;
        this.mFluidStack = null;
    }

    public final synchronized FluidStack getFluidStack() {
        return mFluidStack;
    }

    public final synchronized ItemStack getItemStack() {
        return mItemStack;
    }

    public final synchronized void setItemStack(ItemStack mItemStack) {
        this.mItemStack = mItemStack;
    }

    public final synchronized void setFluidStack(FluidStack mFluidStack) {
        this.mFluidStack = mFluidStack;
    }
}
