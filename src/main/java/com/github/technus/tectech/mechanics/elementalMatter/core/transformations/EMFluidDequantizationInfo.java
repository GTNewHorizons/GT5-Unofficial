package com.github.technus.tectech.mechanics.elementalMatter.core.transformations;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Tec on 23.05.2017.
 */
public class EMFluidDequantizationInfo implements IEMExchangeInfo<IEMStack,FluidStack> {
    private final IEMStack   in;
    private final FluidStack out;

    public EMFluidDequantizationInfo(IEMStack emIn, FluidStack fluidStackOut){
        in=emIn;
        out=fluidStackOut;
    }

    public EMFluidDequantizationInfo(IEMStack emIn, Fluid fluid, int fluidAmount){
        in=emIn;
        out=new FluidStack(fluid,fluidAmount);
    }

    @Override
    public IEMStack input() {
        return in.clone();//MEH!
    }

    @Override
    public FluidStack output() {
        return out.copy();
    }

    @Override
    public int hashCode() {
        return in.getDefinition().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EMFluidDequantizationInfo && hashCode() == obj.hashCode();
    }
}
