package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.junit.jupiter.api.Test;

import gregtech.api.enums.Materials;

/**
 * Unit tests for the USS data model ({@link VoidcraftUSS}) — state transitions, the star tables via
 * {@link USSConstants}, and the NBT round-trip.
 */
public class VoidcraftUSSTest {

    // region cold/ignited state machine

    @Test
    public void testColdInitialState() {
        VoidcraftUSS uss = VoidcraftUSS.cold();
        assertEquals(USSState.COLD, uss.getState());
        assertFalse(uss.isIgnited());
        assertEquals(-1, uss.getTier());
        assertNull(uss.getStarType());
        assertEquals(0L, uss.getLifespanRemaining());
        assertEquals(0L, uss.getIgnitedAt());
        assertTrue(
            uss.getShips()
                .isEmpty());
    }

    @Test
    public void testIgniteUsesStarTables() {
        // The star type comes from the IGNITION ITEM (the parameter), not from the tier.
        VoidcraftUSS uss = VoidcraftUSS.ignite(0, USSStarType.YELLOW_DWARF, 12_345L);
        assertEquals(USSState.IGNITED, uss.getState());
        assertTrue(uss.isIgnited());
        assertEquals(0, uss.getTier());
        assertEquals(USSStarType.YELLOW_DWARF, uss.getStarType());
        assertEquals(USSConstants.lifespanForType(USSStarType.YELLOW_DWARF), uss.getLifespanRemaining());
        assertEquals(12_345L, uss.getIgnitedAt());

        // a higher tier does not change the star class — the item does
        VoidcraftUSS mid = VoidcraftUSS.ignite(4, USSStarType.WHITE_DWARF, 0L);
        assertEquals(USSStarType.WHITE_DWARF, mid.getStarType());
        assertEquals(4, mid.getTier());

        // ...and the same tier can host any star class
        VoidcraftUSS cross = VoidcraftUSS.ignite(0, USSStarType.BLUE_GIANT, 0L);
        assertEquals(USSStarType.BLUE_GIANT, cross.getStarType());
        assertEquals(USSConstants.lifespanForType(USSStarType.BLUE_GIANT), cross.getLifespanRemaining());
    }

    @Test
    public void testIgniteClampsTier() {
        assertEquals(
            0,
            VoidcraftUSS.ignite(-7, USSStarType.YELLOW_DWARF, 0L)
                .getTier());
        assertEquals(
            USSConstants.MAX_TIER,
            VoidcraftUSS.ignite(99, USSStarType.BLUE_GIANT, 0L)
                .getTier());
        assertEquals(
            USSStarType.BLUE_GIANT,
            VoidcraftUSS.ignite(99, USSStarType.BLUE_GIANT, 0L)
                .getStarType());
        // a null type (defensive) falls back to yellow dwarf
        assertEquals(
            USSStarType.YELLOW_DWARF,
            VoidcraftUSS.ignite(3, null, 0L)
                .getStarType());
    }

    @Test
    public void testLifespanDecrementAndBurnout() {
        VoidcraftUSS uss = VoidcraftUSS.ignite(2, USSStarType.YELLOW_DWARF, 0L);
        long full = uss.getLifespanRemaining();
        assertTrue(full > 0);

        VoidcraftUSS after = uss.withLifespan(full - 1);
        assertEquals(full - 1, after.getLifespanRemaining());
        // identity fields preserved
        assertEquals(uss.getTier(), after.getTier());
        assertEquals(uss.getStarType(), after.getStarType());
        assertEquals(uss.getState(), after.getState());
        // original immutable
        assertEquals(full, uss.getLifespanRemaining());

        VoidcraftUSS cold = after.toCold();
        assertEquals(USSState.COLD, cold.getState());
        assertEquals(0L, cold.getLifespanRemaining());
    }

    @Test
    public void testShipRegistryPlaceholder() {
        VoidcraftUSS uss = VoidcraftUSS.cold()
            .withShip("ship-uuid-1")
            .withShip("ship-uuid-2");
        assertEquals(
            2,
            uss.getShips()
                .size());
        assertEquals(
            "ship-uuid-1",
            uss.getShips()
                .get(0));
        // duplicates ignored
        assertEquals(
            2,
            uss.withShip("ship-uuid-1")
                .getShips()
                .size());
        // nulls ignored
        assertEquals(
            2,
            uss.withShip(null)
                .getShips()
                .size());
    }

    // endregion

    // region ripple scan state (scan work)

    @Test
    public void testRippleScanStateStartsEmpty() {
        // All ripples are hidden at USS creation — nothing is scanned yet.
        VoidcraftUSS uss = VoidcraftUSS.ignite(0, USSStarType.YELLOW_DWARF, 0L);
        assertTrue(
            uss.getScannedRipples()
                .isEmpty(),
            "a fresh system has no scanned ripples");
        assertFalse(uss.isRippleScanned(0));
        assertFalse(uss.isRippleScanned(100));
        assertFalse(uss.isRippleScanned(342));
    }

    @Test
    public void testRippleScanStateAccumulates() {
        VoidcraftUSS uss = VoidcraftUSS.ignite(0, USSStarType.YELLOW_DWARF, 0L)
            .withRippleScanned(5)
            .withRippleScanned(342)
            .withRippleScanned(120);
        assertTrue(uss.isRippleScanned(5));
        assertTrue(uss.isRippleScanned(342));
        assertTrue(uss.isRippleScanned(120));
        assertFalse(uss.isRippleScanned(0), "an unmarked point is still hidden");
        assertEquals(
            3,
            uss.getScannedRipples()
                .size(),
            "exactly the three marked points");
        // duplicates are a no-op
        assertEquals(
            3,
            uss.withRippleScanned(5)
                .getScannedRipples()
                .size());
        // negative index is a no-op
        assertEquals(
            3,
            uss.withRippleScanned(-1)
                .getScannedRipples()
                .size());
    }

    @Test
    public void testRippleScanStateIsImmutable() {
        VoidcraftUSS base = VoidcraftUSS.ignite(0, USSStarType.YELLOW_DWARF, 0L)
            .withRippleScanned(10);
        VoidcraftUSS extended = base.withRippleScanned(20);
        assertTrue(base.isRippleScanned(10), "the base keeps its own scanned set");
        assertFalse(base.isRippleScanned(20), "the base is NOT affected by the extension");
        assertTrue(extended.isRippleScanned(10) && extended.isRippleScanned(20), "the extension has both");
    }

    @Test
    public void testNbtRoundTripRippleScanState() {
        VoidcraftUSS original = VoidcraftUSS.ignite(3, USSStarType.BLUE_GIANT, 42L)
            .withRippleScanned(7)
            .withRippleScanned(100)
            .withRippleScanned(342);
        NBTTagCompound nbt = new NBTTagCompound();
        original.writeToNBT(nbt);
        VoidcraftUSS loaded = VoidcraftUSS.readFromNBT(nbt);
        assertNotNull(loaded);
        assertTrue(loaded.isRippleScanned(7));
        assertTrue(loaded.isRippleScanned(100));
        assertTrue(loaded.isRippleScanned(342));
        assertEquals(
            3,
            loaded.getScannedRipples()
                .size());
        assertFalse(loaded.isRippleScanned(0), "unmarked points stay hidden");
    }

    @Test
    public void testNbtRoundTripEmptyRippleScanState() {
        // A system with no scanned ripples round-trips with an empty set (the tag is simply absent).
        VoidcraftUSS original = VoidcraftUSS.ignite(3, USSStarType.BLUE_GIANT, 42L);
        NBTTagCompound nbt = new NBTTagCompound();
        original.writeToNBT(nbt);
        assertFalse(nbt.hasKey(VoidcraftUSS.TAG_RIPPLE_SCANNED), "no tag when nothing is scanned");
        VoidcraftUSS loaded = VoidcraftUSS.readFromNBT(nbt);
        assertNotNull(loaded);
        assertTrue(
            loaded.getScannedRipples()
                .isEmpty());
    }

    // endregion

    // region star tables

    @Test
    public void testStarTypeTables() {
        // The tables are keyed by star TYPE (item-driven), and every type has positive balance.
        for (USSStarType starType : USSStarType.values()) {
            assertTrue(USSConstants.lifespanForType(starType) > 0, "lifespan must be positive for " + starType);
            assertTrue(USSConstants.starDrawEUt(starType) > 0, "EU draw must be positive for " + starType);
            assertNotNull(starType.getLangKey(), "lang key for " + starType);
        }
        // the three types are distinct in lifespan (the table actually differentiates them)
        long ms = USSConstants.lifespanForType(USSStarType.YELLOW_DWARF);
        long wd = USSConstants.lifespanForType(USSStarType.WHITE_DWARF);
        long sm = USSConstants.lifespanForType(USSStarType.BLUE_GIANT);
        assertTrue(ms != wd && wd != sm && ms != sm, "lifespan table must differentiate the star types");
        // null is defensive, not a crash
        assertTrue(USSConstants.lifespanForType(null) > 0);
        assertTrue(USSConstants.starDrawEUt(null) > 0);
    }

    @Test
    public void testClampTier() {
        assertEquals(0, USSConstants.clampTier(-100));
        assertEquals(8, USSConstants.clampTier(100));
        assertEquals(5, USSConstants.clampTier(5));
    }

    // endregion

    // region USS variable space (the 256-slot global variable list)

    @Test
    public void testVariableSpaceStartsFresh() {
        VoidcraftUSS uss = VoidcraftUSS.ignite(0, USSStarType.YELLOW_DWARF, 0L);
        assertNotNull(uss.getVariables(), "every USS carries a variable space");
        assertEquals(
            "",
            uss.getVariables()
                .get(0),
            "unwritten slots read as the empty string");
        assertEquals(
            0,
            uss.getVariables()
                .writtenCount());
        assertFalse(
            uss.getVariables()
                .isWritten(0));
    }

    @Test
    public void testWithVariablesIsImmutableAndPreservedByWithChain() {
        VoidcraftUSS uss = VoidcraftUSS.ignite(2, USSStarType.WHITE_DWARF, 7L);
        USSVariableSpace written = USSVariableSpace.fresh()
            .set(0, "hello")
            .set(255, "edge");
        VoidcraftUSS v = uss.withVariables(written);
        assertNotSame(uss, v, "withVariables is copy-on-write");
        assertEquals(
            "hello",
            v.getVariables()
                .get(0));
        assertEquals(
            "edge",
            v.getVariables()
                .get(255),
            "slot 255 is in range");
        assertEquals(
            2,
            v.getVariables()
                .writtenCount());
        assertEquals(
            0,
            uss.getVariables()
                .writtenCount(),
            "the original is unaffected");
        VoidcraftUSS more = v.withLifespan(11L)
            .withShip("s1");
        assertEquals(
            "hello",
            more.getVariables()
                .get(0),
            "the with* chain preserves the variable space");
    }

    @Test
    public void testVariableSpaceNbtRoundTripIsSparse() {
        VoidcraftUSS uss = VoidcraftUSS.ignite(1, USSStarType.BLUE_GIANT, 5L)
            .withVariables(
                USSVariableSpace.fresh()
                    .set(10, "a")
                    .set(200, "b"));
        NBTTagCompound nbt = new NBTTagCompound();
        uss.writeToNBT(nbt);
        NBTTagList list = nbt.getTagList(VoidcraftUSS.TAG_VARIABLES, 10); // a list of compounds is element type 10
        assertEquals(2, list.tagCount(), "SPARSE — only written slots are serialized (not 256)");
        VoidcraftUSS loaded = VoidcraftUSS.readFromNBT(nbt);
        assertNotNull(loaded);
        assertEquals(
            "a",
            loaded.getVariables()
                .get(10));
        assertEquals(
            "b",
            loaded.getVariables()
                .get(200));
        assertEquals(
            "",
            loaded.getVariables()
                .get(11),
            "an unwritten slot stays empty");
        assertEquals(
            2,
            loaded.getVariables()
                .writtenCount());
    }

    @Test
    public void testVariableSpaceFreshOnColdAndIgnite() {
        VoidcraftUSS v = VoidcraftUSS.ignite(0, USSStarType.YELLOW_DWARF, 0L)
            .withVariables(
                USSVariableSpace.fresh()
                    .set(3, "x"));
        assertEquals(
            "x",
            v.getVariables()
                .get(3));
        assertEquals(
            0,
            v.toCold()
                .getVariables()
                .writtenCount(),
            "a cold system starts with a fresh space");
        assertEquals(
            0,
            VoidcraftUSS.ignite(4, USSStarType.WHITE_DWARF, 0L)
                .getVariables()
                .writtenCount(),
            "a fresh ignition starts with a fresh space");
    }

    @Test
    public void testFreshSpaceWritesNoVariablesTag() {
        // A USS whose space is untouched keeps its tag lean (no empty list in the world file).
        NBTTagCompound nbt = new NBTTagCompound();
        VoidcraftUSS.ignite(0, USSStarType.YELLOW_DWARF, 0L)
            .writeToNBT(nbt);
        assertFalse(nbt.hasKey(VoidcraftUSS.TAG_VARIABLES), "a fresh space writes nothing");
    }

    // endregion

    // region NBT round-trip

    @Test
    public void testNbtRoundTripCold() {
        NBTTagCompound nbt = new NBTTagCompound();
        VoidcraftUSS.cold()
            .writeToNBT(nbt);

        VoidcraftUSS loaded = VoidcraftUSS.readFromNBT(nbt);
        assertNotNull(loaded);
        assertEquals(USSState.COLD, loaded.getState());
    }

    @Test
    public void testNbtRoundTripIgnited() {
        VoidcraftUSS original = VoidcraftUSS.ignite(5, USSStarType.WHITE_DWARF, 987_654_321L)
            .withLifespan(12_345L)
            .withShip("ship-a")
            .withShip("ship-b");

        NBTTagCompound nbt = new NBTTagCompound();
        original.writeToNBT(nbt);

        VoidcraftUSS loaded = VoidcraftUSS.readFromNBT(nbt);
        assertNotNull(loaded);
        assertEquals(USSState.IGNITED, loaded.getState());
        assertEquals(5, loaded.getTier());
        assertEquals(USSStarType.WHITE_DWARF, loaded.getStarType());
        assertEquals(12_345L, loaded.getLifespanRemaining());
        assertEquals(987_654_321L, loaded.getIgnitedAt());
        assertEquals(
            2,
            loaded.getShips()
                .size());
        assertEquals(
            "ship-a",
            loaded.getShips()
                .get(0));
        assertEquals(
            "ship-b",
            loaded.getShips()
                .get(1));
    }

    @Test
    public void testNbtRoundTripDoublePass() {
        // write → read → write → read must be stable (what actually happens across save/reload cycles)
        VoidcraftUSS original = VoidcraftUSS.ignite(7, USSStarType.BLUE_GIANT, 42L)
            .withLifespan(600L);
        NBTTagCompound first = new NBTTagCompound();
        original.writeToNBT(first);
        VoidcraftUSS once = VoidcraftUSS.readFromNBT(first);
        assertNotNull(once);
        NBTTagCompound second = new NBTTagCompound();
        once.writeToNBT(second);
        VoidcraftUSS twice = VoidcraftUSS.readFromNBT(second);
        assertNotNull(twice);
        assertEquals(original.getTier(), twice.getTier());
        assertEquals(original.getStarType(), twice.getStarType());
        assertEquals(original.getLifespanRemaining(), twice.getLifespanRemaining());
        assertEquals(original.getIgnitedAt(), twice.getIgnitedAt());
    }

    @Test
    public void testNbtRejectsCorruptInput() {
        // null / empty / missing format version
        assertNull(VoidcraftUSS.readFromNBT(null));
        assertNull(VoidcraftUSS.readFromNBT(new NBTTagCompound()));

        // wrong format version
        NBTTagCompound bad = new NBTTagCompound();
        bad.setInteger(VoidcraftUSS.TAG_FORMAT, 999);
        assertNull(VoidcraftUSS.readFromNBT(bad));

        // invalid state ordinal
        NBTTagCompound badState = new NBTTagCompound();
        badState.setInteger(VoidcraftUSS.TAG_FORMAT, VoidcraftUSS.NBT_FORMAT_VERSION);
        badState.setInteger(VoidcraftUSS.TAG_STATE, 42);
        assertNull(VoidcraftUSS.readFromNBT(badState));

        // ignited but tier out of range
        NBTTagCompound badTier = new NBTTagCompound();
        badTier.setInteger(VoidcraftUSS.TAG_FORMAT, VoidcraftUSS.NBT_FORMAT_VERSION);
        badTier.setInteger(VoidcraftUSS.TAG_STATE, USSState.IGNITED.ordinal());
        badTier.setInteger(VoidcraftUSS.TAG_TIER, 13);
        badTier.setInteger(VoidcraftUSS.TAG_STAR_TYPE, USSStarType.BLUE_GIANT.ordinal());
        badTier.setLong(VoidcraftUSS.TAG_LIFESPAN, 1000L);
        assertNull(VoidcraftUSS.readFromNBT(badTier));

        // ignited but star type missing
        NBTTagCompound badStar = new NBTTagCompound();
        badStar.setInteger(VoidcraftUSS.TAG_FORMAT, VoidcraftUSS.NBT_FORMAT_VERSION);
        badStar.setInteger(VoidcraftUSS.TAG_STATE, USSState.IGNITED.ordinal());
        badStar.setInteger(VoidcraftUSS.TAG_TIER, 3);
        badStar.setLong(VoidcraftUSS.TAG_LIFESPAN, 1000L);
        assertNull(VoidcraftUSS.readFromNBT(badStar));

        // ignited but lifespan exhausted
        NBTTagCompound badLifespan = new NBTTagCompound();
        badLifespan.setInteger(VoidcraftUSS.TAG_FORMAT, VoidcraftUSS.NBT_FORMAT_VERSION);
        badLifespan.setInteger(VoidcraftUSS.TAG_STATE, USSState.IGNITED.ordinal());
        badLifespan.setInteger(VoidcraftUSS.TAG_TIER, 3);
        badLifespan.setInteger(VoidcraftUSS.TAG_STAR_TYPE, USSStarType.WHITE_DWARF.ordinal());
        badLifespan.setLong(VoidcraftUSS.TAG_LIFESPAN, 0L);
        assertNull(VoidcraftUSS.readFromNBT(badLifespan));
    }

    // endregion

    // region star fluid reserve (starlifter depletion)

    @Test
    public void testStarFluidReserveStartsNull() {
        // A fresh system has not been siphoned — the reserve is null and the tag is absent (sparse write).
        VoidcraftUSS uss = VoidcraftUSS.ignite(3, USSStarType.YELLOW_DWARF, 42L);
        assertNull(uss.getStarFluidReserve(), "not yet siphoned → null reserve");
        NBTTagCompound nbt = new NBTTagCompound();
        uss.writeToNBT(nbt);
        assertFalse(nbt.hasKey(VoidcraftUSS.TAG_STAR_FLUID_RESERVE), "no tag when not siphoned");
    }

    @Test
    public void testStarFluidReserveNbtRoundTrip() {
        java.util.Map<Materials, Long> map = new java.util.LinkedHashMap<>();
        map.put(Materials.Hydrogen, 25_000_000L);
        map.put(Materials.Helium, 10_000_000L);
        VoidcraftUSS original = VoidcraftUSS.ignite(3, USSStarType.YELLOW_DWARF, 42L)
            .withStarFluidReserve(new VoidcraftUSS.MaterialReserve(map));

        NBTTagCompound nbt = new NBTTagCompound();
        original.writeToNBT(nbt);
        VoidcraftUSS loaded = VoidcraftUSS.readFromNBT(nbt);
        assertNotNull(loaded);
        assertNotNull(loaded.getStarFluidReserve(), "the reserve survives the round trip");
        assertEquals(
            25_000_000L,
            loaded.getStarFluidReserve()
                .remaining(Materials.Hydrogen));
        assertEquals(
            10_000_000L,
            loaded.getStarFluidReserve()
                .remaining(Materials.Helium));
        assertEquals(
            0L,
            loaded.getStarFluidReserve()
                .remaining(Materials.Oxygen),
            "absent material → 0");
    }

    @Test
    public void testStarFluidReserveIsPreservedByWithChain() {
        java.util.Map<Materials, Long> map = new java.util.LinkedHashMap<>();
        map.put(Materials.Hydrogen, 42L);
        VoidcraftUSS base = VoidcraftUSS.ignite(3, USSStarType.YELLOW_DWARF, 42L)
            .withStarFluidReserve(new VoidcraftUSS.MaterialReserve(map));
        VoidcraftUSS chained = base.withLifespan(77L)
            .withShip("ship-a")
            .withRippleScanned(5);
        assertEquals(
            42L,
            chained.getStarFluidReserve()
                .remaining(Materials.Hydrogen),
            "the with* chain preserves the reserve");
        // A cleared reserve stays cleared across a NBT round trip.
        VoidcraftUSS cleared = chained.withStarFluidReserve(null);
        assertNull(cleared.getStarFluidReserve(), "withStarFluidReserve(null) clears it");
        NBTTagCompound nbt = new NBTTagCompound();
        cleared.writeToNBT(nbt);
        assertFalse(nbt.hasKey(VoidcraftUSS.TAG_STAR_FLUID_RESERVE), "cleared → sparse (no tag)");
        assertNull(
            VoidcraftUSS.readFromNBT(nbt)
                .getStarFluidReserve());
    }

    @Test
    public void testStarFluidReserveDiscardedOnToCold() {
        java.util.Map<Materials, Long> map = new java.util.LinkedHashMap<>();
        map.put(Materials.Hydrogen, 42L);
        VoidcraftUSS siphoned = VoidcraftUSS.ignite(3, USSStarType.YELLOW_DWARF, 42L)
            .withStarFluidReserve(new VoidcraftUSS.MaterialReserve(map));
        assertNotNull(siphoned.getStarFluidReserve());
        VoidcraftUSS cold = siphoned.toCold();
        assertNull(cold.getStarFluidReserve(), "a re-ignition is a new system with fresh reserves");
    }

    // endregion
}
