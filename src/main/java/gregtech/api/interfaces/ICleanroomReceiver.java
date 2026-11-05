package gregtech.api.interfaces;

import javax.annotation.Nullable;

import net.minecraft.tileentity.TileEntity;

import gregtech.common.pollution.Pollution;

/**
 * Implement this interface for machines that can have association to cleanroom. Calling
 * {@link Pollution#addPollution(TileEntity, int)} from this machine will pollute associated cleanroom. Instead of
 * directly implementing this interface for TileEntity, you can also expose capability implementation via
 * {@link com.gtnewhorizon.gtnhlib.capability.CapabilityProvider#getCapability CapabilityProvider.getCapability}.
 */
public interface ICleanroomReceiver {

    /**
     * Returns the associated room used for processing
     * Multi-room receivers select the cleanest valid room
     */
    @Nullable
    ICleanroom getCleanroom();

    /**
     * Replaces all associations with one room
     * Pass null to clear all associations
     */
    void setCleanroom(@Nullable ICleanroom cleanroom);

    /**
     * Associates a room
     * Multi-room receivers keep their other associations
     * The default implementation replaces the single-room association
     */
    default void addCleanroom(ICleanroom cleanroom) {
        setCleanroom(cleanroom);
    }

    /**
     * Removes only the specified room
     * Receivers supporting multiple rooms must override both membership methods
     */
    default void removeCleanroom(ICleanroom cleanroom) {
        if (getCleanroom() == cleanroom) setCleanroom(null);
    }

    /**
     * Called on the server thread after an associated room changes availability
     */
    default void onCleanroomChanged() {}
}
