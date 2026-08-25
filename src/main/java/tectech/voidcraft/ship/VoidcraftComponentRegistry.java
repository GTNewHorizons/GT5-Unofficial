package tectech.voidcraft.ship;

import java.util.List;
import java.util.Optional;

/**
 * Lookup facade over the Voidcraft component catalog.
 *
 * <p>
 * The catalog is declared once in {@link VoidcraftComponent} (pass 23: two placeable full blocks — controller and
 * frame — plus seven cover-only function definitions); this registry centralizes meta-value lookups (the
 * world-scanning code in the Voidcraft Assembler only ever talks to this class) and the assembler-circuit →
 * max-component-tier gate.
 */
public final class VoidcraftComponentRegistry {

    /** Number of catalog entries (the grid-value space: 1..COUNT). */
    public static final int COUNT = VoidcraftComponent.ALL.length;

    /**
     * PASS 23: the only placeable full blocks (controller + frame). Everything else is a cover-only function.
     */
    public static final List<VoidcraftComponent> FULL_BLOCKS = VoidcraftComponent.PLACEABLE;

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
