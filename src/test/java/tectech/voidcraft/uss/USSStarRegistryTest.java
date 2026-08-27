package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gregtech.api.enums.Materials;

/**
 * Unit tests for the star registration system — {@link USSStarRegistry} and the initial {@link USSStarCatalog}.
 *
 * <p>
 * The registry is a global static, so each test starts from a cleared registry and a reset catalog flag. Bare-JVM:
 * only registry data — no Forge objects.
 */
public class USSStarRegistryTest {

    @BeforeEach
    public void setUp() {
        USSStarRegistry.clear();
        USSStarCatalog.resetForTests();
    }

    private static USSStarDefinition star(String id, String evolutionTarget) {
        return USSStarDefinition.builder()
            .id(id)
            .nameMethod(() -> id)
            .type(id)
            .sizeRange(1.0f, 5.0f)
            .main(Materials.Hydrogen, 1.0)
            .secondary(Materials.Helium, 1.0)
            .tertiary(Materials.Oxygen, 1.0)
            .luminosity(5.0f)
            .planetRange(3, 9)
            .texture("star_" + id)
            .evolutionTarget(evolutionTarget)
            .build();
    }

    // region registry basics

    @Test
    public void testRegisterAndGet() {
        USSStarDefinition s = star("alpha", null);
        USSStarRegistry.register(s);
        assertEquals(s, USSStarRegistry.get("alpha"));
        assertTrue(USSStarRegistry.contains("alpha"));
    }

    @Test
    public void testGetUnknownIsNull() {
        assertNull(USSStarRegistry.get("nope"));
        assertNull(USSStarRegistry.get(null));
        assertFalse(USSStarRegistry.contains("nope"));
    }

    @Test
    public void testRejectsNullDefinition() {
        assertThrows(NullPointerException.class, () -> USSStarRegistry.register(null));
    }

    @Test
    public void testRejectsDuplicateId() {
        USSStarRegistry.register(star("dup", null));
        assertThrows(IllegalArgumentException.class, () -> USSStarRegistry.register(star("dup", null)));
    }

    @Test
    public void testAllPreservesRegistrationOrder() {
        USSStarRegistry.register(star("one", null));
        USSStarRegistry.register(star("two", null));
        USSStarRegistry.register(star("three", null));
        List<USSStarDefinition> all = USSStarRegistry.all();
        assertEquals(3, all.size());
        assertEquals(
            "one",
            all.get(0)
                .getId());
        assertEquals(
            "two",
            all.get(1)
                .getId());
        assertEquals(
            "three",
            all.get(2)
                .getId());
    }

    @Test
    public void testSizeAndClear() {
        assertEquals(0, USSStarRegistry.size());
        USSStarRegistry.register(star("a", null));
        USSStarRegistry.register(star("b", null));
        assertEquals(2, USSStarRegistry.size());
        USSStarRegistry.clear();
        assertEquals(0, USSStarRegistry.size());
        assertTrue(
            USSStarRegistry.all()
                .isEmpty());
    }

    // endregion

    // region evolution target

    @Test
    public void testEvolutionTargetResolves() {
        USSStarRegistry.register(star("predecessor", null));
        USSStarDefinition evolving = star("evolver", "predecessor");
        USSStarRegistry.register(evolving);
        assertEquals(
            "predecessor",
            USSStarRegistry.evolutionTargetOf(evolving)
                .getId());
    }

    @Test
    public void testEvolutionTargetNullWhenAbsent() {
        USSStarDefinition terminal = star("terminal", null);
        USSStarRegistry.register(terminal);
        assertNull(USSStarRegistry.evolutionTargetOf(terminal));
        assertNull(USSStarRegistry.evolutionTargetOf(null));
    }

    @Test
    public void testEvolutionTargetUnregisteredResolvesNull() {
        USSStarDefinition dangling = star("dangling", "not_registered");
        USSStarRegistry.register(dangling);
        assertNull(USSStarRegistry.evolutionTargetOf(dangling));
    }

    // endregion

    // region catalog

    @Test
    public void testCatalogRegistersThreeStars() {
        assertEquals(0, USSStarRegistry.size());
        USSStarCatalog.registerAll();
        assertEquals(3, USSStarRegistry.size());
    }

    @Test
    public void testCatalogIsIdempotent() {
        USSStarCatalog.registerAll();
        int first = USSStarRegistry.size();
        USSStarCatalog.registerAll(); // second call is a no-op
        assertEquals(first, USSStarRegistry.size());
        assertEquals(3, USSStarRegistry.size());
    }

    @Test
    public void testCatalogPreservesTheThreeLegacyStarClasses() {
        USSStarCatalog.registerAll();
        assertNotNull(USSStarRegistry.get("main_sequence"), "main_sequence registered");
        assertNotNull(USSStarRegistry.get("white_dwarf"), "white_dwarf registered");
        assertNotNull(USSStarRegistry.get("supermassive"), "supermassive registered");
    }

    @Test
    public void testCatalogTypeIsDisplayNameDistinctFromId() {
        USSStarCatalog.registerAll();
        assertEquals(
            "Main Sequence",
            USSStarRegistry.get("main_sequence")
                .getType());
        assertEquals(
            "White Dwarf",
            USSStarRegistry.get("white_dwarf")
                .getType());
        assertEquals(
            "Supermassive",
            USSStarRegistry.get("supermassive")
                .getType());
    }

    @Test
    public void testCatalogMaterialsAreWeighted() {
        USSStarCatalog.registerAll();
        for (USSStarDefinition star : USSStarRegistry.all()) {
            assertNotNull(star.getMain(), star.getId() + " main");
            assertNotNull(star.getSecondary(), star.getId() + " secondary");
            assertNotNull(star.getTertiary(), star.getId() + " tertiary");
            assertTrue(
                star.getMain()
                    .getWeight() > 0,
                star.getId() + " main weight");
            assertTrue(
                star.getSecondary()
                    .getWeight() > 0,
                star.getId() + " secondary weight");
            assertTrue(
                star.getTertiary()
                    .getWeight() > 0,
                star.getId() + " tertiary weight");
        }
    }

    @Test
    public void testCatalogColorsFollowTheVisualSpec() {
        USSStarCatalog.registerAll();
        assertEquals(
            0xFFFFD640,
            USSStarRegistry.get("main_sequence")
                .getColor());
        assertEquals(
            0xFFFFFFFF,
            USSStarRegistry.get("white_dwarf")
                .getColor());
        assertEquals(
            0xFF5A8CFF,
            USSStarRegistry.get("supermassive")
                .getColor());
    }

    @Test
    public void testStarColorHelperResolvesRegisteredAndFallsBack() {
        USSStarCatalog.registerAll();
        assertEquals(USSStarColor.DEFAULT, USSStarColor.colorFor(null));
        assertEquals(0xFFFFD640, USSStarColor.colorFor(USSStarRegistry.byType(USSStarType.MAIN_SEQUENCE)));
        assertEquals(USSStarColor.DEFAULT, USSStarColor.colorFor(USSStarRegistry.get("unregistered")));
    }

    @Test
    public void testCatalogEvolutionChain() {
        USSStarCatalog.registerAll();
        // main_sequence evolves into white_dwarf (registered); the others are terminal (null target).
        assertEquals(
            "white_dwarf",
            USSStarRegistry.get("main_sequence")
                .getEvolutionTarget(),
            "main_sequence → white_dwarf");
        assertNull(
            USSStarRegistry.get("white_dwarf")
                .getEvolutionTarget(),
            "white_dwarf is terminal");
        assertNull(
            USSStarRegistry.get("supermassive")
                .getEvolutionTarget(),
            "supermassive is terminal");
        assertEquals(
            "white_dwarf",
            USSStarRegistry.evolutionTargetOf(USSStarRegistry.get("main_sequence"))
                .getId(),
            "evolution resolves to the registered star");
    }

    @Test
    public void testCatalogBoundsAreWithinEnvelope() {
        USSStarCatalog.registerAll();
        for (USSStarDefinition star : USSStarRegistry.all()) {
            assertTrue(
                star.getSizeMin() >= USSStarDefinition.MIN_SIZE && star.getSizeMin() <= USSStarDefinition.MAX_SIZE,
                star.getId() + " sizeMin in [0,10]");
            assertTrue(
                star.getSizeMax() >= USSStarDefinition.MIN_SIZE && star.getSizeMax() <= USSStarDefinition.MAX_SIZE,
                star.getId() + " sizeMax in [0,10]");
            assertTrue(
                star.getLuminosity() >= USSStarDefinition.MIN_LUMINOSITY
                    && star.getLuminosity() <= USSStarDefinition.MAX_LUMINOSITY,
                star.getId() + " luminosity in [0,10]");
            assertTrue(
                star.getPlanetMin() >= USSStarDefinition.MIN_PLANETS
                    && star.getPlanetMin() <= USSStarDefinition.MAX_PLANETS,
                star.getId() + " planetMin in [0,16]");
            assertTrue(
                star.getPlanetMax() >= USSStarDefinition.MIN_PLANETS
                    && star.getPlanetMax() <= USSStarDefinition.MAX_PLANETS,
                star.getId() + " planetMax in [0,16]");
            assertNotNull(star.getTexture(), star.getId() + " texture");
            assertNotNull(star.name(), star.getId() + " name");
        }
    }

    // endregion
}
