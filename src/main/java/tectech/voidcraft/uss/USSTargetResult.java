package tectech.voidcraft.uss;

/**
 * The result of resolving a MOVE target (programming framework, Phase C): the concrete DESTINATION point in the
 * solar system plus the BODY DESCRIPTOR the client hovers at (a planet index, a ripple-point index, the star, or
 * a rendezvous point).
 *
 * <p>
 * Bare-JVM (primitives + {@link USSPosition} only). The {@code index} conventions:
 * <ul>
 * <li>{@code i ≥ 0} — planet index or ripple-point index (the client resolves it against the fleet TE's system,
 * exactly as it already does for a mission target);</li>
 * <li>{@code -1} — the star (the client hovers above the star center);</li>
 * <li>{@code -2} — a ship rendezvous (no system body — the client hovers at the resolved {@code position});</li>
 * <li>{@code -3} — HOME (the launch origin — the client treats this as a travel leg to the gateway).</li>
 * </ul>
 *
 * <p>
 * {@code staticBody}: true when the hover point is a FIXED position (ripple points, ship rendezvous) and the
 * client must hover at {@code position} exactly; false when the client should track the body's LIVE position
 * (planets keep orbiting — the ship follows the planet, as it already does during a work leg).
 */
public final class USSTargetResult {

    public static final int INDEX_STAR = -1;
    public static final int INDEX_SHIP = -2;
    public static final int INDEX_HOME = -3;

    private final USSPosition position;
    private final int index;
    private final boolean staticBody;

    public USSTargetResult(USSPosition position, int index) {
        this(position, index, false);
    }

    public USSTargetResult(USSPosition position, int index, boolean staticBody) {
        this.position = position;
        this.index = index;
        this.staticBody = staticBody;
    }

    /** @return the destination point (never null — a null position is a failed resolution) */
    public USSPosition getPosition() {
        return position;
    }

    /** @return the body descriptor (see the class javadoc conventions) */
    public int getIndex() {
        return index;
    }

    /** @return true when the client hovers at the fixed {@code position} (not the body's live position) */
    public boolean isStaticBody() {
        return staticBody;
    }

    @Override
    public String toString() {
        return "USSTargetResult[pos=" + position + ", index=" + index + ", static=" + staticBody + "]";
    }
}
