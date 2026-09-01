package tectech.voidcraft.uss;

/**
 * The triangle-shell geometry of the star-scale infrastructure (the Dyson Swarm + the constructor-built shells):
 * a latitude-row lattice of near-equilateral triangles over a sphere, one row per pole-to-pole arc step, the
 * triangle COUNT of the shell as a PURE function of (radius, triangle edge) — the server derives a target's
 * BUILD CAPACITY from it (one structure unit per triangle), the client bakes the same mesh it renders. One
 * formula, one count, both sides.
 *
 * <p>
 * Bare JVM (floats + ints only — no MC types).
 */
public final class USSInfraShell {

    private USSInfraShell() {
        throw new AssertionError("Utility holder");
    }

    /**
     * The shell's latitude rows: the pole-to-pole span split so each row's arc height equals the equilateral
     * triangle's height for the given edge — (√3/2) × edge — so the panels keep their shape from equator to
     * pole (minimum 2 rows).
     */
    public static int rowCount(float radius, float edge) {
        final float rowHeight = (float) Math.sqrt(3) * 0.5f * edge;
        return Math.max(2, (int) Math.round((float) Math.PI * radius / rowHeight));
    }

    /**
     * A row's longitude segments: its mid-latitude circumference over the edge, minimum 6 (the pole rows close
     * the shell in a six-panel cap).
     */
    public static int rowSegments(float radius, int row, int rows, float edge) {
        final float mid = -((float) Math.PI / 2f) + (float) Math.PI * (row + 0.5f) / rows;
        final int segments = (int) Math.round(2f * (float) Math.PI * radius * (float) Math.cos(mid) / edge);
        return Math.max(6, segments);
    }

    /**
     * A row's emitted triangles: an interior row emits two per segment (the up and down panels); a pole row
     * emits one — its degenerate twin collapses to the pole vertex and is dropped.
     */
    public static int rowTriangles(int segments, int row, int rows) {
        if (rows > 2 && row > 0 && row < rows - 1) {
            return 2 * segments;
        }
        return segments;
    }

    /**
     * The shell's full triangle count at (radius, edge) — the build CAPACITY a target's shell offers (one
     * structure unit per triangle), and the mesh's total panel count the renderer fills fractionally.
     */
    public static long triangleCount(float radius, float edge) {
        final int rows = rowCount(radius, edge);
        long total = 0L;
        for (int row = 0; row < rows; row++) {
            total += rowTriangles(rowSegments(radius, row, rows, edge), row, rows);
        }
        return total;
    }
}
