package tectech.voidcraft.uss;

import java.util.List;
import java.util.Objects;

/**
 * The LOCATION of a ship inside the solar system (ship-to-ship cargo transfer, SEND / TAKE): the coarse zone the
 * ship is hovering in right now.
 *
 * <ul>
 * <li>{@link Kind#ORBIT} — one per planet: the ship hovers over / works that planet;</li>
 * <li>{@link Kind#STAR} — the star is its OWN location: only a ship actually at the star shares it (a fresh ship
 * holding at its launch gateway is NOT at the star, even though its hover body reads "star/none");</li>
 * <li>{@link Kind#RIPPLE} — one per ripple point: the ship is at that ripple site;</li>
 * <li>{@link Kind#SHIP} — the position of a fleet ship this ship has rendezvoused with (a {@code MOVE SHIP} nudge
 * cloud around it);</li>
 * <li>{@link Kind#NONE} — the ship sits at a non-body point (a fresh ship at its launch gateway, or a
 * rendezvous point whose anchor ship has already left). Such a ship shares a location only when another ship is
 * physically at the same point.</li>
 * </ul>
 *
 * <p>
 * SEND / TAKE requires the two ships to SHARE a location ({@link #shared}): the same orbit / star / ripple /
 * anchor ship — or one of the two ships within {@code USSConstants.SHIP_LOCATION_RADIUS} of the other's position
 * (the shared location is then that ship — which is either the target or the current ship).
 *
 * <p>
 * Bare JVM (positions + flags + a fleet snapshot list): the MTE passes in what it knows, the rules stay
 * unit-testable without Forge. The {@code point} argument is the ship's EFFECTIVE point: its position, or its
 * leg's destination while a WORK leg runs (a working ship hovers at the leg's destination; the server position
 * lags at the departure point until the leg completes).
 */
public final class USSLocation {

    /** What kind of zone a location is. */
    public enum Kind {
        /** A planet orbit (one location per planet). */
        ORBIT,
        /** The star (its own location). */
        STAR,
        /** A ripple site (one location per ripple point). */
        RIPPLE,
        /** A ship's position (the ship a ship has rendezvoused with). */
        SHIP,
        /** No body location (a non-body point such as the launch gateway). */
        NONE
    }

    /** One fleet ship in the rendezvous scan (its identity + where it is right now). */
    public static final class Entry {

        public final String uuid;
        public final USSPosition position;

        public Entry(String uuid, USSPosition position) {
            this.uuid = uuid;
            this.position = position;
        }
    }

    private final Kind kind;
    /** ORBIT: the planet index; RIPPLE: the ripple point index. */
    private final int index;
    /** SHIP: the anchor ship's uuid. */
    private final String shipUuid;

    private USSLocation(Kind kind, int index, String shipUuid) {
        this.kind = kind;
        this.index = index;
        this.shipUuid = shipUuid;
    }

    /** @param planetIndex the planet's fleet index (&ge; 0; a star orbit is {@link #star()}) */
    public static USSLocation orbit(int planetIndex) {
        return new USSLocation(Kind.ORBIT, Math.max(0, planetIndex), null);
    }

    /** @return the star's location */
    public static USSLocation star() {
        return new USSLocation(Kind.STAR, -1, null);
    }

    /** @param rippleIndex the ripple point's index (&ge; 0) */
    public static USSLocation ripple(int rippleIndex) {
        return new USSLocation(Kind.RIPPLE, Math.max(0, rippleIndex), null);
    }

    /**
     * @param anchorUuid the anchor ship's uuid (the ship this one is at; never null/empty)
     * @return the anchor ship's location
     */
    public static USSLocation ship(String anchorUuid) {
        return new USSLocation(Kind.SHIP, -1, anchorUuid);
    }

    /** @return no body location (a non-body point) */
    public static USSLocation none() {
        return new USSLocation(Kind.NONE, -1, null);
    }

    /**
     * Derive the location a ship is at right now.
     *
     * @param bodyStatic the ship's static-body flag (true at a ripple point or a ship rendezvous)
     * @param targetBody the ship's hover body descriptor (planet index / ripple index / -1 = star or none)
     * @param point      the ship's effective point (its position; its leg's destination while a WORK leg runs)
     * @param fleet      the fleet snapshot (all in-flight ships, this one included) for the rendezvous scan
     * @param selfUuid   this ship's uuid (the scan skips it)
     * @return the ship's location (never null)
     */
    public static USSLocation of(boolean bodyStatic, int targetBody, USSPosition point, List<Entry> fleet,
        String selfUuid) {
        if (bodyStatic && targetBody >= 0) {
            return ripple(targetBody);
        }
        if (!bodyStatic) {
            if (targetBody >= 0) {
                return orbit(targetBody);
            }
            // Star or none: a ship hovers AT the star when its effective point is the star point (the gateway
            // anchor sits 2.0 blocks from it — outside the tolerance — so a fresh gateway ship is NOT at the star).
            USSPosition p = (point == null) ? USSPosition.zero() : point;
            if (p.distanceTo(USSPosition.starCenter()) <= USSConstants.STAR_LOCATION_TOLERANCE) {
                return star();
            }
            return none();
        }
        // A ship rendezvous (MOVE SHIP): the location is the fleet ship within the ship radius, if any — the
        // cloud of ships around it share that ship's location; when the anchor has left, the point is orphaned.
        if (point == null) {
            return none();
        }
        Entry anchor = null;
        double best = USSConstants.SHIP_LOCATION_RADIUS;
        if (fleet != null) {
            for (Entry e : fleet) {
                if (e == null || e.position == null || e.uuid == null || e.uuid.equals(selfUuid)) {
                    continue;
                }
                double d = point.distanceTo(e.position);
                if (d <= best) {
                    best = d;
                    anchor = e;
                }
            }
        }
        return (anchor != null) ? ship(anchor.uuid) : none();
    }

    /**
     * Whether two ships share a location (the SEND / TAKE co-location rule): the same orbit / star / ripple /
     * anchor ship — or one within {@code USSConstants.SHIP_LOCATION_RADIUS} of the other (the shared location is
     * then that ship; it is either the target or the current ship).
     *
     * @param posA the first ship's position
     * @param locA the first ship's location (from {@link #of})
     * @param posB the second ship's position
     * @param locB the second ship's location (from {@link #of})
     * @return true when the two ships may transfer cargo
     */
    public static boolean shared(USSPosition posA, USSLocation locA, USSPosition posB, USSLocation locB) {
        if (locA == null || locB == null) {
            return false;
        }
        if (locA.kind != Kind.NONE && locA.equals(locB)) {
            return true;
        }
        if (posA == null || posB == null) {
            return false;
        }
        return posA.distanceTo(posB) <= USSConstants.SHIP_LOCATION_RADIUS;
    }

    public Kind getKind() {
        return kind;
    }

    /** @return the planet / ripple index (ORBIT / RIPPLE kinds; -1 otherwise) */
    public int getIndex() {
        return index;
    }

    /** @return the anchor ship's uuid (SHIP kind; null otherwise) */
    public String getShipUuid() {
        return shipUuid;
    }

    public boolean isNone() {
        return kind == Kind.NONE;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof USSLocation)) {
            return false;
        }
        USSLocation o = (USSLocation) other;
        return kind == o.kind && index == o.index && Objects.equals(shipUuid, o.shipUuid);
    }

    @Override
    public int hashCode() {
        int h = kind.hashCode();
        h = 31 * h + index;
        h = 31 * h + Objects.hashCode(shipUuid);
        return h;
    }

    @Override
    public String toString() {
        switch (kind) {
            case ORBIT:
                return "USSLocation[orbit:" + index + "]";
            case STAR:
                return "USSLocation[star]";
            case RIPPLE:
                return "USSLocation[ripple:" + index + "]";
            case SHIP:
                return "USSLocation[ship:" + shipUuid + "]";
            default:
                return "USSLocation[none]";
        }
    }
}
