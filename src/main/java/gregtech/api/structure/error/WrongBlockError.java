package gregtech.api.structure.error;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.StructureErrorId;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

@Desugar
public record WrongBlockError(int x, int y, int z) implements StructureError {

    @Override
    public StructureErrorId getId() {
        return StructureErrorId.WRONG_BLOCK;
    }

    @Override
    public void serialize(PacketBuffer buffer) {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
    }

    @Override
    public StructureError deserialize(PacketBuffer buffer) {
        return new WrongBlockError(buffer.readInt(), buffer.readInt(), buffer.readInt());
    }

    @Override
    public IWidget createWidget(MTEMultiBlockBaseGui<?> gui) {
        return Flow.row()
            .widthRel(1)
            .coverChildrenHeight(0)
            .crossAxisAlignment(Alignment.CrossAxis.CENTER)
            .child(gui.createHighlightButton(x, y, z))
            .child(
                IKey.lang("GT5U.gui.wrong_block", x, y, z)
                    .asWidget()
                    .expanded());
    }

    @Override
    public StructureError copy() {
        return new WrongBlockError(x, y, z);
    }
}
