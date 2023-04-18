package com.github.technus.tectech.mechanics.elementalMatter.core.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMConstantStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.IEMMapRead;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;

/**
 * Created by Tec on 02.03.2017.
 */
public class EMRecipe<T> implements Comparable<EMRecipe<T>> {

    private final int ID;
    private final EMConstantStackMap inEM;
    private final IEMMapRead<? extends IEMStack> outEM;
    private final ItemStack[] outItems;
    private final FluidStack[] outFluids;
    private T extension;

    public EMRecipe(EMConstantStackMap inEM, // not null plz
            int id, IEMMapRead<? extends IEMStack> outEM, ItemStack[] outItems, FluidStack[] outFluids) {
        this.inEM = inEM;
        this.outEM = outEM;
        this.outItems = outItems;
        this.outFluids = outFluids;
        ID = id; // allows multiple recipes with the same input EM,so u can actually extend...
    }

    public EMRecipe<T> extend(T data) {
        setExtension(data);
        return this;
    }

    @Override
    public int compareTo(EMRecipe<T> o) {
        int compare = getInEM().compareTo(o.getInEM());
        if (compare != 0) {
            return compare;
        }
        return Integer.compare(getID(), o.getID());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EMRecipe) {
            return compareTo((EMRecipe) obj) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getInEM().hashCode();
    }

    public int getID() {
        return ID;
    }

    public EMConstantStackMap getInEM() {
        return inEM;
    }

    public IEMMapRead<? extends IEMStack> getOutEM() {
        return outEM;
    }

    public ItemStack[] getOutItems() {
        return outItems;
    }

    public FluidStack[] getOutFluids() {
        return outFluids;
    }

    public T getExtension() {
        return extension;
    }

    public void setExtension(T extension) {
        this.extension = extension;
    }
}
