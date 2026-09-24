package gregtech.api.enums;

import java.util.Locale;

public enum TieredVariant {

    BRONZE,
    STEEL,
    PRIMITIVE,
    STANDARD;

    @Override
    public String toString() {
        return super.toString().toLowerCase(Locale.ENGLISH);
    }

    public static final TieredVariant[] special_variants = { BRONZE, STEEL, PRIMITIVE };
}
