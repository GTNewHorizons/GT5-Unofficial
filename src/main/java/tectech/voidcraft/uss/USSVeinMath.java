package tectech.voidcraft.uss;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import gregtech.api.interfaces.IOreMaterial;

/**
 * Vein → dust math for the Unstable Solar System, copied from the legacy Eye of Harmony recipe
 * ({@code tectech.recipe.EyeOfHarmonyRecipe} — {@code processDimension} lines 135–141 and the vacuum miner EU
 * formula at line 211).
 *
 * <p>
 * The legacy classes stay untouched (plan §1.1 do-not-modify list); this is the re-implementation the USS planet
 * generator (a later phase) will build on. Pure data math only — {@code ItemStack} construction happens at machine
 * runtime, not here.
 *
 * @see docs/Voidcraft_Implementation_Plan.md
 */
public final class USSVeinMath {

    /**
     * Stone-dust output multiplier, copied from the legacy recipe: stone dust = 3 × total vein dust
     * ({@code outputItemsTemp.merge(stoneDust, sumOfItems * 3L)}).
     */
    public static final long STONE_DUST_MULTIPLIER = 3L;

    private USSVeinMath() {
        throw new AssertionError("Math helpers");
    }

    /**
     * Total dust amount carried by a vein definition (sum of all vein entries, ignoring nulls and non-positive
     * entries — mirroring the legacy loop).
     *
     * @param vein vein entries (material, amount), may be empty.
     * @return the summed amount (0 for null/empty input).
     */
    public static long totalVeinAmount(List<Pair<IOreMaterial, Long>> vein) {
        if (vein == null) {
            return 0L;
        }
        long total = 0L;
        for (Pair<IOreMaterial, Long> entry : vein) {
            if (entry != null && entry.getRight() != null && entry.getRight() > 0) {
                total += entry.getRight();
            }
        }
        return total;
    }

    /**
     * Stone-dust output amount for a vein, using the legacy formula (3 × total vein dust).
     *
     * @param vein vein entries (material, amount), may be empty.
     * @return the stone dust amount.
     */
    public static long stoneDustAmount(List<Pair<IOreMaterial, Long>> vein) {
        return totalVeinAmount(vein) * STONE_DUST_MULTIPLIER;
    }

    /**
     * Vacuum miner EU cost for a mining run, copied from the legacy recipe:
     * {@code miningTimeSeconds * 2^19 * 20} (the "VM3EU" constant).
     *
     * @param miningTimeSeconds mining time in seconds (&lt;= 0 costs nothing).
     * @return the EU cost.
     */
    public static long miningEuCost(long miningTimeSeconds) {
        if (miningTimeSeconds <= 0) {
            return 0L;
        }
        return miningTimeSeconds * (1L << 19) * 20L;
    }
}
