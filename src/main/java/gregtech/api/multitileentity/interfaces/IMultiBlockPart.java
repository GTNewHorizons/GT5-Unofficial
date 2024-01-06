package gregtech.api.multitileentity.interfaces;

import java.util.UUID;

import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.logic.interfaces.FluidInventoryLogicHost;
import gregtech.api.logic.interfaces.ItemInventoryLogicHost;

public interface IMultiBlockPart extends IMultiTileEntity, ItemInventoryLogicHost, FluidInventoryLogicHost {

    ChunkCoordinates getTargetPos();

    void setTargetPos(ChunkCoordinates aTargetPos);

    void setLockedInventoryIndex(int aIndex);

    int getLockedInventoryIndex();

    UUID getLockedInventory();

    boolean tickCoverAtSide(ForgeDirection side, long aTickTimer);

    boolean shouldTick(long tickTimer);
}
