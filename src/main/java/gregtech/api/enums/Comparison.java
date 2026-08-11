package gregtech.api.enums;

import net.minecraft.util.StatCollector;

public enum Comparison {

    EQ,
    LT,
    GT,
    LTEQ,
    GTEQ,
    ANALOG;

    public boolean test(int actual, int configured) {
        return switch (this) {
            case EQ -> actual == configured;
            case LT -> actual < configured;
            case GT -> actual > configured;
            case LTEQ -> actual <= configured;
            case GTEQ -> actual >= configured;
            case ANALOG -> throw new UnsupportedOperationException();
        };
    }

    public boolean test(long actual, long configured) {
        return switch (this) {
            case EQ -> actual == configured;
            case LT -> actual < configured;
            case GT -> actual > configured;
            case LTEQ -> actual <= configured;
            case GTEQ -> actual >= configured;
            case ANALOG -> throw new UnsupportedOperationException();
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case EQ -> "=";
            case LT -> "<";
            case GT -> ">";
            case LTEQ -> "<=";
            case GTEQ -> ">=";
            case ANALOG -> StatCollector.translateToLocal("GT5U.gui.text.nanite-detector-analog");
        };
    }
}
