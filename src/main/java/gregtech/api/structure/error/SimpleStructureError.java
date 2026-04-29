package gregtech.api.structure.error;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.github.bsideup.jabel.Desugar;
import gregtech.api.enums.StructureErrorId;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

@Desugar
public record SimpleStructureError(String lang_key) implements StructureError {

    @Override
    public StructureErrorId getId() {
        return StructureErrorId.SIMPLE_STRUCTURE_ERROR;
    }

    @Override
    public void serialize(PacketBuffer buffer) throws IOException {
        buffer.writeStringToBuffer(lang_key);
    }

    @Override
    public StructureError deserialize(PacketBuffer buffer) throws IOException {
        return new SimpleStructureError(buffer.readStringFromBuffer(32767));
    }

    @Override
    public IWidget createWidget() {
        return IKey.lang(lang_key).asWidget();
    }

    @Override
    public StructureError copy() {
        // this object is completely immutable
        return this;
    }
}
