package gregtech.api.enums;

public enum FluidState {
    GAS,
    LIQUID,
    MOLTEN,
    PLASMA,
    SLURRY;
    public static final FluidState[] VALUES = new FluidState[] {SLURRY, LIQUID, GAS, PLASMA, MOLTEN};
}
