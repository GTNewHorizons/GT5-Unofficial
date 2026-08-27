package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import gregtech.api.enums.Materials;

/**
 * Unit tests for the star registration data model — {@link USSStarMaterial} and {@link USSStarDefinition}.
 *
 * <p>
 * Bare-JVM: only {@link Materials}/{@link Supplier} data — no Forge fluid/block objects.
 */
public class USSStarDefinitionTest {

    // region USSStarMaterial

    @Test
    public void testStarMaterialStoresMaterialAndWeight() {
        USSStarMaterial m = new USSStarMaterial(Materials.Hydrogen, 2.5);
        assertEquals(Materials.Hydrogen, m.getMaterial());
        assertEquals(2.5, m.getWeight(), 1e-9);
    }

    @Test
    public void testStarMaterialRejectsNullMaterial() {
        assertThrows(NullPointerException.class, () -> new USSStarMaterial(null, 1.0));
    }

    @Test
    public void testStarMaterialRejectsNullMaterialSentinel() {
        assertThrows(IllegalArgumentException.class, () -> new USSStarMaterial(Materials._NULL, 1.0));
    }

    @Test
    public void testStarMaterialRejectsNonPositiveOrNonFiniteWeight() {
        assertThrows(IllegalArgumentException.class, () -> new USSStarMaterial(Materials.Hydrogen, 0.0));
        assertThrows(IllegalArgumentException.class, () -> new USSStarMaterial(Materials.Hydrogen, -1.0));
        assertThrows(IllegalArgumentException.class, () -> new USSStarMaterial(Materials.Hydrogen, Double.NaN));
        assertThrows(
            IllegalArgumentException.class,
            () -> new USSStarMaterial(Materials.Hydrogen, Double.POSITIVE_INFINITY));
    }

    // endregion

    // region USSStarDefinition — happy path

    private static USSStarDefinition validStar() {
        return USSStarDefinition.builder()
            .id("test_star")
            .nameMethod(() -> "Test Star")
            .type("Test Type")
            .sizeRange(1.0f, 5.0f)
            .main(Materials.Hydrogen, 3.0)
            .secondary(Materials.Helium, 2.0)
            .tertiary(Materials.Oxygen, 1.0)
            .luminosity(5.0f)
            .planetRange(3, 9)
            .texture("star_test")
            .evolutionTarget(null)
            .build();
    }

    @Test
    public void testStarExposesAllFields() {
        USSStarDefinition star = validStar();
        assertEquals("test_star", star.getId());
        assertEquals("Test Star", star.name());
        assertEquals("Test Type", star.getType());
        assertEquals(1.0f, star.getSizeMin(), 1e-6);
        assertEquals(5.0f, star.getSizeMax(), 1e-6);
        assertEquals(
            Materials.Hydrogen,
            star.getMain()
                .getMaterial());
        assertEquals(
            3.0,
            star.getMain()
                .getWeight(),
            1e-9);
        assertEquals(
            Materials.Helium,
            star.getSecondary()
                .getMaterial());
        assertEquals(
            Materials.Oxygen,
            star.getTertiary()
                .getMaterial());
        assertEquals(5.0f, star.getLuminosity(), 1e-6);
        assertEquals(3, star.getPlanetMin());
        assertEquals(9, star.getPlanetMax());
        assertEquals("star_test", star.getTexture());
        assertNull(star.getEvolutionTarget());
        assertEquals(0xFFFFFFFF, star.getColor(), "color defaults to white");
    }

    @Test
    public void testColorBuilderRoundTrips() {
        USSStarDefinition star = USSStarDefinition.builder()
            .id("blue_star")
            .nameMethod(() -> "Blue Star")
            .type("Blue Type")
            .sizeRange(1.0f, 5.0f)
            .main(Materials.Hydrogen, 3.0)
            .secondary(Materials.Helium, 2.0)
            .tertiary(Materials.Oxygen, 1.0)
            .luminosity(5.0f)
            .planetRange(3, 9)
            .texture("star_blue")
            .evolutionTarget(null)
            .color(0xFF5A8CFF)
            .build();
        assertEquals(0xFF5A8CFF, star.getColor());
    }

    @Test
    public void testNameMethodIsInvocableAndSupplied() {
        final String[] value = { "generated-name" };
        Supplier<String> supplier = () -> value[0];
        USSStarDefinition star = USSStarDefinition.builder()
            .id("gen")
            .nameMethod(supplier)
            .type("T")
            .main(Materials.Hydrogen, 1.0)
            .secondary(Materials.Helium, 1.0)
            .tertiary(Materials.Oxygen, 1.0)
            .texture("x")
            .build();
        assertEquals("generated-name", star.name());
        // the name method is a function: re-invoking it can produce a different value
        value[0] = "another-name";
        assertEquals("another-name", star.name());
    }

    @Test
    public void testSizeInRange() {
        USSStarDefinition star = validStar();
        assertTrue(star.sizeInRange(1.0f));
        assertTrue(star.sizeInRange(3.0f));
        assertTrue(star.sizeInRange(5.0f));
        assertFalse(star.sizeInRange(0.9f));
        assertFalse(star.sizeInRange(5.1f));
    }

    @Test
    public void testPlanetCountInRange() {
        USSStarDefinition star = validStar();
        assertTrue(star.planetCountInRange(3));
        assertTrue(star.planetCountInRange(6));
        assertTrue(star.planetCountInRange(9));
        assertFalse(star.planetCountInRange(2));
        assertFalse(star.planetCountInRange(10));
    }

    @Test
    public void testEvolutionTargetCanBeNullOrSet() {
        assertNull(validStar().getEvolutionTarget());
        USSStarDefinition evolving = USSStarDefinition.builder()
            .id("evolves")
            .nameMethod(() -> "E")
            .type("T")
            .main(Materials.Hydrogen, 1.0)
            .secondary(Materials.Helium, 1.0)
            .tertiary(Materials.Oxygen, 1.0)
            .texture("x")
            .evolutionTarget("white_dwarf")
            .build();
        assertEquals("white_dwarf", evolving.getEvolutionTarget());
    }

    // endregion

    // region USSStarDefinition — validation

    @Test
    public void testRejectsBlankId() {
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("   ")
                .nameMethod(() -> "N")
                .type("T")
                .main(Materials.Hydrogen, 1.0)
                .secondary(Materials.Helium, 1.0)
                .tertiary(Materials.Oxygen, 1.0)
                .texture("x")
                .build());
    }

    @Test
    public void testRejectsNullNameMethod() {
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .type("T")
                .main(Materials.Hydrogen, 1.0)
                .secondary(Materials.Helium, 1.0)
                .tertiary(Materials.Oxygen, 1.0)
                .texture("x")
                .build());
    }

    @Test
    public void testRejectsBlankType() {
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("")
                .main(Materials.Hydrogen, 1.0)
                .secondary(Materials.Helium, 1.0)
                .tertiary(Materials.Oxygen, 1.0)
                .texture("x")
                .build());
    }

    @Test
    public void testRejectsSizeRangeOutOfBounds() {
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .sizeRange(-0.5f, 5.0f)
                .main(Materials.Hydrogen, 1.0)
                .secondary(Materials.Helium, 1.0)
                .tertiary(Materials.Oxygen, 1.0)
                .texture("x")
                .build());
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .sizeRange(1.0f, 10.5f)
                .main(Materials.Hydrogen, 1.0)
                .secondary(Materials.Helium, 1.0)
                .tertiary(Materials.Oxygen, 1.0)
                .texture("x")
                .build());
    }

    @Test
    public void testRejectsInvertedSizeRange() {
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .sizeRange(9.0f, 1.0f)
                .main(Materials.Hydrogen, 1.0)
                .secondary(Materials.Helium, 1.0)
                .tertiary(Materials.Oxygen, 1.0)
                .texture("x")
                .build());
    }

    @Test
    public void testBoundarySizeAndLuminosityAreAllowed() {
        USSStarDefinition star = USSStarDefinition.builder()
            .id("boundary")
            .nameMethod(() -> "N")
            .type("T")
            .sizeRange(0.0f, 10.0f)
            .main(Materials.Hydrogen, 1.0)
            .secondary(Materials.Helium, 1.0)
            .tertiary(Materials.Oxygen, 1.0)
            .luminosity(10.0f)
            .planetRange(0, 16)
            .texture("x")
            .build();
        assertTrue(star.sizeInRange(0.0f));
        assertTrue(star.sizeInRange(10.0f));
        assertEquals(10.0f, star.getLuminosity(), 1e-6);
        assertTrue(star.planetCountInRange(0));
        assertTrue(star.planetCountInRange(16));
    }

    @Test
    public void testRejectsLuminosityOutOfBounds() {
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .main(Materials.Hydrogen, 1.0)
                .secondary(Materials.Helium, 1.0)
                .tertiary(Materials.Oxygen, 1.0)
                .luminosity(10.5f)
                .texture("x")
                .build());
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .main(Materials.Hydrogen, 1.0)
                .secondary(Materials.Helium, 1.0)
                .tertiary(Materials.Oxygen, 1.0)
                .luminosity(-0.5f)
                .texture("x")
                .build());
    }

    @Test
    public void testRejectsPlanetRangeOutOfBounds() {
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .main(Materials.Hydrogen, 1.0)
                .secondary(Materials.Helium, 1.0)
                .tertiary(Materials.Oxygen, 1.0)
                .planetRange(-1, 9)
                .texture("x")
                .build());
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .main(Materials.Hydrogen, 1.0)
                .secondary(Materials.Helium, 1.0)
                .tertiary(Materials.Oxygen, 1.0)
                .planetRange(3, 17)
                .texture("x")
                .build());
    }

    @Test
    public void testRejectsInvertedPlanetRange() {
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .main(Materials.Hydrogen, 1.0)
                .secondary(Materials.Helium, 1.0)
                .tertiary(Materials.Oxygen, 1.0)
                .planetRange(9, 3)
                .texture("x")
                .build());
    }

    @Test
    public void testRippleRangeIsExposed() {
        USSStarDefinition star = USSStarDefinition.builder()
            .id("ripples")
            .nameMethod(() -> "N")
            .type("T")
            .main(Materials.Hydrogen, 1.0)
            .secondary(Materials.Helium, 1.0)
            .tertiary(Materials.Oxygen, 1.0)
            .rippleRange(4, 20)
            .texture("x")
            .build();
        assertEquals(4, star.getRippleMin());
        assertEquals(20, star.getRippleMax());
        assertTrue(star.rippleCountInRange(4));
        assertTrue(star.rippleCountInRange(12));
        assertTrue(star.rippleCountInRange(20));
        assertFalse(star.rippleCountInRange(3));
        assertFalse(star.rippleCountInRange(21));
    }

    @Test
    public void testRippleRangeDefaultsToFullSpan() {
        // No explicit rippleRange → the full [0, 128] span (defensive default).
        USSStarDefinition star = validStar();
        assertEquals(USSStarDefinition.MIN_RIPPLES, star.getRippleMin());
        assertEquals(USSStarDefinition.MAX_RIPPLES, star.getRippleMax());
        assertTrue(star.rippleCountInRange(0));
        assertTrue(star.rippleCountInRange(128));
    }

    @Test
    public void testRejectsRippleRangeOutOfBounds() {
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .main(Materials.Hydrogen, 1.0)
                .secondary(Materials.Helium, 1.0)
                .tertiary(Materials.Oxygen, 1.0)
                .rippleRange(-1, 9)
                .texture("x")
                .build());
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .main(Materials.Hydrogen, 1.0)
                .secondary(Materials.Helium, 1.0)
                .tertiary(Materials.Oxygen, 1.0)
                .rippleRange(0, 129)
                .texture("x")
                .build());
    }

    @Test
    public void testRejectsInvertedRippleRange() {
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .main(Materials.Hydrogen, 1.0)
                .secondary(Materials.Helium, 1.0)
                .tertiary(Materials.Oxygen, 1.0)
                .rippleRange(20, 4)
                .texture("x")
                .build());
    }

    @Test
    public void testRejectsMissingMaterials() {
        // main missing
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .secondary(Materials.Helium, 1.0)
                .tertiary(Materials.Oxygen, 1.0)
                .texture("x")
                .build());
        // secondary missing
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .main(Materials.Hydrogen, 1.0)
                .tertiary(Materials.Oxygen, 1.0)
                .texture("x")
                .build());
        // tertiary missing
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .main(Materials.Hydrogen, 1.0)
                .secondary(Materials.Helium, 1.0)
                .texture("x")
                .build());
    }

    @Test
    public void testRejectsBlankTexture() {
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .main(Materials.Hydrogen, 1.0)
                .secondary(Materials.Helium, 1.0)
                .tertiary(Materials.Oxygen, 1.0)
                .texture("  ")
                .build());
    }

    // endregion

    @Test
    public void testToStringContainsIdAndType() {
        String s = validStar().toString();
        assertTrue(s.contains("test_star"), "toString should carry the id: " + s);
        assertTrue(s.contains("Test Type"), "toString should carry the type: " + s);
    }
}
