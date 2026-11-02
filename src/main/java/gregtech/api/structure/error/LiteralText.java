package gregtech.api.structure.error;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.github.bsideup.jabel.Desugar;

@Desugar
record LiteralText(String text) implements TranslatableText {

    static final int DISCRIMINATOR = 1;

    @Override
    public String translate() {
        return text;
    }

    @Override
    public IKey toIKey() {
        return IKey.str(text);
    }

    @Override
    public void serialize(PacketBuffer buffer) throws IOException {
        buffer.writeByte(DISCRIMINATOR);
        buffer.writeStringToBuffer(text);
    }

    static TranslatableText read(PacketBuffer buffer) throws IOException {
        return new LiteralText(buffer.readStringFromBuffer(32767));
    }
}
