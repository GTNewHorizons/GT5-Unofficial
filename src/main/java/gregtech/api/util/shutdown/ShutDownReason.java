package gregtech.api.util.shutdown;

import javax.annotation.Nonnull;

import net.minecraft.network.PacketBuffer;

public interface ShutDownReason {

    /**
     * @return Unique registry ID
     */
    @Nonnull
    String getID();

    /**
     * @return Actual text to show on client GUI
     */
    @Nonnull
    String getDisplayString();

    /**
     * Create new instance to receive packet.
     */
    @Nonnull
    ShutDownReason newInstance();

    /**
     * Encode value to sync.
     */
    void encode(@Nonnull PacketBuffer buffer);

    /**
     * Decode synced value.
     */
    void decode(PacketBuffer buffer);

    /**
     * @return Whether the reason is critical.
     */
    boolean wasCritical();
}
