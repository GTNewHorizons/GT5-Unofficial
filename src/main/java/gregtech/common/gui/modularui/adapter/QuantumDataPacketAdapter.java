package gregtech.common.gui.modularui.adapter;

import java.io.IOException;
import java.util.Objects;

import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;

import tectech.mechanics.dataTransport.QuantumDataPacket;

public class QuantumDataPacketAdapter implements IByteBufAdapter<QuantumDataPacket> {

    @Override
    public @Nullable QuantumDataPacket deserialize(PacketBuffer buffer) throws IOException {
        if (!buffer.readBoolean()) return null;
        return new QuantumDataPacket(buffer.readLong());
    }

    @Override
    public void serialize(PacketBuffer buffer, @Nullable QuantumDataPacket u) throws IOException {
        if (u != null) {
            buffer.writeBoolean(true);
            buffer.writeLong(u.getContent());
        } else buffer.writeBoolean(false);
    }

    @Override
    public boolean areEqual(@Nullable QuantumDataPacket t1, @Nullable QuantumDataPacket t2) {
        return t1 == null && t2 == null || t1 != null && t2 != null && Objects.equals(t1.getContent(), t2.getContent());
    }
}
