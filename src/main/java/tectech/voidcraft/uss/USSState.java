package tectech.voidcraft.uss;

/**
 * Lifecycle state of the Unstable Solar System star (EoH rework, Phase 2 vertical slice).
 *
 * @see VoidcraftUSS
 * @see docs/Voidcraft_Implementation_Plan.md
 */
public enum USSState {

    /**
     * The multiblock is built and checked, but the star is not burning. Placing a USS Controller into the controller
     * slot ignites the system on the next tick.
     */
    COLD,

    /**
     * The star is burning. The remaining lifespan is counted down once per machine tick; when it reaches zero the
     * star burns out, the controller is consumed and the system returns to {@link #COLD}.
     */
    IGNITED;

    /**
     * @return this state as a language key for info-data display.
     */
    public String getLangKey() {
        switch (this) {
            case IGNITED:
                return "tt.voidcraft_uss.state.ignited";
            default:
                return "tt.voidcraft_uss.state.cold";
        }
    }
}
