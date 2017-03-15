package com.github.technus.tectech.elementalMatter.classes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Tec on 02.03.2017.
 */
public class rElementalRecipe implements Comparable<rElementalRecipe> {
    public final cElementalDefinitionStackTree inEM;
    public final cElementalDefinitionStackTree outEM;
    public final ItemStack[] outItems;
    public final FluidStack[] outFluids;
    public Object[] extension = null;

    public rElementalRecipe(
            cElementalDefinitionStackTree inEMnotNull,
            cElementalDefinitionStackTree outEM,
            ItemStack[] outItems,
            FluidStack[] outFluids) {
        this.inEM = inEMnotNull;
        this.outEM = outEM;
        this.outItems = outItems;
        this.outFluids = outFluids;
    }

    public void extend(Object... data) {
        extension = data;
    }

    @Override
    public int compareTo(rElementalRecipe o) {
        return inEM.compareTo(o.inEM);
    }
}
