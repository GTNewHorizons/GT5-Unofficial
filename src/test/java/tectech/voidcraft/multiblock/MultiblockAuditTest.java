package tectech.voidcraft.multiblock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import tectech.voidcraft.ship.VoidcraftComponent;

/** Pure-audit behavior of the assembler's multiblock component checks. */
public class MultiblockAuditTest {

    private static final int EXPECTED_CELLS = 18;

    private static MultiblockAudit.ComponentAudit mining(long controllers, long casings, int formed) {
        return new MultiblockAudit.ComponentAudit(
            VoidcraftComponent.MINING_ARRAY,
            Arrays.asList(VoidcraftComponent.MINING_ARRAY_CASING, VoidcraftComponent.MINING_ARRAY_PANEL),
            EXPECTED_CELLS,
            controllers,
            casings,
            formed);
    }

    @Test
    public void testEmptyVolumeHasNoErrors() {
        assertTrue(
            MultiblockAudit.audit(Collections.emptyList())
                .isEmpty());
    }

    @Test
    public void testStrayCasingsWithoutControllersAreTolerated() {
        assertTrue(
            MultiblockAudit.audit(Arrays.asList(mining(0, 5, 0)))
                .isEmpty());
    }

    @Test
    public void testUnformedControllerIsIncomplete() {
        assertEquals(
            Collections.singletonList(MultiblockAudit.ERROR_INCOMPLETE),
            MultiblockAudit.audit(Arrays.asList(mining(1, 17, 0))));
    }

    @Test
    public void testFormedFullStructurePasses() {
        assertTrue(
            MultiblockAudit.audit(Arrays.asList(mining(1, 17, 1)))
                .isEmpty());
    }

    @Test
    public void testFormedStructureShortOfCellsIsOutOfVolume() {
        // All controllers formed, but fewer cells in the volume than the full structure needs.
        assertEquals(
            Collections.singletonList(MultiblockAudit.ERROR_OUT_OF_VOLUME),
            MultiblockAudit.audit(Arrays.asList(mining(1, 10, 1))));
    }

    @Test
    public void testExtraCasingsNeverError() {
        assertTrue(
            MultiblockAudit.audit(Arrays.asList(mining(1, 40, 1)))
                .isEmpty());
    }

    @Test
    public void testTwoControllersOneFormedIsIncomplete() {
        assertEquals(
            Collections.singletonList(MultiblockAudit.ERROR_INCOMPLETE),
            MultiblockAudit.audit(Arrays.asList(mining(2, 30, 1))));
    }

    @Test
    public void testTwoFormedControllersShortOfCellsIsOutOfVolume() {
        // 2 × 18 = 36 cells required; 1 controller + 30 casings = 31 in the volume.
        assertEquals(
            Collections.singletonList(MultiblockAudit.ERROR_OUT_OF_VOLUME),
            MultiblockAudit.audit(Arrays.asList(mining(2, 30, 2))));
    }

    @Test
    public void testComponentsAreAuditedIndependently() {
        // A healthy component plus a broken one: only the broken one's error appears.
        List<String> errors = MultiblockAudit.audit(Arrays.asList(mining(1, 17, 1), mining(1, 0, 0)));
        assertEquals(Collections.singletonList(MultiblockAudit.ERROR_INCOMPLETE), errors);
    }

    @Test
    public void testErrorKeysDedupe() {
        List<String> errors = MultiblockAudit.audit(Arrays.asList(mining(1, 0, 0), mining(1, 0, 0)));
        assertEquals(1, errors.size(), "each key appears at most once");
        assertEquals(MultiblockAudit.ERROR_INCOMPLETE, errors.get(0));
    }
}
