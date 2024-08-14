package gregtech.common.tileentities.machines.multi.nanochip.util;

public enum CircuitComponentType {

    Invalid(-1),
    Wire(0);

    public final int id;

    CircuitComponentType(int id) {
        this.id = id;
    }
}
