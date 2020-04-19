package com.github.technus.tectech.mechanics.constructible;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by Tec on 24.03.2017.
 */
public interface IConstructable {
    void construct(int stackSize, boolean hintsOnly);

    @SideOnly(Side.CLIENT)
    String[] getStructureDescription(int stackSize);
}

