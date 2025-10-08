package gregtech.common.covers.modes;

import com.cleanroommc.modularui.api.drawable.IKey;

import gregtech.api.interfaces.modularui.KeyProvider;
import gregtech.api.util.GTUtility;

public enum FilterType implements KeyProvider {

    WHITELIST(IKey.str(GTUtility.trans("125.1", "Whitelist Mode"))),
    BLACKLIST(IKey.str(GTUtility.trans("124.1", "Blacklist Mode")));

    private final IKey key;

    FilterType(IKey key) {
        this.key = key;
    }

    @Override
    public IKey getKey() {
        return this.key;
    }
}
