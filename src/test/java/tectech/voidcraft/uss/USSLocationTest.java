package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Bare-JVM tests for the ship LOCATION model (SEND / TAKE co-location gate): the kind derivation (orbit / star /
 * ripple / ship / none), the star tolerance, and the shared-location rule.
 */
public class USSLocationTest {

    private static USSLocation.Entry entry(String uuid, USSPosition position) {
        return new USSLocation.Entry(uuid, position);
    }

    private static USSPosition starPoint() {
        return USSPosition.starCenter();
    }

    @Test
    public void testConstantsPinTheRuleScale() {
        // The tolerance is float-drift guard only: the gateway-scale distance (the fleet orbit's max radius) must
        // stay far outside it, or a fresh ship at its gateway would count as "at the star".
        assertEquals(0.5, USSConstants.STAR_LOCATION_TOLERANCE);
        assertTrue(
            USSFleetOrbit.MAX_RADIUS > USSConstants.STAR_LOCATION_TOLERANCE,
            "a gateway-scale distance must stay outside the star tolerance");
        // The ship radius must cover the rendezvous nudge (magnitude <= the fleet orbit's max radius) plus margin.
        assertEquals(2.5, USSConstants.SHIP_LOCATION_RADIUS);
        assertTrue(USSConstants.SHIP_LOCATION_RADIUS >= USSFleetOrbit.MAX_RADIUS);
    }

    @Test
    public void testOrbitAndRippleKinds() {
        USSPosition anywhere = USSPosition.of(40, 3, -12);
        assertEquals(
            USSLocation.Kind.ORBIT,
            USSLocation.of(false, 3, anywhere, null, "self")
                .getKind());
        assertEquals(
            3,
            USSLocation.of(false, 3, anywhere, null, "self")
                .getIndex());
        // a planet orbit is independent of the ship's exact hover point
        assertNotEquals(
            USSLocation.of(false, 3, anywhere, null, "self"),
            USSLocation.of(false, 4, anywhere, null, "self"));
        assertEquals(
            USSLocation.Kind.RIPPLE,
            USSLocation.of(true, 2, anywhere, null, "self")
                .getKind());
        assertEquals(
            2,
            USSLocation.of(true, 2, anywhere, null, "self")
                .getIndex());
        assertNotEquals(USSLocation.orbit(2), USSLocation.ripple(2), "same index, different kind");
    }

    @Test
    public void testStarIsItsOwnLocation() {
        List<USSLocation.Entry> emptyFleet = Collections.emptyList();
        // a ship whose effective point IS the star center is at the star
        assertEquals(USSLocation.star(), USSLocation.of(false, -1, starPoint(), emptyFleet, "self"));
        // the tolerance boundary: exactly STAR_LOCATION_TOLERANCE away still counts, one block farther does not
        USSPosition atEdge = USSPosition.of(0, USSFleetOrbit.STAR_CENTER_Y, USSConstants.STAR_LOCATION_TOLERANCE);
        assertEquals(USSLocation.star(), USSLocation.of(false, -1, atEdge, emptyFleet, "self"));
        USSPosition pastEdge = USSPosition
            .of(0, USSFleetOrbit.STAR_CENTER_Y, USSConstants.STAR_LOCATION_TOLERANCE + 0.1);
        assertEquals(
            USSLocation.Kind.NONE,
            USSLocation.of(false, -1, pastEdge, emptyFleet, "self")
                .getKind());
    }

    @Test
    public void testFreshGatewayShipIsNotAtTheStar() {
        // A ship hovering at its launch gateway (hover body "star/none", position the gateway anchor) is NOT at
        // the star: the gateway-scale distance must land in NONE, so only a ship physically at the star shares the
        // star location.
        USSPosition gatewayScalePoint = USSPosition.of(USSFleetOrbit.MAX_RADIUS, USSFleetOrbit.STAR_CENTER_Y, 0.0);
        USSLocation loc = USSLocation.of(false, -1, gatewayScalePoint, Collections.emptyList(), "self");
        assertEquals(USSLocation.Kind.NONE, loc.getKind());
        assertTrue(loc.isNone());
    }

    @Test
    public void testRendezvousPicksTheNearestOtherFleetShip() {
        USSPosition point = USSPosition.of(5, 0, 5);
        USSLocation.Entry near = entry("ship-near", USSPosition.of(5.5, 0, 5)); // 0.5 away
        USSLocation.Entry far = entry("ship-far", USSPosition.of(7.5, 0, 5)); // 2.5 away (on the radius)
        List<USSLocation.Entry> fleet = Arrays.asList(entry("self", point), near, far);
        assertEquals(USSLocation.ship("ship-near"), USSLocation.of(true, -1, point, fleet, "self"));
        // the scan skips the ship itself: a lone ship at an orphaned rendezvous point has no location
        assertEquals(
            USSLocation.none(),
            USSLocation.of(true, -1, point, Collections.singletonList(entry("self", point)), "self"));
        // no other ship within the radius -> orphaned
        assertEquals(
            USSLocation.none(),
            USSLocation.of(true, -1, point, Arrays.asList(entry("ship-far", USSPosition.of(9, 0, 5))), "self"));
    }

    @Test
    public void testSharedBySameKind() {
        USSPosition a = USSPosition.of(1, 2, 3);
        USSPosition farAway = USSPosition.of(90, -5, 40);
        // same orbit -> shared, no matter the (impossible) hover points
        assertTrue(USSLocation.shared(a, USSLocation.orbit(1), farAway, USSLocation.orbit(1)));
        assertTrue(USSLocation.shared(a, USSLocation.star(), farAway, USSLocation.star()));
        assertTrue(USSLocation.shared(a, USSLocation.ripple(4), farAway, USSLocation.ripple(4)));
        assertTrue(USSLocation.shared(a, USSLocation.ship("uuid-1"), farAway, USSLocation.ship("uuid-1")));
        // different indices / anchors -> not shared (and the points are far apart, so no rescue)
        assertFalse(USSLocation.shared(a, USSLocation.orbit(1), farAway, USSLocation.orbit(2)));
        assertFalse(USSLocation.shared(a, USSLocation.ripple(1), farAway, USSLocation.ripple(2)));
        assertFalse(USSLocation.shared(a, USSLocation.ship("uuid-1"), farAway, USSLocation.ship("uuid-2")));
        assertFalse(USSLocation.shared(a, USSLocation.star(), farAway, USSLocation.orbit(0)));
    }

    @Test
    public void testSharedByProximityWhenKindsDiffer() {
        // One of the two ships within the ship radius of the other -> shared, even when the derived kinds differ
        // (a ship at a non-body point next to a ship on an orbit; the shared location is that ship).
        USSPosition a = USSPosition.of(10, 0, 10);
        USSPosition b = USSPosition.of(10, 1, 10); // 1.0 away
        assertTrue(USSLocation.shared(a, USSLocation.none(), b, USSLocation.orbit(3)));
        assertTrue(USSLocation.shared(a, USSLocation.orbit(3), b, USSLocation.none()));
        // the boundary: exactly SHIP_LOCATION_RADIUS away still counts
        USSPosition atRadius = USSPosition.of(10 + USSConstants.SHIP_LOCATION_RADIUS, 0, 10);
        assertTrue(USSLocation.shared(a, USSLocation.none(), atRadius, USSLocation.none()));
        USSPosition beyond = USSPosition.of(10 + USSConstants.SHIP_LOCATION_RADIUS + 0.1, 0, 10);
        assertFalse(USSLocation.shared(a, USSLocation.none(), beyond, USSLocation.none()));
    }

    @Test
    public void testSharedNeedsBothShips() {
        USSPosition a = USSPosition.of(0, 0, 0);
        assertFalse(USSLocation.shared(a, null, a, USSLocation.orbit(0)));
        assertFalse(USSLocation.shared(a, USSLocation.orbit(0), a, null));
        // a null position never rescues a mismatch, but a matching kind is enough on its own
        assertTrue(USSLocation.shared(null, USSLocation.orbit(0), a, USSLocation.orbit(0)));
        // same orbit but one position missing: the kinds still match, so the point check is not even reached
        assertTrue(USSLocation.shared(null, USSLocation.orbit(0), null, USSLocation.orbit(0)));
    }

    @Test
    public void testEquality() {
        assertEquals(USSLocation.orbit(1), USSLocation.orbit(1));
        assertEquals(USSLocation.star(), USSLocation.star());
        assertEquals(USSLocation.ripple(2), USSLocation.ripple(2));
        assertEquals(USSLocation.ship("u"), USSLocation.ship("u"));
        assertEquals(USSLocation.none(), USSLocation.none());
        assertNotEquals(USSLocation.orbit(1), USSLocation.orbit(2));
        assertNotEquals(USSLocation.ship("u1"), USSLocation.ship("u2"));
        assertEquals(
            USSLocation.orbit(1)
                .hashCode(),
            USSLocation.orbit(1)
                .hashCode());
    }
}
