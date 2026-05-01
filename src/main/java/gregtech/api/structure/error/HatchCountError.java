package gregtech.api.structure.error;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.HatchElement;
import gregtech.api.enums.StructureErrorId;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

@Desugar
public record HatchCountError(ErrorType type, HatchElement hatch, int current, int target) implements StructureError {

    @Override
    public StructureErrorId getId() {
        return StructureErrorId.HATCH_COUNT_ERROR;
    }

    @Override
    public void serialize(PacketBuffer buffer) throws IOException {
        buffer.writeInt(type.ordinal());
        buffer.writeInt(hatch.ordinal());
        buffer.writeInt(current);
        buffer.writeInt(target);
    }

    @Override
    public StructureError deserialize(PacketBuffer buffer) throws IOException {
        return new HatchCountError(
            ErrorType.from(buffer.readInt()),
            HatchElement.fromOrdinal(buffer.readInt()),
            buffer.readInt(),
            buffer.readInt());
    }

    @Override
    public IWidget createWidget(MTEMultiBlockBaseGui<?> gui) {
        return switch (type) {
            case TOO_FEW -> {
                if (target == 1) {
                    yield IKey.lang("GT5U.gui.missing_hatch", hatch.getDisplayName())
                        .asWidget();
                } else {
                    yield IKey.lang("GT5U.gui.text.too_few_hatch", hatch.getDisplayName(), target, current)
                        .asWidget();
                }
            }
            case NOT_MATCH -> IKey.lang("GT5U.gui.text.not_match_hatch", target, hatch.getDisplayName())
                .asWidget();
            case TOO_MANY -> IKey.lang("GT5U.gui.text.too_many_hatch", hatch.getDisplayName(), target, current)
                .asWidget();
        };
    }

    @Override
    public String getDisplayString() {
        return switch (type) {
            case TOO_FEW -> {
                if (target == 1) {
                    yield StatCollector.translateToLocalFormatted("GT5U.gui.missing_hatch", hatch.getDisplayName());
                } else {
                    yield StatCollector.translateToLocalFormatted(
                        "GT5U.gui.text.too_few_hatch",
                        hatch.getDisplayName(),
                        target,
                        current);
                }
            }
            case NOT_MATCH -> StatCollector
                .translateToLocalFormatted("GT5U.gui.text.not_match_hatch", target, hatch.getDisplayName());
            case TOO_MANY -> StatCollector
                .translateToLocalFormatted("GT5U.gui.text.too_many_hatch", hatch.getDisplayName(), target, current);
        };
    }

    @Override
    public StructureError copy() {
        return new HatchCountError(type, hatch, current, target);
    }
}
