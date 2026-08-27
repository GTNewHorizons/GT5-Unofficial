package tectech.voidcraft.multiblock;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import tectech.voidcraft.ship.VoidcraftComponent;

/**
 * A multiblock component controller found in the assembler's scan volume — its catalog entry and its in-world
 * MTE (the assemblers force the controller's own structure check on this tile during the scan audit).
 */
public final class MultiblockControllerRef {

    public final VoidcraftComponent entry;
    public final IMetaTileEntity mte;
    public final IGregTechTileEntity tile;

    public MultiblockControllerRef(VoidcraftComponent entry, IMetaTileEntity mte, IGregTechTileEntity tile) {
        this.entry = entry;
        this.mte = mte;
        this.tile = tile;
    }
}
