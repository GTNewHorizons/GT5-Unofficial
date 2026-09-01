package tectech.voidcraft.uss;

/**
 * The Dyson Swarm's pure tuning — satellite capacity and decay as functions of the star (bare-JVM safe: catalog
 * lookups only, no MC types beyond the catalog's own).
 */
public final class USSInfra {

    /** The cargo key of the Power Satellite (the Dyson Swarm pass's builder payload). */
    public static final String KEY_POWER_SATELLITE = "power_satellite";

    /** The cargo key of the Injector Component (the Stellar Injector's builder payload). */
    public static final String KEY_INJECTOR_COMPONENT = "injector_component";

    /** The cargo key of the Stabilizer Component (the Continuum Stabilizer's builder payload). */
    public static final String KEY_STABILIZER_COMPONENT = "stabilizer_component";

    /** The cargo key of the Lens Component (the Stellar Gravitational Lens's builder payload). */
    public static final String KEY_LENS_COMPONENT = "lens_component";

    /**
     * The infrastructure cargo keys — the hold items the CONSTRUCT leg delivers to the build site (unbounded,
     * unpaced) instead of crediting it as parts: the Power Satellite, the three builder components, and the
     * matrix's UMV / UXV Field Generator tiers.
     */
    public static final String[] INFRA_CARGO_KEYS = { KEY_POWER_SATELLITE, KEY_INJECTOR_COMPONENT,
        KEY_STABILIZER_COMPONENT, KEY_LENS_COMPONENT, USSConstants.FIELD_GENERATOR_UMV,
        USSConstants.FIELD_GENERATOR_UXV };

    private USSInfra() {
        throw new AssertionError("Utility holder");
    }

    /**
     * The cargo key of the given infrastructure type's component (the hold key the builder draws from) — null for
     * an unknown type.
     */
    public static String componentKey(int type) {
        switch (type) {
            case USSInfraBuild.INJECTOR:
                return KEY_INJECTOR_COMPONENT;
            case USSInfraBuild.STABILIZER:
                return KEY_STABILIZER_COMPONENT;
            case USSInfraBuild.LENS:
                return KEY_LENS_COMPONENT;
            default:
                return null;
        }
    }

    /**
     * The star's satellite capacity: {@code round(DYSON_SATELLITE_CAPACITY_PER_RENDER_AREA · renderSize²)}
     * (minimum 1). The render size (blocks) is the star's rendered radius ({@code (2/3)·√size}); the shell's
     * surface area scales with it, so the capacity tracks the geometry the shell renders.
     *
     * @param starRenderSize the star's render size in blocks
     * @return the maximum satellites the star's swarm can host
     */
    public static long starSatelliteCapacity(double starRenderSize) {
        double area = starRenderSize * starRenderSize;
        return Math.max(1L, Math.round(USSConstants.DYSON_SATELLITE_CAPACITY_PER_RENDER_AREA * area));
    }

    /**
     * The render size (blocks) of the largest star the catalog defines (the capacity the decay rate calibrates
     * against). An unregistered catalog (bare-JVM tests without a catalog) degrades to the giant maximum.
     *
     * @return the largest catalog star's render size in blocks
     */
    public static double largestStarRenderSize() {
        double maxSize = 10.0;
        for (USSStarDefinition def : USSStarRegistry.all()) {
            maxSize = Math.max(maxSize, def.getSizeMax());
        }
        return USSPlanets.starRenderSize(maxSize);
    }

    /**
     * The swarm's decay per satellite per tick (star-size independent): {@link
     * USSConstants#DYSON_SATELLITE_DECAY_FRACTION} of the single-launcher-equivalent rate — one whole satellite
     * per {@link USSConstants#DYSON_SATELLITE_LAUNCH_INTERVAL} at the LARGEST fully-saturated star. Smaller
     * swarms (fewer satellites) decay proportionally slower.
     *
     * @return the decay fraction per satellite per tick
     */
    public static double decayPerUnitPerTick() {
        double maxRender = largestStarRenderSize();
        long capacity = starSatelliteCapacity(maxRender);
        return USSConstants.DYSON_SATELLITE_DECAY_FRACTION
            / ((double) USSConstants.DYSON_SATELLITE_LAUNCH_INTERVAL * (double) Math.max(1L, capacity));
    }
}
