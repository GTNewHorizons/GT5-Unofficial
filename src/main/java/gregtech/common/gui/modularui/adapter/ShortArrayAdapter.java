package gregtech.common.gui.modularui.adapter;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;

public class ShortArrayAdapter implements IByteBufAdapter<short[]> {

    @Override
    public short[] deserialize(PacketBuffer buffer) throws IOException {
        short[] u = new short[buffer.readVarIntFromBuffer()];
        for (int i = 0; i < u.length; i++) {
            u[i] = buffer.readShort();
        }
        return u;
    }

    @Override
    public void serialize(PacketBuffer buffer, short[] u) throws IOException {
        buffer.writeVarIntToBuffer(u.length);
        for (short i : u) {
            buffer.writeShort(i);
        }
    }

    @Override
    public boolean areEqual(short @NotNull [] t1, short @NotNull [] t2) {
        if (t1.length != t2.length) return false;
        for (int i = 0; i < t1.length; i++) {
            if (t1[i] != t2[i]) return false;
        }
        return true;
    }
}
