package gregtech.api.structure.error;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.StructureErrorId;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

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
    public IWidget createWidget(MTEMultiBlockBaseGui<?> gui) {
        return IKey.lang("GT5U.gui.text.dt_missing_output_hatch", layer)
            .asWidget();
    }

    @Override
    public String getDisplayString() {
        return net.minecraft.util.StatCollector
            .translateToLocalFormatted("GT5U.gui.text.dt_missing_output_hatch", layer);
    }

    @Override
    public StructureError copy() {
        return new MissingOutputHatchDT(layer);
    }
}
