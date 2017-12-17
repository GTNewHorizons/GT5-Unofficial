package com.github.technus.tectech.elementalMatter.core.transformations;

import com.github.technus.tectech.elementalMatter.core.interfaces.iExchangeInfo;
import com.github.technus.tectech.elementalMatter.core.interfaces.iHasElementalDefinition;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Tec on 23.05.2017.
 */
public class aFluidDequantizationInfo implements iExchangeInfo {
    private final iHasElementalDefinition in;
    private final FluidStack out;

    public aFluidDequantizationInfo(iHasElementalDefinition emIn, FluidStack fluidStackOut){
        in=emIn;
        out=fluidStackOut;
    }

    public aFluidDequantizationInfo(iHasElementalDefinition emIn ,int fluidID,int fluidAmount) {
        in = emIn;
        out = new FluidStack(fluidID, fluidAmount);
    }

    public aFluidDequantizationInfo(iHasElementalDefinition emIn, Fluid fluid, int fluidAmount){
        in=emIn;
        out=new FluidStack(fluid,fluidAmount);
    }

    @Override
    public iHasElementalDefinition input() {
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
        return obj instanceof aFluidDequantizationInfo && hashCode() == obj.hashCode();
    }
}
