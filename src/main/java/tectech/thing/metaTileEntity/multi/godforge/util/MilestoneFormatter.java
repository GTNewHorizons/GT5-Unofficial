package tectech.thing.metaTileEntity.multi.godforge.util;

import static gregtech.api.util.GTUtility.formatNumbers;
import static tectech.util.TTUtility.toExponentForm;

import java.math.BigInteger;

public enum MilestoneFormatter {

    NONE,
    COMMA,
    EXPONENT;

    public static final MilestoneFormatter[] VALUES = values();

    public MilestoneFormatter cycle() {
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
                if (number instanceof BigInteger bi) yield formatNumbers(bi);
                else yield formatNumbers(number.longValue());
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
