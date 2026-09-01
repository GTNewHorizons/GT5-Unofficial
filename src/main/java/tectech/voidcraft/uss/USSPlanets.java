package tectech.voidcraft.uss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
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
 * (star type, seed) still yields exactly one fixed system.
 *
 * <p>
 * <strong>Orbits as fixed band slots.</strong> The orbit radius is NOT a random draw — a randomized distance let
 * two planets drift onto nearly the same radius and overlap. The system's orbits are the fixed slots of
 * {@link USSOrbitBand}: NEAR 2 slots (3.0–6.0), MEDIUM 6 slots (7.0–17.0), FAR 3 slots (18.0–23.1) — 11 slots,
 * each at a predetermined radius (the band's range split into equal-width slots, a 1-block gap between the
 * bands' ranges). A planet's registered band ({@link USSPlanetDefinition#getOrbitBand()}) decides where it can
 * spawn — volcanic planets orbit close to the star, habitable worlds in the middle, gas giants at the edge. The
 * generator shuffles the 11 slots, takes one per planet (the count, capped at the slot total when a star wants
 * more planets than slots), sorts the chosen slots inner → outer, and fills each with a type drawn from ITS
 * band's shuffled pool. Invariants: no two planets share a slot, a band never hosts more planets than it has
 * slots, every planet orbits in its type's band, and the radius is a pure function of (band, slot) — so the
 * orbits can never overlap. The bands span the legacy distance range: inner edge {@link #MIN_DISTANCE}
 * (3 blocks, clear of the star surface) and outer edge {@link #MAX_DISTANCE} (pass 14, user: "ships exiting the
 * dome isn't good" — 4 blocks of margin, which covers the worst-case ship extension (hover 0.5 + planet half
 * 0.375 + spread 2.0 = 2.875); MAX_DISTANCE = USSConstants.SPACE_SHELL_RADIUS − 4 = 23.1, dome 27.1). Each
 * planet gets rendered scale 0.175–0.66 across the texture tiers (the definition's size range 0.35–1.32 reduced
 * by PLANET_RENDER_SCALE) and orbit/spin speeds 0.5–1.5. Pass 11 (user spec):
 * all orbits sit on the SAME horizontal (xz) plane like a real solar system, each inclined only 0–1.67° from it
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
     * small random angle in 0–5/3° (≈1.67°; the legacy ±30° dual tilts are gone; the old constant is replaced).
     */
    public static final double MAX_INCLINATION_DEG = 5.0 / 3.0;

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

        /** The orbit band this planet's slot belongs to (its type's band); null for a free-form planet. */
        public final USSOrbitBand orbitBand;

        /**
         * The slot index within {@link #orbitBand} ({@code 0..orbitBand.slotCount-1}); {@code -1} for a
         * free-form planet.
         */
        public final int slot;

        /**
         * Orbit radius in blocks from the star center — the predetermined radius of this planet's slot
         * ({@code orbitBand.slotRadius(slot)}, within {@link #MIN_DISTANCE}–{@link #MAX_DISTANCE}); a free-form
         * planet (a null band) carries an arbitrary distance.
         */
        public final double distance;

        /** Hologram scale — sampled from the definition's size range (0.0–5.0), applied to the render. */
        public final double scale;

        /** Orbit angular speed factor (within {@link #MIN_SPEED}–{@link #MAX_SPEED}). */
        public final double orbitSpeed;

        /** Self-rotation speed factor (within {@link #MIN_SPEED}–{@link #MAX_SPEED}). */
        public final double rotationSpeed;

        /**
         * Orbit-plane tilt about X, in degrees — one component of the small 0–1.67° inclination (pass 11: coplanar
         * orbits; |x| ≤ {@link #MAX_INCLINATION_DEG}).
         */
        public final double xAngle;

        /**
         * Orbit-plane tilt about Z, in degrees — the other component of the small 0–1.67° inclination (pass 11:
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

        public USSPlanet(USSPlanetDefinition definition, USSOrbitBand orbitBand, int slot, double distance,
            double scale, double orbitSpeed, double rotationSpeed, double xAngle, double zAngle, boolean hasRing,
            int ringVariant) {
            this.definition = definition;
            this.orbitBand = orbitBand;
            this.slot = slot;
            this.distance = distance;
            this.scale = scale;
            this.orbitSpeed = orbitSpeed;
            this.rotationSpeed = rotationSpeed;
            this.xAngle = xAngle;
            this.zAngle = zAngle;
            this.hasRing = hasRing;
            this.ringVariant = ringVariant;
        }

        /**
         * A free-form planet (orbit math, tests): no band slot — the distance is an arbitrary value, not a slot
         * radius.
         */
        public USSPlanet(USSPlanetDefinition definition, double distance, double scale, double orbitSpeed,
            double rotationSpeed, double xAngle, double zAngle, boolean hasRing, int ringVariant) {
            this(
                definition,
                null,
                -1,
                distance,
                scale,
                orbitSpeed,
                rotationSpeed,
                xAngle,
                zAngle,
                hasRing,
                ringVariant);
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
            return this.definition == that.definition && this.orbitBand == that.orbitBand
                && this.slot == that.slot
                && this.distance == that.distance
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
            h = 31 * h + (orbitBand == null ? 0 : orbitBand.hashCode());
            h = 31 * h + slot;
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
            return "USSPlanet[" + definition
                .getId() + ", band=" + orbitBand + ", slot=" + slot + ", d=" + distance + ", s=" + scale + "]";
        }
    }

    /**
     * One orbit slot: the band it belongs to, its index within the band, and the slot's predetermined orbit
     * radius.
     */
    private static final class Slot {

        final USSOrbitBand band;
        final int slot;
        final double radius;

        Slot(USSOrbitBand band, int slot, double radius) {
            this.band = band;
            this.slot = slot;
            this.radius = radius;
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
     * <li><strong>Planet types</strong> — drawn per slot from the registered planets of the slot's band
     * (each band's pool shuffled; a planet's registered band decides where it can spawn).</li>
     * <li><strong>Orbit radius</strong> — the slot's predetermined radius ({@link USSOrbitBand#slotRadius(int)},
     * a pure function of (band, slot) — no RNG draw).</li>
     * <li><strong>Planet size</strong> — sampled from each planet's size range
     * ({@link USSPlanetDefinition#getSizeMin()}…{@link USSPlanetDefinition#getSizeMax()}, 0.0–5.0).</li>
     * </ul>
     *
     * @param starType the star's type (null → {@link USSStarType#YELLOW_DWARF}, defensive) — selects the pool
     * @param seed     any stable long (the USS ignition timestamp in practice) — same (starType, seed) always yields
     *                 the same system
     * @return the system's planets (never null; empty when the star's planet range admits 0): planet i is the i-th
     *         chosen slot in inner → outer order, its orbit radius the slot's predetermined radius; a band never
     *         hosts more planets than it has slots and no two planets share a slot
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

        // The pool grouped by orbit band — a band's slots are filled only with that band's planets (a planet's
        // registered band decides where it can spawn). A band with no planets in the pool loses its slots.
        final USSOrbitBand[] bands = USSOrbitBand.values();
        final Map<USSOrbitBand, List<USSPlanetDefinition>> bandPools = new EnumMap<>(USSOrbitBand.class);
        boolean anyBandPool = false;
        for (USSOrbitBand band : bands) {
            List<USSPlanetDefinition> bandPool = new ArrayList<>();
            for (USSPlanetDefinition def : pool) {
                if (def.getOrbitBand() == band) {
                    bandPool.add(def);
                }
            }
            bandPools.put(band, bandPool);
            if (!bandPool.isEmpty()) {
                anyBandPool = true;
            }
        }
        if (!anyBandPool) {
            throw new IllegalStateException("No registered planet has an orbit band for star type " + starType);
        }

        // The orbit slots: every slot of every band that has a pool, at its predetermined radius.
        List<Slot> slots = new ArrayList<>();
        for (USSOrbitBand band : bands) {
            if (bandPools.get(band)
                .isEmpty()) {
                continue;
            }
            for (int slot = 0; slot < band.slotCount; slot++) {
                slots.add(new Slot(band, slot, band.slotRadius(slot)));
            }
        }
        // Shuffled — which slots the system uses (and the band mix) varies per seed; the radii never vary.
        for (int i = slots.size() - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            Slot tmp = slots.get(i);
            slots.set(i, slots.get(j));
            slots.set(j, tmp);
        }
        // A star may want more planets than there are slots — the system then hosts every slot once.
        final int taken = Math.min(count, slots.size());
        List<Slot> chosen = new ArrayList<>(slots.subList(0, taken));
        // Inner → outer: planet 0 is the innermost, so the infodata and the targeting index read as a real
        // solar-system listing.
        Collections.sort(chosen, new Comparator<Slot>() {

            @Override
            public int compare(Slot a, Slot b) {
                return Double.compare(a.radius, b.radius);
            }
        });

        // Each band's types, shuffled (in band order, so the per-band draw order is fixed): a slot of a band is
        // filled by the band's next type, wrapping when the band's pool is smaller than its slot count
        // (duplicates then unavoidable).
        for (USSOrbitBand band : bands) {
            List<USSPlanetDefinition> bandPool = bandPools.get(band);
            for (int i = bandPool.size() - 1; i > 0; i--) {
                int j = rng.nextInt(i + 1);
                USSPlanetDefinition tmp = bandPool.get(i);
                bandPool.set(i, bandPool.get(j));
                bandPool.set(j, tmp);
            }
        }
        final int[] bandCursor = new int[bands.length];
        List<USSPlanet> planets = new ArrayList<>(taken);
        for (Slot slot : chosen) {
            List<USSPlanetDefinition> bandPool = bandPools.get(slot.band);
            int cursor = bandCursor[slot.band.ordinal()];
            USSPlanetDefinition def = bandPool.get(cursor % Math.max(1, bandPool.size()));
            bandCursor[slot.band.ordinal()] = cursor + 1;
            // The planet's SIZE — sampled from the definition's size range (0.0–5.0). This is both the hologram
            // scale (the render size of the planet cube) and the size used in the miner's ore-amount calculation
            // (ore.amount × planetSize²).
            double scale = (def.getSizeMin() + (def.getSizeMax() - def.getSizeMin()) * rng.nextFloat())
                * PLANET_RENDER_SCALE;
            double orbitSpeed = MIN_SPEED + (MAX_SPEED - MIN_SPEED) * rng.nextFloat();
            double rotationSpeed = MIN_SPEED + (MAX_SPEED - MIN_SPEED) * rng.nextFloat();
            // Orbits on the same horizontal xz plane, like real solar systems: the orbit plane is the xz plane
            // inclined by a small random inclination (0–1.67°) toward a random node direction.
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
            // The orbit radius is the slot's predetermined radius — no RNG draw, so orbits can never overlap.
            planets.add(
                new USSPlanet(
                    def,
                    slot.band,
                    slot.slot,
                    slot.radius,
                    scale,
                    orbitSpeed,
                    rotationSpeed,
                    xAngle,
                    zAngle,
                    hasRing,
                    ringVariant));
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
