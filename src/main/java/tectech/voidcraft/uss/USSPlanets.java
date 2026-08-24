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
 * <strong>Shape of a system.</strong> {@link #PLANETS_PER_SYSTEM} planets with DISTINCT types, drawn WITHOUT
 * replacement from the star type's pool ({@link USSPlanetType#pool(USSStarType)} — 4 types, so a system is 3
 * different worlds of its star's element family). Drawing with replacement (the pass-3 behavior) put duplicate
 * types in 62.5% of systems — same-colored stacked spheres that read as "one planet" in the infodata. Each planet
 * gets distinct orbital parameters in the legacy EoH ranges
 * (distance 4.0–9.0 blocks — inside the 12.95 space shell and clear of the star surface ≤ 1.4; scale 0.35–0.75;
 * orbit/spin speeds 0.5–1.5), and — pass 11, user spec — all orbits sit on the SAME horizontal (xz) plane like a
 * real solar system, each inclined only 0–5° from it (the inclination and its direction are decomposed into the
 * renderer's xAngle/zAngle tilt pair, so the renderer chain and the ship-tracked orbit math are unchanged).
 *
 * <p>
 * No RNG outside the single seeded {@link Random}: the {@code java.util.Random(long)} contract guarantees
 * identical sequences across JVMs, which is what makes server and client agree.
 */
public final class USSPlanets {

    /** Number of planets in every system ("a few"). */
    public static final int PLANETS_PER_SYSTEM = 3;

    /** Inner planet distance (blocks from the star center; the star radius is at most 1.4). */
    public static final double MIN_DISTANCE = 4.0;

    /** Outer planet distance (blocks; must stay inside the 12.95-block space shell). */
    public static final double MAX_DISTANCE = 9.0;

    /** Planet hologram scale range (the legacy EoH uses 0.2–0.9). */
    public static final double MIN_SCALE = 0.35;
    public static final double MAX_SCALE = 0.75;

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
     * One generated planet: its type (what can be mined from it) plus the orbital parameters the renderer needs.
     */
    public static final class USSPlanet {

        /** The planet type (catalog entry — carries the mineable materials + hologram block). */
        public final USSPlanetType type;

        /** Orbit radius in blocks from the star center (within {@link #MIN_DISTANCE}–{@link #MAX_DISTANCE}). */
        public final double distance;

        /** Hologram scale (within {@link #MIN_SCALE}–{@link #MAX_SCALE}). */
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

        public USSPlanet(USSPlanetType type, double distance, double scale, double orbitSpeed, double rotationSpeed,
            double xAngle, double zAngle) {
            this.type = type;
            this.distance = distance;
            this.scale = scale;
            this.orbitSpeed = orbitSpeed;
            this.rotationSpeed = rotationSpeed;
            this.xAngle = xAngle;
            this.zAngle = zAngle;
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
            return this.type == that.type && this.distance == that.distance
                && this.scale == that.scale
                && this.orbitSpeed == that.orbitSpeed
                && this.rotationSpeed == that.rotationSpeed
                && this.xAngle == that.xAngle
                && this.zAngle == that.zAngle;
        }

        @Override
        public int hashCode() {
            int h = type.hashCode();
            h = 31 * h + Double.hashCode(distance);
            h = 31 * h + Double.hashCode(scale);
            h = 31 * h + Double.hashCode(orbitSpeed);
            h = 31 * h + Double.hashCode(rotationSpeed);
            h = 31 * h + Double.hashCode(xAngle);
            h = 31 * h + Double.hashCode(zAngle);
            return h;
        }

        @Override
        public String toString() {
            return "USSPlanet[" + type + ", d=" + distance + ", s=" + scale + "]";
        }
    }

    /**
     * Generate the planet system of a star.
     *
     * @param starType the star's type (null → {@link USSStarType#MAIN_SEQUENCE}, defensive) — selects the pool
     * @param seed     any stable long (the USS ignition timestamp in practice) — same (starType, seed) always yields
     *                 the same system
     * @return exactly {@link #PLANETS_PER_SYSTEM} planets with distinct types (never null, never empty); planet i
     *         orbits in the distance band {@code MIN_DISTANCE + i*(MAX-MIN)/3 … MIN_DISTANCE + (i+1)*(MAX-MIN)/3}
     */
    public static List<USSPlanet> generate(USSStarType starType, long seed) {
        if (starType == null) {
            starType = USSStarType.MAIN_SEQUENCE;
        }
        List<USSPlanetType> pool = USSPlanetType.pool(starType);

        Random rng = new Random(seed);
        double band = (MAX_DISTANCE - MIN_DISTANCE) / PLANETS_PER_SYSTEM;
        // Distinct types (pass 7): shuffle the pool with the SAME seeded rng and take the first N — the system is
        // still a pure function of (starType, seed). Duplicate types made same-colored stacked spheres that users
        // read as a single planet (infodata lists one type while 3 spheres orbit).
        List<USSPlanetType> types = new ArrayList<>(pool);
        for (int i = types.size() - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            USSPlanetType tmp = types.get(i);
            types.set(i, types.get(j));
            types.set(j, tmp);
        }
        List<USSPlanet> planets = new ArrayList<>(PLANETS_PER_SYSTEM);
        for (int i = 0; i < PLANETS_PER_SYSTEM; i++) {
            USSPlanetType type = types.get(i % Math.max(1, types.size()));
            double distance = MIN_DISTANCE + band * (i + rng.nextFloat());
            double scale = MIN_SCALE + (MAX_SCALE - MIN_SCALE) * rng.nextFloat();
            double orbitSpeed = MIN_SPEED + (MAX_SPEED - MIN_SPEED) * rng.nextFloat();
            double rotationSpeed = MIN_SPEED + (MAX_SPEED - MIN_SPEED) * rng.nextFloat();
            // Pass 11 (user spec: orbits on the same horizontal xz plane, like real solar systems): the orbit
            // plane is the xz plane inclined by a small random inclination (0–5°) toward a random node direction.
            // The renderer chain is Rx(xAngle)·Rz(zAngle), whose result tilts the plane by ≈√(x²+z²) with the
            // tilt direction set by (x, z) — so decompose inclination·(cos node, sin node) onto the two axes and
            // the existing two-field spec (renderer + NBT + ship math) needs no change.
            double inclination = MAX_INCLINATION_DEG * rng.nextFloat();
            double node = 360.0 * rng.nextFloat();
            double incRad = Math.toRadians(inclination);
            double nodeRad = Math.toRadians(node);
            double xAngle = Math.toDegrees(incRad * Math.cos(nodeRad));
            double zAngle = Math.toDegrees(incRad * Math.sin(nodeRad));
            planets.add(new USSPlanet(type, distance, scale, orbitSpeed, rotationSpeed, xAngle, zAngle));
        }
        return Collections.unmodifiableList(planets);
    }

    /**
     * The union of the ore materials a system's planets offer (what a Miner can harvest from it), deduplicated in
     * first-seen order (planet order, then catalog material order).
     *
     * @param planets a generated system (null or empty → empty list, defensive)
     * @return the deduplicated material list (never null; at most 3 × PLANETS_PER_SYSTEM entries)
     */
    public static List<Materials> materialsOf(List<USSPlanet> planets) {
        if (planets == null || planets.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Materials, Boolean> seen = new LinkedHashMap<>();
        for (USSPlanet planet : planets) {
            for (Materials material : planet.type.getMaterials()) {
                seen.put(material, Boolean.TRUE);
            }
        }
        return new ArrayList<>(seen.keySet());
    }
}
