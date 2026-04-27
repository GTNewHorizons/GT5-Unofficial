package gregtech.common.gui.modularui.adapter;

import java.util.BitSet;
import java.util.EnumSet;

import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;

import gregtech.api.enums.StructureErrorId;

public class StructureErrorAdapter implements IByteBufAdapter<EnumSet<StructureErrorId>> {

    @Override
    public EnumSet<StructureErrorId> deserialize(PacketBuffer buffer) {
        byte[] data = new byte[buffer.readVarIntFromBuffer()];
        buffer.readBytes(data);

        BitSet bits = BitSet.valueOf(data);

        EnumSet<StructureErrorId> out = EnumSet.noneOf(StructureErrorId.class);

        for (StructureErrorId error : StructureErrorId.values()) {
            if (bits.get(error.ordinal())) {
                out.add(error);
            }
        }

        return out;
    }

    @Override
    public void serialize(PacketBuffer buffer, EnumSet<StructureErrorId> errors) {
        BitSet bits = new BitSet();

        for (StructureErrorId error : errors) {
            bits.set(error.ordinal());
        }

        byte[] data = bits.toByteArray();

        buffer.writeVarIntToBuffer(data.length);
        buffer.writeBytes(data);
    }

    @Override
    public boolean areEqual(@NotNull EnumSet<StructureErrorId> t1, @NotNull EnumSet<StructureErrorId> t2) {
        return t1.containsAll(t2) && t2.containsAll(t1);
    }
}
