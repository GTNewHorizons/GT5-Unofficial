package gregtech.api.structure.error;

import java.io.IOException;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.github.bsideup.jabel.Desugar;

@Desugar
record ItemNameText(int itemId, int itemMeta) implements TranslatableText {

    static final int DISCRIMINATOR = 2;

    @Override
    public String translate() {
        return new ItemStack(Item.getItemById(itemId), 1, itemMeta).getDisplayName();
    }

    @Override
    public IKey toIKey() {
        return IKey.str(translate());
    }

    @Override
    public void serialize(PacketBuffer buffer) throws IOException {
        buffer.writeByte(DISCRIMINATOR);
        buffer.writeInt(itemId);
        buffer.writeInt(itemMeta);
    }

    static TranslatableText read(PacketBuffer buffer) throws IOException {
        return new ItemNameText(buffer.readInt(), buffer.readInt());
    }
}
