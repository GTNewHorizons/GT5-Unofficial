package gregtech.api.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

// FluidStack that can store more than INTMAX
public class FluidStackLong extends FluidStack {

    private long amountLong = 0;

    public FluidStackLong(FluidStack stack) {
        this(stack, GTUtility.getFluidAmount(stack));
    }

    public FluidStackLong(FluidStack stack, long amount) {
        this(stack.getFluid(), amount);
    }

    public FluidStackLong(Fluid fluid, int amount) {
        this(fluid, amount, null);
    }

    public FluidStackLong(Fluid fluid, long amount) {
        this(fluid, amount, null);
    }

    public FluidStackLong(Fluid fluid, int amount, NBTTagCompound nbt) {
        this(fluid, (long) amount, nbt);
    }

    public FluidStackLong(Fluid fluid, long amount, NBTTagCompound nbt) {
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
                } else {
                    this.amount = GTUtility.longToInt(amountLong);
                }
            } else {
                long diff = Integer.MAX_VALUE - this.amount;

                amountLong -= diff;
                this.amount = Integer.MAX_VALUE;
            }
        }

        return amountLong;
    }

    public void setAmountLong(long amount) {
        amountLong = amount;
        this.amount = GTUtility.longToInt(amount);
    }

    public FluidStackLong copy() {
        return new FluidStackLong(getFluid(), amountLong);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setLong("amountLong", amountLong);

        return nbt;
    }

    public static FluidStackLong loadFluidStackFromNBT(NBTTagCompound nbt) {
        FluidStack stack = FluidStack.loadFluidStackFromNBT(nbt);
        long amountLong = nbt.getLong("amountLong");

        return stack == null ? null : new FluidStackLong(stack.getFluid(), amountLong);
    }
}
