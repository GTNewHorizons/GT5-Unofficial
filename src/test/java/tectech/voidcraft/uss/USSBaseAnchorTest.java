package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Voidbase anchor descriptor (bare-JVM: NBT + primitives).
 */
public class USSBaseAnchorTest {

    @Test
    public void testFactories() {
        USSBaseAnchor star = USSBaseAnchor.star();
        assertTrue(star.isStar());
        assertEquals(-1, star.index());
        USSBaseAnchor planet = USSBaseAnchor.planet(3);
        assertTrue(planet.isPlanet());
        assertEquals(3, planet.index());
        USSBaseAnchor ripple = USSBaseAnchor.ripple(42);
        assertTrue(ripple.isRipple());
        assertEquals(42, ripple.index());
        assertThrows(IllegalArgumentException.class, () -> USSBaseAnchor.planet(-1));
        assertThrows(IllegalArgumentException.class, () -> USSBaseAnchor.ripple(-1));
    }

    @Test
    public void testNbtRoundTrip() {
        for (USSBaseAnchor anchor : new USSBaseAnchor[] { USSBaseAnchor.star(), USSBaseAnchor.planet(0),
            USSBaseAnchor.planet(5), USSBaseAnchor.ripple(99) }) {
            NBTTagCompound nbt = new NBTTagCompound();
            anchor.writeToNBT(nbt);
            assertEquals(anchor, USSBaseAnchor.readFromNBT(nbt));
            assertEquals(
                anchor.hashCode(),
                USSBaseAnchor.readFromNBT(nbt)
                    .hashCode());
        }
    }

    @Test
    public void testFromMoveTarget() {
        assertEquals(
            USSBaseAnchor.star(),
            USSBaseAnchor.fromMoveTarget(USSProgramDefaults.TARGET_STAR, -1),
            "STAR → the star anchor");
        assertEquals(
            USSBaseAnchor.planet(2),
            USSBaseAnchor.fromMoveTarget(USSProgramDefaults.TARGET_PLANET, 2),
            "PLANET i → planet i");
        assertEquals(
            USSBaseAnchor.planet(0),
            USSBaseAnchor.fromMoveTarget(USSProgramDefaults.TARGET_NEAREST_PLANET, 0),
            "a resolved NEAREST_PLANET carries its index");
        assertEquals(
            USSBaseAnchor.planet(4),
            USSBaseAnchor.fromMoveTarget(USSProgramDefaults.TARGET_RANDOM_PLANET, 4),
            "a resolved RANDOM_PLANET carries its index");
        assertEquals(
            USSBaseAnchor.ripple(7),
            USSBaseAnchor.fromMoveTarget(USSProgramDefaults.TARGET_RIPPLE, 7),
            "RIPPLE j → ripple j");
        assertEquals(
            USSBaseAnchor.ripple(342),
            USSBaseAnchor.fromMoveTarget(USSProgramDefaults.TARGET_RIPPLE_UNSCANNED, 342),
            "a resolved RIPPLE_UNSCANNED carries its index");
        assertNull(USSBaseAnchor.fromMoveTarget(USSProgramDefaults.TARGET_HOME, 0), "HOME has no build anchor");
        assertNull(
            USSBaseAnchor.fromMoveTarget(USSProgramDefaults.TARGET_SHIP, 0),
            "SHIP rendezvous has no build anchor");
        assertNull(USSBaseAnchor.fromMoveTarget("BOGUS", 0), "unknown kinds have no build anchor");
        assertNull(
            USSBaseAnchor.fromMoveTarget(USSProgramDefaults.TARGET_PLANET, -1),
            "an unresolved planet index (the hover never resolved) has no build anchor");
        assertNull(USSBaseAnchor.fromMoveTarget(null, 0), "null kind has no build anchor");
        assertNull(USSBaseAnchor.fromMoveTarget("", 0), "empty kind has no build anchor");
    }

    @Test
    public void testNullDefaultsToStar() {
        assertEquals(USSBaseAnchor.star(), USSBaseAnchor.readFromNBT(null));
        assertEquals(USSBaseAnchor.star(), USSBaseAnchor.readFromNBT(new NBTTagCompound()));
    }

    @Test
    public void testEqualsAndToString() {
        assertEquals(USSBaseAnchor.planet(2), USSBaseAnchor.planet(2));
        assertFalse(
            USSBaseAnchor.planet(2)
                .equals(USSBaseAnchor.ripple(2)));
        assertFalse(
            USSBaseAnchor.planet(2)
                .equals(USSBaseAnchor.planet(3)));
        assertEquals(
            "STAR",
            USSBaseAnchor.star()
                .toString());
        assertEquals(
            "PLANET 2",
            USSBaseAnchor.planet(2)
                .toString());
        assertEquals(
            "RIPPLE 7",
            USSBaseAnchor.ripple(7)
                .toString());
    }
}
