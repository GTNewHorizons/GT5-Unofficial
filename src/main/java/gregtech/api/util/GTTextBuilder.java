package gregtech.api.util;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GOLD;

import java.util.ArrayList;

import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;

public class GTTextBuilder {

    public String langKey;
    public ArrayList<String> values = new ArrayList<>();
    public EnumChatFormatting base = EnumChatFormatting.WHITE;

    public GTTextBuilder(String langKey) {
        this.langKey = langKey;
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

    public GTTextBuilder addName(String name) {
        add(EnumChatFormatting.DARK_AQUA, name);
        return this;
    }

    public GTTextBuilder addCoord(int x, int y, int z) {
        values.add("X="
            + GOLD
            + GTUtility.formatNumbers(x)
            + base
            + " Y="
            + GOLD
            + GTUtility.formatNumbers(y)
            + base
            + " Z="
            + GOLD
            + GTUtility.formatNumbers(z)
            + base);
        return this;
    }

    public GTTextBuilder addCoord(IHasWorldObjectAndCoords machine) {
        addCoord(machine.getXCoord(), machine.getYCoord(), machine.getZCoord());
        return this;
    }

    public GTTextBuilder addValue(String value) {
        add(EnumChatFormatting.GREEN, value);
        return this;
    }

    public GTTextBuilder addNumber(int i) {
        add(EnumChatFormatting.GOLD, GTUtility.formatNumbers(i));
        return this;
    }

    public GTTextBuilder addNumber(long l) {
        add(EnumChatFormatting.GOLD, GTUtility.formatNumbers(l));
        return this;
    }

    public GTTextBuilder addNumber(float f) {
        add(EnumChatFormatting.GOLD, GTUtility.formatNumbers(f));
        return this;
    }

    public GTTextBuilder addNumber(double d) {
        add(EnumChatFormatting.GOLD, GTUtility.formatNumbers(d));
        return this;
    }

    public GTTextBuilder addNumber(String s) {
        add(EnumChatFormatting.GOLD, s);
        return this;
    }

    @Override
    public String toString() {
        return base.toString() + GTUtility.translate(langKey, values.toArray());
    }
}
