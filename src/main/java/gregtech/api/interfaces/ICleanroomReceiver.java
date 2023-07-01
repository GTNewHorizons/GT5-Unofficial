package gregtech.api.interfaces;

import javax.annotation.Nullable;

import net.minecraft.tileentity.TileEntity;

/**
 * Implement this interface for TileEntities that can have association to cleanroom.
 * Calling {@link gregtech.common.GT_Pollution#addPollution(TileEntity, int)} from this machine
 * will pollute associated cleanroom.
 */
public interface ICleanroomReceiver {

    @Nullable
    ICleanroom getCleanroom();

    void setCleanroom(ICleanroom cleanroom);
}
