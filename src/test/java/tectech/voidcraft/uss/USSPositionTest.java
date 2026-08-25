package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

/**
 * Bare-JVM tests for the stateful-position vector ({@link USSPosition}): the star center, the "distance within the
 * solar system" (Euclidean blocks), the vector math the ship's move-toward-target uses, and the NBT round-trip.
 */
public class USSPositionTest {

    @Test
    public void testStarCenterIsTwoBelowAnchor() {
        USSPosition star = USSPosition.starCenter();
        assertEquals(0.0, star.x(), 1e-9);
        assertEquals(USSFleetOrbit.STAR_CENTER_Y, star.y(), 1e-9, "the star center sits at the anchor's -2");
        assertEquals(0.0, star.z(), 1e-9);
    }

    @Test
    public void testZeroIsTheAnchorOrigin() {
        USSPosition o = USSPosition.zero();
        assertEquals(0.0, o.length(), 1e-9);
        assertEquals(USSPosition.zero(), USSPosition.of(0.0, 0.0, 0.0), "of(0,0,0) is the origin (value-equal)");
    }

    @Test
    public void testDistanceIsSymmetricEuclidean() {
        USSPosition a = USSPosition.of(1.0, 2.0, 3.0);
        USSPosition b = USSPosition.of(4.0, 6.0, 3.0);
        // Δ = (3, 4, 0) → length 5 (a 3-4-5 right triangle).
        assertEquals(5.0, a.distanceTo(b), 1e-9);
        assertEquals(a.distanceTo(b), b.distanceTo(a), 1e-12, "distance is symmetric");
        assertEquals(0.0, a.distanceTo(a), 1e-12, "a point is 0 from itself");
        assertEquals(
            5.0,
            b.subtract(a)
                .length(),
            1e-9,
            "|Δ| == distanceTo");
    }

    @Test
    public void testDistanceNullSafe() {
        assertEquals(
            0.0,
            USSPosition.of(1, 2, 3)
                .distanceTo(null),
            1e-12,
            "null → 0 (defensive)");
    }

    @Test
    public void testLerpEndpointsAndMidpoint() {
        USSPosition a = USSPosition.of(0.0, 0.0, 0.0);
        USSPosition b = USSPosition.of(10.0, 20.0, 30.0);
        assertSame(a, a.lerp(null, 0.5), "null target → this");
        assertEquals(a, a.lerp(b, 0.0), "t=0 → this");
        assertEquals(b, a.lerp(b, 1.0), "t=1 → other");
        USSPosition mid = a.lerp(b, 0.5);
        assertEquals(5.0, mid.x(), 1e-9);
        assertEquals(10.0, mid.y(), 1e-9);
        assertEquals(15.0, mid.z(), 1e-9);
    }

    @Test
    public void testAddSubtractScale() {
        USSPosition p = USSPosition.of(1.0, 2.0, 3.0);
        assertEquals(USSPosition.of(3.0, 5.0, 7.0), p.add(2.0, 3.0, 4.0));
        assertEquals(USSPosition.of(3.0, 5.0, 7.0), p.add(USSPosition.of(2.0, 3.0, 4.0)));
        assertSame(p, p.add((USSPosition) null), "null delta → this");
        assertEquals(USSPosition.of(0.0, 0.0, 0.0), p.subtract(p), "p − p = origin");
        assertSame(p, p.subtract(null), "null → this");
        assertEquals(USSPosition.of(2.0, 4.0, 6.0), p.scale(2.0));
    }

    @Test
    public void testDotAndCross() {
        USSPosition x = USSPosition.of(1.0, 0.0, 0.0);
        USSPosition y = USSPosition.of(0.0, 1.0, 0.0);
        USSPosition z = USSPosition.of(0.0, 0.0, 1.0);
        assertEquals(1.0, x.dot(x), 1e-9, "unit x · x = 1");
        assertEquals(0.0, x.dot(y), 1e-9, "orthogonal → 0");
        assertEquals(0.0, x.dot(null), 1e-9, "null → 0");
        // x × y = z (right-handed).
        assertEquals(z, x.cross(y), "x cross y = z");
        assertEquals(USSPosition.zero(), x.cross(null), "null → zero");
    }

    @Test
    public void testNormalize() {
        USSPosition v = USSPosition.of(3.0, 4.0, 0.0);
        USSPosition n = v.normalize();
        assertEquals(1.0, n.length(), 1e-9, "unit length");
        assertEquals(0.6, n.x(), 1e-9);
        assertEquals(0.8, n.y(), 1e-9);
        USSPosition z = USSPosition.zero();
        assertSame(z, z.normalize(), "zero vector is a no-op (returns the same instance)");
    }

    @Test
    public void testImmutability() {
        USSPosition a = USSPosition.of(1.0, 2.0, 3.0);
        USSPosition b = a.add(1.0, 1.0, 1.0);
        assertEquals(USSPosition.of(1.0, 2.0, 3.0), a, "the source is unchanged by add");
        assertEquals(USSPosition.of(2.0, 3.0, 4.0), b);
        assertNotEquals(a, b);
        assertEquals(a, USSPosition.of(1.0, 2.0, 3.0), "equals is value-based");
        assertEquals(
            a.hashCode(),
            USSPosition.of(1.0, 2.0, 3.0)
                .hashCode(),
            "hashCode agrees with equals");
    }

    @Test
    public void testNbtRoundTrip() {
        USSPosition original = USSPosition.of(1.5, -2.0, 99.25);
        NBTTagCompound nbt = new NBTTagCompound();
        original.writeToNBT(nbt);
        USSPosition restored = USSPosition.readFromNBT(nbt);
        assertEquals(original, restored, "NBT round-trip preserves the position");
        assertEquals(1.5, restored.x(), 1e-12);
        assertEquals(-2.0, restored.y(), 1e-12);
        assertEquals(99.25, restored.z(), 1e-12);
    }

    @Test
    public void testNbtNullSafe() {
        USSPosition p = USSPosition.of(1.0, 2.0, 3.0);
        p.writeToNBT(null); // no-op, must not throw
        assertEquals(USSPosition.zero(), USSPosition.readFromNBT(null), "null → origin");
    }

    @Test
    public void testToStringIncludesComponents() {
        String s = USSPosition.of(1.0, -2.0, 3.0)
            .toString();
        assertTrue(s.contains("1.0") && s.contains("-2.0") && s.contains("3.0"), "toString lists the components: " + s);
    }
}
