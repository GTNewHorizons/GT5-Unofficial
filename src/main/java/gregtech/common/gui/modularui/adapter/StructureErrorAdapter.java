package gregtech.common.gui.modularui.adapter;

import java.util.BitSet;
import java.util.EnumSet;

import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;

import gregtech.api.enums.StructureError;

public class StructureErrorAdapter implements IByteBufAdapter<EnumSet<StructureError>> {

    @Override
    public EnumSet<StructureError> deserialize(PacketBuffer buffer) {
        byte[] data = new byte[buffer.readVarIntFromBuffer()];
        buffer.readBytes(data);

        BitSet bits = BitSet.valueOf(data);

        EnumSet<StructureError> out = EnumSet.noneOf(StructureError.class);

        for (StructureError error : StructureError.values()) {
            if (bits.get(error.ordinal())) {
                out.add(error);
            }
        }

        return out;
    }

    @Override
    public void serialize(PacketBuffer buffer, EnumSet<StructureError> errors) {
        BitSet bits = new BitSet();

        for (StructureError error : errors) {
            bits.set(error.ordinal());
        }

        byte[] data = bits.toByteArray();

        buffer.writeVarIntToBuffer(data.length);
        buffer.writeBytes(data);
    }

    @Override
    public boolean areEqual(@NotNull EnumSet<StructureError> t1, @NotNull EnumSet<StructureError> t2) {
        return t1.containsAll(t2) && t2.containsAll(t1);
    }
}
