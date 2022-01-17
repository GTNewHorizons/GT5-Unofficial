package com.github.technus.tectech.mechanics.elementalMatter.core.transformations;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;

/**
 * Created by Tec on 23.05.2017.
 */
public class EMItemDequantizationInfo implements IEMExchangeInfo<IEMStack,ItemStack> {
    private final IEMStack  in;
    private final ItemStack out;

    public EMItemDequantizationInfo(IEMStack emIn, ItemStack itemStackOut){
        in=emIn;
        out=itemStackOut;
    }

    public EMItemDequantizationInfo(IEMStack emIn, OrePrefixes prefix, Materials material, int amount) {
        in = emIn;
        out = GT_OreDictUnificator.get(prefix, material, amount);
    }

    @Override
    public IEMStack input() {
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
        return obj instanceof EMItemDequantizationInfo && hashCode() == obj.hashCode();
    }
}
