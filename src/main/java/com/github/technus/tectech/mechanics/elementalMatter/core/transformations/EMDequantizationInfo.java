package com.github.technus.tectech.mechanics.elementalMatter.core.transformations;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class EMDequantizationInfo {
    private final IEMStack definition;
    private Object stack;

    public EMDequantizationInfo(IEMStack definition) {
        this.definition = definition;
    }

    public IEMStack getInput() {
        return definition;
    }

    public FluidStack getFluid() {
        return ((FluidStack) stack).copy();
    }

    public void setFluid(FluidStack fluid) {
        this.stack = fluid;
    }

    public ItemStack getItem() {
        return ((ItemStack) stack).copy();
    }

    public void setItem(ItemStack item) {
        this.stack = item;
    }

    public OreDictionaryStack getOre() {
        return (OreDictionaryStack) stack;
    }

    public void setOre(OreDictionaryStack ore) {
        this.stack = ore;
    }

    public Object getStack() {
        return stack;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof EMDequantizationInfo && definition.equals(((EMDequantizationInfo) o).definition);
    }

    @Override
    public int hashCode() {
        return definition.hashCode();
    }
}
