package com.github.technus.tectech.mechanics.elementalMatter.core;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Tec on 02.03.2017.
 */
public class rElementalRecipe implements Comparable<rElementalRecipe> {
    public final short ID;
    public final cElementalDefinitionStackMap inEM;
    public final cElementalDefinitionStackMap outEM;
    public final ItemStack[] outItems;
    public final FluidStack[] outFluids;
    public Object[] extension;

    public rElementalRecipe(
            cElementalDefinitionStackMap inEM,//not null plz
            short id,
            cElementalDefinitionStackMap outEM,
            ItemStack[] outItems,
            FluidStack[] outFluids) {
        this.inEM = inEM;
        this.outEM = outEM;
        this.outItems = outItems;
        this.outFluids = outFluids;
        ID = id;//allows multiple recipes with the same input EM,so u can actually extend...
    }

    public rElementalRecipe extend(Object... data) {
        extension = data;
        return this;
    }

    @Override
    public int compareTo(rElementalRecipe o) {
        int compare = inEM.compareTo(o.inEM);
        if(compare!=0) {
            return compare;
        }
        if(ID>o.ID) {
            return 1;
        }
        if(ID<o.ID) {
            return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof rElementalRecipe) {
            return compareTo((rElementalRecipe) obj) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return inEM.hashCode();
    }
}
