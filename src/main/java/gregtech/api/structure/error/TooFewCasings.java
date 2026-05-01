package gregtech.api.structure.error;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.StructureErrorId;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

@Desugar
public record TooFewCasings(int current, int required) implements StructureError {

    @Override
    public StructureErrorId getId() {
        return StructureErrorId.TOO_FEW_CASINGS;
    }

    @Override
    public void serialize(PacketBuffer buffer) throws IOException {
        buffer.writeInt(current);
        buffer.writeInt(required);
    }

    @Override
    public StructureError deserialize(PacketBuffer buffer) throws IOException {
        return new TooFewCasings(buffer.readInt(), buffer.readInt());
    }

    @Override
    public IWidget createWidget(MTEMultiBlockBaseGui<?> gui) {
        return IKey.lang("GT5U.gui.missing_casings", required, current)
            .asWidget();
    }

    @Override
    public String getDisplayString() {
        return net.minecraft.util.StatCollector
            .translateToLocalFormatted("GT5U.gui.missing_casings", required, current);
    }

    @Override
    public StructureError copy() {
        return new TooFewCasings(current, required);
    }
}
