package com.github.technus.tectech.mechanics.elementalMatter.core.transformations;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Tec on 23.05.2017.
 */
public class EMFluidQuantizationInfo {
    private final FluidStack in;
    private final IEMStack out;

    public EMFluidQuantizationInfo(FluidStack fluidStackIn, IEMStack emOut) {
        in = fluidStackIn;
        out = emOut;
    }

    public EMFluidQuantizationInfo(Fluid fluid, int fluidAmount, IEMStack emOut) {
        in = new FluidStack(fluid, fluidAmount);
        out = emOut;
    }

    public FluidStack input() {
        return in.copy();
    }

    public IEMStack output() {
        return out.clone();
    }

    @Override
    public int hashCode() {
        return in.getFluidID();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EMFluidQuantizationInfo && hashCode() == obj.hashCode();
    }
}
