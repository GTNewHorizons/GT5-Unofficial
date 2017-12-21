package com.github.technus.tectech.elementalMatter.core.transformations;

/**
 * Created by Tec on 23.05.2017.
 */
public interface iExchangeInfo {
    Object output();//what should be given - ItemStack,FluidStack,AspectStack, (EM definitionStack->)EM instance stack - etc.
    //This must return new Object! - if obj is immutable dont care that much (applies to defStacks)

    Object input();//same as above but for input
}
