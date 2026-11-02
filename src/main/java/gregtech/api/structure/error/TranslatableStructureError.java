package gregtech.api.structure.error;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.utils.Alignment;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.StructureErrorId;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

@Desugar
public record TranslatableStructureError(TranslatableText message) implements StructureError {

    @Override
    public StructureErrorId getId() {
        return StructureErrorId.TRANSLATABLE_ERROR;
    }

    @Override
    public void serialize(PacketBuffer buffer) throws IOException {
        message.serialize(buffer);
    }

    @Override
    public StructureError deserialize(PacketBuffer buffer) throws IOException {
        return new TranslatableStructureError(TranslatableText.deserialize(buffer));
    }

    private static final int TEXT_COLOR = 0xFFE0E0E0;

    @Override
    public IWidget createWidget(MTEMultiBlockBaseGui<?> gui) {
        return message.toIKey()
            .color(TEXT_COLOR)
            .alignment(Alignment.CenterLeft)
            .asWidget();
    }

    @Override
    public String getDisplayString() {
        return message.translate();
    }

    @Override
    public StructureError copy() {
        return new TranslatableStructureError(message);
    }
}
