package gregtech.api.material;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/// Pins [PipeStats]' size transforms against representative pipe-family table rows, truncating-division
/// rows included.
public class PipeStatsTest {

    private static long[] wireAmperages(int base) {
        long[] amps = new long[6];
        for (int size = 0; size < 6; size++) {
            amps[size] = PipeStats.wireAmperage(base, size);
        }
        return amps;
    }

    private static int[] fluidCapacities(int base) {
        int[] capacities = new int[5];
        for (int size = 0; size < 5; size++) {
            capacities[size] = PipeStats.fluidPipeCapacity(base, size);
        }
        return capacities;
    }

    private static int[] itemSlots(int hugeSlots) {
        int[] slots = new int[5];
        for (int size = 0; size < 5; size++) {
            slots[size] = PipeStats.itemPipeSlots(hugeSlots, size);
        }
        return slots;
    }

    private static int[] itemStepSizes(int hugeSlots, boolean restrictive) {
        int[] steps = new int[5];
        for (int size = 0; size < 5; size++) {
            steps[size] = PipeStats.itemPipeStepSize(hugeSlots, size, restrictive);
        }
        return steps;
    }

    private static int[] itemTickTimes(int hugeSlots) {
        int[] ticks = new int[5];
        for (int size = 0; size < 5; size++) {
            ticks[size] = PipeStats.itemPipeTickTime(hugeSlots, size);
        }
        return ticks;
    }

    @Test
    void wireAmperageMultipliesPerSize() {
        assertArrayEquals(new long[] { 4, 8, 16, 32, 48, 64 }, wireAmperages(4));
        assertArrayEquals(
            new long[] { 1_000_000, 2_000_000, 4_000_000, 8_000_000, 12_000_000, 16_000_000 },
            wireAmperages(1_000_000));
    }

    @Test
    void wireLossUsesOverrideElseTwiceCableLoss() {
        assertEquals(2, PipeStats.wireLoss(null, 1));
        assertEquals(8, PipeStats.wireLoss(null, 4));
        assertEquals(1, PipeStats.wireLoss(1, 0));
        assertEquals(0, PipeStats.wireLoss(0, 0));
    }

    @Test
    void fluidPipeCapacityTruncatesSmallBases() {
        assertArrayEquals(new int[] { 3, 6, 20, 40, 80 }, fluidCapacities(20));
        assertArrayEquals(new int[] { 20, 40, 120, 240, 480 }, fluidCapacities(120));
        assertArrayEquals(new int[] { 100, 200, 600, 1200, 2400 }, fluidCapacities(600));
    }

    @Test
    void multiChannelFluidPipesDeriveFromTheBase() {
        assertEquals(20, PipeStats.quadrupleFluidPipeCapacity(20));
        assertEquals(6, PipeStats.nonupleFluidPipeCapacity(20));
        assertEquals(200, PipeStats.nonupleFluidPipeCapacity(600));
    }

    @Test
    void itemPipeSlotsFloorAtOne() {
        assertArrayEquals(new int[] { 1, 1, 1, 1, 2 }, itemSlots(2));
        assertArrayEquals(new int[] { 1, 2, 4, 8, 16 }, itemSlots(16));
        assertArrayEquals(new int[] { 32, 64, 128, 256, 512 }, itemSlots(512));
    }

    @Test
    void itemPipeStepSizeDividesPerSize() {
        assertArrayEquals(new int[] { 262144, 131072, 65536, 32768, 16384 }, itemStepSizes(2, false));
        assertArrayEquals(new int[] { 1024, 512, 256, 128, 64 }, itemStepSizes(512, false));
    }

    @Test
    void restrictiveItemPipeStepSizeIsHundredfold() {
        assertArrayEquals(new int[] { 26214400, 13107200, 6553600, 3276800, 1638400 }, itemStepSizes(2, true));
        assertArrayEquals(new int[] { 102400, 51200, 25600, 12800, 6400 }, itemStepSizes(512, true));
    }

    @Test
    void itemPipeTickTimeFloorsAtOneTransferPerSecond() {
        assertArrayEquals(new int[] { 160, 80, 40, 20, 20 }, itemTickTimes(2));
        assertArrayEquals(new int[] { 80, 40, 20, 20, 20 }, itemTickTimes(4));
        assertArrayEquals(new int[] { 20, 20, 20, 20, 20 }, itemTickTimes(512));
    }
}
