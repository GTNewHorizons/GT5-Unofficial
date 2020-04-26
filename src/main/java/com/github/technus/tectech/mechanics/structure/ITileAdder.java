package com.github.technus.tectech.mechanics.structure;

import net.minecraft.tileentity.TileEntity;

public interface ITileAdder<T> {
    /**
     * Callback to add hatch
     * @param tileEntity tile
     * @return managed to add hatch (structure still valid)
     */
    boolean apply(T t,TileEntity tileEntity);
}
