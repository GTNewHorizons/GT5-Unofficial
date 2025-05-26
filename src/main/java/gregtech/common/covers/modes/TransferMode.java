package gregtech.common.covers.modes;

import com.cleanroommc.modularui.api.drawable.IKey;

import gregtech.api.interfaces.modularui.KeyProvider;

public enum TransferMode implements KeyProvider {

    EXPORT(IKey.lang("gt.interact.desc.export")),
    IMPORT(IKey.lang("gt.interact.desc.import"));

    private final IKey key;

    TransferMode(IKey key) {
        this.key = key;
    }

    @Override
    public IKey getKey() {
        return this.key;
    }
}
