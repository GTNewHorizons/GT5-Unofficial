package gregtech.api.structure.error;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.github.bsideup.jabel.Desugar;

@Desugar
record LangText(String key, TranslatableText[] args) implements TranslatableText {

    static final int DISCRIMINATOR = 0;

    @Override
    public String translate() {
        Object[] resolved = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            resolved[i] = args[i].translate();
        }
        return StatCollector.translateToLocalFormatted(key, resolved);
    }

    @Override
    public IKey toIKey() {
        return IKey.dynamic(this::translate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LangText other)) return false;
        return Objects.equals(key, other.key) && Arrays.equals(args, other.args);
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hashCode(key) + Arrays.hashCode(args);
    }

    @Override
    public void serialize(PacketBuffer buffer) throws IOException {
        buffer.writeByte(DISCRIMINATOR);
        buffer.writeStringToBuffer(key);
        buffer.writeInt(args.length);
        for (TranslatableText arg : args) {
            arg.serialize(buffer);
        }
    }

    static TranslatableText read(PacketBuffer buffer) throws IOException {
        String key = buffer.readStringFromBuffer(32767);
        int count = buffer.readInt();
        TranslatableText[] args = new TranslatableText[count];
        for (int i = 0; i < count; i++) {
            args[i] = TranslatableText.deserialize(buffer);
        }
        return new LangText(key, args);
    }
}
