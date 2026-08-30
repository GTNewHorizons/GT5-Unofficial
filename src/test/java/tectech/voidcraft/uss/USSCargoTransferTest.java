package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

import gregtech.api.enums.Materials;

/**
 * Bare-JVM tests for the paced SEND / TAKE unit transfer ({@link USSCargoTransfer}): the rate (1 logistics power
 * = 1 cargo unit per second), the filter, the unit limit, the target-full stop, and the stop reasons (the error
 * contract the executor and the logs depend on).
 */
public class USSCargoTransferTest {

    @Test
    public void testPacingConstantPinsTheRate() {
        // 1 power = 1 cargo unit per second (20 machine ticks), the single tuning point
        assertEquals(1L, USSConstants.LOGISTICS_POWER_PER_CARGO_PER_SECOND);
        assertEquals(20L, USSConstants.transferTicksPerUnit(1L));
        assertEquals(5L, USSConstants.transferTicksPerUnit(4L));
        assertEquals(1L, USSConstants.transferTicksPerUnit(20L), "fast enough, the countdown floors at 1");
        assertEquals(20L, USSConstants.transferTicksPerUnit(0L), "no power reads the 1-power rate");
    }

    @Test
    public void testNormalizeFilter() {
        assertEquals("", USSCargoTransfer.normalizeFilter(null));
        assertEquals("", USSCargoTransfer.normalizeFilter(""));
        assertEquals("", USSCargoTransfer.normalizeFilter("   "));
        assertEquals("", USSCargoTransfer.normalizeFilter("*"));
        assertEquals("copper", USSCargoTransfer.normalizeFilter(" copper "), "trim only - the case is the user's");
    }

    @Test
    public void testMatchesIsCaseInsensitiveNameMatch() {
        assertTrue(USSCargoTransfer.matches("", Materials.Copper), "match-all passes everything");
        assertTrue(USSCargoTransfer.matches(null, Materials.Copper));
        assertTrue(USSCargoTransfer.matches("copper", Materials.Copper));
        assertTrue(USSCargoTransfer.matches("COPPER", Materials.Copper));
        assertFalse(USSCargoTransfer.matches("Iron", Materials.Copper));
        assertFalse(USSCargoTransfer.matches("Copper", Materials._NULL), "_NULL never matches a named filter");
    }

    @Test
    public void testFirstUnitLandsAfterTheCountdown() {
        // The leg is hold-stateless: every tick is handed the CURRENT holds (the game side applies the result),
        // so the test threads the updated holds through.
        CargoHold source = CargoHold.of(64)
            .addItems(Materials.Copper, 10L);
        CargoHold target = CargoHold.of(64);
        USSCargoTransfer leg = USSCargoTransfer.arm("*", -1L, 4);
        for (int i = 0; i < 3; i++) {
            USSCargoTransfer.Result r = leg.tick(source, target);
            assertTrue(r.running, "tick " + (i + 1) + " is still inside the countdown");
            assertNull(r.source, "no hold change inside the countdown");
            assertNull(r.target);
        }
        USSCargoTransfer.Result move = leg.tick(source, target);
        assertTrue(move.running);
        assertEquals(9L, move.source.itemsOf(Materials.Copper), "source lost one unit");
        assertEquals(1L, move.target.itemsOf(Materials.Copper), "target gained one unit");
        assertEquals(1L, leg.transferred());
        source = move.source;
        target = move.target;
        // the countdown restarts: the next unit needs 4 ticks again
        for (int i = 0; i < 3; i++) {
            assertTrue(leg.tick(source, target).running);
        }
        USSCargoTransfer.Result second = leg.tick(source, target);
        assertEquals(2L, second.target.itemsOf(Materials.Copper));
        assertEquals(2L, leg.transferred());
    }

    @Test
    public void testLimitStopsAtTheLastUnit() {
        CargoHold source = CargoHold.of(64)
            .addItems(Materials.Iron, 10L);
        CargoHold target = CargoHold.of(64);
        USSCargoTransfer leg = USSCargoTransfer.arm("*", 2L, 1);
        assertEquals(2L, leg.remaining());
        USSCargoTransfer.Result first = leg.tick(source, target);
        assertTrue(first.running, "one of two units moved");
        assertEquals(1L, first.target.itemsOf(Materials.Iron));
        assertEquals(1L, leg.remaining());
        source = first.source;
        target = first.target;
        USSCargoTransfer.Result second = leg.tick(source, target);
        assertFalse(second.running, "the limit is reached");
        assertEquals(USSCargoTransfer.REASON_LIMIT_REACHED, second.reason);
        assertEquals(2L, second.target.itemsOf(Materials.Iron), "the last unit still lands (the hold is returned)");
        assertEquals(8L, second.source.itemsOf(Materials.Iron));
        assertEquals(2L, leg.transferred());
        assertEquals(0L, leg.remaining());
        // a third tick: the limit is already 0 -> stopped again, no hold change
        USSCargoTransfer.Result third = leg.tick(source, target);
        assertFalse(third.running);
        assertEquals(USSCargoTransfer.REASON_LIMIT_REACHED, third.reason);
        assertNull(third.source);
    }

    @Test
    public void testAllLimitIsUnlimited() {
        USSCargoTransfer leg = USSCargoTransfer.arm("*", -1L, 1);
        assertEquals(-1L, leg.remaining());
        USSCargoTransfer legClamped = USSCargoTransfer.arm("*", -5L, 1);
        assertEquals(-1L, legClamped.remaining(), "limits below -1 clamp to ALL");
    }

    @Test
    public void testTargetFullStopsTheTransfer() {
        CargoHold source = CargoHold.of(64)
            .addItems(Materials.Copper, 10L);
        CargoHold target = CargoHold.of(1)
            .addItems(Materials.Iron, 1L); // one unit, at capacity
        USSCargoTransfer leg = USSCargoTransfer.arm("*", -1L, 1);
        USSCargoTransfer.Result r = leg.tick(source, target);
        assertFalse(r.running);
        assertEquals(USSCargoTransfer.REASON_TARGET_FULL, r.reason);
        assertNull(r.source, "nothing moved");
        assertEquals(10L, source.itemsOf(Materials.Copper));
    }

    @Test
    public void testFilterOnlyMovesMatchingItems() {
        CargoHold source = CargoHold.of(64)
            .addItems(Materials.Copper, 3L)
            .addItems(Materials.Iron, 5L);
        CargoHold target = CargoHold.of(64);
        USSCargoTransfer leg = USSCargoTransfer.arm("iron", -1L, 1);
        USSCargoTransfer.Result r = leg.tick(source, target);
        assertTrue(r.running);
        assertEquals(1L, r.target.itemsOf(Materials.Iron));
        assertEquals(0L, r.target.itemsOf(Materials.Copper), "the filter excludes copper");
        source = r.source;
        target = r.target;
        // drain the iron: the source still has copper, but it does not match
        for (int i = 0; i < 4; i++) {
            USSCargoTransfer.Result step = leg.tick(source, target);
            assertTrue(step.running, "iron " + (i + 2) + " of 5 still moves");
            source = step.source;
            target = step.target;
        }
        USSCargoTransfer.Result stopped = leg.tick(source, target);
        assertFalse(stopped.running);
        assertEquals(USSCargoTransfer.REASON_NO_CARGO, stopped.reason);
        assertEquals(0L, source.itemsOf(Materials.Iron));
        assertEquals(3L, source.itemsOf(Materials.Copper), "non-matching cargo stays on board");
        assertEquals(5L, leg.transferred());
    }

    /** One tick, threading the updated holds (the game-side contract: the caller applies every result). */
    private static USSCargoTransfer.Result step(USSCargoTransfer leg, CargoHold[] holds) {
        USSCargoTransfer.Result r = leg.tick(holds[0], holds[1]);
        if (r.source != null) {
            holds[0] = r.source;
            holds[1] = r.target;
        }
        return r;
    }

    @Test
    public void testItemsMoveBeforeFluids() {
        CargoHold[] holds = { CargoHold.of(64)
            .addItems(Materials.Copper, 1L)
            .addFluids(Materials.Water, 500L), CargoHold.of(64) };
        USSCargoTransfer leg = USSCargoTransfer.arm("*", -1L, 1);
        USSCargoTransfer.Result first = step(leg, holds);
        assertEquals(1L, first.target.itemsOf(Materials.Copper), "the item goes first");
        assertEquals(0L, first.target.fluidsOf(Materials.Water));
        // then the fluid, 100 mB per unit (fluidsOf reports mB)
        USSCargoTransfer.Result second = step(leg, holds);
        assertEquals(100L, second.target.fluidsOf(Materials.Water));
        assertEquals(0L, second.source.itemsOf(Materials.Copper));
        assertEquals(2L, leg.transferred());
        assertEquals(200L, step(leg, holds).target.fluidsOf(Materials.Water));
        assertEquals(300L, step(leg, holds).target.fluidsOf(Materials.Water));
        assertEquals(400L, step(leg, holds).target.fluidsOf(Materials.Water));
        assertEquals(500L, step(leg, holds).target.fluidsOf(Materials.Water));
        USSCargoTransfer.Result done = step(leg, holds);
        assertFalse(done.running, "the source carried exactly 6 units (1 item + 500 mB)");
        assertEquals(USSCargoTransfer.REASON_NO_CARGO, done.reason);
    }

    @Test
    public void testFluidUnitIsOneHundredMB() {
        assertEquals(100L, CargoHold.MB_PER_UNIT);
        // a sub-unit fluid remainder (50 mB) is not a unit: the transfer stops with nothing left
        CargoHold source = CargoHold.of(64)
            .addFluids(Materials.Water, 50L);
        CargoHold target = CargoHold.of(64);
        USSCargoTransfer leg = USSCargoTransfer.arm("*", -1L, 1);
        USSCargoTransfer.Result r = leg.tick(source, target);
        assertFalse(r.running);
        assertEquals(USSCargoTransfer.REASON_NO_CARGO, r.reason);
        assertEquals(50L, source.fluidsOf(Materials.Water), "the remainder stays on board");
    }

    @Test
    public void testMissingHoldsStopCleanly() {
        USSCargoTransfer leg = USSCargoTransfer.arm("*", -1L, 1);
        assertEquals(USSCargoTransfer.REASON_NO_HOLD, leg.tick(null, CargoHold.of(8)).reason);
        assertEquals(USSCargoTransfer.REASON_NO_HOLD, leg.tick(CargoHold.of(8), null).reason);
    }

    @Test
    public void testHoldsAreImmutable() {
        CargoHold source = CargoHold.of(64)
            .addItems(Materials.Copper, 5L);
        CargoHold target = CargoHold.of(64);
        USSCargoTransfer leg = USSCargoTransfer.arm("*", -1L, 1);
        USSCargoTransfer.Result r = leg.tick(source, target);
        assertEquals(5L, source.itemsOf(Materials.Copper), "the original source is untouched");
        assertEquals(0L, target.itemsOf(Materials.Copper), "the original target is untouched");
        assertEquals(4L, r.source.itemsOf(Materials.Copper), "the returned source has one less");
        assertEquals(1L, r.target.itemsOf(Materials.Copper), "the returned target has one more");
    }

    @Test
    public void testGetFilterReturnsTheNormalizedFilter() {
        assertEquals(
            "",
            USSCargoTransfer.arm(null, -1L, 1)
                .getFilter());
        assertEquals(
            "Tin",
            USSCargoTransfer.arm("Tin", -1L, 1)
                .getFilter(),
            "the case the user typed is kept");
    }

    @Test
    public void testNbtRoundTripRestoresProgressAndPace() {
        CargoHold[] holds = { CargoHold.of(64)
            .addItems(Materials.Iron, 10L), CargoHold.of(64) };
        USSCargoTransfer leg = USSCargoTransfer.arm("iron", 10L, 5);
        // 4 ticks inside the countdown, the first unit (tick 5, the countdown restarts), then 3 more inside: one
        // unit moved, the countdown sits at 2.
        for (int i = 0; i < 4; i++) {
            assertTrue(step(leg, holds).running);
        }
        assertTrue(step(leg, holds).running, "tick 5: the first unit lands");
        for (int i = 0; i < 3; i++) {
            assertTrue(step(leg, holds).running);
        }
        assertEquals(1L, leg.transferred());
        assertEquals(1L, holds[1].itemsOf(Materials.Iron));

        NBTTagCompound tag = new NBTTagCompound();
        leg.writeToNBT(tag);
        USSCargoTransfer restored = USSCargoTransfer.readFromNBT(tag);
        assertNotNull(restored);
        assertEquals("iron", restored.getFilter());
        assertEquals(1L, restored.transferred(), "the progress survives the round trip");
        assertEquals(9L, restored.remaining(), "the limit counts the moved unit");
        // The pacing phase is restored exactly: the second unit lands two ticks later (a countdown reset to
        // ticksPerUnit would land it five ticks later).
        assertTrue(step(restored, holds).running, "tick 1: inside the restored countdown");
        USSCargoTransfer.Result move = step(restored, holds);
        assertTrue(move.running, "tick 2: the restored countdown expired, the unit moves");
        assertEquals(2L, holds[1].itemsOf(Materials.Iron));
        assertEquals(2L, restored.transferred());
    }

    @Test
    public void testNbtReadFailsSafeOnCorruptRecords() {
        assertNull(USSCargoTransfer.readFromNBT(null));
        assertNull(USSCargoTransfer.readFromNBT(new NBTTagCompound()), "no core tags: the record is dropped");
        NBTTagCompound partial = new NBTTagCompound();
        partial.setString("vc_tr_filter", "iron");
        partial.setLong("vc_tr_limit", 3L);
        partial.setInteger("vc_tr_ticks_per_unit", 4);
        USSCargoTransfer restored = USSCargoTransfer.readFromNBT(partial);
        assertNotNull(restored, "a partial record degrades to a fresh pace, not a drop");
        assertEquals("iron", restored.getFilter());
        assertEquals(3L, restored.remaining());
        assertEquals(0L, restored.transferred());
        CargoHold[] holds = { CargoHold.of(8)
            .addItems(Materials.Iron, 1L), CargoHold.of(8) };
        for (int i = 0; i < 3; i++) {
            assertTrue(step(restored, holds).running, "tick " + (i + 1) + ": inside the fresh countdown");
        }
        USSCargoTransfer.Result move = step(restored, holds);
        assertTrue(move.running, "tick 4: the fresh countdown expired");
        assertEquals(1L, holds[1].itemsOf(Materials.Iron));
    }
}
