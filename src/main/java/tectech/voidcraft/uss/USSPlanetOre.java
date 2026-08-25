package tectech.voidcraft.uss;

import gregtech.api.enums.Materials;

/**
 * One ore entry of a {@link USSPlanetDefinition}: the element the planet holds, how much of it (in millions), and
 * its relative weight for weighted selection.
 *
 * <p>
 * Pure data — no game world, no Forge fluid objects — so it stays unit-testable in a bare JVM (the {@link Materials}
 * enum is plain registry data). The <em>meaning</em> of {@code amount} and {@code weight} (reserve size, selection
 * probability) is interpreted by the mining mechanism, which is deliberately NOT wired in yet — the registration
 * system only stores the numbers.
 *
 * <p>
 * Immutable: all fields are final and validated at construction.
 */
public final class USSPlanetOre {

    /** The element (GT material) this ore is. */
    private final Materials oreType;

    /** The amount of this ore on the planet, in millions (e.g. {@code 500} = 500 million). Unit-agnostic here. */
    private final long amount;

    /** The relative weight for weighted selection (must be &gt; 0). */
    private final double weight;

    /**
     * @param oreType the element (must not be null and not {@link Materials#_NULL})
     * @param amount  the amount in millions (must be &gt;= 0)
     * @param weight  the relative selection weight (must be &gt; 0 and finite)
     * @throws NullPointerException     if {@code oreType} is null
     * @throws IllegalArgumentException if {@code oreType} is {@link Materials#_NULL}, {@code amount} &lt; 0, or
     *                                  {@code weight} is not a positive finite number
     */
    public USSPlanetOre(Materials oreType, long amount, double weight) {
        if (oreType == null) {
            throw new NullPointerException("oreType must not be null");
        }
        if (oreType == Materials._NULL) {
            throw new IllegalArgumentException("oreType must not be Materials._NULL");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("amount must be >= 0, got " + amount);
        }
        if (!Double.isFinite(weight) || weight <= 0.0) {
            throw new IllegalArgumentException("weight must be a positive finite number, got " + weight);
        }
        this.oreType = oreType;
        this.amount = amount;
        this.weight = weight;
    }

    /**
     * @return the element of this ore (never null, never {@link Materials#_NULL}).
     */
    public Materials getOreType() {
        return oreType;
    }

    /**
     * @return the amount of this ore on the planet, in millions (never negative).
     */
    public long getAmount() {
        return amount;
    }

    /**
     * @return the relative selection weight (always &gt; 0 and finite).
     */
    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "USSPlanetOre[" + oreType + ", amount=" + amount + " (millions), weight=" + weight + "]";
    }
}
