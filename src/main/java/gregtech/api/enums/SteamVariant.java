package gregtech.api.enums;

import java.util.Locale;

public enum SteamVariant {

    BRONZE,
    STEEL,
    PRIMITIVE,
    NONE;

    @Override
    public String toString() {
        return super.toString().toLowerCase(Locale.ENGLISH);
    }
}
