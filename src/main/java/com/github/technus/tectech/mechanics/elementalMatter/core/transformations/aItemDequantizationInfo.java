package com.github.technus.tectech.mechanics.elementalMatter.core.transformations;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.iElementalStack;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;

/**
 * Created by Tec on 23.05.2017.
 */
public class aItemDequantizationInfo implements iExchangeInfo<iElementalStack,ItemStack> {
    private final iElementalStack in;
    private final ItemStack       out;

    public aItemDequantizationInfo(iElementalStack emIn, ItemStack itemStackOut){
        in=emIn;
        out=itemStackOut;
    }

    public aItemDequantizationInfo(iElementalStack emIn, OrePrefixes prefix, Materials material, int amount) {
        in = emIn;
        out = GT_OreDictUnificator.get(prefix, material, amount);
    }

    @Override
    public iElementalStack input() {
        return in.clone();
    }

    @Override
    public ItemStack output() {
        return out.copy();
    }

    @Override
    public int hashCode() {
        return in.getDefinition().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof aItemDequantizationInfo && hashCode() == obj.hashCode();
    }
}
