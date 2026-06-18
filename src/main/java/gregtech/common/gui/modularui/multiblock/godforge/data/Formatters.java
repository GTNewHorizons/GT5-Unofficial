package gregtech.common.gui.modularui.multiblock.godforge.data;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static tectech.util.TTUtility.toExponentForm;

import java.math.BigInteger;

public enum Formatters {

    NONE,
    COMMA,
    EXPONENT;

    public static final Formatters[] VALUES = values();

    public Formatters cycle() {
        return switch (this) {
            case NONE -> COMMA;
            case COMMA -> EXPONENT;
            case EXPONENT -> NONE;
        };
    }

    public String format(Number number) {
        return switch (this) {
            case NONE -> number.toString();
            case COMMA -> {
                if (number instanceof BigInteger bi) yield formatNumber(bi);
                else yield formatNumber(number.longValue());
            }
            case EXPONENT -> {
                if (number instanceof BigInteger bi) {
                    if (bi.compareTo(BigInteger.valueOf(1_000L)) > 0) {
                        yield toExponentForm(bi);
                    }
                    yield bi.toString();
                } else {
                    long value = number.longValue();
                    if (value > 1_000L) {
                        yield toExponentForm(value);
                    }
                    yield Long.toString(value);
                }
            }
        };
    }
}
