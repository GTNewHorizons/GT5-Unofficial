package gregtech.common.covers.modes;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;

import gregtech.api.interfaces.modularui.KeyProvider;

public enum MachineProcessingCondition implements KeyProvider {

    ALWAYS(IKey.str(StatCollector.translateToLocal("gt.interact.desc.Pump.AlwaysOn"))),
    CONDITIONAL(IKey.str(StatCollector.translateToLocal("gt.interact.desc.Pump.MachProcState"))),
    INVERTED(IKey.str(StatCollector.translateToLocal("gt.interact.desc.Pump.InvertedMachProcState")));

    private final IKey key;

    MachineProcessingCondition(IKey key) {
        this.key = key;
    }

    @Override
    public IKey getKey() {
        return this.key;
    }
}
