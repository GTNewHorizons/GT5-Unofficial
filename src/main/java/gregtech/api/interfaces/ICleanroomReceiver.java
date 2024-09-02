package gregtech.api.interfaces;

import javax.annotation.Nullable;

import net.minecraft.tileentity.TileEntity;

import gregtech.common.Pollution;

/**
 * Implement this interface for TileEntities that can have association to cleanroom.
 * Calling {@link Pollution#addPollution(TileEntity, int)} from this machine
 * will pollute associated cleanroom.
 */
public interface ICleanroomReceiver {

    @Nullable
    ICleanroom getCleanroom();

    void setCleanroom(ICleanroom cleanroom);
}
