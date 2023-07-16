package gregtech.api.fluid;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public interface FluidHandler {

    int getTanks();

    @Nullable
    FluidStack getFluidInTank(int tank);

    FluidStackHolder getFluidHolderInTank(int tank);

    @Nullable
    FluidStack insertFluid(int tank, Fluid fluid, long amount, boolean simulate);

    @Nullable
    FluidStack extractFluid(int tank, int amount, boolean simulate);

    long getTankCapacity(int tank);

    long getTankAmount(int tank);

    default boolean isFluidValid(int slot, FluidStack stack) {
        return true;
    }

    default List<FluidStack> getFluids() {
        List<FluidStack> ret = new ArrayList<>();

        for (int i = 0; i < this.getTanks(); ++i) {
            ret.add(this.getFluidInTank(i));
        }

        return ret;
    }

    void setFluidInTank(int tank, Fluid fluid, long amount);

    default void setFluidInTank(int tank, Fluid fluid) {
        setFluidInTank(tank, fluid, 0);
    }

    default void setFluidInTank(int tank, FluidStackHolder fluid) {
        setFluidInTank(tank, fluid != null ? fluid.getFluid() : null, fluid != null ? fluid.getStoredAmount() : 0);
    }
}
