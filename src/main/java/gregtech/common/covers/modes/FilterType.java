package gregtech.common.covers.modes;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;

import gregtech.api.interfaces.modularui.KeyProvider;

public enum FilterType implements KeyProvider {

    WHITELIST(IKey.str(StatCollector.translateToLocal("gt.interact.desc.Item_Filter.Whitelist_Mode"))),
    BLACKLIST(IKey.str(StatCollector.translateToLocal("gt.interact.desc.Item_Filter.Blacklist_Mode")));

    private final IKey key;

    FilterType(IKey key) {
        this.key = key;
    }

    @Override
    public IKey getKey() {
        return this.key;
    }
}
