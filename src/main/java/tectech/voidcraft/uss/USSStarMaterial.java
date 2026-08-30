package tectech.voidcraft.uss;

import gregtech.api.enums.Materials;

/**
 * One material slot of a {@link USSStarDefinition}: the material (primarily a fluid), its relative weight, and its
 * fluid capacity in millions of millibuckets (0 = this slot produces NO fluid — a star may produce 1–3 of its slots).
 *
 * <p>
 * Like {@link USSPlanetOre}, a star material carries an <em>amount</em>: the star's per-fluid reserve is initialized
 * from it (amount × 1 000 000 × star-size²) and depleted by starlifters. The star's materials are "primarily fluids"
 * (the star is composed of / emits them as fluid/plasma), resolved later through the GT material registry.
 *
 * <p>
 * Pure data — no game world, no Forge fluid objects — so it stays unit-testable in a bare JVM (the {@link Materials}
 * enum is plain registry data).
 */
public final class USSStarMaterial {

    /** The material (primarily a fluid) in this slot. */
    private final Materials material;

    /** The relative weight of this material (must be &gt; 0 and finite). */
    private final double weight;

    /** The fluid capacity of this slot, in millions of millibuckets (0 = the slot produces no fluid). */
    private final long amount;

    /**
     * @param material the material (must not be null and not {@link Materials#_NULL})
     * @param weight   the relative weight (must be &gt; 0 and finite)
     * @param amount   the fluid capacity in millions of millibuckets (must be &gt;= 0; 0 = no fluid produced)
     * @throws NullPointerException     if {@code material} is null
     * @throws IllegalArgumentException if {@code material} is {@link Materials#_NULL}, {@code weight} is not a
     *                                  positive finite number, or {@code amount} &lt; 0
     */
    public USSStarMaterial(Materials material, double weight, long amount) {
        if (material == null) {
            throw new NullPointerException("material must not be null");
        }
        if (material == Materials._NULL) {
            throw new IllegalArgumentException("material must not be Materials._NULL");
        }
        if (!Double.isFinite(weight) || weight <= 0.0) {
            throw new IllegalArgumentException("weight must be a positive finite number, got " + weight);
        }
        if (amount < 0) {
            throw new IllegalArgumentException("amount must be >= 0 (0 = no fluid produced), got " + amount);
        }
        this.material = material;
        this.weight = weight;
        this.amount = amount;
    }

    /**
     * @return the material of this slot (never null, never {@link Materials#_NULL}).
     */
    public Materials getMaterial() {
        return material;
    }

    /**
     * @return the relative weight (always &gt; 0 and finite).
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @return the fluid capacity in millions of millibuckets (0 = the slot produces no fluid).
     */
    public long getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "USSStarMaterial[" + material + ", amount=" + amount + " (millions), weight=" + weight + "]";
    }
}
