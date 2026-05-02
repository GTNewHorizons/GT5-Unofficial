package gregtech.api.structure.error;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.HatchElement;

@Desugar
record HatchNameText(int ordinal) implements TranslatableText {

    static final int DISCRIMINATOR = 3;

    @Override
    public String translate() {
        return HatchElement.fromOrdinal(ordinal)
            .getDisplayName();
    }

    @Override
    public IKey toIKey() {
        return IKey.str(translate());
    }

    @Override
    public void serialize(PacketBuffer buffer) throws IOException {
        buffer.writeByte(DISCRIMINATOR);
        buffer.writeInt(ordinal);
    }

    static TranslatableText read(PacketBuffer buffer) throws IOException {
        return new HatchNameText(buffer.readInt());
    }
}
