package gregtech.common.covers.modes;

import com.cleanroommc.modularui.api.drawable.IKey;

import gregtech.api.interfaces.modularui.KeyProvider;
import gregtech.api.util.GTUtility;

public enum FilterDirectionMode implements KeyProvider {

    INPUT(IKey.str(GTUtility.trans("232", "Filter Input"))),
    OUTPUT(IKey.str(GTUtility.trans("233", "Filter Output")));

    private final IKey key;

    FilterDirectionMode(IKey key) {
        this.key = key;
    }

    @Override
    public IKey getKey() {
        return this.key;
    }
}
