package gregtech.api.multitileentity.interfaces;

import net.minecraft.util.ChunkCoordinates;

public interface IMultiBlockPart extends IMultiTileEntity {

    ChunkCoordinates getTargetPos();

    void setTargetPos(ChunkCoordinates aTargetPos);

    void setLockedInventoryIndex(int aIndex);

    int getLockedInventoryIndex();

    boolean tickCoverAtSide(ForgeDirection aSide, long aTickTimer);
}
