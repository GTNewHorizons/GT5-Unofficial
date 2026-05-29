package gregtech.api.structure.error;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.enums.StructureErrorId;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

public class PositionedStructureError implements StructureError {

    private static final int TEXT_COLOR = 0xFFE0E0E0;

    private final int x;
    private final int y;
    private final int z;
    private final List<String> expectedBlocks;

    public PositionedStructureError(int x, int y, int z) {
        this(x, y, z, Collections.emptyList());
    }

    public PositionedStructureError(int x, int y, int z, List<String> expectedBlocks) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.expectedBlocks = expectedBlocks;
    }

    @Override
    public StructureErrorId getId() {
        return StructureErrorId.POSITIONED_ERROR;
    }

    @Override
    public void serialize(PacketBuffer buffer) throws IOException {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeInt(expectedBlocks.size());
        for (String s : expectedBlocks) {
            buffer.writeStringToBuffer(s);
        }
    }

    @Override
    public StructureError deserialize(PacketBuffer buffer) throws IOException {
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        int count = buffer.readInt();
        List<String> blocks = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            blocks.add(buffer.readStringFromBuffer(256));
        }
        return new PositionedStructureError(x, y, z, blocks);
    }

    @Override
    public IWidget createWidget(MTEMultiBlockBaseGui<?> gui) {
        String text = buildDisplayText();
        boolean multiLine = expectedBlocks.size() > 1;

        return Flow.row()
            .widthRel(1)
            .coverChildrenHeight(0)
            .crossAxisAlignment(multiLine ? Alignment.CrossAxis.START : Alignment.CrossAxis.CENTER)
            .child(gui.createHighlightButton(x, y, z))
            .child(
                IKey.str(text)
                    .color(TEXT_COLOR)
                    .alignment(multiLine ? Alignment.TopLeft : Alignment.CenterLeft)
                    .asWidget()
                    .expanded());
    }

    @Override
    public String getDisplayString() {
        return buildDisplayText();
    }

    private String buildDisplayText() {
        if (expectedBlocks.isEmpty()) {
            return StatCollector.translateToLocalFormatted("GT5U.gui.text.structure_error.wrong_block", x, y, z);
        }
        if (expectedBlocks.size() == 1) {
            return StatCollector.translateToLocalFormatted(
                "GT5U.gui.text.structure_error.wrong_block_expected_single",
                StatCollector.translateToLocal(expectedBlocks.get(0)),
                x,
                y,
                z);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(
            StatCollector.translateToLocalFormatted("GT5U.gui.text.structure_error.wrong_block_expected", x, y, z));
        sb.append('\n')
            .append(StatCollector.translateToLocal("GT5U.gui.text.structure_error.any_of"));
        for (String block : expectedBlocks) {
            sb.append('\n')
                .append("- ")
                .append(StatCollector.translateToLocal(block));
        }
        return sb.toString();
    }

    @Override
    public StructureError copy() {
        return new PositionedStructureError(x, y, z, new ArrayList<>(expectedBlocks));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PositionedStructureError other)) return false;
        return x == other.x && y == other.y && z == other.z && Objects.equals(expectedBlocks, other.expectedBlocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, expectedBlocks);
    }
}
