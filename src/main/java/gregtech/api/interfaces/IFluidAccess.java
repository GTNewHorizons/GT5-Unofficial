package gregtech.api.interfaces;

import net.minecraftforge.fluids.FluidStack;

public interface IFluidAccess {

    void set(FluidStack stack);

    FluidStack get();

    int getCapacity();

    default int getRealCapacity() {
        return getCapacity();
    }

    default void addAmount(int amount) {
        if (get() != null) {
            get().amount = Math.min(get().amount + amount, getRealCapacity());
        }
    }

    default void verifyFluidStack() {
        if (get() != null && get().amount <= 0) set(null);
    }
}
