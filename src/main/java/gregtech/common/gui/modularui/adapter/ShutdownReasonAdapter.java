package gregtech.common.gui.modularui.adapter;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;

public class ShutdownReasonAdapter implements IByteBufAdapter<ShutDownReason> {

    @Override
    public ShutDownReason deserialize(PacketBuffer buffer) throws IOException {
        String id = NetworkUtils.readStringSafe(buffer);
        ShutDownReason result = ShutDownReasonRegistry.getSampleFromRegistry(id)
            .newInstance();
        result.decode(buffer);
        return result;
    }

    @Override
    public void serialize(PacketBuffer buffer, ShutDownReason result) throws IOException {
        NetworkUtils.writeStringSafe(buffer, result.getID());
        result.encode(buffer);
    }

    @Override
    public boolean areEqual(@NotNull ShutDownReason t1, @NotNull ShutDownReason t2) {
        return t1.getID()
            .equals(t2.getID());
    }
}
