package com.github.technus.tectech.mechanics.elementalMatter.core.transformations;

/**
 * Created by Tec on 23.05.2017.
 */
public interface iExchangeInfo<IN,OUT> {
    OUT output();//what should be given - ItemStack,FluidStack,AspectStack, (EM definitionStack->)EM instance stack - etc.
    //This must return new Object! - if obj is immutable don't care that much (applies to defStacks)

    IN input();//same as above but for input
}
