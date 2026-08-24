package tectech.voidcraft.uss;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import gregtech.api.enums.Materials;

/**
 * One USS infrastructure project (EoH rework, Phase 4 pass 2 — Constructor ships build the system's
 * infrastructure incrementally).
 *
 * <p>
 * A project is a <em>fixed catalog entry</em>: an ordered list of material costs. The constructor always works on
 * the first not-yet-complete project (no selection UI — "no grand systems"), so the catalog order IS the build
 * order. Progress per project lives in {@link USSInfrastructure}; this class is the static definition (pure data,
 * no runtime state, unit-testable).
 *
 * <p>
 * <strong>Material identity</strong> follows the voidcraft convention: costs are keyed by GT material NAME
 * ({@link Materials#getName()}) — a stable string, never a registry id — so a loadout can be persisted in ship
 * payloads and applied later without resolving Forge objects (a bare JVM cannot even construct a FluidStack).
 *
 * <p>
 * Each cost entry is either an <em>item</em> (dust, pulled from the gateway's input buses) or a <em>fluid</em>
 * (Stellar Plasma, drained from the gateway's input hatches).
 */
public final class USSProject {

    /** Whether a cost entry is drawn from the item inputs (buses) or the fluid inputs (hatches). */
    public enum Kind {

        ITEM,
        FLUID;

        private Kind() {}
    }

    /**
     * One material requirement of a project: a material name plus the total amount needed to finish the project.
     * Immutable.
     */
    public static final class Cost {

        /** GT material name (stable string; see class javadoc). */
        public final String materialName;

        /** Total amount required (dust units or millibuckets, depending on {@link #kind}). */
        public final long amount;

        /** Item (dust, from input buses) or fluid (from input hatches). */
        public final Kind kind;

        public Cost(String materialName, long amount, Kind kind) {
            this.materialName = materialName;
            this.amount = amount;
            this.kind = kind;
        }
    }

    /** Project id (stable; persisted in ship payloads and in {@link USSInfrastructure}). */
    public final int id;

    /** Display-name lang key. */
    public final String langKey;

    /** The material costs (in build order; never empty). */
    public final List<Cost> costs;

    public USSProject(int id, String langKey, List<Cost> costs) {
        this.id = id;
        this.langKey = langKey;
        this.costs = Collections.unmodifiableList(costs);
    }

    /**
     * The material cost of this project (null when the material is not part of it).
     */
    public Cost costOf(String materialName) {
        for (Cost cost : costs) {
            if (cost.materialName.equals(materialName)) {
                return cost;
            }
        }
        return null;
    }

    // region catalog

    /**
     * The fixed project catalog, in build order (Phase 4 pass 2 vertical slice — placeholder balance, creative-loop
     * friendly: a few constructor missions per project at typical 2–4-arm constructor power).
     *
     * <p>
     * Amounts are sized against the per-mission loadout caps ({@code USSConstants.starlifterPlasmaAmount} /
     * {@code starlifterMatterAmount} of the ship's construction power): a 200-power constructor (2 arms) carries
     * ~200 000 mB plasma and ~2 000 dust per mission.
     */
    public static final List<USSProject> CATALOG;

    static {
        java.util.ArrayList<USSProject> list = new java.util.ArrayList<>();
        list.add(
            new USSProject(
                0,
                "tt.voidcraft_uss.project.hyperlane",
                java.util.Arrays.asList(
                    new Cost(Materials.RawStarMatter.getName(), 300_000L, Kind.FLUID),
                    new Cost(Materials.WhiteDwarfMatter.getName(), 3_000L, Kind.ITEM))));
        list.add(
            new USSProject(
                1,
                "tt.voidcraft_uss.project.railgun",
                java.util.Arrays.asList(
                    new Cost(Materials.RawStarMatter.getName(), 800_000L, Kind.FLUID),
                    new Cost(Materials.WhiteDwarfMatter.getName(), 5_000L, Kind.ITEM),
                    new Cost(Materials.BlackDwarfMatter.getName(), 2_500L, Kind.ITEM))));
        list.add(
            new USSProject(
                2,
                "tt.voidcraft_uss.project.dyson",
                java.util.Arrays.asList(
                    new Cost(Materials.RawStarMatter.getName(), 2_000_000L, Kind.FLUID),
                    new Cost(Materials.WhiteDwarfMatter.getName(), 4_000L, Kind.ITEM),
                    new Cost(Materials.BlackDwarfMatter.getName(), 8_000L, Kind.ITEM))));
        CATALOG = Collections.unmodifiableList(list);
    }

    /**
     * @param id project id
     * @return the catalog project, or null for an unknown id
     */
    public static USSProject byId(int id) {
        for (USSProject project : CATALOG) {
            if (project.id == id) {
                return project;
            }
        }
        return null;
    }

    // endregion

    @Override
    public String toString() {
        Map<String, String> names = new LinkedHashMap<>();
        for (Cost cost : costs) {
            names.put(cost.materialName, String.valueOf(cost.amount));
        }
        return "USSProject[id=" + id + " " + langKey + " " + names + "]";
    }
}
