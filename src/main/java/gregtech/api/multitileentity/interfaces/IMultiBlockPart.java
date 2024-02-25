package gregtech.api.multitileentity.interfaces;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.ChunkCoordinates;

import gregtech.api.logic.interfaces.FluidInventoryLogicHost;
import gregtech.api.logic.interfaces.ItemInventoryLogicHost;

public interface IMultiBlockPart extends IMultiTileEntity, ItemInventoryLogicHost, FluidInventoryLogicHost {

    @Nonnull
    ChunkCoordinates getTargetPos();

    void setTargetPos(@Nonnull final ChunkCoordinates targetPos);

    @Nullable
    UUID getLockedInventory();

    boolean shouldTick(final long tickTimer);
}
