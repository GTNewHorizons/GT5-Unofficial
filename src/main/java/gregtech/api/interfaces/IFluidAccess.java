package gregtech.api.interfaces;

import net.minecraftforge.fluids.FluidStack;

public interface IFluidAccess {

    void set(FluidStack stack);

    FluidStack get();

    int getCapacity();
}
