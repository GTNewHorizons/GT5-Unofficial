package gregtech.api.multitileentity.interfaces;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import net.minecraft.util.ChunkCoordinates;

public interface IMultiBlockPart extends IMultiTileEntity {
    ChunkCoordinates getTargetPos();

    void setTargetPos(ChunkCoordinates aTargetPos);

    void setLockedInventoryIndex(int aIndex);

    int getLockedInventoryIndex();

    boolean tickCoverAtSide(byte aSide, long aTickTimer);

    public interface IMBP_InventoryUpgrade {
        String getInventoryName();

        IItemHandlerModifiable getInventory();
    }
}
