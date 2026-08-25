package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

/**
 * Bare-JVM tests for the ship target model ({@link USSTarget}): the three kinds (star / planet / ship), the payload
 * accessors, the NBT round-trip, and the defensive fallbacks (null / unknown kind → the star).
 */
public class USSTargetTest {

    @Test
    public void testStarTarget() {
        USSTarget t = USSTarget.star();
        assertEquals(USSTarget.Kind.STAR, t.kind());
        assertEquals(0, t.payloadA(), "star target has no payload");
        assertEquals(0, t.planetIndex(), "star target is not a planet");
        assertEquals(0, t.shipSeed(), "star target is not a ship");
    }

    @Test
    public void testPlanetTargetCarriesIndex() {
        USSTarget t = USSTarget.planet(3);
        assertEquals(USSTarget.Kind.PLANET, t.kind());
        assertEquals(3, t.planetIndex(), "planet index in payload A");
        assertEquals(0, t.shipSeed(), "planet target is not a ship");
    }

    @Test
    public void testShipTargetCarriesSeed() {
        USSTarget t = USSTarget.ship(123456);
        assertEquals(USSTarget.Kind.SHIP, t.kind());
        assertEquals(123456, t.shipSeed(), "targeted ship's per-launch seed");
        assertEquals(0, t.planetIndex(), "ship target is not a planet");
    }

    @Test
    public void testDistinctTargetsAreNotEqual() {
        assertNotEquals(USSTarget.star(), USSTarget.planet(0), "star ≠ planet 0");
        assertNotEquals(USSTarget.planet(1), USSTarget.planet(2), "different planets differ");
        assertNotEquals(USSTarget.ship(1), USSTarget.ship(2), "different ships differ");
        assertNotEquals(USSTarget.planet(5), USSTarget.ship(5), "same payload, different kind");
        assertEquals(USSTarget.planet(7), USSTarget.planet(7), "equal targets are equal");
        assertEquals(
            USSTarget.planet(7)
                .hashCode(),
            USSTarget.planet(7)
                .hashCode());
    }

    @Test
    public void testNbtRoundTripStar() {
        USSTarget original = USSTarget.star();
        NBTTagCompound nbt = new NBTTagCompound();
        original.writeToNBT(nbt);
        assertEquals(original, USSTarget.readFromNBT(nbt));
    }

    @Test
    public void testNbtRoundTripPlanet() {
        USSTarget original = USSTarget.planet(4);
        NBTTagCompound nbt = new NBTTagCompound();
        original.writeToNBT(nbt);
        assertEquals(original, USSTarget.readFromNBT(nbt), "planet index survives the round-trip");
        assertEquals(
            4,
            USSTarget.readFromNBT(nbt)
                .planetIndex());
    }

    @Test
    public void testNbtRoundTripShip() {
        USSTarget original = USSTarget.ship(987654);
        NBTTagCompound nbt = new NBTTagCompound();
        original.writeToNBT(nbt);
        assertEquals(original, USSTarget.readFromNBT(nbt), "ship seed survives the round-trip");
        assertEquals(
            987654,
            USSTarget.readFromNBT(nbt)
                .shipSeed());
    }

    @Test
    public void testNbtNullAndUnknownKindFallBackToStar() {
        assertEquals(USSTarget.star(), USSTarget.readFromNBT(null), "null → star");
        NBTTagCompound bad = new NBTTagCompound();
        bad.setString("vc_tgt_kind", "NOT_A_KIND");
        assertEquals(USSTarget.star(), USSTarget.readFromNBT(bad), "unknown kind → star (defensive)");
        NBTTagCompound missing = new NBTTagCompound();
        assertEquals(USSTarget.star(), USSTarget.readFromNBT(missing), "missing kind → star");
    }

    @Test
    public void testWriteToNbtNullSafe() {
        USSTarget.star()
            .writeToNBT(null); // no-op, must not throw
    }

    @Test
    public void testToStringNamesTheKind() {
        assertTrue(
            USSTarget.star()
                .toString()
                .contains("STAR"));
        assertTrue(
            USSTarget.planet(2)
                .toString()
                .contains("PLANET")
                && USSTarget.planet(2)
                    .toString()
                    .contains("2"));
        assertTrue(
            USSTarget.ship(9)
                .toString()
                .contains("SHIP")
                && USSTarget.ship(9)
                    .toString()
                    .contains("9"));
    }
}
