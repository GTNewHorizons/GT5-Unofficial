package tectech.voidcraft.uss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A solar system's spacetime-ripple FIELD: the full {@code 7×7×7} point grid (343 points) and which of them are
 * ripples (the Explorer mechanic).
 *
 * <p>
 * The field is a PURE function of (star type, seed) — see {@link USSRipples#generate} — so the server (Explorer
 * scanning), the client (rendering the shells), and any GUI derive the IDENTICAL field. It is immutable + bare-JVM
 * safe (no Forge objects).
 *
 * <p>
 * The field's POINTS (positions + ripple flags) are fixed at creation. The SCAN STATE (which points an Explorer has
 * already scanned/revealed) is STATEFUL and lives on the {@link VoidcraftUSS} (like the per-planet ore reserves),
 * NOT here — a point never needs rescanning once revealed.
 *
 * @see USSRipplePoint
 * @see USSRipples
 */
public final class USSRippleField {

    /** The grid edge length (7 for a 7×7×7 field — 343 points). */
    private final int gridSize;

    /** The number of ripples in the field (sampled from the star's {@code spacetimeRipples} range at creation). */
    private final int rippleCount;

    /** All 343 points, in grid order (index 0..342). Never null, never empty (a system always has a full grid). */
    private final List<USSRipplePoint> points;

    public USSRippleField(int gridSize, int rippleCount, List<USSRipplePoint> points) {
        if (points == null || points.isEmpty()) {
            throw new IllegalArgumentException("a ripple field must contain its full point grid");
        }
        this.gridSize = gridSize;
        this.rippleCount = rippleCount;
        this.points = Collections.unmodifiableList(new ArrayList<>(points));
    }

    /**
     * @return the grid edge length (7)
     */
    public int getGridSize() {
        return gridSize;
    }

    /**
     * @return the number of points in the field (343 for a 7×7×7 grid)
     */
    public int size() {
        return points.size();
    }

    /**
     * @return the number of ripples in the field (0..342, sampled from the star's ripple range)
     */
    public int getRippleCount() {
        return rippleCount;
    }

    /**
     * @param index a point index (0..{@link #size()}−1)
     * @return that point (never null); throws on an out-of-range index
     */
    public USSRipplePoint pointAt(int index) {
        return points.get(index);
    }

    /**
     * @param index a point index
     * @return true if that point is a ripple
     */
    public boolean isRipple(int index) {
        return points.get(index)
            .isRipple();
    }

    /**
     * @param index a point index
     * @return that point's position in the solar system (fleet-anchor blocks)
     */
    public USSPosition positionOf(int index) {
        return points.get(index)
            .getPosition();
    }

    /**
     * @param index a point index
     * @return the shell index that point sits on
     */
    public int shellOf(int index) {
        return points.get(index)
            .getShell();
    }

    /**
     * @return the indices of ALL ripple points, in ascending order
     */
    public List<Integer> rippleIndices() {
        List<Integer> out = new ArrayList<>(rippleCount);
        for (USSRipplePoint p : points) {
            if (p.isRipple()) {
                out.add(p.getIndex());
            }
        }
        return Collections.unmodifiableList(out);
    }

    /**
     * @return the indices of the ripple points on the given shell, in ascending order
     */
    public List<Integer> rippleIndicesOnShell(int shell) {
        List<Integer> out = new ArrayList<>();
        for (USSRipplePoint p : points) {
            if (p.isRipple() && p.getShell() == shell) {
                out.add(p.getIndex());
            }
        }
        return Collections.unmodifiableList(out);
    }

    @Override
    public String toString() {
        return "USSRippleField[grid=" + gridSize + "³=" + size() + " points, ripples=" + rippleCount + "]";
    }
}
