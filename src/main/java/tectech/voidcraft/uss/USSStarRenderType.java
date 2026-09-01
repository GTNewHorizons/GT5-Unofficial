package tectech.voidcraft.uss;

/**
 * The custom render treatment of a USS star — the extra geometry the star's renderer draws beyond the shared
 * tinted three-layer sphere (which every star body still gets).
 *
 * <p>
 * Pure data: unit-testable in a bare JVM.
 */
public enum USSStarRenderType {

    /**
     * The standard tinted three-layer sphere only (no extra geometry).
     */
    STANDARD,

    /**
     * The standard star body plus magnetic dipole field loops passing through the core (the magnetar — the field
     * lines emerge from one pole, bulge out beyond the star, and re-enter at the other).
     */
    MAGNETAR,

    /**
     * The standard star body plus the supernova explosion treatment: the detonation flash overdrives the layer
     * gains, an additive shock shell expands from the star's rim to the dome radius, and the final collapse
     * shrinks the core into the remnant — driven by the synced remaining lifespan.
     */
    SUPERNOVA,

    /**
     * The supernova treatment on the hotter, faster phase table (the hypernova).
     */
    HYPERNOVA;

    /**
     * Resolve a stored ordinal (the render tile's NBT) to a render type.
     *
     * @param ordinal a stored ordinal (any int, including values from older or newer builds)
     * @return the render type for the ordinal, or {@link #STANDARD} for an unknown value (forward-compatible)
     */
    public static USSStarRenderType fromOrdinal(int ordinal) {
        for (USSStarRenderType type : values()) {
            if (type.ordinal() == ordinal) {
                return type;
            }
        }
        return STANDARD;
    }
}
