package gregtech.common.covers.modes;

import gregtech.api.interfaces.modularui.KeyProvider;

public enum TransferMode implements KeyProvider {

    EXPORT("gt.interact.desc.export"),
    IMPORT("gt.interact.desc.import");

    private final String key;

    TransferMode(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
