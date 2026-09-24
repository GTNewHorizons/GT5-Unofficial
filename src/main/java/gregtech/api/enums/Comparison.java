package gregtech.api.enums;

public enum Comparison {

    EQ,
    LT,
    GT,
    LTEQ,
    GTEQ;

    public boolean test(int actual, int configured) {
        return switch (this) {
            case EQ -> actual == configured;
            case LT -> actual < configured;
            case GT -> actual > configured;
            case LTEQ -> actual <= configured;
            case GTEQ -> actual >= configured;
        };
    }

    public boolean test(long actual, long configured) {
        return switch (this) {
            case EQ -> actual == configured;
            case LT -> actual < configured;
            case GT -> actual > configured;
            case LTEQ -> actual <= configured;
            case GTEQ -> actual >= configured;
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
        };
    }
}
