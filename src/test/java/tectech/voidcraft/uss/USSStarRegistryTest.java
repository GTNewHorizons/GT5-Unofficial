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
            .main(Materials.Hydrogen, 1.0, 1L)
            .secondary(Materials.Helium, 1.0, 1L)
            .tertiary(Materials.Oxygen, 1.0, 25L)
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
    public void testCatalogRegistersSixteenStars() {
        assertEquals(0, USSStarRegistry.size());
        USSStarCatalog.registerAll();
        assertEquals(16, USSStarRegistry.size());
    }

    @Test
    public void testCatalogIsIdempotent() {
        USSStarCatalog.registerAll();
        int first = USSStarRegistry.size();
        USSStarCatalog.registerAll(); // second call is a no-op
        assertEquals(first, USSStarRegistry.size());
        assertEquals(16, USSStarRegistry.size());
    }

    @Test
    public void testCatalogRegistersTheMappedClasses() {
        USSStarCatalog.registerAll();
        // The three mapped legacy classes under their new ids, plus a spot of the new catalog.
        assertNotNull(USSStarRegistry.get("yellow_dwarf"), "yellow_dwarf registered");
        assertNotNull(USSStarRegistry.get("white_dwarf"), "white_dwarf registered");
        assertNotNull(USSStarRegistry.get("blue_giant"), "blue_giant registered");
        assertNotNull(USSStarRegistry.get("black_hole"), "black_hole registered");
        assertNotNull(USSStarRegistry.get("quark_star"), "quark_star registered");
    }

    @Test
    public void testCatalogTypeIsDisplayNameDistinctFromId() {
        USSStarCatalog.registerAll();
        assertEquals(
            "Yellow Dwarf",
            USSStarRegistry.get("yellow_dwarf")
                .getType());
        assertEquals(
            "White Dwarf",
            USSStarRegistry.get("white_dwarf")
                .getType());
        assertEquals(
            "Blue Giant",
            USSStarRegistry.get("blue_giant")
                .getType());
        assertEquals(
            "Gravastar",
            USSStarRegistry.get("gravastar")
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
            0xFFE07020,
            USSStarRegistry.get("yellow_dwarf")
                .getColor(),
            "yellow dwarf core orange");
        assertEquals(
            0xFF9FC8FF,
            USSStarRegistry.get("white_dwarf")
                .getColor(),
            "white dwarf core light blue");
        assertEquals(
            0xFF9FC8FF,
            USSStarRegistry.get("blue_giant")
                .getColor(),
            "blue giant core light blue");
        assertEquals(
            0xFF000000,
            USSStarRegistry.get("black_hole")
                .getColor(),
            "black hole core black");
        assertEquals(
            0xFFFFB000,
            USSStarRegistry.get("black_hole")
                .getShellColor(),
            "black hole shell amber");
    }

    @Test
    public void testStarColorHelperResolvesRegisteredAndFallsBack() {
        USSStarCatalog.registerAll();
        assertEquals(USSStarColor.DEFAULT, USSStarColor.colorFor(null));
        assertEquals(0xFFE07020, USSStarColor.colorFor(USSStarRegistry.byType(USSStarType.YELLOW_DWARF)));
        assertEquals(USSStarColor.DEFAULT, USSStarColor.colorFor(USSStarRegistry.get("unregistered")));
        // Shell: the registered shell when set (black hole amber), the core color when they match (blue giant).
        assertEquals(0xFFFFB000, USSStarColor.shellColorFor(USSStarRegistry.byType(USSStarType.BLACK_HOLE)));
        assertEquals(0xFF9FC8FF, USSStarColor.shellColorFor(USSStarRegistry.byType(USSStarType.BLUE_GIANT)));
        assertEquals(USSStarColor.DEFAULT, USSStarColor.shellColorFor(null));
    }

    @Test
    public void testCatalogRenderTypes() {
        USSStarCatalog.registerAll();
        // The magnetar is the first custom render treatment: magnetic dipole field loops through the core.
        assertEquals(
            USSStarRenderType.MAGNETAR,
            USSStarRegistry.get("magnetar")
                .getRenderType(),
            "magnetar field loops");
        // The other catalog stars keep the standard three-layer sphere.
        assertEquals(
            USSStarRenderType.STANDARD,
            USSStarRegistry.get("yellow_dwarf")
                .getRenderType(),
            "yellow dwarf standard");
        assertEquals(
            USSStarRenderType.STANDARD,
            USSStarRegistry.get("black_hole")
                .getRenderType(),
            "black hole standard");
    }

    @Test
    public void testStarRenderTypeHelperResolvesRegisteredAndFallsBack() {
        USSStarCatalog.registerAll();
        assertEquals(
            USSStarRenderType.MAGNETAR,
            USSStarColor.renderTypeFor(USSStarRegistry.byType(USSStarType.MAGNETAR)));
        assertEquals(
            USSStarRenderType.STANDARD,
            USSStarColor.renderTypeFor(USSStarRegistry.byType(USSStarType.YELLOW_DWARF)));
        assertEquals(USSStarRenderType.STANDARD, USSStarColor.renderTypeFor(null));
    }

    @Test
    public void testCatalogStarsProduceOneToThreeFluids() {
        // Every catalog star produces 1–3 of its three materials (a zero-capacity slot produces none), and every
        // produced fluid has a positive capacity.
        USSStarCatalog.registerAll();
        for (USSStarDefinition star : USSStarRegistry.all()) {
            int produced = 0;
            for (USSStarMaterial material : star.getMaterials()) {
                if (material.getAmount() > 0L) {
                    produced++;
                }
            }
            assertTrue(produced >= 1 && produced <= 3, star.getId() + " produces " + produced + " fluid(s) (1–3)");
        }
    }

    @Test
    public void testCatalogEvolutionTable() {
        USSStarCatalog.registerAll();
        // The star-evolution design table: the seven chains, the six terminal types, every target registered.
        assertEquals(
            "white_dwarf",
            USSStarRegistry.get("red_dwarf")
                .getEvolutionTarget(),
            "red_dwarf → white_dwarf");
        assertEquals(
            "red_giant",
            USSStarRegistry.get("yellow_dwarf")
                .getEvolutionTarget(),
            "yellow_dwarf → red_giant");
        assertEquals(
            "white_dwarf",
            USSStarRegistry.get("red_giant")
                .getEvolutionTarget(),
            "red_giant → white_dwarf");
        assertEquals(
            "black_dwarf",
            USSStarRegistry.get("white_dwarf")
                .getEvolutionTarget(),
            "white_dwarf → black_dwarf");
        assertEquals(
            "red_supergiant",
            USSStarRegistry.get("blue_giant")
                .getEvolutionTarget(),
            "blue_giant → red_supergiant");
        assertEquals(
            "supernova",
            USSStarRegistry.get("red_supergiant")
                .getEvolutionTarget(),
            "red_supergiant → supernova");
        assertEquals(
            "black_hole",
            USSStarRegistry.get("supernova")
                .getEvolutionTarget(),
            "supernova → black_hole");
        assertEquals(
            "hypernova",
            USSStarRegistry.get("blue_supergiant")
                .getEvolutionTarget(),
            "blue_supergiant → hypernova");
        assertEquals(
            "neutron_star",
            USSStarRegistry.get("hypernova")
                .getEvolutionTarget(),
            "hypernova → neutron_star");
        assertEquals(
            "magnetar",
            USSStarRegistry.get("neutron_star")
                .getEvolutionTarget(),
            "neutron_star → magnetar");
        for (String terminal : new String[] { "black_dwarf", "black_hole", "quasi_star", "magnetar", "gravastar",
            "quark_star" }) {
            assertNull(
                USSStarRegistry.get(terminal)
                    .getEvolutionTarget(),
                terminal + " is terminal");
        }
        // Every target resolves to a registered star.
        for (USSStarDefinition star : USSStarRegistry.all()) {
            if (star.getEvolutionTarget() != null) {
                assertNotNull(
                    USSStarRegistry.evolutionTargetOf(star),
                    star.getId() + " target resolves to a registered star");
            }
        }
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
