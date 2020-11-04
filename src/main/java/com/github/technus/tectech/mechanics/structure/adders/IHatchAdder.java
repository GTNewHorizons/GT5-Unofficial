package com.github.technus.tectech.mechanics.structure.adders;


import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public interface IHatchAdder<MultiBlock> {
    /**
     * Callback to add hatch, needs to check if hatch is valid (and add it)
     * @param iGregTechTileEntity hatch
     * @param textureIndex requested texture index, or null if not...
     * @return managed to add hatch (structure still valid)
     */
    boolean apply(MultiBlock multiBlock, IGregTechTileEntity iGregTechTileEntity, int textureIndex);
}
