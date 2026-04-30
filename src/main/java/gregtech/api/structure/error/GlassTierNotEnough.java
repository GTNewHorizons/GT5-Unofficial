package gregtech.api.structure.error;

import static gregtech.api.enums.GTValues.VN;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.StructureErrorId;

@Desugar
public record GlassTierNotEnough(int requiredTier) implements StructureError {

    @Override
    public StructureErrorId getId() {
        return StructureErrorId.GLASS_TIER_NOT_ENOUGH;
    }

    @Override
    public void serialize(PacketBuffer buffer) throws IOException {
        buffer.writeInt(requiredTier);
    }

    @Override
    public StructureError deserialize(PacketBuffer buffer) throws IOException {
        return new GlassTierNotEnough(buffer.readInt());
    }

    @Override
    public IWidget createWidget() {
        return IKey.lang("GT5U.gui.text.glass_tier_not_enough", VN[requiredTier])
            .asWidget();
    }

    @Override
    public StructureError copy() {
        return new GlassTierNotEnough(requiredTier);
    }
}
