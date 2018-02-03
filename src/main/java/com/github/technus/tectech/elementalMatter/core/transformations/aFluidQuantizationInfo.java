package com.github.technus.tectech.elementalMatter.core.transformations;

import com.github.technus.tectech.elementalMatter.core.stacks.iHasElementalDefinition;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Tec on 23.05.2017.
 */
public class aFluidQuantizationInfo implements iExchangeInfo {
    private final FluidStack in;
    private final iHasElementalDefinition out;

    public aFluidQuantizationInfo(FluidStack fluidStackIn, iHasElementalDefinition emOut){
        in=fluidStackIn;
        out=emOut;
    }

    public aFluidQuantizationInfo(int fluidID,int fluidAmount, iHasElementalDefinition emOut){
        in=new FluidStack(fluidID,fluidAmount);
        out=emOut;
    }

    public aFluidQuantizationInfo(Fluid fluid, int fluidAmount, iHasElementalDefinition emOut){
        in=new FluidStack(fluid,fluidAmount);
        out=emOut;
    }

    @Override
    public FluidStack input() {
        return in.copy();
    }

    @Override
    public iHasElementalDefinition output() {
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
