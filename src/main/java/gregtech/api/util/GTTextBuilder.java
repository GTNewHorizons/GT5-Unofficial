package gregtech.api.util;

import net.minecraft.util.EnumChatFormatting;

import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;

public class GTTextBuilder {

    public StringBuilder builder = new StringBuilder();
    private EnumChatFormatting base = EnumChatFormatting.WHITE;

    public GTTextBuilder setBase(EnumChatFormatting base) {
        this.base = base;
        return this;
    }

    public GTTextBuilder addText(String text) {
        builder.append(text)
            .append(base);
        return this;
    }

    public GTTextBuilder addName(String name) {
        builder.append(EnumChatFormatting.DARK_AQUA)
            .append(name)
            .append(base);
        return this;
    }

    public GTTextBuilder addCoord(int x, int y, int z) {
        builder.append("X=");
        addNumber(x);
        builder.append(" Y=");
        addNumber(y);
        builder.append(" Z=");
        addNumber(z);
        return this;
    }

    public GTTextBuilder addCoord(IHasWorldObjectAndCoords machine) {
        addCoord(machine.getXCoord(), machine.getYCoord(), machine.getZCoord());
        return this;
    }

    public GTTextBuilder addValue(String value) {
        builder.append(EnumChatFormatting.GREEN)
            .append(value)
            .append(base);
        return this;
    }

    public GTTextBuilder addNumber(int i) {
        builder.append(EnumChatFormatting.GOLD)
            .append(GTUtility.formatNumbers(i))
            .append(base);
        return this;
    }

    public GTTextBuilder addNumber(long l) {
        builder.append(EnumChatFormatting.GOLD)
            .append(GTUtility.formatNumbers(l))
            .append(base);
        return this;
    }

    public GTTextBuilder addNumber(float f) {
        builder.append(EnumChatFormatting.GOLD)
            .append(GTUtility.formatNumbers(f))
            .append(base);
        return this;
    }

    public GTTextBuilder addNumber(double d) {
        builder.append(EnumChatFormatting.GOLD)
            .append(GTUtility.formatNumbers(d))
            .append(base);
        return this;
    }

    public GTTextBuilder addNumber(String s) {
        builder.append(EnumChatFormatting.GOLD)
            .append(s)
            .append(base);
        return this;
    }

    @Override
    public String toString() {
        return builder.toString() + base;
    }
}
