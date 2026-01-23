package gregtech.api.util;

import java.util.ArrayList;

import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.enums.ChatMessage;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.net.GTPacketChat;

/**
 * A text builder meant for use with complex localization formats. All formatting is handled by this text builder, so
 * localization keys should just include the text. All values are converted to strings when localized. Localization is
 * deferred as long as possible, and may be deferred to the client if {@link #toLocalized()} is used.
 * 
 * @see gregtech.api.enums.ChatMessage
 * @see GTPacketChat
 * @see Localized
 * @see GTUtility#processFormatStacks(String)
 */
public class GTTextBuilder {

    public static final EnumChatFormatting NAME = EnumChatFormatting.DARK_AQUA;
    public static final EnumChatFormatting NUMERIC = EnumChatFormatting.GOLD;
    public static final EnumChatFormatting VALUE = EnumChatFormatting.GREEN;

    public Object key;
    public ArrayList<Object> values = new ArrayList<>();
    public EnumChatFormatting base = EnumChatFormatting.WHITE;

    public GTTextBuilder(String langKey) {
        this.key = langKey;
    }

    public GTTextBuilder(ChatMessage message) {
        this.key = message;
    }

    public GTTextBuilder setBase(EnumChatFormatting base) {
        this.base = base;
        return this;
    }

    public GTTextBuilder add(@Nullable Object style, @NotNull Object data) {
        values.add((style == null ? "" : style.toString()) + data + base);

        return this;
    }

    public GTTextBuilder addText(String text) {
        add(base, text);
        return this;
    }

    public GTTextBuilder addLocalized(Localized l) {
        values.add(l);
        return this;
    }

    public GTTextBuilder addName(String name) {
        add(NAME, name);
        return this;
    }

    public GTTextBuilder addCoord(int x, int y, int z) {
        values.add(
            "X=" + NUMERIC
                + formatNumber(x)
                + base
                + " Y="
                + NUMERIC
                + formatNumber(y)
                + base
                + " Z="
                + NUMERIC
                + formatNumber(z)
                + base);
        return this;
    }

    public GTTextBuilder addCoord(IHasWorldObjectAndCoords machine) {
        addCoord(machine.getXCoord(), machine.getYCoord(), machine.getZCoord());
        return this;
    }

    public GTTextBuilder addValue(String value) {
        add(VALUE, value);
        return this;
    }

    public GTTextBuilder addNumber(int i) {
        add(NUMERIC, formatNumber(i));
        return this;
    }

    public GTTextBuilder addNumber(long l) {
        add(NUMERIC, formatNumber(l));
        return this;
    }

    public GTTextBuilder addNumber(float f) {
        add(NUMERIC, formatNumber(f));
        return this;
    }

    public GTTextBuilder addNumber(double d) {
        add(NUMERIC, formatNumber(d));
        return this;
    }

    public GTTextBuilder addNumber(String s) {
        add(NUMERIC, s);
        return this;
    }

    public Localized toLocalized() {
        return new Localized(key, values.toArray()).setBase(base);
    }

    @Override
    public String toString() {
        return toLocalized().toString();
    }
}
