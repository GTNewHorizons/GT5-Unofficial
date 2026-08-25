package tectech.voidcraft.uss;

/**
 * One scannable point of a solar system's spacetime-ripple field (the Explorer mechanic).
 *
 * <p>
 * The field is a uniformly spaced {@code 7×7×7} point grid (343 points). Each point has a POSITION within the solar
 * system (fleet-anchor coordinates, see {@link USSPosition}) — its grid cell mapped onto one of three
 * evenly-spaced sphere shells (see {@link USSRipples}) — and is either a RIPPLE or not. A point starts HIDDEN
 * (unscanned); an Explorer flies to it and scans it (duration from its scan power), which reveals whether it is a
 * ripple. Scanned ripple points render as a pulsating dark-blue transparent triangle (client side).
 *
 * <p>
 * Immutable + bare-JVM safe (no Forge objects) so the field is unit-testable in a bare JVM.
 *
 * @see USSRippleField
 * @see USSRipples
 */
public final class USSRipplePoint {

    /** The field's index of this point (0..342, stable within a system). */
    private final int index;

    /** The point's X cell of the 7×7×7 grid (0..6). */
    private final int gridX;
    /** The point's Y cell of the 7×7×7 grid (0..6). */
    private final int gridY;
    /** The point's Z cell of the 7×7×7 grid (0..6). */
    private final int gridZ;

    /** The point's position in the solar system (fleet-anchor blocks; on one of the three shells). */
    private final USSPosition position;

    /** True if this point is a spacetime ripple (fixed at system creation — a pure function of star + seed). */
    private final boolean ripple;

    /** The shell this point sits on (0 = innermost … {@code NUM_SHELLS-1} = outermost), for the client render. */
    private final int shell;

    public USSRipplePoint(int index, int gridX, int gridY, int gridZ, USSPosition position, boolean ripple, int shell) {
        this.index = index;
        this.gridX = gridX;
        this.gridY = gridY;
        this.gridZ = gridZ;
        this.position = position;
        this.ripple = ripple;
        this.shell = shell;
    }

    /**
     * @return the field index (0..342)
     */
    public int getIndex() {
        return index;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public int getGridZ() {
        return gridZ;
    }

    /**
     * @return the point's position in the solar system (fleet-anchor blocks, never null)
     */
    public USSPosition getPosition() {
        return position;
    }

    /**
     * @return true if this point is a spacetime ripple
     */
    public boolean isRipple() {
        return ripple;
    }

    /**
     * @return the shell index this point sits on (0 = innermost … outermost)
     */
    public int getShell() {
        return shell;
    }

    @Override
    public String toString() {
        return "USSRipplePoint[" + index
            + " @ grid("
            + gridX
            + ","
            + gridY
            + ","
            + gridZ
            + ") shell="
            + shell
            + (ripple ? " RIPPLE" : "")
            + " "
            + position
            + "]";
    }
}
