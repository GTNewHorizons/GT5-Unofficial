package tectech.voidcraft.multiblock;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/** Invariants of the Mining Array structure shape (plain data, no MTE or world references). */
public class MiningArrayStructureTest {

    @Test
    public void testShapeDimensions() {
        // 3 wide (x) × 3 tall (y, layers) × 2 deep (z, slices)
        assertEquals(3, MiningArrayStructure.RAW_SHAPE.length, "y layers");
        for (String[] layer : MiningArrayStructure.RAW_SHAPE) {
            assertEquals(2, layer.length, "z slices per layer");
            for (String slice : layer) {
                assertEquals(3, slice.length(), "x width per slice");
            }
        }
    }

    @Test
    public void testCellCounts() {
        assertEquals(18, MiningArrayStructure.EXPECTED_CELLS, "18 cells = 1 controller + 17 casing blocks");
        assertEquals(18, MiningArrayStructure.countCells(MiningArrayStructure.RAW_SHAPE));
        assertEquals(1, MiningArrayStructure.countCells('~', MiningArrayStructure.RAW_SHAPE), "exactly one anchor");
        assertEquals(9, MiningArrayStructure.countCells('A', MiningArrayStructure.RAW_SHAPE), "plain casings");
        assertEquals(8, MiningArrayStructure.countCells('B', MiningArrayStructure.RAW_SHAPE), "accent panels");
    }

    @Test
    public void testAnchorIsFrontCenterMiddle() {
        // The controller sits at the front slice's center of the middle layer: x=1, y=1, z=0.
        assertArrayEquals(new int[] { 1, 1, 0 }, MiningArrayStructure.findAnchor(MiningArrayStructure.RAW_SHAPE));
    }

    @Test
    public void testFindAnchorRejectsBadShapes() {
        assertThrows(
            IllegalStateException.class,
            () -> MiningArrayStructure.findAnchor(new String[][] { { "AAA" } }),
            "a shape without an anchor");
        assertThrows(
            IllegalStateException.class,
            () -> MiningArrayStructure.findAnchor(new String[][] { { "~~~" } }),
            "a shape with multiple anchors");
    }
}
