package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import gregtech.api.enums.Materials;

/**
 * Unit tests for the planet registration data model — {@link USSPlanetOre} and {@link USSPlanetDefinition}.
 *
 * <p>
 * Bare-JVM: only {@link Materials}/{@link USSStarType} registry data — no Forge fluid/block objects.
 */
public class USSPlanetDefinitionTest {

    // region USSPlanetOre

    @Test
    public void testOreRejectsNullType() {
        assertThrows(NullPointerException.class, () -> new USSPlanetOre(null, 100L, 1.0));
    }

    @Test
    public void testOreRejectsNullMaterial() {
        assertThrows(IllegalArgumentException.class, () -> new USSPlanetOre(Materials._NULL, 100L, 1.0));
    }

    @Test
    public void testOreRejectsNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> new USSPlanetOre(Materials.Copper, -1L, 1.0));
    }

    @Test
    public void testOreAllowsZeroAmount() {
        // A zero amount is legal data (a planet may list an ore it currently holds none of); only negative is rejected.
        assertEquals(0L, new USSPlanetOre(Materials.Copper, 0L, 1.0).getAmount());
    }

    @Test
    public void testOreRejectsNonPositiveOrNonFiniteWeight() {
        assertThrows(IllegalArgumentException.class, () -> new USSPlanetOre(Materials.Copper, 100L, 0.0));
        assertThrows(IllegalArgumentException.class, () -> new USSPlanetOre(Materials.Copper, 100L, -3.0));
        assertThrows(IllegalArgumentException.class, () -> new USSPlanetOre(Materials.Copper, 100L, Double.NaN));
        assertThrows(
            IllegalArgumentException.class,
            () -> new USSPlanetOre(Materials.Copper, 100L, Double.POSITIVE_INFINITY));
    }

    // endregion

    // region USSPlanetDefinition — happy path

    private static USSPlanetDefinition validDefinition() {
        return USSPlanetDefinition.builder()
            .id("test_planet")
            .texture("Ma")
            .sizeRange(0.35f, 0.75f)
            .allowedStarType(USSStarType.YELLOW_DWARF)
            .ore(Materials.Copper, 500L, 1.0)
            .ore(Materials.Iron, 300L, 2.0)
            .fluid(Materials.Water)
            .build();
    }

    @Test
    public void testDefinitionSupportsMultipleStarTypes() {
        USSPlanetDefinition planet = USSPlanetDefinition.builder()
            .id("dual_star")
            .texture("Eu")
            .sizeRange(0.5f, 1.0f)
            .allowedStarType(USSStarType.YELLOW_DWARF)
            .allowedStarType(USSStarType.WHITE_DWARF)
            .ore(Materials.Gold, 10L, 1.0)
            .build();
        assertEquals(
            2,
            planet.getAllowedStarTypes()
                .size());
        assertTrue(planet.allowsStarType(USSStarType.YELLOW_DWARF));
        assertTrue(planet.allowsStarType(USSStarType.WHITE_DWARF));
        assertFalse(planet.allowsStarType(USSStarType.BLUE_GIANT));
    }

    @Test
    public void testSizeInRange() {
        USSPlanetDefinition planet = validDefinition();
        assertTrue(planet.sizeInRange(0.35f));
        assertTrue(planet.sizeInRange(0.55f));
        assertTrue(planet.sizeInRange(0.75f));
        assertFalse(planet.sizeInRange(0.34f));
        assertFalse(planet.sizeInRange(0.76f));
    }

    @Test
    public void testAllowsStarTypeIsNullSafe() {
        assertFalse(validDefinition().allowsStarType(null));
    }

    // endregion

    // region USSPlanetDefinition — validation

    @Test
    public void testRejectsBlankId() {
        assertThrows(
            IllegalArgumentException.class,
            () -> USSPlanetDefinition.builder()
                .id("   ")
                .texture("Ma")
                .allowedStarType(USSStarType.YELLOW_DWARF)
                .build());
    }

    @Test
    public void testRejectsBlankTexture() {
        assertThrows(
            IllegalArgumentException.class,
            () -> USSPlanetDefinition.builder()
                .id("x")
                .texture("")
                .allowedStarType(USSStarType.YELLOW_DWARF)
                .build());
    }

    @Test
    public void testRejectsSizeRangeOutOfBounds() {
        // Below 0.0
        assertThrows(
            IllegalArgumentException.class,
            () -> USSPlanetDefinition.builder()
                .id("x")
                .texture("Ma")
                .sizeRange(-0.1f, 0.5f)
                .allowedStarType(USSStarType.YELLOW_DWARF)
                .build());
        // Above 5.0
        assertThrows(
            IllegalArgumentException.class,
            () -> USSPlanetDefinition.builder()
                .id("x")
                .texture("Ma")
                .sizeRange(1.0f, 5.5f)
                .allowedStarType(USSStarType.YELLOW_DWARF)
                .build());
    }

    @Test
    public void testRejectsInvertedSizeRange() {
        assertThrows(
            IllegalArgumentException.class,
            () -> USSPlanetDefinition.builder()
                .id("x")
                .texture("Ma")
                .sizeRange(0.9f, 0.2f)
                .allowedStarType(USSStarType.YELLOW_DWARF)
                .build());
    }

    @Test
    public void testBoundarySizesAreAllowed() {
        // 0.0 and 5.0 are inclusive bounds (user spec: float 0.0–5.0).
        USSPlanetDefinition planet = USSPlanetDefinition.builder()
            .id("boundary")
            .texture("Ma")
            .sizeRange(0.0f, 5.0f)
            .allowedStarType(USSStarType.YELLOW_DWARF)
            .build();
        assertTrue(planet.sizeInRange(0.0f));
        assertTrue(planet.sizeInRange(5.0f));
    }

    @Test
    public void testRejectsNoAllowedStarType() {
        assertThrows(
            IllegalArgumentException.class,
            () -> USSPlanetDefinition.builder()
                .id("x")
                .texture("Ma")
                .sizeRange(0.35f, 0.75f)
                .build());
    }

    @Test
    public void testEmptyOresAndFluidsAreAllowed() {
        USSPlanetDefinition planet = USSPlanetDefinition.builder()
            .id("bare")
            .texture("Ma")
            .sizeRange(0.3f, 0.4f)
            .allowedStarType(USSStarType.BLUE_GIANT)
            .build();
        assertTrue(
            planet.getOres()
                .isEmpty());
        assertTrue(
            planet.getFluids()
                .isEmpty());
    }

    @Test
    public void testCollectionsAreUnmodifiable() {
        USSPlanetDefinition planet = validDefinition();
        assertThrows(
            UnsupportedOperationException.class,
            () -> planet.getOres()
                .add(new USSPlanetOre(Materials.Gold, 1, 1)));
        assertThrows(
            UnsupportedOperationException.class,
            () -> planet.getFluids()
                .add(Materials.Gold));
        assertThrows(
            UnsupportedOperationException.class,
            () -> planet.getAllowedStarTypes()
                .add(USSStarType.WHITE_DWARF));
    }

    // endregion

    @Test
    public void testToStringContainsId() {
        String s = validDefinition().toString();
        assertTrue(s.contains("test_planet"), "toString should carry the id: " + s);
    }

}
