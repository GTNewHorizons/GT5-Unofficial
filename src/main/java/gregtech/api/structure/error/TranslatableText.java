package gregtech.api.structure.error;

import java.io.IOException;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.drawable.IKey;

import gregtech.api.enums.HatchElement;

public interface TranslatableText {

    /** Produce the translated string. Client-side only. */
    String translate();

    /** Produce an IKey for ModularUI widget rendering. Client-side only. */
    IKey toIKey();

    void serialize(PacketBuffer buffer) throws IOException;

    // --- Factory methods ---

    static TranslatableText lang(String key, TranslatableText... args) {
        return new LangText(key, args);
    }

    static TranslatableText literal(String text) {
        return new LiteralText(text);
    }

    static TranslatableText literal(int value) {
        return new LiteralText(String.valueOf(value));
    }

    static TranslatableText itemName(ItemStack stack) {
        return new ItemNameText(Item.getIdFromItem(stack.getItem()), stack.getItemDamage());
    }

    static TranslatableText itemName(int itemId, int itemMeta) {
        return new ItemNameText(itemId, itemMeta);
    }

    static TranslatableText hatchName(HatchElement element) {
        return new HatchNameText(element.ordinal());
    }

    // --- Deserialization ---

    static TranslatableText deserialize(PacketBuffer buffer) throws IOException {
        int type = buffer.readByte();
        return switch (type) {
            case LangText.DISCRIMINATOR -> LangText.read(buffer);
            case LiteralText.DISCRIMINATOR -> LiteralText.read(buffer);
            case ItemNameText.DISCRIMINATOR -> ItemNameText.read(buffer);
            case HatchNameText.DISCRIMINATOR -> HatchNameText.read(buffer);
            default -> throw new IOException("Unknown TranslatableText discriminator: " + type);
        };
    }
}
