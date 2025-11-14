package gregtech.common.covers.modes;

import gregtech.api.interfaces.modularui.KeyProvider;

public enum FilterType implements KeyProvider {

    WHITELIST("gt.interact.desc.Item_Filter.Whitelist_Mode"),
    BLACKLIST("gt.interact.desc.Item_Filter.Blacklist_Mode");

    private final String key;

    FilterType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
