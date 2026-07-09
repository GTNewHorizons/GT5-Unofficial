package gregtech.api.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

// FluidStack that can store more than INTMAX
public class LongFluidStack extends FluidStack {

    private long amountLong;

    public LongFluidStack(FluidStack stack) {
        this(stack, GTUtility.getFluidAmount(stack));
    }

    public LongFluidStack(FluidStack stack, long amount) {
        this(stack.getFluid(), amount);
    }

    public LongFluidStack(Fluid fluid, int amount) {
        this(fluid, amount, null);
    }

    public LongFluidStack(Fluid fluid, long amount) {
        this(fluid, amount, null);
    }

    public LongFluidStack(Fluid fluid, int amount, NBTTagCompound nbt) {
        this(fluid, (long) amount, nbt);
    }

    public LongFluidStack(Fluid fluid, long amount, NBTTagCompound nbt) {
        super(fluid, GTUtility.longToInt(amount), nbt);
        amountLong = amount;
    }

    public long getAmountLong() {
        // Sync with default amount property
        if (this.amount != amountLong) {
            // Fluid Stack only ever decrements if they're not synced

            if (amountLong < Integer.MAX_VALUE) {
                if (this.amount < amountLong) {
                    amountLong = this.amount;
                }
            } else {
                long diff = Integer.MAX_VALUE - this.amount;
                amountLong -= diff;
            }

            this.amount = GTUtility.longToInt(amountLong);
        }

        return amountLong;
    }

    public void setAmountLong(long amount) {
        amountLong = amount;
        this.amount = GTUtility.longToInt(amount);
    }

    public LongFluidStack copy() {
        return new LongFluidStack(getFluid(), getAmountLong(), tag == null ? null : (NBTTagCompound) tag.copy());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setLong("amountLong", getAmountLong());

        return nbt;
    }

    public static LongFluidStack loadFluidStackFromNBT(NBTTagCompound nbt) {
        FluidStack stack = FluidStack.loadFluidStackFromNBT(nbt);
        long amountLong = nbt.hasKey("amountLong") ? nbt.getLong("amountLong") : stack.amount;

        return stack == null ? null : new LongFluidStack(stack.getFluid(), amountLong);
    }
}
