package tectech.voidcraft.uss;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Pure loadout computation for a Constructor mission (EoH rework, Phase 4 pass 2).
 *
 * <p>
 * A Constructor ship leaves the gateway loaded with as much material as it can carry toward the USS's current
 * project: for every cost entry, the take is {@code min(remaining, per-mission cap, available at the gateway)}.
 * The three inputs come from different worlds and are kept separate so the math stays a pure function (testable in
 * a bare JVM):
 *
 * <ul>
 * <li>{@code remaining} — from {@link USSInfrastructure} (per-USS progress),</li>
 * <li>{@code cap} — from the ship's construction power via {@code USSConstants} (the same scale the Starlifter
 * uses: {@code starlifterPlasmaAmount} for fluids, {@code starlifterMatterAmount} for dust),</li>
 * <li>{@code available} — from the gateway's input buses (item dust) and input hatches (Stellar Plasma).</li>
 * </ul>
 *
 * <p>
 * No Minecraft runtime here: the gateway scans the availability, calls {@link #compute}, and then depletes the
 * inputs for exactly the returned amounts.
 */
public final class USSLoadout {

    private USSLoadout() {
        throw new AssertionError("Static helpers");
    }

    /**
     * Compute one Constructor mission's loadout for the given project.
     *
     * @param project   the project being built (never null)
     * @param consumed  material name → amount already applied to this project (null-safe)
     * @param plasmaCap per-mission fluid cap in mB (the ship's construction-power scaled cap)
     * @param dustCap   per-mission item (dust) cap in units (the ship's construction-power scaled cap)
     * @param available material name → amount currently available at the gateway inputs (null-safe)
     * @return material name → amount to take (only entries &gt; 0; empty when nothing can be loaded)
     */
    public static Map<String, Long> compute(USSProject project, Map<String, Long> consumed, long plasmaCap,
        long dustCap, Map<String, Long> available) {
        Map<String, Long> result = new LinkedHashMap<>();
        if (project == null) {
            return result;
        }
        for (USSProject.Cost cost : project.costs) {
            long already = consumed == null ? 0L : Math.max(0L, consumed.getOrDefault(cost.materialName, 0L));
            long remaining = Math.max(0L, cost.amount - already);
            if (remaining <= 0L) {
                continue;
            }
            long cap = cost.kind == USSProject.Kind.FLUID ? plasmaCap : dustCap;
            cap = Math.max(1L, cap);
            long have = available == null ? 0L : Math.max(0L, available.getOrDefault(cost.materialName, 0L));
            long take = Math.min(remaining, Math.min(cap, have));
            if (take > 0L) {
                result.put(cost.materialName, take);
            }
        }
        return result;
    }
}
