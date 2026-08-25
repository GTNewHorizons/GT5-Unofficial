package tectech.voidcraft.uss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import gregtech.api.enums.Materials;

/**
 * The initial Voidcraft planet catalog — the existing 12 worlds (previously the fixed {@link USSPlanetType} enum)
 * re-registered into the new registration-based system ({@link USSPlanetRegistry}).
 *
 * <p>
 * <strong>What is preserved vs. placeholder.</strong> The fields that already existed on {@link USSPlanetType} are
 * carried over verbatim — the texture (the IORE dimension abbreviation, was {@code visual}), the allowed star type
 * (was the one-star pool), and the ore elements (was the 3-material set). The fields that are NEW in the rework are
 * set to neutral, valid placeholders to be tuned by game design: the size range uses the current rendered scale band
 * ({@code 0.35–0.75}, inside the user-specified {@code 0.0–5.0} envelope), each ore gets {@code amount = 100}
 * (million) and {@code weight = 1.0}, and no fluids are defined yet.
 *
 * <p>
 * The {@link #register} helper is <em>list-based</em>: it takes the allowed star types, the ore definitions (each an
 * {@link USSPlanetOre} with its own amount + weight), and the available fluids as lists — so per-planet variation
 * (multiple star classes, differing ore amounts/weights, fluid sets) is expressed directly at the call site rather
 * than through parallel scalar parameters.
 *
 * <p>
 * Call {@link #registerAll()} once (idempotent) to populate the registry. Bare-JVM safe: only
 * {@link Materials}/{@link USSStarType} data.
 */
public final class USSPlanetCatalog {

    /** Neutral placeholder ore amount (in millions) for the re-registered catalog — tune per planet as desired. */
    public static final long DEFAULT_ORE_AMOUNT = 100L;

    /** Neutral placeholder ore weight for the re-registered catalog — tune per planet as desired. */
    public static final double DEFAULT_ORE_WEIGHT = 1.0;

    /** Default size range (preserves the current rendered scale band, inside the 0.0–5.0 envelope). */
    public static final float DEFAULT_SIZE_MIN = 0.35f;
    public static final float DEFAULT_SIZE_MAX = 0.75f;

    private static boolean registered;

    private USSPlanetCatalog() {
        throw new AssertionError("Catalog holder");
    }

    /**
     * Register the 12 catalog planets into the {@link USSPlanetRegistry}. Idempotent — a second call is a no-op.
     */
    public static synchronized void registerAll() {
        if (registered) {
            return;
        }
        registered = true;

        // Main sequence — young star, common elements.
        register(
            "rocky_world",
            "Ma",
            stars(USSStarType.MAIN_SEQUENCE),
            ores(Materials.Copper, Materials.Iron, Materials.Tin),
            Collections.<Materials>emptyList());
        register(
            "ocean_world",
            "Eu",
            stars(USSStarType.MAIN_SEQUENCE),
            ores(Materials.Nickel, Materials.Zinc, Materials.Magnesium),
            Collections.<Materials>emptyList());
        register(
            "forest_world",
            "Eg",
            stars(USSStarType.MAIN_SEQUENCE),
            ores(Materials.Aluminium, Materials.Boron, Materials.Silicon),
            Collections.<Materials>emptyList());
        register(
            "moon_world",
            "Mo",
            stars(USSStarType.MAIN_SEQUENCE),
            ores(Materials.Lithium, Materials.Lead, Materials.Silver),
            Collections.<Materials>emptyList());

        // White dwarf — ancient dense remnant, dense mid elements.
        register(
            "dense_world",
            "Ce",
            stars(USSStarType.WHITE_DWARF),
            ores(Materials.Osmium, Materials.Yttrium, Materials.Platinum),
            Collections.<Materials>emptyList());
        register(
            "crystal_world",
            "Tr",
            stars(USSStarType.WHITE_DWARF),
            ores(Materials.Tantalum, Materials.Molybdenum, Materials.Scandium),
            Collections.<Materials>emptyList());
        register(
            "volcanic_world",
            "Io",
            stars(USSStarType.WHITE_DWARF),
            ores(Materials.Tungsten, Materials.Cobalt, Materials.Bismuth),
            Collections.<Materials>emptyList());
        register(
            "metallic_world",
            "Ph",
            stars(USSStarType.WHITE_DWARF),
            ores(Materials.Niobium, Materials.Titanium, Materials.Vanadium),
            Collections.<Materials>emptyList());

        // Supermassive — extreme star, rare/exotic heavy elements.
        register(
            "gas_giant",
            "Ve",
            stars(USSStarType.SUPERMASSIVE),
            ores(Materials.Uranium, Materials.Mercury, Materials.Phosphorus),
            Collections.<Materials>emptyList());
        register(
            "rare_earth_world",
            "Ga",
            stars(USSStarType.SUPERMASSIVE),
            ores(Materials.Indium, Materials.Neodymium, Materials.Tellurium),
            Collections.<Materials>emptyList());
        register(
            "gold_world",
            "As",
            stars(USSStarType.SUPERMASSIVE),
            ores(Materials.Gold, Materials.Palladium, Materials.Iridium),
            Collections.<Materials>emptyList());
        register(
            "heavy_world",
            "Ra",
            stars(USSStarType.SUPERMASSIVE),
            ores(Materials.Rubidium, Materials.Caesium, Materials.Sulfur),
            Collections.<Materials>emptyList());
    }

    /**
     * Register one planet from list-based data.
     *
     * @param id               the stable, registry-unique identifier
     * @param texture          the texture reference (IORE dimension abbreviation for now)
     * @param allowedStarTypes the star types this planet may orbit (must be non-empty)
     * @param oreDefinitions   the available ores, each with its own amount (in millions) and weight
     * @param fluids           the available fluids (materials that yield a fluid; empty when none)
     */
    private static void register(String id, String texture, List<USSStarType> allowedStarTypes,
        List<USSPlanetOre> oreDefinitions, List<Materials> fluids) {
        USSPlanetDefinition.Builder b = USSPlanetDefinition.builder()
            .id(id)
            .texture(texture)
            .sizeRange(DEFAULT_SIZE_MIN, DEFAULT_SIZE_MAX)
            .allowedStarTypes(allowedStarTypes)
            .ores(oreDefinitions)
            .fluids(fluids);
        USSPlanetRegistry.register(b.build());
    }

    /**
     * Wrap one or more star types into a list (a single-element list for the common one-star case).
     */
    private static List<USSStarType> stars(USSStarType... types) {
        return Arrays.asList(types);
    }

    /**
     * Build the ore-definition list from elements, applying the catalog's neutral placeholder amount + weight.
     */
    private static List<USSPlanetOre> ores(Materials... materials) {
        List<USSPlanetOre> result = new ArrayList<>(materials.length);
        for (Materials material : materials) {
            result.add(new USSPlanetOre(material, DEFAULT_ORE_AMOUNT, DEFAULT_ORE_WEIGHT));
        }
        return result;
    }

    /**
     * Test hook: reset the {@link #registered} flag so {@link #registerAll()} can run again after a
     * {@link USSPlanetRegistry#clear()}.
     */
    public static synchronized void resetForTests() {
        registered = false;
    }
}
