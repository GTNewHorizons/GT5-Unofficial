package tectech.voidcraft.uss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import gregtech.api.enums.Materials;

/**
 * Deterministic planet generation for a Voidcraft star (Phase 4 pass 3).
 *
 * <p>
 * A system's planets are a <strong>pure function of (star type, ignition timestamp)</strong>:
 *
 * <pre>
 * planets = USSPlanets.generate(uss.getStarType(), uss.getIgnitedAt())
 * </pre>
 *
 * so the three consumers — the server (what a Miner actually mines), the client (the holograms the legacy EoH
 * renderer draws) and the GUI (the infodata "Planets" section) — all derive the exact same system with NO extra
 * NBT to persist and no server→client planet sync beyond what the render TE already carries.
 *
 * <p>
 * <strong>Shape of a system.</strong> A RANDOM planet count — PASS 22 (user: "planet count randomized between
 * 3-9"): {@code MIN_PLANETS_PER_SYSTEM}…{@code MAX_PLANETS_PER_SYSTEM} (3–9), drawn as the FIRST rng call so
 * (star type, seed) still yields exactly one fixed system. Types are shuffled WITHOUT replacement from the star
 * type's pool ({@link USSPlanetType#pool(USSStarType)} — 4 types) and walked in order: the first
 * {@code min(count, 4)} planets are all different types, and only systems larger than the pool wrap and repeat
 * (a 9-planet system has at most one type twice). Drawing with replacement (the pass-3 behavior) put duplicate
 * types in 62.5% of systems — same-colored stacked spheres that read as "one planet" in the infodata. Each planet
 * gets distinct orbital parameters — pass 13 (user: "the planets are still very close to the star"): distance is
 * randomized from MIN_DISTANCE (3 blocks from the star) all the way to the system's edge leaving 4 blocks from
 * the shell edge (pass 14, user: "ships exiting the dome isn't good" — the 2-block margin was smaller than hover
 * + planet half + spread = 2.875; MAX_DISTANCE = USSConstants.SPACE_SHELL_RADIUS − 4 = 23.1, dome 27.1), clear
 * of the star surface ≤ 1.9; rendered scale 0.175–0.66 across the texture tiers (the definition's size range
 * 0.35–1.32 reduced by PLANET_RENDER_SCALE) and orbit/spin speeds 0.5–1.5. The
 * distance range is split into COUNT bands (inner → outer), so a 9-planet system still spreads across the dome.
 * Pass 11
 * (user spec):
 * all orbits sit on the SAME horizontal (xz) plane like a real solar system, each inclined only 0–5° from it
 * (the inclination and its direction are decomposed into the renderer's xAngle/zAngle tilt pair, so the
 * renderer chain and the ship-tracked orbit math are unchanged).
 *
 * <p>
 * No RNG outside the single seeded {@link Random}: the {@code java.util.Random(long)} contract guarantees
 * identical sequences across JVMs, which is what makes server and client agree.
 */
public final class USSPlanets {

    /** PASS 22 (user: "planet count randomized between 3-9"): the minimum planets in a system. */
    public static final int MIN_PLANETS_PER_SYSTEM = 3;

    /** PASS 22 (user: "planet count randomized between 3-9"): the maximum planets in a system. */
    public static final int MAX_PLANETS_PER_SYSTEM = 9;

    /**
     * Inner planet distance (blocks from the star center; the star radius is at most 1.9). Pass 13 (user: "planets are
     * still very close to the star"): no closer than 3 blocks.
     */
    public static final double MIN_DISTANCE = 3.0;

    /**
     * Outer planet distance (blocks). Pass 13: the system's edge with a margin from the shell — pass 14 (user:
     * "the ships being able to exit the dome isn't good — make the distance from the edge 4 blocks"): 4 BLOCKS
     * of margin, which covers the worst-case ship extension (hover 0.5 + planet half 0.375 + spread 2.0 = 2.875),
     * so the fleet stays INSIDE the dome. Derived from {@link USSConstants#SPACE_SHELL_RADIUS} (27.1) so the
     * dome and the orbit range can never drift apart. (Replaces the legacy 4.0–9.0 range inside the 12.95 dome.)
     */
    public static final double MAX_DISTANCE = USSConstants.SPACE_SHELL_RADIUS - 4.0;

    /**
     * The planet hologram's rendered scale range — the definition's size range (0.35–1.32 across the texture tiers)
     * reduced by {@link #PLANET_RENDER_SCALE}.
     */
    public static final double MIN_SCALE = 0.175;
    public static final double MAX_SCALE = 0.66;

    /**
     * The factor applied to the planet's sampled size for the hologram's rendered scale — planets render half the
     * size of their definition's size range.
     */
    public static final double PLANET_RENDER_SCALE = 0.5;

    /** Orbit/spin speed range (the legacy EoH uses 0.5–1.5). */
    public static final double MIN_SPEED = 0.5;
    public static final double MAX_SPEED = 1.5;

    /**
     * Pass 11 (user spec: "like real solar systems") — each orbit lies on the horizontal (xz) plane inclined by a
     * small random angle in 0–5° (the legacy ±30° dual tilts are gone; the old constant is replaced).
     */
    public static final double MAX_INCLINATION_DEG = 5.0;

    private USSPlanets() {
        throw new AssertionError("Static helpers");
    }

    /**
     * One generated planet: its registered definition (what can be mined from it + the hologram texture + the size
     * range) plus the orbital parameters the renderer needs and the sampled scale.
     */
    public static final class USSPlanet {

        /** The registered planet definition (carries the mineable ores + fluids, hologram texture, and size range). */
        public final USSPlanetDefinition definition;

        /** Orbit radius in blocks from the star center (within {@link #MIN_DISTANCE}–{@link #MAX_DISTANCE}). */
        public final double distance;

        /** Hologram scale — sampled from the definition's size range (0.0–5.0), applied to the render. */
        public final double scale;

        /** Orbit angular speed factor (within {@link #MIN_SPEED}–{@link #MAX_SPEED}). */
        public final double orbitSpeed;

        /** Self-rotation speed factor (within {@link #MIN_SPEED}–{@link #MAX_SPEED}). */
        public final double rotationSpeed;

        /**
         * Orbit-plane tilt about X, in degrees — one component of the small 0–5° inclination (pass 11: coplanar
         * orbits; |x| ≤ {@link #MAX_INCLINATION_DEG}).
         */
        public final double xAngle;

        /**
         * Orbit-plane tilt about Z, in degrees — the other component of the small 0–5° inclination (pass 11:
         * coplanar orbits; |z| ≤ {@link #MAX_INCLINATION_DEG}).
         */
        public final double zAngle;

        /** Whether this planet orbits with a ring (a seeded draw from the definition's ring probability). */
        public final boolean hasRing;

        /**
         * The orbit-ring texture variant for this planet, in {@code 1..tier.ringVariantCount()} (normal/big: 1–8,
         * small/huge: 1–6); {@code -1} when {@link #hasRing} is false (no ring).
         */
        public final int ringVariant;

        public USSPlanet(USSPlanetDefinition definition, double distance, double scale, double orbitSpeed,
            double rotationSpeed, double xAngle, double zAngle, boolean hasRing, int ringVariant) {
            this.definition = definition;
            this.distance = distance;
            this.scale = scale;
            this.orbitSpeed = orbitSpeed;
            this.rotationSpeed = rotationSpeed;
            this.xAngle = xAngle;
            this.zAngle = zAngle;
            this.hasRing = hasRing;
            this.ringVariant = ringVariant;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof USSPlanet)) {
                return false;
            }
            USSPlanet that = (USSPlanet) other;
            return this.definition == that.definition && this.distance == that.distance
                && this.scale == that.scale
                && this.orbitSpeed == that.orbitSpeed
                && this.rotationSpeed == that.rotationSpeed
                && this.xAngle == that.xAngle
                && this.zAngle == that.zAngle
                && this.hasRing == that.hasRing
                && this.ringVariant == that.ringVariant;
        }

        @Override
        public int hashCode() {
            int h = definition.hashCode();
            h = 31 * h + Double.hashCode(distance);
            h = 31 * h + Double.hashCode(scale);
            h = 31 * h + Double.hashCode(orbitSpeed);
            h = 31 * h + Double.hashCode(rotationSpeed);
            h = 31 * h + Double.hashCode(xAngle);
            h = 31 * h + Double.hashCode(zAngle);
            h = 31 * h + Boolean.hashCode(hasRing);
            h = 31 * h + ringVariant;
            return h;
        }

        @Override
        public String toString() {
            return "USSPlanet[" + definition.getId() + ", d=" + distance + ", s=" + scale + "]";
        }
    }

    /**
     * Generate the planet system of a star (the registration-based mechanics pass).
     *
     * <p>
     * The system is a pure function of (star type, seed) — the same (starType, seed) always yields the same system,
     * so server (mining), client (rendering) and GUI (infodata) all derive the identical system. The three inputs
     * come from the registered definitions:
     * <ul>
     * <li><strong>Planet count</strong> — sampled from the star's planet range
     * ({@link USSStarDefinition#getPlanetMin()}…{@link USSStarDefinition#getPlanetMax()}).</li>
     * <li><strong>Planet types</strong> — the registered planets that allow this star type
     * ({@link USSPlanetRegistry#pool(USSStarType)}), shuffled and walked.</li>
     * <li><strong>Planet size</strong> — sampled from each planet's size range
     * ({@link USSPlanetDefinition#getSizeMin()}…{@link USSPlanetDefinition#getSizeMax()}, 0.0–5.0).</li>
     * </ul>
     *
     * @param starType the star's type (null → {@link USSStarType#YELLOW_DWARF}, defensive) — selects the pool
     * @param seed     any stable long (the USS ignition timestamp in practice) — same (starType, seed) always yields
     *                 the same system
     * @return the system's planets (never null, never empty); planet i orbits in the i-th of COUNT equal distance
     *         bands {@code MIN_DISTANCE + i*(MAX-MIN)/count … MIN_DISTANCE + (i+1)*(MAX-MIN)/count}
     */
    public static List<USSPlanet> generate(USSStarType starType, long seed) {
        if (starType == null) {
            starType = USSStarType.YELLOW_DWARF;
        }
        // The star's planet range (how many planets this star has) — from the registered star definition.
        USSStarDefinition star = USSStarRegistry.byType(starType);
        final int planetMin = star != null ? star.getPlanetMin() : MIN_PLANETS_PER_SYSTEM;
        final int planetMax = star != null ? star.getPlanetMax() : MAX_PLANETS_PER_SYSTEM;

        // The planets that can orbit this star — from the registered planet pool (their allowedStarTypes).
        List<USSPlanetDefinition> pool = USSPlanetRegistry.pool(starType);
        if (pool.isEmpty()) {
            // Defensive: no registered planets for this star type — fall back to the whole catalog so the system
            // is never empty.
            pool = USSPlanetRegistry.all();
        }
        if (pool.isEmpty()) {
            throw new IllegalStateException(
                "No planets registered for star type " + starType + " and the catalog is empty");
        }

        Random rng = new Random(seed);
        // The count is the FIRST draw — planetMin…planetMax inclusive — so (starType, seed) still yields exactly
        // one fixed system and server/client agree (Random(long) contract).
        final int count = planetMin + rng.nextInt(planetMax - planetMin + 1);
        double band = (MAX_DISTANCE - MIN_DISTANCE) / count;
        // Distinct types: shuffle the pool with the SAME seeded rng and walk it in order — the first min(count,
        // pool) planets are all different types; only beyond the pool does it wrap.
        List<USSPlanetDefinition> types = new ArrayList<>(pool);
        for (int i = types.size() - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            USSPlanetDefinition tmp = types.get(i);
            types.set(i, types.get(j));
            types.set(j, tmp);
        }
        List<USSPlanet> planets = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            USSPlanetDefinition def = types.get(i % Math.max(1, types.size()));
            double distance = MIN_DISTANCE + band * (i + rng.nextFloat());
            // The planet's SIZE — sampled from the definition's size range (0.0–5.0). This is both the hologram
            // scale (the render size of the planet cube) and the size used in the miner's ore-amount calculation
            // (ore.amount × planetSize²).
            double scale = (def.getSizeMin() + (def.getSizeMax() - def.getSizeMin()) * rng.nextFloat())
                * PLANET_RENDER_SCALE;
            double orbitSpeed = MIN_SPEED + (MAX_SPEED - MIN_SPEED) * rng.nextFloat();
            double rotationSpeed = MIN_SPEED + (MAX_SPEED - MIN_SPEED) * rng.nextFloat();
            // Orbits on the same horizontal xz plane, like real solar systems: the orbit plane is the xz plane
            // inclined by a small random inclination (0–5°) toward a random node direction.
            double inclination = MAX_INCLINATION_DEG * rng.nextFloat();
            double node = 360.0 * rng.nextFloat();
            double incRad = Math.toRadians(inclination);
            double nodeRad = Math.toRadians(node);
            double xAngle = Math.toDegrees(incRad * Math.cos(nodeRad));
            double zAngle = Math.toDegrees(incRad * Math.sin(nodeRad));
            // Orbit ring: a seeded draw from the definition's ring probability (gas giants 50%, normal+ non-giants 10%,
            // tiny/small 0%). The variant is a uniform pick within the planet's tier ring set (1..count); -1 = no ring.
            // Both draws use the SAME seeded rng, so (starType, seed) yields the identical ring layout on every
            // consumer.
            boolean hasRing = false;
            int ringVariant = -1;
            final float chance = def.ringChance();
            if (chance > 0f && rng.nextFloat() < chance) {
                final int ringCount = def.getTier()
                    .ringVariantCount();
                if (ringCount > 0) {
                    hasRing = true;
                    ringVariant = 1 + rng.nextInt(ringCount);
                }
            }
            planets.add(
                new USSPlanet(def, distance, scale, orbitSpeed, rotationSpeed, xAngle, zAngle, hasRing, ringVariant));
        }
        return Collections.unmodifiableList(planets);
    }

    /**
     * Sample the star's SIZE from its registered size range (0.0–10.0) — a pure function of (star type, seed), so
     * server (starlifter output) and client (render) derive the identical value.
     *
     * @param starType the star's type (null → {@link USSStarType#YELLOW_DWARF}, defensive)
     * @param seed     any stable long (the USS ignition timestamp in practice)
     * @return the sampled size (within the star's size range; 5.0 when the star is unregistered, defensive)
     */
    public static double sampleStarSize(USSStarType starType, long seed) {
        if (starType == null) {
            starType = USSStarType.YELLOW_DWARF;
        }
        USSStarDefinition star = USSStarRegistry.byType(starType);
        if (star == null) {
            return 5.0;
        }
        Random rng = new Random(seed ^ 0x5550534152535A4DL); // salt so the size draw is independent of the planet draws
        return star.getSizeMin() + (star.getSizeMax() - star.getSizeMin()) * rng.nextFloat();
    }

    /**
     * The star's rendered size: a 2/3-block base radius multiplied by the square root of the sampled size. Keeps
     * large stars from ballooning — a size-10 star is (2/3)·√10 ≈ 2.11 blocks, a size-1 star is 0.67 blocks.
     *
     * @param size the sampled star size (0.0–10.0; negative clamped to 0)
     * @return the rendered size in blocks (always &ge; 0)
     */
    public static float starRenderSize(double size) {
        if (size <= 0.0) {
            return 0.0f;
        }
        return (2.0f / 3.0f) * (float) Math.sqrt(size);
    }

    /**
     * The union of the ore materials a system's planets offer (what a Miner can harvest from it), deduplicated in
     * first-seen order (planet order, then definition ore order).
     *
     * @param planets a generated system (null or empty → empty list, defensive)
     * @return the deduplicated material list (never null)
     */
    public static List<Materials> materialsOf(List<USSPlanet> planets) {
        if (planets == null || planets.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Materials, Boolean> seen = new LinkedHashMap<>();
        for (USSPlanet planet : planets) {
            for (USSPlanetOre ore : planet.definition.getOres()) {
                seen.put(ore.getOreType(), Boolean.TRUE);
            }
        }
        return new ArrayList<>(seen.keySet());
    }
}
