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
    public void testStarMaterialRejectsNullMaterial() {
        assertThrows(NullPointerException.class, () -> new USSStarMaterial(null, 1.0, 1L));
    }

    @Test
    public void testStarMaterialRejectsNullMaterialSentinel() {
        assertThrows(IllegalArgumentException.class, () -> new USSStarMaterial(Materials._NULL, 1.0, 1L));
    }

    @Test
    public void testStarMaterialRejectsNonPositiveOrNonFiniteWeight() {
        assertThrows(IllegalArgumentException.class, () -> new USSStarMaterial(Materials.Hydrogen, 0.0, 1L));
        assertThrows(IllegalArgumentException.class, () -> new USSStarMaterial(Materials.Hydrogen, -1.0, 1L));
        assertThrows(IllegalArgumentException.class, () -> new USSStarMaterial(Materials.Hydrogen, Double.NaN, 1L));
        assertThrows(
            IllegalArgumentException.class,
            () -> new USSStarMaterial(Materials.Hydrogen, Double.POSITIVE_INFINITY, 1L));
    }

    @Test
    public void testStarMaterialRejectsNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> new USSStarMaterial(Materials.Hydrogen, 1.0, -1L));
    }

    @Test
    public void testStarMaterialCarriesAmount() {
        // A zero amount is VALID — the slot simply produces no fluid (a star may produce 1–3 of its 3 slots).
        assertEquals(0L, new USSStarMaterial(Materials.Helium, 2.0, 0L).getAmount(), "0 = no fluid produced");
        assertEquals(200L, new USSStarMaterial(Materials.Hydrogen, 3.0, 200L).getAmount(), "the amount round-trips");
        assertEquals(3.0, new USSStarMaterial(Materials.Hydrogen, 3.0, 200L).getWeight());
    }

    // endregion

    // region USSStarDefinition — happy path

    private static USSStarDefinition validStar() {
        return USSStarDefinition.builder()
            .id("test_star")
            .nameMethod(() -> "Test Star")
            .type("Test Type")
            .sizeRange(1.0f, 5.0f)
            .main(Materials.Hydrogen, 3.0, 100L)
            .secondary(Materials.Helium, 2.0, 50L)
            .tertiary(Materials.Oxygen, 1.0, 25L)
            .luminosity(5.0f)
            .planetRange(3, 9)
            .texture("star_test")
            .evolutionTarget(null)
            .build();
    }

    @Test
    public void testColorDefaultsToWhite() {
        assertEquals(0xFFFFFFFF, validStar().getColor(), "color defaults to white");
    }

    @Test
    public void testColorBuilderRoundTrips() {
        USSStarDefinition star = USSStarDefinition.builder()
            .id("blue_star")
            .nameMethod(() -> "Blue Star")
            .type("Blue Type")
            .sizeRange(1.0f, 5.0f)
            .main(Materials.Hydrogen, 3.0, 100L)
            .secondary(Materials.Helium, 2.0, 50L)
            .tertiary(Materials.Oxygen, 1.0, 25L)
            .luminosity(5.0f)
            .planetRange(3, 9)
            .texture("star_blue")
            .evolutionTarget(null)
            .color(0xFF5A8CFF)
            .build();
        assertEquals(0xFF5A8CFF, star.getColor());
    }

    @Test
    public void testShellColorDefaultsToUnset() {
        assertEquals(0, validStar().getShellColor(), "shell color defaults to unset (0)");
    }

    @Test
    public void testShellColorBuilderRoundTripsAndFallsBack() {
        USSStarDefinition star = validStar();
        // Unset shell: the renderer's fallback resolves the core color.
        assertEquals(star.getColor(), USSStarColor.shellColorFor(star));
        USSStarDefinition shelled = USSStarDefinition.builder()
            .id("black_hole_star")
            .nameMethod(() -> "Black Hole Star")
            .type("Black Hole Type")
            .sizeRange(1.0f, 5.0f)
            .main(Materials.Iron, 3.0, 100L)
            .secondary(Materials.Osmium, 2.0, 50L)
            .tertiary(Materials.Platinum, 1.0, 25L)
            .luminosity(0.0f)
            .planetRange(0, 3)
            .texture("star_black_hole")
            .evolutionTarget(null)
            .color(0xFF000000)
            .shellColor(0xFFFFB000)
            .build();
        assertEquals(0xFFFFB000, shelled.getShellColor());
        assertEquals(0xFFFFB000, USSStarColor.shellColorFor(shelled));
    }

    @Test
    public void testRenderTypeDefaultsToStandard() {
        assertEquals(
            USSStarRenderType.STANDARD,
            validStar().getRenderType(),
            "render type defaults to standard (no extra geometry)");
    }

    @Test
    public void testRenderTypeBuilderRoundTripsAndNullFallsBack() {
        USSStarDefinition magnetar = USSStarDefinition.builder()
            .id("magnetar_star")
            .nameMethod(() -> "Magnetar Star")
            .type("Magnetar Type")
            .sizeRange(1.0f, 3.0f)
            .main(Materials.Cobalt, 3.0, 100L)
            .secondary(Materials.Iron, 2.0, 50L)
            .tertiary(Materials.Nickel, 1.0, 25L)
            .luminosity(1.0f)
            .planetRange(0, 3)
            .texture("star_magnetar")
            .evolutionTarget(null)
            .renderType(USSStarRenderType.MAGNETAR)
            .build();
        assertEquals(USSStarRenderType.MAGNETAR, magnetar.getRenderType());
        // A null render type normalizes to the standard treatment.
        assertEquals(
            USSStarRenderType.STANDARD,
            USSStarDefinition.builder()
                .id("null_render_star")
                .nameMethod(() -> "Null Render Star")
                .type("Null Render Type")
                .sizeRange(1.0f, 5.0f)
                .main(Materials.Hydrogen, 3.0, 100L)
                .secondary(Materials.Helium, 2.0, 50L)
                .tertiary(Materials.Oxygen, 1.0, 25L)
                .luminosity(5.0f)
                .planetRange(3, 9)
                .texture("star_null_render")
                .evolutionTarget(null)
                .renderType(null)
                .build()
                .getRenderType());
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
            .main(Materials.Hydrogen, 1.0, 1L)
            .secondary(Materials.Helium, 1.0, 1L)
            .tertiary(Materials.Oxygen, 1.0, 25L)
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
                .main(Materials.Hydrogen, 1.0, 1L)
                .secondary(Materials.Helium, 1.0, 1L)
                .tertiary(Materials.Oxygen, 1.0, 25L)
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
                .main(Materials.Hydrogen, 1.0, 1L)
                .secondary(Materials.Helium, 1.0, 1L)
                .tertiary(Materials.Oxygen, 1.0, 25L)
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
                .main(Materials.Hydrogen, 1.0, 1L)
                .secondary(Materials.Helium, 1.0, 1L)
                .tertiary(Materials.Oxygen, 1.0, 25L)
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
                .main(Materials.Hydrogen, 1.0, 1L)
                .secondary(Materials.Helium, 1.0, 1L)
                .tertiary(Materials.Oxygen, 1.0, 25L)
                .texture("x")
                .build());
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .sizeRange(1.0f, 10.5f)
                .main(Materials.Hydrogen, 1.0, 1L)
                .secondary(Materials.Helium, 1.0, 1L)
                .tertiary(Materials.Oxygen, 1.0, 25L)
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
                .main(Materials.Hydrogen, 1.0, 1L)
                .secondary(Materials.Helium, 1.0, 1L)
                .tertiary(Materials.Oxygen, 1.0, 25L)
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
            .main(Materials.Hydrogen, 1.0, 1L)
            .secondary(Materials.Helium, 1.0, 1L)
            .tertiary(Materials.Oxygen, 1.0, 25L)
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
                .main(Materials.Hydrogen, 1.0, 1L)
                .secondary(Materials.Helium, 1.0, 1L)
                .tertiary(Materials.Oxygen, 1.0, 25L)
                .luminosity(10.5f)
                .texture("x")
                .build());
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .main(Materials.Hydrogen, 1.0, 1L)
                .secondary(Materials.Helium, 1.0, 1L)
                .tertiary(Materials.Oxygen, 1.0, 25L)
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
                .main(Materials.Hydrogen, 1.0, 1L)
                .secondary(Materials.Helium, 1.0, 1L)
                .tertiary(Materials.Oxygen, 1.0, 25L)
                .planetRange(-1, 9)
                .texture("x")
                .build());
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .main(Materials.Hydrogen, 1.0, 1L)
                .secondary(Materials.Helium, 1.0, 1L)
                .tertiary(Materials.Oxygen, 1.0, 25L)
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
                .main(Materials.Hydrogen, 1.0, 1L)
                .secondary(Materials.Helium, 1.0, 1L)
                .tertiary(Materials.Oxygen, 1.0, 25L)
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
            .main(Materials.Hydrogen, 1.0, 1L)
            .secondary(Materials.Helium, 1.0, 1L)
            .tertiary(Materials.Oxygen, 1.0, 25L)
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
                .main(Materials.Hydrogen, 1.0, 1L)
                .secondary(Materials.Helium, 1.0, 1L)
                .tertiary(Materials.Oxygen, 1.0, 25L)
                .rippleRange(-1, 9)
                .texture("x")
                .build());
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .main(Materials.Hydrogen, 1.0, 1L)
                .secondary(Materials.Helium, 1.0, 1L)
                .tertiary(Materials.Oxygen, 1.0, 25L)
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
                .main(Materials.Hydrogen, 1.0, 1L)
                .secondary(Materials.Helium, 1.0, 1L)
                .tertiary(Materials.Oxygen, 1.0, 25L)
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
                .secondary(Materials.Helium, 1.0, 1L)
                .tertiary(Materials.Oxygen, 1.0, 25L)
                .texture("x")
                .build());
        // secondary missing
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .main(Materials.Hydrogen, 1.0, 1L)
                .tertiary(Materials.Oxygen, 1.0, 25L)
                .texture("x")
                .build());
        // tertiary missing
        assertThrows(
            IllegalArgumentException.class,
            () -> USSStarDefinition.builder()
                .id("x")
                .nameMethod(() -> "N")
                .type("T")
                .main(Materials.Hydrogen, 1.0, 1L)
                .secondary(Materials.Helium, 1.0, 1L)
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
                .main(Materials.Hydrogen, 1.0, 1L)
                .secondary(Materials.Helium, 1.0, 1L)
                .tertiary(Materials.Oxygen, 1.0, 25L)
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
