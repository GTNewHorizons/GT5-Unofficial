package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.interfaces.ISubTagContainer;

/**
 * Unit tests for {@link USSVeinMath} — the vein→dust math copied from the legacy Eye of Harmony recipe.
 *
 * <p>
 * The material side of a vein entry is never dereferenced by the math (only the amount), so a throwaway
 * {@link IOreMaterial} keeps the tests off the heavy {@code Materials} static init.
 */
public class USSVeinMathTest {

    /** Minimal stand-in material — the math only reads the amount, never the material. */
    private static final class DummyMaterial implements IOreMaterial {

        private static final DummyMaterial INSTANCE = new DummyMaterial();

        public static DummyMaterial instance() {
            return INSTANCE;
        }

        private DummyMaterial() {}

        @Override
        public void addTooltips(List<String> list) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public String getInternalName() {
            return "test";
        }

        @Override
        public String getDefaultLocalName() {
            return "Test";
        }

        @Override
        public short[] getRGBA() {
            return new short[4];
        }

        @Override
        public TextureSet getTextureSet() {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<IStoneType> getValidStones() {
            return null;
        }

        @Override
        public gregtech.api.enums.Materials getGTMaterial() {
            return null;
        }

        @Override
        public boolean generatesPrefix(OrePrefixes prefix) {
            return false;
        }

        // ISubTagContainer
        @Override
        public boolean contains(SubTag aTag) {
            return false;
        }

        @Override
        public ISubTagContainer add(SubTag... aTags) {
            return this;
        }

        @Override
        public boolean remove(SubTag aTag) {
            return false;
        }
    }

    private static Pair<IOreMaterial, Long> entry(long amount) {
        return Pair.of(DummyMaterial.instance(), amount);
    }

    @Test
    public void testTotalVeinAmount() {
        List<Pair<IOreMaterial, Long>> vein = Arrays.asList(entry(10), entry(5), entry(1));
        assertEquals(16L, USSVeinMath.totalVeinAmount(vein));

        assertEquals(0L, USSVeinMath.totalVeinAmount(new ArrayList<>()));
        assertEquals(0L, USSVeinMath.totalVeinAmount(null));
    }

    @Test
    public void testTotalIgnoresJunkEntries() {
        List<Pair<IOreMaterial, Long>> vein = new ArrayList<>();
        vein.add(null);
        vein.add(entry(7));
        vein.add(Pair.of(DummyMaterial.instance(), null));
        vein.add(Pair.of(DummyMaterial.instance(), 0L));
        vein.add(Pair.of(DummyMaterial.instance(), -3L));
        assertEquals(7L, USSVeinMath.totalVeinAmount(vein));
    }

    @Test
    public void testStoneDustIsTripledTotal() {
        List<Pair<IOreMaterial, Long>> vein = Arrays.asList(entry(10), entry(5));
        assertEquals(45L, USSVeinMath.stoneDustAmount(vein)); // (10+5) * 3
        assertEquals(
            USSVeinMath.totalVeinAmount(vein) * USSVeinMath.STONE_DUST_MULTIPLIER,
            USSVeinMath.stoneDustAmount(vein));
    }

    @Test
    public void testMiningEuCostFormula() {
        // legacy formula: miningTimeSeconds * 2^19 * 20
        long unit = (1L << 19) * 20L;
        assertEquals(0L, USSVeinMath.miningEuCost(0));
        assertEquals(0L, USSVeinMath.miningEuCost(-5));
        assertEquals(unit, USSVeinMath.miningEuCost(1));
        assertEquals(2L * unit, USSVeinMath.miningEuCost(2));
        assertEquals(100L * unit, USSVeinMath.miningEuCost(100));
    }
}
