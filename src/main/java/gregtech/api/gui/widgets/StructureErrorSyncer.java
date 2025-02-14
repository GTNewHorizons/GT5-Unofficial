package gregtech.api.gui.widgets;

import java.util.BitSet;
import java.util.EnumSet;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;

import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import gregtech.api.enums.StructureError;

public class StructureErrorSyncer extends FakeSyncWidget<EnumSet<StructureError>> {

    public StructureErrorSyncer(Supplier<EnumSet<StructureError>> getter, Consumer<EnumSet<StructureError>> setter) {
        super(getter, setter, StructureErrorSyncer::save, StructureErrorSyncer::load);
    }

    private static void save(PacketBuffer buffer, EnumSet<StructureError> errors) {
        BitSet bits = new BitSet();

        for (StructureError error : errors) {
            bits.set(error.ordinal());
        }

        byte[] data = bits.toByteArray();

        buffer.writeVarIntToBuffer(data.length);
        buffer.writeBytes(data);
    }

    private static EnumSet<StructureError> load(PacketBuffer buffer) {
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
}
