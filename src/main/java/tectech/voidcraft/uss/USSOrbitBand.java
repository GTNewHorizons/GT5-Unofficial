package tectech.voidcraft.uss;

/**
 * The orbit bands of a Voidcraft star — the fixed orbital slots a system's planets occupy.
 *
 * <p>
 * Instead of a randomized distance per planet (two planets could drift onto nearly the same radius and overlap),
 * a system's orbits are split into three bands:
 *
 * <ul>
 * <li><b>{@link #NEAR}</b> — the hot, inner worlds (volcanic and tiny rocky bodies) closest to the star.</li>
 * <li><b>{@link #MEDIUM}</b> — the habitable and mid worlds of the system's middle.</li>
 * <li><b>{@link #FAR}</b> — the gas giants and heavy worlds at the system's edge.</li>
 * </ul>
 *
 * <p>
 * Each band owns a fixed number of <em>slots</em> — 2 near, 6 medium, 3 far — and a radius range. Slot
 * {@code k} of a band has the predetermined radius {@code minRadius + (k + 0.5) · (maxRadius − minRadius) /
 * slotCount}: the band's range split into {@code slotCount} equal-width slots, the planet orbiting at its slot's
 * center. No randomness touches a radius, so two planets can never drift onto the same orbit.
 *
 * <p>
 * A 1-block gap is kept between the bands' ranges ({@code MEDIUM.minRadius − NEAR.maxRadius},
 * {@code FAR.minRadius − MEDIUM.maxRadius}), so orbit paths of different bands can never come closer than that
 * gap (the widest planet hologram spans 0.66 blocks, so even adjacent bands' planets can never overlap).
 *
 * <p>
 * The inner edge of the near band is {@link USSPlanets#MIN_DISTANCE} (clear of the star) and the outer edge of
 * the far band is {@link USSPlanets#MAX_DISTANCE} (the 4-block margin inside the dome), so the bands span the
 * whole legacy distance range: 3.0 … 23.1.
 *
 * <p>
 * A planet type's band (see {@link USSPlanetDefinition#getOrbitBand()}) decides where it can spawn: the generator
 * ({@link USSPlanets#generate(USSStarType, long)}) fills a planet's orbit only from ITS band's slots — volcanic
 * planets always orbit close to the star, gas giants always far out.
 */
public enum USSOrbitBand {

    /** The inner band: 2 slots, 3.0–6.0 — hot/volcanic worlds close to the star. */
    NEAR(2, USSPlanets.MIN_DISTANCE, 6.0),

    /** The middle band: 6 slots, 7.0–17.0 — the habitable and mid worlds. */
    MEDIUM(6, 7.0, 17.0),

    /** The outer band: 3 slots, 18.0–23.1 — the gas giants and heavy worlds at the system's edge. */
    FAR(3, 18.0, USSPlanets.MAX_DISTANCE);

    /** How many orbit slots this band owns (a system has at most this many planets in the band). */
    public final int slotCount;

    /** The band's inner radius edge (blocks from the star center, inclusive). */
    public final double minRadius;

    /** The band's outer radius edge (blocks from the star center, inclusive). */
    public final double maxRadius;

    USSOrbitBand(int slotCount, double minRadius, double maxRadius) {
        this.slotCount = slotCount;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
    }

    /**
     * The predetermined orbit radius (blocks from the star center) of slot {@code slot} of this band: the band's
     * range split into {@link #slotCount} equal-width slots, the slot's center.
     *
     * @param slot the slot index, {@code 0..slotCount-1}
     * @return the slot's orbit radius (within {@code [minRadius, maxRadius]})
     * @throws IllegalArgumentException if the slot is outside {@code 0..slotCount-1}
     */
    public double slotRadius(int slot) {
        if (slot < 0 || slot >= slotCount) {
            throw new IllegalArgumentException(
                "slot must be in 0.." + (slotCount - 1) + " for " + this + ", got " + slot);
        }
        return minRadius + (slot + 0.5) * (maxRadius - minRadius) / slotCount;
    }

    /** @return the total number of orbit slots across all bands (2 + 6 + 3 = 11). */
    public static int totalSlots() {
        int total = 0;
        for (USSOrbitBand band : values()) {
            total += band.slotCount;
        }
        return total;
    }
}
