package tectech.voidcraft.render;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraftforge.common.util.ForgeDirection;

import org.junit.jupiter.api.Test;

import tectech.voidcraft.render.AssemblerVisuals.Snapshot;

/**
 * Pins the assembler-visual geometry contract: the scan-volume axes (mirroring the assemblers' scanCell
 * basis for all six front directions), the grid indexing, the scanning-plane sweep and the preview
 * placement. The GL draw itself is client-only and verified by playtest.
 */
class AssemblerVisualsTest {

    private static final double TOL = 1e-9;

    private static Snapshot snap(int facing, boolean machineValid, boolean scanning) {
        return new Snapshot(-100, null, 10, 64, 20, facing, 5, 5, 10, false, machineValid, scanning);
    }

    // ---- scan axes (the assembler's scanCell basis, all six front directions) ----

    @Test
    void scanAxesNorthSouthUseTheXYPlane() {
        double[] north = AssemblerVisuals.scanAxes(ForgeDirection.NORTH.ordinal());
        assertEquals(1.0, north[0], TOL);
        assertEquals(0.0, north[1], TOL);
        assertEquals(0.0, north[2], TOL);
        assertEquals(0.0, north[3], TOL);
        assertEquals(1.0, north[4], TOL);
        assertEquals(0.0, north[5], TOL);
        assertTrue(same(AssemblerVisuals.scanAxes(ForgeDirection.SOUTH.ordinal()), north));
    }

    @Test
    void scanAxesEastWestUseTheYZPlane() {
        double[] east = AssemblerVisuals.scanAxes(ForgeDirection.EAST.ordinal());
        assertEquals(0.0, east[0], TOL);
        assertEquals(1.0, east[1], TOL);
        assertEquals(0.0, east[2], TOL);
        assertEquals(0.0, east[3], TOL);
        assertEquals(0.0, east[4], TOL);
        assertEquals(1.0, east[5], TOL);
        assertTrue(same(AssemblerVisuals.scanAxes(ForgeDirection.WEST.ordinal()), east));
    }

    @Test
    void scanAxesUpDownUseTheXZPlane() {
        double[] up = AssemblerVisuals.scanAxes(ForgeDirection.UP.ordinal());
        assertEquals(1.0, up[0], TOL);
        assertEquals(0.0, up[1], TOL);
        assertEquals(0.0, up[2], TOL);
        assertEquals(0.0, up[3], TOL);
        assertEquals(0.0, up[4], TOL);
        assertEquals(1.0, up[5], TOL);
        assertTrue(same(AssemblerVisuals.scanAxes(ForgeDirection.DOWN.ordinal()), up));
    }

    @Test
    void scanAxesAreOrthogonalToEachOtherAndToTheFront() {
        for (int facing = 0; facing < 6; facing++) {
            double[] axes = AssemblerVisuals.scanAxes(facing);
            ForgeDirection front = ForgeDirection.getOrientation(facing);
            assertEquals(0.0, dot(axes, 0, front), TOL); // a1 ⊥ front
            assertEquals(0.0, dot(axes, 3, front), TOL); // a2 ⊥ front
            assertEquals(0.0, axes[0] * axes[3] + axes[1] * axes[4] + axes[2] * axes[5], TOL); // a1 ⊥ a2
        }
    }

    // ---- scan cell world positions ----

    @Test
    void scanCellNorthFacingExtendsAlongMinusZ() {
        double[] axes = AssemblerVisuals.scanAxes(ForgeDirection.NORTH.ordinal());
        // i/j in the X-Y plane, depth along -Z from the controller block corner.
        double[] cell = AssemblerVisuals.scanCell(2, -1, 3, 10, 64, 20, ForgeDirection.NORTH.ordinal(), axes);
        assertEquals(12.0, cell[0], TOL);
        assertEquals(63.0, cell[1], TOL);
        assertEquals(17.0, cell[2], TOL);
    }

    @Test
    void scanCellUpFacingExtendsAlongPlusYInTheXZPlane() {
        double[] axes = AssemblerVisuals.scanAxes(ForgeDirection.UP.ordinal());
        double[] cell = AssemblerVisuals.scanCell(-3, 4, 2, 5, 70, 5, ForgeDirection.UP.ordinal(), axes);
        assertEquals(2.0, cell[0], TOL);
        assertEquals(72.0, cell[1], TOL);
        assertEquals(9.0, cell[2], TOL);
    }

    // ---- grid indexing (mirrors the assemblers' scan) ----

    @Test
    void gridIndexShipCorners() {
        // half = 2, width = height = 5 (ship volume 5×5×10).
        assertEquals(0, AssemblerVisuals.gridIndex(-2, -2, 1, 2, 5, 5));
        assertEquals(4, AssemblerVisuals.gridIndex(2, -2, 1, 2, 5, 5));
        assertEquals(20, AssemblerVisuals.gridIndex(-2, 2, 1, 2, 5, 5));
        assertEquals(24, AssemblerVisuals.gridIndex(2, 2, 1, 2, 5, 5));
        assertEquals(249, AssemblerVisuals.gridIndex(2, 2, 10, 2, 5, 5)); // last of 250 cells
    }

    @Test
    void gridIndexBaseCorners() {
        // half = 7, width = height = 15 (base volume 15×15×15).
        assertEquals(0, AssemblerVisuals.gridIndex(-7, -7, 1, 7, 15, 15));
        assertEquals(3374, AssemblerVisuals.gridIndex(7, 7, 15, 7, 15, 15)); // last of 3375 cells
    }

    // ---- scanning-plane sweep ----

    @Test
    void planeDepthsSweepFromBothEndsToTheMiddle() {
        double[] ends = AssemblerVisuals.planeDepths(0.0, 10);
        assertEquals(0.5, ends[0], TOL);
        assertEquals(10.5, ends[1], TOL);

        double[] middle = AssemblerVisuals.planeDepths(1.0, 10);
        assertEquals(5.5, middle[0], TOL);
        assertEquals(5.5, middle[1], TOL);

        double[] mid = AssemblerVisuals.planeDepths(0.5, 10);
        assertEquals(3.0, mid[0], TOL);
        assertEquals(8.0, mid[1], TOL); // symmetric about the volume middle

        double[] baseMiddle = AssemblerVisuals.planeDepths(1.0, 15);
        assertEquals(8.0, baseMiddle[0], TOL);
        assertEquals(8.0, baseMiddle[1], TOL);
    }

    // ---- preview placement ----

    @Test
    void previewOffsetBehindTheMachineAboveTheTop() {
        // North-facing 5-tall ship machine (3 deep): behind = +Z, above = +Y.
        double[] off = AssemblerVisuals.previewOffset(
            ForgeDirection.NORTH.ordinal(),
            5,
            VoidcraftConfigDefaults.PREVIEW_DISTANCE,
            VoidcraftConfigDefaults.PREVIEW_HEIGHT);
        assertEquals(0.0, off[0], TOL);
        assertEquals(2.5 + 1.5, off[1], TOL); // machine top (2.5 above center) + config height
        assertEquals(2.5 + 1.5, off[2], TOL); // back face (2.5 behind center) + config distance
    }

    @Test
    void previewOffsetUpFacingFallsBelowTheMachine() {
        // Up-facing machine: the build volume extends up, so the preview floats below the machine —
        // distance behind the back face, which is machineHeight/2 from the controller center.
        double[] ship = AssemblerVisuals.previewOffset(
            ForgeDirection.UP.ordinal(),
            5,
            VoidcraftConfigDefaults.PREVIEW_DISTANCE,
            VoidcraftConfigDefaults.PREVIEW_HEIGHT);
        assertEquals(0.0, ship[0], TOL);
        assertEquals(-2.5 - 1.5, ship[1], TOL); // 1.5 below the machine's bottom face
        assertEquals(0.0, ship[2], TOL);

        double[] base = AssemblerVisuals.previewOffset(
            ForgeDirection.UP.ordinal(),
            15,
            VoidcraftConfigDefaults.PREVIEW_DISTANCE,
            VoidcraftConfigDefaults.PREVIEW_HEIGHT);
        assertEquals(0.0, base[0], TOL);
        assertEquals(-7.5 - 1.5, base[1], TOL);
        assertEquals(0.0, base[2], TOL);
    }

    @Test
    void previewOffsetDownFacingRisesAboveTheMachine() {
        double[] off = AssemblerVisuals.previewOffset(
            ForgeDirection.DOWN.ordinal(),
            5,
            VoidcraftConfigDefaults.PREVIEW_DISTANCE,
            VoidcraftConfigDefaults.PREVIEW_HEIGHT);
        assertEquals(0.0, off[0], TOL);
        assertEquals(2.5 + 1.5, off[1], TOL); // 1.5 above the machine's top face
        assertEquals(0.0, off[2], TOL);
    }

    // ---- snapshot change detection ----

    @Test
    void snapshotChangeDetection() {
        Snapshot a = snap(2, true, false);
        Snapshot b = snap(2, true, false);
        assertTrue(a.equalsState(b));
        assertFalse(AssemblerVisuals.shouldReplace(a, b)); // unchanged → no republish
        assertTrue(AssemblerVisuals.shouldReplace(null, b)); // missing → publish

        Snapshot invalid = snap(2, false, false);
        assertTrue(!a.equalsState(invalid));
        assertTrue(AssemblerVisuals.shouldReplace(a, invalid));

        Snapshot scanning = snap(2, true, true);
        assertTrue(!a.equalsState(scanning));

        Snapshot moved = new Snapshot(-100, null, 11, 64, 20, 2, 5, 5, 10, false, true, false);
        assertTrue(!a.equalsState(moved));
    }

    // ---- helpers ----

    private static boolean same(double[] a, double[] b) {
        for (int i = 0; i < a.length; i++) {
            if (Math.abs(a[i] - b[i]) > TOL) {
                return false;
            }
        }
        return true;
    }

    private static double dot(double[] axes, int offset, ForgeDirection front) {
        return axes[offset] * front.offsetX + axes[offset + 1] * front.offsetY + axes[offset + 2] * front.offsetZ;
    }

    /** The shipped config defaults (the values {@code VoidcraftConfig} loads for the preview location). */
    private static final class VoidcraftConfigDefaults {

        static final double PREVIEW_DISTANCE = 1.5;
        static final double PREVIEW_HEIGHT = 1.5;
    }
}
