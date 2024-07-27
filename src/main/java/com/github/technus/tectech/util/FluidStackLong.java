package com.github.technus.tectech.util;

import net.minecraftforge.fluids.FluidStack;

public class FluidStackLong {

    public final FluidStack fluidStack;
    public long amount;

    public FluidStackLong(FluidStack fluidStack, long amount) {
        this.fluidStack = fluidStack;
        this.amount = amount;
    }

    // Copy constructor.
    public FluidStackLong(FluidStackLong fluidStackLong) {
        this.fluidStack = fluidStackLong.fluidStack;
        this.amount = fluidStackLong.amount;
    }

    public long getFluidAmount() {
        return amount;
    }

    public FluidStack getRegularFluidStack(FluidStackLong fluidStackLong, int amount) {
        return new FluidStack(fluidStackLong.fluidStack, amount);
    }

}
