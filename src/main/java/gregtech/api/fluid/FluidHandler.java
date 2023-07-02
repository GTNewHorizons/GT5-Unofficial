package gregtech.api.fluid;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public interface FluidHandler {

    int getTanks();

    @Nullable
    FluidStack getFluidInSlot(int tank);

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
            ret.add(this.getFluidInSlot(i));
        }

        return ret;
    }

    void setFluidInTank(int tank, FluidStack fluid);
}
