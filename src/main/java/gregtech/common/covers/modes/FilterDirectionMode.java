package gregtech.common.covers.modes;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;

import gregtech.api.interfaces.modularui.KeyProvider;

public enum FilterDirectionMode implements KeyProvider {

    INPUT(IKey.str(StatCollector.translateToLocal("gt.interact.desc.FluidFilter.FilterInput"))),
    OUTPUT(IKey.str(StatCollector.translateToLocal("gt.interact.desc.FluidFilter.FilterOutput")));

    private final IKey key;

    FilterDirectionMode(IKey key) {
        this.key = key;
    }

    @Override
    public IKey getKey() {
        return this.key;
    }
}
