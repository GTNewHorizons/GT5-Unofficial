package gregtech.api.enums;

public enum FluidState {

    GAS,
    LIQUID,
    MOLTEN,
    PLASMA,
    SLURRY;

    public static final FluidState[] VALID_STATES = new FluidState[] { SLURRY, LIQUID, GAS, PLASMA, MOLTEN };

    public static FluidState fromValue(int stateValue) {
        return stateValue >= 0 && stateValue < FluidState.VALID_STATES.length ? FluidState.VALID_STATES[stateValue]
            : FluidState.LIQUID;
    }
}
