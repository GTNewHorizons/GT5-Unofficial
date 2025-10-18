package gregtech.common.covers.modes;

import gregtech.api.interfaces.modularui.KeyProvider;

public enum MachineProcessingCondition implements KeyProvider {

    ALWAYS("gt.interact.desc.Pump.AlwaysOn"),
    CONDITIONAL("gt.interact.desc.Pump.MachProcState"),
    INVERTED("gt.interact.desc.Pump.InvertedMachProcState");

    private final String key;

    MachineProcessingCondition(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
