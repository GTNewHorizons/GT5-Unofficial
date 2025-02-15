package gregtech.api.capability;

import com.gtnewhorizon.gtnhlib.capability.Capability;
import com.gtnewhorizon.gtnhlib.capability.CapabilityRegistry;

import gregtech.api.interfaces.ICleanroomReceiver;

public final class GTCapabilities {

    /**
     * Indicates the ability to behave as {@link ICleanroomReceiver}.
     */
    public static Capability<ICleanroomReceiver> CAPABILITY_CLEANROOM_RECEIVER = CapabilityRegistry.INSTANCE
        .create(ICleanroomReceiver.class);

    public static void init() {}
}
