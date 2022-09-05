package gregtech.api.enums;

public enum FluidState {
    GAS,
    LIQUID,
    MOLTEN,
    PLASMA,
    SOLID;
    public static final FluidState[] VALUES = new FluidState[] {SOLID, LIQUID, GAS, PLASMA, MOLTEN};
}
