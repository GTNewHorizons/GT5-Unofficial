package tectech.voidcraft.uss;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import gregtech.api.enums.Materials;

/**
 * The Voidcraft planet catalog — the 45 rendered planet textures, each registered into {@link USSPlanetRegistry}.
 *
 * <p>
 * Every planet is one of the bundled textures (see {@code assets/tectech/textures/uss/planets/}), sized by its
 * texture tier ({@link PlanetTier}: tiny 8×, small 12×, normal 16×, big 24×, huge 32×), and either a gas giant or a
 * rocky world. A planet's hologram scale is the tier's base scale with a ±10% variation (set by
 * {@link USSPlanetDefinition.Builder#tier(PlanetTier)}); its orbit-ring probability follows the gas-giant rule
 * (gas giants 50%, normal-and-larger non-giants 10%, tiny/small 0% — see
 * {@link USSPlanetDefinition#ringChance()}).
 *
 * <p>
 * All planets may orbit any star class; the per-star pool can be refined later (the star-registration reconciliation
 * pass). The ore sets are placeholders (3 materials each, amount {@value #DEFAULT_ORE_AMOUNT} million, weight
 * {@value #DEFAULT_ORE_WEIGHT}) to be rebalanced in game design.
 *
 * <p>
 * Call {@link #registerAll()} once (idempotent) to populate the registry. Bare-JVM safe: only
 * {@link Materials}/{@link USSStarType}/{@link PlanetTier} data.
 */
public final class USSPlanetCatalog {

    /** Placeholder ore amount (in millions) for the catalog — rebalanced later. */
    public static final long DEFAULT_ORE_AMOUNT = 100L;

    /** Placeholder ore weight for the catalog — rebalanced later. */
    public static final double DEFAULT_ORE_WEIGHT = 1.0;

    /** Every star class (the catalog is star-agnostic; the per-star pool can be refined later). */
    private static final List<USSStarType> ALL_STARS = Arrays.asList(USSStarType.values());

    private static boolean registered;

    private USSPlanetCatalog() {
        throw new AssertionError("Catalog holder");
    }

    /**
     * Register the 45 catalog planets into the {@link USSPlanetRegistry}. Idempotent — a second call is a no-op.
     */
    public static synchronized void registerAll() {
        if (registered) {
            return;
        }
        registered = true;

        // TINY (8× faces) — the smallest rocky bodies.
        register("tiny_rock_1", PlanetTier.TINY, false, Materials.Iron, Materials.Copper, Materials.Tin);
        register("tiny_rock_2", PlanetTier.TINY, false, Materials.Iron, Materials.Copper, Materials.Tin);

        // SMALL (12× faces) — small rocky worlds and the two named inner planets.
        register("small_rock_1", PlanetTier.SMALL, false, Materials.Iron, Materials.Copper, Materials.Tin);
        register("small_rock_2", PlanetTier.SMALL, false, Materials.Iron, Materials.Copper, Materials.Tin);
        register("mars", PlanetTier.SMALL, false, Materials.Iron, Materials.Copper, Materials.Tin);
        register("mercury", PlanetTier.SMALL, false, Materials.Iron, Materials.Copper, Materials.Tin);

        // NORMAL (16× faces) — the common mid-tier worlds.
        for (int i = 1; i <= 9; i++) {
            register(
                "normal_rocky_" + i,
                PlanetTier.NORMAL,
                false,
                Materials.Aluminium,
                Materials.Nickel,
                Materials.Zinc);
        }
        for (int i = 1; i <= 5; i++) {
            register(
                "normal_gas_" + i,
                PlanetTier.NORMAL,
                true,
                Materials.Uranium,
                Materials.Mercury,
                Materials.Phosphorus);
        }
        register("earth", PlanetTier.NORMAL, false, Materials.Aluminium, Materials.Copper, Materials.Tin);
        register("venus", PlanetTier.NORMAL, false, Materials.Sulfur, Materials.Phosphorus, Materials.Silicon);
        register("tidal_habit", PlanetTier.NORMAL, false, Materials.Magnesium, Materials.Lithium, Materials.Boron);
        register("tidal_hot", PlanetTier.NORMAL, false, Materials.Bismuth, Materials.Lead, Materials.Silicon);

        // BIG (24× faces) — large worlds and the mid gas giants.
        for (int i = 1; i <= 4; i++) {
            register(
                "big_rocky_" + i,
                PlanetTier.BIG,
                false,
                Materials.Titanium,
                Materials.Niobium,
                Materials.Vanadium);
        }
        for (int i = 1; i <= 8; i++) {
            register("big_gas_" + i, PlanetTier.BIG, true, Materials.Tungsten, Materials.Cobalt, Materials.Bismuth);
        }
        register("neptune", PlanetTier.BIG, true, Materials.Osmium, Materials.Platinum, Materials.Palladium);
        register("uranus", PlanetTier.BIG, true, Materials.Platinum, Materials.Gold, Materials.Silver);
        register("waterworld", PlanetTier.BIG, false, Materials.Magnesium, Materials.Lithium, Materials.Boron);

        // HUGE (32× faces) — the largest gas giants.
        for (int i = 1; i <= 4; i++) {
            register("huge_gas_" + i, PlanetTier.HUGE, true, Materials.Osmium, Materials.Platinum, Materials.Iridium);
        }
        register("jupiter", PlanetTier.HUGE, true, Materials.Gold, Materials.Platinum, Materials.Iridium);
        register("saturn", PlanetTier.HUGE, true, Materials.Platinum, Materials.Gold, Materials.Palladium);
    }

    /**
     * Register one planet: its texture is {@code textures/uss/planets/<id>/stitched.png} (relative to the mod's
     * texture root), sized by its tier, allowed around every star class, carrying the given placeholder ore set.
     *
     * @param id        the stable, registry-unique identifier (also the texture folder name)
     * @param tier      the rendered-size tier (set by the texture resolution)
     * @param gasGiant  whether this is a gas giant (affects the orbit-ring probability)
     * @param materials the placeholder ore materials (3)
     */
    private static void register(String id, PlanetTier tier, boolean gasGiant, Materials... materials) {
        final List<USSPlanetOre> ores = new java.util.ArrayList<>(materials.length);
        for (Materials material : materials) {
            ores.add(new USSPlanetOre(material, DEFAULT_ORE_AMOUNT, DEFAULT_ORE_WEIGHT));
        }
        USSPlanetDefinition definition = USSPlanetDefinition.builder()
            .id(id)
            .texture("textures/uss/planets/" + id + "/stitched.png")
            .tier(tier)
            .gasGiant(gasGiant)
            .allowedStarTypes(ALL_STARS)
            .ores(ores)
            .fluids(Collections.<Materials>emptyList())
            .build();
        USSPlanetRegistry.register(definition);
    }

    /**
     * Test hook: reset the {@link #registered} flag so {@link #registerAll()} can run again after a
     * {@link USSPlanetRegistry#clear()}.
     */
    public static synchronized void resetForTests() {
        registered = false;
    }
}
