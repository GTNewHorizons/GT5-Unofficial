package gregtech.api.interfaces;

import javax.annotation.Nullable;

import net.minecraft.tileentity.TileEntity;

import gregtech.common.pollution.Pollution;

/**
 * Implement this interface for machines that can have association to cleanroom.
 * Calling {@link Pollution#addPollution(TileEntity, int)} from this machine
 * will pollute associated cleanroom.
 * Other than directly implementing this interface for TileEntity, you can also pass capability via
 * {@link com.gtnewhorizon.gtnhlib.capability.CapabilityProvider#getCapability CapabilityProvider.getCapability}.
 */
public interface ICleanroomReceiver {

    @Nullable
    ICleanroom getCleanroom();

    void setCleanroom(ICleanroom cleanroom);
}
