package gregtech.api.structure.error;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;

import gregtech.api.enums.StructureErrorId;

public record TooManyInputHatch(int current, int max) implements StructureError {

    @Override
    public StructureErrorId getId() {
        return StructureErrorId.TOO_MANY_INPUT_HATCH;
    }

    @Override
    public void serialize(PacketBuffer buffer) throws IOException {
        buffer.writeInt(current);
        buffer.writeInt(max);
    }

    @Override
    public StructureError deserialize(PacketBuffer buffer) throws IOException {
        return new TooManyInputHatch(buffer.readInt(), buffer.readInt());
    }

    @Override
    public IWidget createWidget() {
        return IKey.lang("GT5U.gui.text.too_many_input_hatch", max, current)
            .asWidget();
    }

    @Override
    public StructureError copy() {
        return null;
    }
}
