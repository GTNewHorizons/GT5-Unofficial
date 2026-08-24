package tectech.voidcraft.ship;

import java.util.Optional;

/**
 * Lookup facade over the registered Voidcraft components.
 *
 * <p>
 * The 9 components are declared once in {@link VoidcraftComponent}; this registry centralizes meta-value lookups
 * (the world-scanning code in the Voidcraft Assembler only ever talks to this class) and the assembler-circuit →
 * max-component-tier gate.
 */
public final class VoidcraftComponentRegistry {

    /** Number of registered components (also the number of block metas the component block uses). */
    public static final int COUNT = VoidcraftComponent.ALL.length;

    /** Highest component tier in the registry (used to clamp the circuit gate). */
    public static final int MAX_TIER = maxTier();

    private VoidcraftComponentRegistry() {
        throw new AssertionError("Static helpers");
    }

    private static int maxTier() {
        int max = 0;
        for (VoidcraftComponent component : VoidcraftComponent.ALL) {
            max = Math.max(max, component.getTier());
        }
        return max;
    }

    /**
     * @param meta block meta value from the world
     * @return the component for that meta, or empty if the meta is not a Voidcraft component
     */
    public static Optional<VoidcraftComponent> byMeta(int meta) {
        return VoidcraftComponent.fromMeta(meta);
    }

    /**
     * Map an assembler integrated-circuit damage value to the highest component tier it may digitize.
     *
     * <p>
     * Circuit damage 0-2 = tier 0 (base components), 3-5 = tier 1, 6+ = tier 2 — the EoH machine uses the same
     * integrated-circuit-in-input-bus convention for its overclock, so operators already know the pattern.
     *
     * @param circuitDamage integrated circuit damage (0-24)
     * @return allowed max component tier (0..MAX_TIER)
     */
    public static int maxTierForCircuit(int circuitDamage) {
        int tier = circuitDamage / 3;
        return Math.max(0, Math.min(MAX_TIER, tier));
    }
}
