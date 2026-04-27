package gregtech.api.gui.widgets;

import java.util.BitSet;
import java.util.EnumSet;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;

import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import gregtech.api.enums.StructureErrorId;

public class StructureErrorSyncer extends FakeSyncWidget<EnumSet<StructureErrorId>> {

    public StructureErrorSyncer(Supplier<EnumSet<StructureErrorId>> getter,
        Consumer<EnumSet<StructureErrorId>> setter) {
        super(getter, setter, StructureErrorSyncer::save, StructureErrorSyncer::load);
    }

    private static void save(PacketBuffer buffer, EnumSet<StructureErrorId> errors) {
        BitSet bits = new BitSet();

        for (StructureErrorId error : errors) {
            bits.set(error.ordinal());
        }

        byte[] data = bits.toByteArray();

        buffer.writeVarIntToBuffer(data.length);
        buffer.writeBytes(data);
    }

    private static EnumSet<StructureErrorId> load(PacketBuffer buffer) {
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
}
