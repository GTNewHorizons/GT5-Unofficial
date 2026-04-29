package gregtech.api.structure.error;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.github.bsideup.jabel.Desugar;
import gregtech.api.enums.StructureErrorId;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

@Desugar
public record MissingOutputHatchDT(int layer) implements StructureError {

    @Override
    public StructureErrorId getId() {
        return StructureErrorId.MISSING_OUTPUT_HATCH_DT;
    }

    @Override
    public void serialize(PacketBuffer buffer) throws IOException {
        buffer.writeInt(layer);
    }

    @Override
    public StructureError deserialize(PacketBuffer buffer) throws IOException {
        return new MissingOutputHatchDT(buffer.readInt());
    }

    @Override
    public IWidget createWidget() {
        return IKey.lang("GT5U.gui.text.dt_missing_output_hatch", layer).asWidget();
    }

    @Override
    public StructureError copy() {
        return new MissingOutputHatchDT(layer);
    }
}
