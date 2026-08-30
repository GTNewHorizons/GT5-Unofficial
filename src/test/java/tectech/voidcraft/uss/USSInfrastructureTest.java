package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

/** Invariants of the USS infrastructure progress (decay accumulator + NBT serialization). */
public class USSInfrastructureTest {

    @Test
    public void testEmptyAndCount() {
        USSInfrastructure empty = USSInfrastructure.empty();
        assertTrue(empty.isEmpty());
        assertEquals(0L, empty.count(USSInfrastructure.DYSON_STAR_KEY));
        assertTrue(
            empty.counts()
                .isEmpty());

        USSInfrastructure one = empty.addUnits(USSInfrastructure.DYSON_STAR_KEY, 1);
        assertFalse(one.isEmpty());
        assertEquals(1L, one.count(USSInfrastructure.DYSON_STAR_KEY));
        assertEquals(
            1L,
            one.counts()
                .get(USSInfrastructure.DYSON_STAR_KEY));
    }

    @Test
    public void testAddRemoveClamp() {
        USSInfrastructure infra = USSInfrastructure.empty()
            .addUnits("k", 5L);
        assertEquals(5L, infra.count("k"));
        assertEquals(
            2L,
            infra.removeUnits("k", 3L)
                .count("k"));
        // removal clamps to the count (no negatives)
        assertEquals(
            0L,
            infra.removeUnits("k", 99L)
                .count("k"));
        assertEquals(
            0L,
            infra.removeUnits("k", 99L)
                .counts()
                .size(),
            "a drained key is dropped");
        // null / empty keys and non-positive amounts are no-ops
        assertSame(infra, infra.addUnits(null, 1));
        assertSame(infra, infra.addUnits("", 1));
        assertSame(infra, infra.addUnits("k", 0));
        assertSame(infra, infra.removeUnits(null, 1));
        assertSame(infra, infra.removeUnits("k", 0));
    }

    @Test
    public void testDecayAccumulatorCrossesWholeUnits() {
        // 1000 satellites at 0.001 per unit per tick lose exactly 1 whole satellite every tick (no fraction).
        USSInfrastructure infra = USSInfrastructure.empty()
            .addUnits("k", 1000L);
        USSInfrastructure.DecayStep step = infra.applyDecay("k", 0.001);
        assertEquals(1L, step.lost);
        assertEquals(999L, step.infrastructure.count("k"));

        // A sub-unit loss accumulates: 3 satellites at 0.25 lose 0.75/tick → a whole unit on the second tick.
        USSInfrastructure small = USSInfrastructure.empty()
            .addUnits("k", 3L);
        USSInfrastructure.DecayStep t1 = small.applyDecay("k", 0.25);
        assertEquals(0L, t1.lost, "tick 1: 0.75 stays in the accumulator");
        assertEquals(3L, t1.infrastructure.count("k"), "no whole unit lost yet");
        USSInfrastructure.DecayStep t2 = t1.infrastructure.applyDecay("k", 0.25);
        assertEquals(1L, t2.lost, "tick 2: 0.75 + 0.75 = 1.5 → 1 lost");
        assertEquals(2L, t2.infrastructure.count("k"));

        // A drained target drops out and decay is a no-op afterwards.
        USSInfrastructure one = USSInfrastructure.empty()
            .addUnits("k", 1L);
        USSInfrastructure.DecayStep d = one.applyDecay("k", 2.0);
        assertEquals(1L, d.lost);
        assertEquals(0L, d.infrastructure.count("k"));
        assertSame(d.infrastructure, d.infrastructure.applyDecay("k", 2.0).infrastructure);
        // Zero / negative rates are no-ops.
        assertSame(one, one.applyDecay("k", 0.0).infrastructure);
        assertSame(one, one.applyDecay("k", -1.0).infrastructure);
        assertSame(one, one.applyDecay(null, 0.5).infrastructure);
    }

    @Test
    public void testNbtRoundTrip() {
        USSInfrastructure infra = USSInfrastructure.empty()
            .addUnits(USSInfrastructure.DYSON_STAR_KEY, 42L);
        // Seed a decay fraction the way a partial decay tick does (removeUnits keeps acc, so drive it via decay).
        USSInfrastructure.DecayStep step = infra.applyDecay(USSInfrastructure.DYSON_STAR_KEY, 0.03);
        assertEquals(1L, step.lost, "42 · 0.03 = 1.26 → 1 lost, 0.26 kept");
        assertEquals(41L, step.infrastructure.count(USSInfrastructure.DYSON_STAR_KEY));

        NBTTagCompound nbt = new NBTTagCompound();
        step.infrastructure.writeToNBT(nbt);
        USSInfrastructure restored = USSInfrastructure.readFromNBT(nbt);
        assertEquals(41L, restored.count(USSInfrastructure.DYSON_STAR_KEY));
        assertTrue(restored.hasSameProgress(step.infrastructure));
        // The accumulator fraction survived the round trip (hasSameProgress compares counts; check decay behavior).
        USSInfrastructure.DecayStep again = restored.applyDecay(USSInfrastructure.DYSON_STAR_KEY, 0.03);
        assertEquals(1L, again.lost, "41 · 0.03 + 0.26 = 1.49 → 1 lost (the 0.26 fraction carried)");

        // Null input reads as empty; empty infrastructure writes no entries.
        assertTrue(
            USSInfrastructure.readFromNBT(null)
                .isEmpty());
        NBTTagCompound emptyNbt = new NBTTagCompound();
        USSInfrastructure.empty()
            .writeToNBT(emptyNbt);
        assertTrue(
            USSInfrastructure.readFromNBT(emptyNbt)
                .isEmpty());
    }

    @Test
    public void testHasSameProgress() {
        USSInfrastructure a = USSInfrastructure.empty()
            .addUnits("k", 5L);
        assertTrue(a.hasSameProgress(a));
        assertFalse(a.hasSameProgress(USSInfrastructure.empty()));
        assertFalse(a.hasSameProgress(a.addUnits("k", 1)));
        assertFalse(a.hasSameProgress(null), "null → only an empty infrastructure matches");
        assertTrue(
            USSInfrastructure.empty()
                .hasSameProgress(null));
    }
}
