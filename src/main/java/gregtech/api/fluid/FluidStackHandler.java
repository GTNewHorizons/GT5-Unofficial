package gregtech.api.fluid;

import static com.google.common.primitives.Ints.saturatedCast;

import java.util.Arrays;
import java.util.List;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidStackHandler implements FluidHandler {

    protected List<FluidStackHolder> fluids;

    public FluidStackHandler(int tankAmount, long capacity) {
        FluidStackHolder[] fluids = new FluidStackHolder[tankAmount];
        Arrays.fill(fluids, new FluidStackHolder(capacity));
        this.fluids = Arrays.asList(fluids);
    }

    public FluidStackHandler(int tankAmount) {
        this(tankAmount, 8000);
    }

    @Override
    public int getTanks() {
        return fluids.size();
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return fluids.get(tank)
            .getFluidStack();
    }

    @Override
    public FluidStack insertFluid(int tank, Fluid fluid, long amount, boolean simulate) {
        return fluid == null ? null
            : new FluidStack(
                fluid,
                saturatedCast(
                    fluids.get(tank)
                        .fill(fluid, amount, simulate)));
    }

    @Override
    public FluidStack extractFluid(int tank, int amount, boolean simulate) {
        return fluids.get(tank)
            .drain(amount, simulate);
    }

    @Override
    public long getTankCapacity(int tank) {
        return fluids.get(tank)
            .getCapacity();
    }

    @Override
    public long getTankAmount(int tank) {
        return fluids.get(tank)
            .getStoredAmount();
    }

    @Override
    public void setFluidInTank(int tank, Fluid fluid, long amount) {
        fluids.get(tank)
            .setFluid(fluid, amount);
    }

    @Override
    public FluidStackHolder getFluidHolderInTank(int tank) {
        return fluids.get(tank);
    }

}
