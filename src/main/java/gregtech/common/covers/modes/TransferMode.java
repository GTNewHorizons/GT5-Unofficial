package gregtech.common.covers.modes;

import com.cleanroommc.modularui.api.drawable.IKey;

import gregtech.api.interfaces.modularui.KeyProvider;
import gregtech.api.util.GTUtility;

public enum TransferMode implements KeyProvider {

    EXPORT(IKey.str(GTUtility.trans("006", "Export"))),
    IMPORT(IKey.str(GTUtility.trans("007", "Import")));

    private final IKey key;

    TransferMode(IKey key) {
        this.key = key;
    }

    @Override
    public IKey getKey() {
        return this.key;
    }
}
