package gregtech.api.fluid;

import static com.google.common.primitives.Ints.saturatedCast;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * A fluid stack holder holding the fluid stack amounts as a long instead of an int
 * 
 * @author BlueWeabo
 */
public class FluidStackHolder {

    private Fluid fluid;
    private long storedAmount;
    private long capacity;
    private FluidStack internal;
    private boolean locked;

    public FluidStackHolder(Fluid fluid, long capacity, long amount) {
        this.fluid = fluid;
        this.storedAmount = amount;
        this.capacity = capacity;
        if (fluid == null) return;
        internal = new FluidStack(fluid, saturatedCast(amount));
    }

    public FluidStackHolder(Fluid fluid, long capacity) {
        this(fluid, capacity, 0);
    }

    public FluidStackHolder(long capacity) {
        this(null, capacity);
    }

    public FluidStack getFluidStack() {
        return internal;
    }

    public long getCapacity() {
        return capacity;
    }

    public long getStoredAmount() {
        return storedAmount;
    }

    /**
     * @param fluid    The fluid we are trying to fill
     * @param amount   Amount of fluid trying to be filled in
     * @param simulate Should it update the stack internally
     * @return Amount of fluid filled into the stack
     */
    public long fill(Fluid fluid, long amount, boolean simulate) {
        if (this.fluid != null && this.fluid != fluid || fluid == null) return 0;

        if (simulate) {
            return Math.min(capacity - storedAmount, amount);
        }

        long amountFilled = Math.min(capacity - storedAmount, amount);
        this.storedAmount += amountFilled;
        if (internal == null) {
            internal = new FluidStack(fluid, 0);
        }
        internal.amount = saturatedCast(storedAmount);
        return amountFilled;
    }

    /**
     * 
     * @param amount   Amount of fluid to try and drain
     * @param simulate Should it update the stack internally
     * @return a Fluid stack with the amount drained
     */
    public FluidStack drain(int amount, boolean simulate) {
        if (fluid == null) {
            return null;
        }
        if (simulate) {
            return new FluidStack(fluid, saturatedCast(Math.min(storedAmount, amount)));
        }

        long amountDrained = Math.min(storedAmount, amount);
        storedAmount -= amountDrained;
        FluidStack fluidDrained = new FluidStack(fluid, saturatedCast(amountDrained));
        if (storedAmount <= 0 && !locked) {
            fluid = null;
            internal = null;
        }
        return fluidDrained;
    }

    public void setFluid(FluidStack fluid) {
        this.fluid = fluid.getFluid();
        storedAmount = fluid.amount;
        internal = new FluidStack(this.fluid, saturatedCast(storedAmount));
    }
}
