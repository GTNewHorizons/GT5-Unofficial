package gregtech.api.util;

import java.util.function.BooleanSupplier;

public enum OptionalBoolean implements BooleanSupplier {

    NONE,
    FALSE,
    TRUE;

    @Override
    public boolean getAsBoolean() {
        return switch (this) {
            case NONE -> {
                throw new IllegalStateException("OptionalBoolean was not present");
            }
            case FALSE -> false;
            case TRUE -> true;
        };
    }

    public boolean orElse(boolean defaultValue) {
        if (this == NONE) {
            return defaultValue;
        }

        return this == TRUE;
    }

    public boolean orElseGet(BooleanSupplier defaultValue) {
        if (this == NONE) {
            return defaultValue.getAsBoolean();
        }

        return this == TRUE;
    }
}
