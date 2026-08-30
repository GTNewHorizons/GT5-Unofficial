package tectech.voidcraft.multiblock;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/** Invariants of the Satellite Rail Launcher structure shape (plain data, no MTE or world references). */
public class SatelliteLauncherStructureTest {

    @Test
    public void testShapeDimensions() {
        // 7 wide (x) × 12 tall (y, layers) × 7 deep (z, slices)
        assertEquals(12, SatelliteLauncherStructure.RAW_SHAPE.length, "y layers");
        for (String[] layer : SatelliteLauncherStructure.RAW_SHAPE) {
            assertEquals(7, layer.length, "z slices per layer");
            for (String slice : layer) {
                assertEquals(7, slice.length(), "x width per slice");
            }
        }
    }

    @Test
    public void testCellCounts() {
        assertEquals(
            588,
            SatelliteLauncherStructure.EXPECTED_CELLS,
            "588 cells = 1 controller + 440 casings + 147 panels");
        assertEquals(588, SatelliteLauncherStructure.countCells(SatelliteLauncherStructure.RAW_SHAPE));
        assertEquals(
            1,
            SatelliteLauncherStructure.countCells('~', SatelliteLauncherStructure.RAW_SHAPE),
            "exactly one anchor");
        assertEquals(
            440,
            SatelliteLauncherStructure.countCells('A', SatelliteLauncherStructure.RAW_SHAPE),
            "plain casings");
        assertEquals(
            147,
            SatelliteLauncherStructure.countCells('B', SatelliteLauncherStructure.RAW_SHAPE),
            "deck panels (the top three layers)");
    }

    @Test
    public void testAnchorIsBottomCenter() {
        // The controller sits at the bottom layer's center of the middle slice: x=3, y=0, z=3.
        assertArrayEquals(
            new int[] { 3, 0, 3 },
            SatelliteLauncherStructure.findAnchor(SatelliteLauncherStructure.RAW_SHAPE));
    }

    @Test
    public void testDeckPanelsAreTheTopThreeLayers() {
        // Layers 9, 10, 11 (the top three) are entirely panels; every lower layer carries no panels.
        for (int y = 0; y < 9; y++) {
            for (String slice : SatelliteLauncherStructure.RAW_SHAPE[y]) {
                assertEquals(0, count(slice, 'B'), "layer " + y + " must carry no panels");
            }
        }
        for (int y = 9; y < 12; y++) {
            for (String slice : SatelliteLauncherStructure.RAW_SHAPE[y]) {
                assertEquals(7, count(slice, 'B'), "layer " + y + " must be all panels");
            }
        }
    }

    @Test
    public void testFindAnchorRejectsBadShapes() {
        assertThrows(
            IllegalStateException.class,
            () -> SatelliteLauncherStructure.findAnchor(new String[][] { { "AAAAAAA" } }),
            "a shape without an anchor");
        assertThrows(
            IllegalStateException.class,
            () -> SatelliteLauncherStructure.findAnchor(new String[][] { { "A~AAAAA", "A~AAAAA" } }),
            "a shape with multiple anchors");
    }

    private static int count(String s, char c) {
        int n = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                n++;
            }
        }
        return n;
    }
}
