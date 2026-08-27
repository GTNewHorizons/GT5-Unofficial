package tectech.voidcraft.uss;

/**
 * The rendered-size tier of a planet, set by its texture resolution. A planet's hologram scale is drawn from its
 * tier's base scale with a small random variation (see {@link USSPlanetDefinition#getSizeMin()} /
 * {@code getSizeMax}());
 * the tier also selects the orbit-ring texture set a ringed planet uses.
 *
 * <ul>
 * <li>{@link #TINY} — 8×8 faces, smallest bodies</li>
 * <li>{@link #SMALL} — 12×12 faces</li>
 * <li>{@link #NORMAL} — 16×16 faces</li>
 * <li>{@link #BIG} — 24×24 faces</li>
 * <li>{@link #HUGE} — 32×32 faces, the gas giants</li>
 * </ul>
 */
public enum PlanetTier {

    TINY,
    SMALL,
    NORMAL,
    BIG,
    HUGE;

    /**
     * Base hologram scale for this tier (the planet's rendered size before the ±10% variation). Larger tiers render
     * larger, so a huge gas giant reads bigger than a tiny rock.
     *
     * @return the base scale (blocks, the cube's ±0.5·scale half-extent is multiplied into the model)
     */
    public float baseScale() {
        switch (this) {
            case TINY:
                return 0.35f;
            case SMALL:
                return 0.45f;
            case NORMAL:
                return 0.55f;
            case BIG:
                return 0.65f;
            case HUGE:
                return 1.2f;
            default:
                return 0.55f;
        }
    }

    /**
     * How many distinct orbit-ring textures this tier ships (the ring set is indexed 1..count). TINY has no ring set.
     *
     * @return the ring-variant count for this tier (0 = no rings)
     */
    public int ringVariantCount() {
        switch (this) {
            case TINY:
                return 0;
            case SMALL:
            case HUGE:
                return 6;
            case NORMAL:
            case BIG:
                return 8;
            default:
                return 0;
        }
    }

    /**
     * The resource path (relative to {@code assets/tectech/textures/uss/rings/}) of this tier's ring set — ring
     * variant {@code n} is {@code <dir>/<n>.png}.
     *
     * @return the ring-set directory for this tier
     */
    public String ringDir() {
        switch (this) {
            case TINY:
                return "tiny";
            case SMALL:
                return "small";
            case NORMAL:
                return "normal";
            case BIG:
                return "big";
            case HUGE:
                return "huge";
            default:
                return "normal";
        }
    }
}
