package gregtech.common.covers.modes;

import gregtech.api.interfaces.modularui.KeyProvider;

public enum FilterDirectionMode implements KeyProvider {

    INPUT("gt.interact.desc.FluidFilter.FilterInput"),
    OUTPUT("gt.interact.desc.FluidFilter.FilterOutput");

    private final String key;

    FilterDirectionMode(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
