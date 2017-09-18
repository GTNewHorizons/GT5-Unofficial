package com.github.technus.tectech.elementalMatter.core.transformations;

import com.github.technus.tectech.elementalMatter.core.interfaces.iExchangeInfo;
import com.github.technus.tectech.elementalMatter.core.interfaces.iHasElementalDefinition;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;

/**
 * Created by Tec on 23.05.2017.
 */
public class aItemDequantizationInfo implements iExchangeInfo {
    private final iHasElementalDefinition in;
    private final ItemStack out;

    public aItemDequantizationInfo(iHasElementalDefinition emIn, ItemStack itemStackOut){
        in=emIn;
        out=itemStackOut;
    }

    public aItemDequantizationInfo(iHasElementalDefinition emIn, OrePrefixes prefix, Materials material, int amount) {
        in = emIn;
        out = GT_OreDictUnificator.get(prefix, material, amount);
    }

    @Override
    public iHasElementalDefinition input() {
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
