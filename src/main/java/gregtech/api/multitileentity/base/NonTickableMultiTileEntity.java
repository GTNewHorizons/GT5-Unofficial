package gregtech.api.multitileentity.base;

import net.minecraft.network.Packet;

public abstract class NonTickableMultiTileEntity extends MultiTileEntity {

    boolean mConstructed = false; // Keeps track of whether this TE has been constructed and placed in the world

    public NonTickableMultiTileEntity() {
        super(false);
    }
}
