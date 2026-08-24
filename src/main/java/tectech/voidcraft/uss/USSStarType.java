package tectech.voidcraft.uss;

/**
 * Star class of an ignited Unstable Solar System.
 *
 * <p>
 * Phase 4 pass 1: the star class is chosen by the <strong>ignition item</strong> — the controller in the
 * controller slot carries it (see {@link tectech.voidcraft.item.ItemUSSController}) — and is no longer derived
 * from the spacetime tier. The tier still scales the star's rendering size and the miner's ore band; the type
 * drives the lifespan table ({@link USSConstants#lifespanForType}) and what a Starlifter can mine from it.
 *
 * <p>
 * Placeholder balance — see {@code docs/Voidcraft_Implementation_Plan.md}.
 *
 * @see USSConstants
 */
public enum USSStarType {

    /** A long-burning main sequence star — a Starlifter harvests its Stellar Plasma. */
    MAIN_SEQUENCE,

    /**
     * A dense white dwarf with a very long remaining lifespan — a Starlifter harvests White Dwarf Matter + Stellar
     * Plasma.
     */
    WHITE_DWARF,

    /**
     * A supermassive star that burns brightest and shortest — a Starlifter harvests Black Dwarf Matter + Stellar
     * Plasma.
     */
    SUPERMASSIVE;

    /**
     * @return this star class as a language key for info-data display.
     */
    public String getLangKey() {
        switch (this) {
            case WHITE_DWARF:
                return "tt.voidcraft_uss.star.white_dwarf";
            case SUPERMASSIVE:
                return "tt.voidcraft_uss.star.supermassive";
            default:
                return "tt.voidcraft_uss.star.main_sequence";
        }
    }
}
