package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Phase 4 pass 1 bay fluid pool ({@link VoidcraftFluidPool}) — the abstract
 * {@code {material, mB}} slots: merge-aware insert, remove, overflow semantics (the caller keeps the remainder),
 * and the NBT round-trip.
 *
 * <p>
 * Same design contract as {@link VoidcraftCargoPool} (see {@code VoidcraftCargoPoolTest}): the pool never
 * silently voids — {@code insert} returns the accepted amount and the bay delivers the rest.
 */
public class VoidcraftFluidPoolTest {

    private static final String FLUID_A = "RawStarMatter";
    private static final String FLUID_B = "WhiteDwarfMatter";

    @Test
    public void testEmptyPool() {
        VoidcraftFluidPool pool = new VoidcraftFluidPool();
        assertTrue(pool.isEmpty());
        assertEquals(0, pool.size());
        for (int slot = 0; slot < VoidcraftFluidPool.SLOTS; slot++) {
            assertNull(pool.getMaterial(slot));
            assertEquals(0, pool.getAmount(slot));
        }
        assertEquals(0, pool.insert(FLUID_A, 0), "zero amount inserts nothing");
        assertEquals(0, pool.insert(null, 1000), "null material inserts nothing");
        assertEquals(0, pool.insert("", 1000), "empty material inserts nothing");
        assertTrue(pool.isEmpty(), "still empty");
    }

    @Test
    public void testInsertIntoEmptySlot() {
        VoidcraftFluidPool pool = new VoidcraftFluidPool();
        long accepted = pool.insert(FLUID_A, 1234L);
        assertEquals(1234L, accepted, "whole amount accepted");
        assertEquals(1, pool.size());
        assertEquals(FLUID_A, pool.getMaterial(0));
        assertEquals(1234L, pool.getAmount(0));
    }

    @Test
    public void testSameMaterialMergesBeforeTakingANewSlot() {
        VoidcraftFluidPool pool = new VoidcraftFluidPool();
        pool.insert(FLUID_A, 100L);
        long accepted = pool.insert(FLUID_A, 200L);
        assertEquals(200L, accepted, "the whole second amount is accepted into the merged slot");
        assertEquals(1, pool.size(), "merged into the existing slot");
        assertEquals(300L, pool.getAmount(0), "the slot now holds both amounts");

        // a different material takes the NEXT slot (no cross-fluid merging)
        long acceptedB = pool.insert(FLUID_B, 50L);
        assertEquals(50L, acceptedB);
        assertEquals(2, pool.size());
        assertEquals(FLUID_B, pool.getMaterial(1));
        assertEquals(50L, pool.getAmount(1));
        assertEquals(300L, pool.getAmount(0), "the first slot is untouched by the merge of the other fluid");
    }

    @Test
    public void testPerSlotCapSplitsIntoExtraSlots() {
        VoidcraftFluidPool pool = new VoidcraftFluidPool();
        long cap = VoidcraftFluidPool.MAX_PER_SLOT;
        long amount = cap + 1000L;
        long accepted = pool.insert(FLUID_A, amount);
        assertEquals(amount, accepted, "everything fits across two slots");
        assertEquals(2, pool.size());
        assertEquals(cap, pool.getAmount(0));
        assertEquals(1000L, pool.getAmount(1));
    }

    @Test
    public void testOverflowReturnsTheAcceptedAmount() {
        VoidcraftFluidPool pool = new VoidcraftFluidPool();
        long capacity = (long) VoidcraftFluidPool.SLOTS * VoidcraftFluidPool.MAX_PER_SLOT;
        long accepted = pool.insert(FLUID_A, capacity + 1234L);
        assertEquals(capacity, accepted, "the pool never voids — the excess is reported");
        assertEquals(VoidcraftFluidPool.SLOTS, pool.size(), "all slots full");
    }

    @Test
    public void testRemoveDrainsAndClearsSlots() {
        VoidcraftFluidPool pool = new VoidcraftFluidPool();
        pool.insert(FLUID_A, 500L);
        assertEquals(200L, pool.remove(0, 200L));
        assertEquals(300L, pool.getAmount(0));
        assertEquals(1, pool.size());

        assertEquals(300L, pool.remove(0, 10_000L), "remove more than present → only what is there");
        assertNull(pool.getMaterial(0), "drained slot is cleared");
        assertTrue(pool.isEmpty());
        assertEquals(0, pool.remove(0, 10L), "removing from an empty slot is a no-op");
        assertEquals(0, pool.remove(-1, 10L), "negative slot is a no-op");
    }

    @Test
    public void testNbtRoundTrip() {
        VoidcraftFluidPool pool = new VoidcraftFluidPool();
        pool.insert(FLUID_A, 12_345L);
        pool.insert(FLUID_B, 678L);
        pool.insert(FLUID_A, 42L); // merges into slot 0 → 12_387

        NBTTagCompound nbt = new NBTTagCompound();
        pool.writeToNBT(nbt);
        VoidcraftFluidPool loaded = VoidcraftFluidPool.readFromNBT(nbt);

        assertEquals(2, loaded.size());
        assertEquals(FLUID_A, loaded.getMaterial(0));
        assertEquals(12_387L, loaded.getAmount(0));
        assertEquals(FLUID_B, loaded.getMaterial(1));
        assertEquals(678L, loaded.getAmount(1));

        // double pass (save → load → save → load) must be stable
        NBTTagCompound nbt2 = new NBTTagCompound();
        loaded.writeToNBT(nbt2);
        VoidcraftFluidPool twice = VoidcraftFluidPool.readFromNBT(nbt2);
        assertEquals(2, twice.size());
        assertEquals(12_387L, twice.getAmount(0));
        assertEquals(678L, twice.getAmount(1));
    }

    @Test
    public void testNbtRejectsCorruptInput() {
        assertTrue(
            VoidcraftFluidPool.readFromNBT(null)
                .isEmpty());
        assertTrue(
            VoidcraftFluidPool.readFromNBT(new NBTTagCompound())
                .isEmpty());
    }
}
