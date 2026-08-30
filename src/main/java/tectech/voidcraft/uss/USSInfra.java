package tectech.voidcraft.uss;

/**
 * The Dyson Swarm's pure tuning — satellite capacity and decay as functions of the star (bare-JVM safe: catalog
 * lookups only, no MC types beyond the catalog's own).
 */
public final class USSInfra {

    /** The hold / site-cargo / loadout key of the Power Satellite (the {@code item.} loadout prefix + this key). */
    public static final String KEY_POWER_SATELLITE = "power_satellite";

    /**
     * The constructor-loadout key of the Power Satellite ({@code item.<key>} — the {@code item.} prefix routes the
     * loadout entry to the build site's CARGO (infrastructure payloads) instead of its parts.
     */
    public static final String LOADOUT_KEY_SATELLITE = "item." + KEY_POWER_SATELLITE;

    private USSInfra() {
        throw new AssertionError("Utility holder");
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
