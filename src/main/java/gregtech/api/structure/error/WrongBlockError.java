package gregtech.api.structure.error;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.StructureErrorId;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

@Desugar
public record WrongBlockError(int x, int y, int z, String description) implements StructureError {

    public WrongBlockError(int x, int y, int z) {
        this(x, y, z, null);
    }

    @Override
    public StructureErrorId getId() {
        return StructureErrorId.WRONG_BLOCK;
    }

    @Override
    public void serialize(PacketBuffer buffer) throws IOException {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeBoolean(description != null);
        if (description != null) {
            buffer.writeStringToBuffer(description);
        }
    }

    @Override
    public StructureError deserialize(PacketBuffer buffer) throws IOException {
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        String desc = buffer.readBoolean() ? buffer.readStringFromBuffer(32767) : null;
        return new WrongBlockError(x, y, z, desc);
    }

    /**
     * Translates the description on the client. The description is a lang key (e.g. an unlocalized item name) so that
     * clients with a different language than the server see the correct translation.
     */
    private String translateDescription() {
        return StatCollector.translateToLocal(description);
    }

    @Override
    public IWidget createWidget(MTEMultiBlockBaseGui<?> gui) {
        return Flow.row()
            .widthRel(1)
            .coverChildrenHeight(0)
            .crossAxisAlignment(Alignment.CrossAxis.CENTER)
            .child(gui.createHighlightButton(x, y, z))
            .child(
                (description != null ? IKey.lang("GT5U.gui.wrong_block_expected", translateDescription(), x, y, z)
                    : IKey.lang("GT5U.gui.wrong_block", x, y, z)).asWidget()
                        .expanded());
    }

    @Override
    public String getDisplayString() {
        if (description != null) {
            return StatCollector
                .translateToLocalFormatted("GT5U.gui.wrong_block_expected", translateDescription(), x, y, z);
        }
        return StatCollector.translateToLocalFormatted("GT5U.gui.wrong_block", x, y, z);
    }

    @Override
    public StructureError copy() {
        return new WrongBlockError(x, y, z, description);
    }
}
