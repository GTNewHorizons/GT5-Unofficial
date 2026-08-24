package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Phase 2 USS data model ({@link VoidcraftUSS}) — state transitions, the star tables via
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
        // Phase 4 pass 1: the star type comes from the IGNITION ITEM (the parameter), not from the tier.
        VoidcraftUSS uss = VoidcraftUSS.ignite(0, USSStarType.MAIN_SEQUENCE, 12_345L);
        assertEquals(USSState.IGNITED, uss.getState());
        assertTrue(uss.isIgnited());
        assertEquals(0, uss.getTier());
        assertEquals(USSStarType.MAIN_SEQUENCE, uss.getStarType());
        assertEquals(USSConstants.lifespanForType(USSStarType.MAIN_SEQUENCE), uss.getLifespanRemaining());
        assertEquals(12_345L, uss.getIgnitedAt());

        // a higher tier no longer changes the star class — the item does
        VoidcraftUSS mid = VoidcraftUSS.ignite(4, USSStarType.WHITE_DWARF, 0L);
        assertEquals(USSStarType.WHITE_DWARF, mid.getStarType());
        assertEquals(4, mid.getTier());

        // ...and the same tier can now host any star class
        VoidcraftUSS cross = VoidcraftUSS.ignite(0, USSStarType.SUPERMASSIVE, 0L);
        assertEquals(USSStarType.SUPERMASSIVE, cross.getStarType());
        assertEquals(USSConstants.lifespanForType(USSStarType.SUPERMASSIVE), cross.getLifespanRemaining());
    }

    @Test
    public void testIgniteClampsTier() {
        assertEquals(
            0,
            VoidcraftUSS.ignite(-7, USSStarType.MAIN_SEQUENCE, 0L)
                .getTier());
        assertEquals(
            USSConstants.MAX_TIER,
            VoidcraftUSS.ignite(99, USSStarType.SUPERMASSIVE, 0L)
                .getTier());
        assertEquals(
            USSStarType.SUPERMASSIVE,
            VoidcraftUSS.ignite(99, USSStarType.SUPERMASSIVE, 0L)
                .getStarType());
        // a null type (defensive) falls back to main sequence
        assertEquals(
            USSStarType.MAIN_SEQUENCE,
            VoidcraftUSS.ignite(3, null, 0L)
                .getStarType());
    }

    @Test
    public void testLifespanDecrementAndBurnout() {
        VoidcraftUSS uss = VoidcraftUSS.ignite(2, USSStarType.MAIN_SEQUENCE, 0L);
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

    // region star tables

    @Test
    public void testStarTypeTables() {
        // Phase 4 pass 1: the tables are keyed by star TYPE (item-driven), and every type has positive balance.
        for (USSStarType starType : USSStarType.values()) {
            assertTrue(USSConstants.lifespanForType(starType) > 0, "lifespan must be positive for " + starType);
            assertTrue(USSConstants.starDrawEUt(starType) > 0, "EU draw must be positive for " + starType);
            assertNotNull(starType.getLangKey(), "lang key for " + starType);
        }
        // the three types are distinct in lifespan (the table actually differentiates them)
        long ms = USSConstants.lifespanForType(USSStarType.MAIN_SEQUENCE);
        long wd = USSConstants.lifespanForType(USSStarType.WHITE_DWARF);
        long sm = USSConstants.lifespanForType(USSStarType.SUPERMASSIVE);
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
        VoidcraftUSS original = VoidcraftUSS.ignite(7, USSStarType.SUPERMASSIVE, 42L)
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
        badTier.setInteger(VoidcraftUSS.TAG_STAR_TYPE, USSStarType.SUPERMASSIVE.ordinal());
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
}
