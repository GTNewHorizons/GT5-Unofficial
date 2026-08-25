package tectech.voidcraft.uss;

import gregtech.api.enums.Materials;

/**
 * One material slot of a {@link USSStarDefinition}: the material (primarily a fluid) and its relative weight.
 *
 * <p>
 * Unlike {@link USSPlanetOre}, a star material carries <em>no amount</em> — only a material and a weight. The star's
 * materials are "primarily fluids" (the star is composed of / emits them as fluid/plasma), resolved later through the
 * GT material registry.
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

    /**
     * @param material the material (must not be null and not {@link Materials#_NULL})
     * @param weight   the relative weight (must be &gt; 0 and finite)
     * @throws NullPointerException     if {@code material} is null
     * @throws IllegalArgumentException if {@code material} is {@link Materials#_NULL} or {@code weight} is not a
     *                                  positive finite number
     */
    public USSStarMaterial(Materials material, double weight) {
        if (material == null) {
            throw new NullPointerException("material must not be null");
        }
        if (material == Materials._NULL) {
            throw new IllegalArgumentException("material must not be Materials._NULL");
        }
        if (!Double.isFinite(weight) || weight <= 0.0) {
            throw new IllegalArgumentException("weight must be a positive finite number, got " + weight);
        }
        this.material = material;
        this.weight = weight;
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

    @Override
    public String toString() {
        return "USSStarMaterial[" + material + ", weight=" + weight + "]";
    }
}
