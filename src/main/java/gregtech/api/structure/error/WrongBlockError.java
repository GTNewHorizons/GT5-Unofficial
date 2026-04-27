package gregtech.api.structure.error;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.StructureErrorId;

@Desugar
public record WrongBlockError(int x, int y, int z) implements StructureError {

    static {
        StructureErrorRegistry.register(new WrongBlockError(0, 0, 0));
    }

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
    public IWidget createWidget() {
        return IKey.lang("GT5U.gui.wrong_block", x, y, z)
            .asWidget();
    }

    @Override
    public StructureError copy() {
        return new WrongBlockError(x, y, z);
    }
}
