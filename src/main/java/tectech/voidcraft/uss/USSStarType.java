package tectech.voidcraft.uss;

/**
 * Star class of an ignited Unstable Solar System.
 *
 * <p>
 * The star class is chosen by the <strong>ignition item</strong> — the controller in the controller slot carries it
 * (see {@link tectech.voidcraft.item.ItemUSSController}) — and is not derived from the spacetime tier. The tier
 * still scales the star's rendering size and the miner's ore band; the type drives the lifespan table
 * ({@link USSConstants#lifespanForType}) and what a Starlifter can mine from it.
 *
 * <p>
 * Sixteen classes in ignition order (the controller item's meta is the ordinal); each class's data (colors, size
 * range, materials, evolution target) lives in the catalog ({@link USSStarCatalog}).
 *
 * @see USSStarCatalog
 * @see USSConstants
 */
public enum USSStarType {

    RED_DWARF,
    YELLOW_DWARF,
    RED_GIANT,
    WHITE_DWARF,
    BLACK_DWARF,
    BLUE_GIANT,
    RED_SUPERGIANT,
    SUPERNOVA,
    BLACK_HOLE,
    QUASI_STAR,
    BLUE_SUPERGIANT,
    HYPERNOVA,
    NEUTRON_STAR,
    MAGNETAR,
    GRAVASTAR,
    QUARK_STAR;

    /**
     * @return this star class as a language key for info-data display.
     */
    public String getLangKey() {
        return "tt.voidcraft_uss.star." + name().toLowerCase();
    }
}
