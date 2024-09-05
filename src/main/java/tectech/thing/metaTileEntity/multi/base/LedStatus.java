package tectech.thing.metaTileEntity.multi.base;

import java.util.function.Supplier;

import net.minecraft.util.EnumChatFormatting;

import tectech.TecTech;

public enum LedStatus {

    STATUS_UNUSED(() -> EnumChatFormatting.DARK_GRAY + "Unused", true), //
    STATUS_TOO_LOW(() -> EnumChatFormatting.BLUE + "Too Low", false), //
    STATUS_LOW(() -> EnumChatFormatting.AQUA + "Low", true), //
    STATUS_WRONG(() -> EnumChatFormatting.DARK_PURPLE + "Wrong", false), //
    STATUS_OK(() -> EnumChatFormatting.GREEN + "Valid", true), //
    STATUS_TOO_HIGH(() -> EnumChatFormatting.RED + "Too High", false), //
    STATUS_HIGH(() -> EnumChatFormatting.GOLD + "High", true), //
    STATUS_UNDEFINED(() -> EnumChatFormatting.GRAY + "Unknown", false),
    STATUS_NEUTRAL(() -> EnumChatFormatting.WHITE + "Neutral", true),
    STATUS_WTF(() -> LedStatus.values()[TecTech.RANDOM.nextInt(9)].name.get(), false);

    public final Supplier<String> name;
    public final boolean isOk;

    LedStatus(Supplier<String> name, boolean ok) {
        this.name = name;
        this.isOk = ok;
    }

    public byte getOrdinalByte() {
        return (byte) ordinal();
    }

    public static LedStatus getStatus(byte value) {
        try {
            return LedStatus.values()[value];
        } catch (Exception e) {
            return STATUS_UNDEFINED;
        }
    }

    public static LedStatus[] makeArray(int count, LedStatus defaultValue) {
        LedStatus[] statuses = new LedStatus[count];
        for (int i = 0; i < count; i++) {
            statuses[i] = defaultValue;
        }
        return statuses;
    }

    public static LedStatus fromLimitsInclusiveOuterBoundary(double value, double min, double low, double high,
        double max, double... excludedNumbers) {
        if (value < min) return STATUS_TOO_LOW;
        if (value > max) return STATUS_TOO_HIGH;

        if (value < low) return STATUS_LOW;
        if (value > high) return STATUS_HIGH;
        for (double val : excludedNumbers) {
            if (val == value) return STATUS_WRONG;
        }
        if (Double.isNaN(value)) return STATUS_WRONG;
        return STATUS_OK;
    }

}
