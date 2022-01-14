package com.github.technus.tectech.mechanics.elementalMatter.core.transformations;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.iElementalStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Tec on 23.05.2017.
 */
public class aFluidQuantizationInfo implements iExchangeInfo<FluidStack, iElementalStack> {
    private final FluidStack      in;
    private final iElementalStack out;

    public aFluidQuantizationInfo(FluidStack fluidStackIn, iElementalStack emOut){
        in=fluidStackIn;
        out=emOut;
    }

    public aFluidQuantizationInfo(Fluid fluid, int fluidAmount, iElementalStack emOut){
        in=new FluidStack(fluid,fluidAmount);
        out=emOut;
    }

    @Override
    public FluidStack input() {
        return in.copy();
    }

    @Override
    public iElementalStack output() {
        return out.clone();
    }

    @Override
    public int hashCode() {
        return in.getFluidID();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof aFluidQuantizationInfo && hashCode() == obj.hashCode();
    }
}
