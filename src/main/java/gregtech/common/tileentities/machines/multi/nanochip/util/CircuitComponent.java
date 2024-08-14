package gregtech.common.tileentities.machines.multi.nanochip.util;

import net.minecraft.util.StatCollector;

public enum CircuitComponent {

    Invalid(-1, "gt.circuitcomponent.invalid"),
    Wire(0, "gt.circuitcomponent.wire"),
    SMDTransistor(1, "gt.circuitcomponent.smd.transistor"),
    SMDInductor(2, "gt.circuitcomponent.smd.inductor"),
    SMDCapacitor(3, "gt.circuitcomponent.smd.capacitor"),
    SMDDiode(4, "gt.circuitcomponent.smd.diode");

    public final int id;
    public final String unlocalizedName;

    CircuitComponent(int id, String unlocalizedName) {
        this.id = id;
        this.unlocalizedName = unlocalizedName;
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal(unlocalizedName);
    }
}
