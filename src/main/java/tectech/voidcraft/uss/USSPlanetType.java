package tectech.voidcraft.uss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import gregtech.api.enums.Materials;

/**
 * Fixed catalog of the planet types that orbit a Voidcraft star (Phase 4 pass 3).
 *
 * <p>
 * A planet type is a <em>mineable body</em>: it carries a set of three ore materials that a Miner harvests
 * (as dust, plus the universal stone dust) when it works a planet of this type — "the different planet types
 * determine what can be mined from the system". The star's TYPE decides which planet types can exist at all:
 * every {@link USSStarType} owns a pool of four types and the pools are pairwise disjoint, so a system's
 * mineable set always stays inside that star's element family — "the star type determines what kind of planets
 * spawn".
 *
 * <p>
 * <strong>Element families.</strong> Main sequence stars (young, active) host common early elements; white
 * dwarfs (ancient dense remnants) host dense mid elements; supermassive stars host rare and exotic heavy
 * elements. All 36 catalog materials are distinct and resolve through the GT material registry.
 *
 * <p>
 * The {@link #visual} field is the IORE dimension abbreviation (the {@code gtneioreplugin.ModBlocks} key) of
 * the dimension-display block drawn as the planet hologram — the legacy EoH renderer pipeline draws any orbiting
 * object's block as-is, so reusing these blocks keeps the planet look consistent with the legacy EoH.
 *
 * <p>
 * Pure data: no game world, no RNG — unit-testable in a bare JVM (the materials resolve through
 * {@link gregtech.api.enums.Materials#getName()} / {@link gregtech.api.enums.Materials#get(String)} only).
 */
public enum USSPlanetType {

    // Main sequence — young star, common elements.
    ROCKY_WORLD(0, "rocky_world", Materials.Copper, Materials.Iron, Materials.Tin, "Ma"),
    OCEAN_WORLD(1, "ocean_world", Materials.Nickel, Materials.Zinc, Materials.Magnesium, "Eu"),
    FOREST_WORLD(2, "forest_world", Materials.Aluminium, Materials.Boron, Materials.Silicon, "Eg"),
    MOON_WORLD(3, "moon_world", Materials.Lithium, Materials.Lead, Materials.Silver, "Mo"),

    // White dwarf — ancient dense remnant, dense mid elements.
    DENSE_WORLD(4, "dense_world", Materials.Osmium, Materials.Yttrium, Materials.Platinum, "Ce"),
    CRYSTAL_WORLD(5, "crystal_world", Materials.Tantalum, Materials.Molybdenum, Materials.Scandium, "Tr"),
    VOLCANIC_WORLD(6, "volcanic_world", Materials.Tungsten, Materials.Cobalt, Materials.Bismuth, "Io"),
    METALLIC_WORLD(7, "metallic_world", Materials.Niobium, Materials.Titanium, Materials.Vanadium, "Ph"),

    // Supermassive — extreme star, rare/exotic heavy elements.
    GAS_GIANT(8, "gas_giant", Materials.Uranium, Materials.Mercury, Materials.Phosphorus, "Ve"),
    RARE_EARTH_WORLD(9, "rare_earth_world", Materials.Indium, Materials.Neodymium, Materials.Tellurium, "Ga"),
    GOLD_WORLD(10, "gold_world", Materials.Gold, Materials.Palladium, Materials.Iridium, "As"),
    HEAVY_WORLD(11, "heavy_world", Materials.Rubidium, Materials.Caesium, Materials.Sulfur, "Ra");

    /** Stable numeric id (0–11, in catalog order). */
    public final int id;

    /** Lang key for the human-readable name (see the en/ru lang files). */
    public final String langKey;

    /** The three ore materials a Miner harvests from a planet of this type (order is display order). */
    public final Materials[] materials;

    /** IORE dimension abbreviation — the key of the dimension-display block drawn as the planet hologram. */
    public final String visual;

    /** The star type whose pool this planet type belongs to. */
    public final USSStarType starType;

    USSPlanetType(int id, String key, Materials first, Materials second, Materials third, String visual) {
        this.id = id;
        this.langKey = "tt.voidcraft_uss.planet." + key;
        this.materials = new Materials[] { first, second, third };
        this.visual = visual;
        // Catalog grouping (declaration order): ids 0–3 main sequence, 4–7 white dwarf, 8–11 supermassive.
        this.starType = id < 4 ? USSStarType.MAIN_SEQUENCE
            : id < 8 ? USSStarType.WHITE_DWARF : USSStarType.SUPERMASSIVE;
    }

    private static final Map<USSStarType, List<USSPlanetType>> POOLS = new EnumMap<>(USSStarType.class);

    static {
        for (USSStarType starType : USSStarType.values()) {
            List<USSPlanetType> pool = new ArrayList<>();
            for (USSPlanetType type : values()) {
                if (type.starType == starType) {
                    pool.add(type);
                }
            }
            POOLS.put(starType, Collections.unmodifiableList(pool));
        }
    }

    /**
     * @return the human-readable lang key of this planet type (e.g. {@code tt.voidcraft_uss.planet.rocky_world}).
     */
    public String getLangKey() {
        return langKey;
    }

    /**
     * @return the three ore materials of this planet type (never null; see class javadoc).
     */
    public Materials[] getMaterials() {
        return materials;
    }

    /**
     * @return the IORE dimension abbreviation of the hologram block (see class javadoc).
     */
    public String getVisual() {
        return visual;
    }

    /**
     * @return the star type that owns this planet type (its spawn pool).
     */
    public USSStarType getStarType() {
        return starType;
    }

    /**
     * @return all planet types in catalog order (12 entries).
     */
    public static List<USSPlanetType> all() {
        List<USSPlanetType> all = new ArrayList<>();
        for (USSPlanetType type : values()) {
            all.add(type);
        }
        return all;
    }

    /**
     * Look up a planet type by its stable id.
     *
     * @param id 0–11
     * @return the type with that id, or null when the id is out of range
     */
    public static USSPlanetType byId(int id) {
        for (USSPlanetType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }

    /**
     * The spawn pool of a star type: the four planet types a system of this star may generate.
     *
     * @param starType the star type (null → {@link USSStarType#MAIN_SEQUENCE}, defensive)
     * @return the pool in catalog order (4 entries, never null)
     */
    public static List<USSPlanetType> pool(USSStarType starType) {
        if (starType == null) {
            starType = USSStarType.MAIN_SEQUENCE;
        }
        return POOLS.get(starType);
    }
}
