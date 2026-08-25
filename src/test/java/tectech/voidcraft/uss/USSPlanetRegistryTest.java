package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gregtech.api.enums.Materials;

/**
 * Unit tests for the registration system — {@link USSPlanetRegistry} and the initial {@link USSPlanetCatalog}.
 *
 * <p>
 * The registry is a global static, so each test starts from a cleared registry and a reset catalog flag
 * ({@link #setUp()}). Bare-JVM: only registry data — no Forge objects.
 */
public class USSPlanetRegistryTest {

    @BeforeEach
    public void setUp() {
        USSPlanetRegistry.clear();
        USSPlanetCatalog.resetForTests();
    }

    private static USSPlanetDefinition planet(String id, USSStarType starType) {
        return USSPlanetDefinition.builder()
            .id(id)
            .texture("Ma")
            .sizeRange(0.35f, 0.75f)
            .allowedStarType(starType)
            .ore(Materials.Copper, 100L, 1.0)
            .build();
    }

    // region registry basics

    @Test
    public void testRegisterAndGet() {
        USSPlanetDefinition p = planet("alpha", USSStarType.MAIN_SEQUENCE);
        USSPlanetRegistry.register(p);
        assertEquals(p, USSPlanetRegistry.get("alpha"));
        assertTrue(USSPlanetRegistry.contains("alpha"));
    }

    @Test
    public void testGetUnknownIsNull() {
        assertNull(USSPlanetRegistry.get("nope"));
        assertNull(USSPlanetRegistry.get(null));
        assertFalse(USSPlanetRegistry.contains("nope"));
    }

    @Test
    public void testRejectsNullDefinition() {
        assertThrows(NullPointerException.class, () -> USSPlanetRegistry.register(null));
    }

    @Test
    public void testRejectsDuplicateId() {
        USSPlanetRegistry.register(planet("dup", USSStarType.MAIN_SEQUENCE));
        assertThrows(
            IllegalArgumentException.class,
            () -> USSPlanetRegistry.register(planet("dup", USSStarType.WHITE_DWARF)));
    }

    @Test
    public void testAllPreservesRegistrationOrder() {
        USSPlanetRegistry.register(planet("one", USSStarType.MAIN_SEQUENCE));
        USSPlanetRegistry.register(planet("two", USSStarType.WHITE_DWARF));
        USSPlanetRegistry.register(planet("three", USSStarType.SUPERMASSIVE));
        List<USSPlanetDefinition> all = USSPlanetRegistry.all();
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
        assertEquals(0, USSPlanetRegistry.size());
        USSPlanetRegistry.register(planet("a", USSStarType.MAIN_SEQUENCE));
        USSPlanetRegistry.register(planet("b", USSStarType.MAIN_SEQUENCE));
        assertEquals(2, USSPlanetRegistry.size());
        USSPlanetRegistry.clear();
        assertEquals(0, USSPlanetRegistry.size());
        assertTrue(
            USSPlanetRegistry.all()
                .isEmpty());
    }

    // endregion

    // region pool by star type

    @Test
    public void testPoolFiltersByAllowedStarType() {
        USSPlanetRegistry.register(planet("ms_only", USSStarType.MAIN_SEQUENCE));
        USSPlanetRegistry.register(planet("wd_only", USSStarType.WHITE_DWARF));
        USSPlanetDefinition dual = USSPlanetDefinition.builder()
            .id("dual")
            .texture("Eu")
            .sizeRange(0.4f, 0.8f)
            .allowedStarType(USSStarType.MAIN_SEQUENCE)
            .allowedStarType(USSStarType.SUPERMASSIVE)
            .ore(Materials.Gold, 10L, 1.0)
            .build();
        USSPlanetRegistry.register(dual);

        List<USSPlanetDefinition> ms = USSPlanetRegistry.pool(USSStarType.MAIN_SEQUENCE);
        assertEquals(2, ms.size());
        assertTrue(
            ms.stream()
                .anyMatch(
                    x -> x.getId()
                        .equals("ms_only")));
        assertTrue(
            ms.stream()
                .anyMatch(
                    x -> x.getId()
                        .equals("dual")));
        assertFalse(
            ms.stream()
                .anyMatch(
                    x -> x.getId()
                        .equals("wd_only")));

        assertEquals(
            1,
            USSPlanetRegistry.pool(USSStarType.WHITE_DWARF)
                .size());
        assertEquals(
            1,
            USSPlanetRegistry.pool(USSStarType.SUPERMASSIVE)
                .size());
    }

    @Test
    public void testPoolNullStarTypeIsEmpty() {
        USSPlanetRegistry.register(planet("x", USSStarType.MAIN_SEQUENCE));
        assertTrue(
            USSPlanetRegistry.pool(null)
                .isEmpty());
    }

    // endregion

    // region catalog

    @Test
    public void testCatalogRegistersTwelvePlanets() {
        assertEquals(0, USSPlanetRegistry.size());
        USSPlanetCatalog.registerAll();
        assertEquals(12, USSPlanetRegistry.size());
    }

    @Test
    public void testCatalogIsIdempotent() {
        USSPlanetCatalog.registerAll();
        int first = USSPlanetRegistry.size();
        USSPlanetCatalog.registerAll(); // second call is a no-op
        assertEquals(first, USSPlanetRegistry.size());
        assertEquals(12, USSPlanetRegistry.size());
    }

    @Test
    public void testCatalogPoolsMatchLegacyFourPerStar() {
        USSPlanetCatalog.registerAll();
        assertEquals(
            4,
            USSPlanetRegistry.pool(USSStarType.MAIN_SEQUENCE)
                .size());
        assertEquals(
            4,
            USSPlanetRegistry.pool(USSStarType.WHITE_DWARF)
                .size());
        assertEquals(
            4,
            USSPlanetRegistry.pool(USSStarType.SUPERMASSIVE)
                .size());
    }

    @Test
    public void testCatalogPreservesLegacyData() {
        USSPlanetCatalog.registerAll();

        USSPlanetDefinition rocky = USSPlanetRegistry.get("rocky_world");
        assertEquals("Ma", rocky.getTexture());
        assertTrue(rocky.allowsStarType(USSStarType.MAIN_SEQUENCE));
        assertEquals(
            3,
            rocky.getOres()
                .size());
        assertEquals(
            Materials.Copper,
            rocky.getOres()
                .get(0)
                .getOreType());
        assertEquals(
            Materials.Iron,
            rocky.getOres()
                .get(1)
                .getOreType());
        assertEquals(
            Materials.Tin,
            rocky.getOres()
                .get(2)
                .getOreType());

        USSPlanetDefinition gas = USSPlanetRegistry.get("gas_giant");
        assertEquals("Ve", gas.getTexture());
        assertTrue(gas.allowsStarType(USSStarType.SUPERMASSIVE));
        assertEquals(
            Materials.Uranium,
            gas.getOres()
                .get(0)
                .getOreType());
    }

    @Test
    public void testCatalogOresCarryPlaceholderAmountAndWeight() {
        USSPlanetCatalog.registerAll();
        USSPlanetDefinition rocky = USSPlanetRegistry.get("rocky_world");
        for (USSPlanetOre ore : rocky.getOres()) {
            assertEquals(USSPlanetCatalog.DEFAULT_ORE_AMOUNT, ore.getAmount());
            assertEquals(USSPlanetCatalog.DEFAULT_ORE_WEIGHT, ore.getWeight(), 1e-9);
        }
    }

    @Test
    public void testCatalogSizeRangesAreWithinEnvelope() {
        USSPlanetCatalog.registerAll();
        for (USSPlanetDefinition p : USSPlanetRegistry.all()) {
            assertTrue(
                p.getSizeMin() >= USSPlanetDefinition.MIN_SIZE && p.getSizeMin() <= USSPlanetDefinition.MAX_SIZE,
                p.getId() + " sizeMin in [0,5]");
            assertTrue(
                p.getSizeMax() >= USSPlanetDefinition.MIN_SIZE && p.getSizeMax() <= USSPlanetDefinition.MAX_SIZE,
                p.getId() + " sizeMax in [0,5]");
        }
    }

    // endregion
}
