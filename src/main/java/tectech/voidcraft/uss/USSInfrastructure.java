package tectech.voidcraft.uss;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Per-USS infrastructure build progress (EoH rework, Phase 4 pass 2).
 *
 * <p>
 * Tracks, per project in the {@link USSProject#CATALOG} (fixed build order), how much of each material has already
 * been applied by completed Constructor missions. A project is complete when every cost entry has at least its full
 * amount; the constructor always works on the <em>first not-yet-complete</em> project
 * ({@link #firstIncomplete()}) — so incremental building across multiple Constructor missions falls out of the
 * model without any selection state.
 *
 * <p>
 * Progress is <em>permanent</em>: it persists across star burnouts and re-ignitions (the infrastructure belongs to
 * the solar system, not to the current star) and across chunk reloads (NBT round-trip). There is deliberately no
 * backwards-compatibility: a corrupt or unknown NBT yields a fresh, empty progress (project directive).
 *
 * <p>
 * Pure data + NBT (no Minecraft runtime) — unit-testable in a bare JVM (see {@code USSInfrastructureTest}).
 */
public final class USSInfrastructure {

    /** NBT tag under which per-project compounds live (voidcraft "vc_" naming convention). */
    public static final String TAG_PROJECTS = "vc_projects";

    /** NBT tag inside a project compound: the "materialName" → consumed-amount entries. */
    public static final String TAG_COSTS = "vc_costs";

    /** project id → (material name → consumed amount). */
    private final Map<Integer, Map<String, Long>> progress = new LinkedHashMap<>();

    /**
     * @param projectId    project id
     * @param materialName GT material name
     * @return the amount already applied to this project (0 when none)
     */
    public long consumed(int projectId, String materialName) {
        Map<String, Long> costs = progress.get(projectId);
        if (costs == null || materialName == null) {
            return 0L;
        }
        Long amount = costs.get(materialName);
        return amount == null ? 0L : amount;
    }

    /**
     * @param projectId    project id
     * @param materialName GT material name
     * @return the amount still missing (0 when complete or unknown material — defensive)
     */
    public long remaining(int projectId, String materialName) {
        USSProject project = USSProject.byId(projectId);
        if (project == null) {
            return 0L;
        }
        USSProject.Cost cost = project.costOf(materialName);
        if (cost == null) {
            return 0L;
        }
        return Math.max(0L, cost.amount - consumed(projectId, materialName));
    }

    /**
     * @param projectId project id
     * @return true when every cost entry is fully consumed (false for unknown projects)
     */
    public boolean isComplete(int projectId) {
        USSProject project = USSProject.byId(projectId);
        if (project == null) {
            return false;
        }
        for (USSProject.Cost cost : project.costs) {
            if (consumed(projectId, cost.materialName) < cost.amount) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the first catalog project that is not yet complete (the Constructor's current work), or null when
     *         the whole catalog is finished
     */
    public USSProject firstIncomplete() {
        for (USSProject project : USSProject.CATALOG) {
            if (!isComplete(project.id)) {
                return project;
            }
        }
        return null;
    }

    /**
     * Apply a completed Constructor mission's loadout to its project: each material is credited with
     * {@code min(amount, remaining)} — overflow is NOT applied (the project stays exactly at its cost, the
     * "waste" is the ship's design problem, not the system's).
     *
     * @param projectId the project the loadout was computed for (see {@code vc_project} in the ship payload)
     * @param amounts   material name → amount carried (never null; entries ≤ 0 are ignored)
     * @return the total amount actually applied (for logging; may be less than the sum of {@code amounts} when the
     *         project was already complete — a stale payload)
     */
    public long apply(int projectId, Map<String, Long> amounts) {
        if (amounts == null) {
            return 0L;
        }
        USSProject project = USSProject.byId(projectId);
        if (project == null) {
            return 0L;
        }
        Map<String, Long> costs = progress.computeIfAbsent(projectId, k -> new LinkedHashMap<>());
        long applied = 0L;
        for (Map.Entry<String, Long> entry : amounts.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null || entry.getValue() <= 0L) {
                continue;
            }
            USSProject.Cost cost = project.costOf(entry.getKey());
            if (cost == null) {
                continue; // material not part of the project — ignore rather than corrupt progress
            }
            long room = Math.max(0L, cost.amount - costs.getOrDefault(entry.getKey(), 0L));
            long take = Math.min(entry.getValue(), room);
            if (take > 0L) {
                costs.put(entry.getKey(), costs.getOrDefault(entry.getKey(), 0L) + take);
                applied += take;
            }
        }
        return applied;
    }

    // region NBT

    /**
     * Serialize the progress into the given tag compound (a self-contained compound — the caller nests it).
     */
    public void writeToNBT(NBTTagCompound nbt) {
        if (nbt == null || progress.isEmpty()) {
            return;
        }
        NBTTagList projects = new NBTTagList();
        for (Map.Entry<Integer, Map<String, Long>> projectEntry : progress.entrySet()) {
            NBTTagCompound projectTag = new NBTTagCompound();
            projectTag.setInteger("id", projectEntry.getKey());
            NBTTagList costs = new NBTTagList();
            for (Map.Entry<String, Long> costEntry : projectEntry.getValue()
                .entrySet()) {
                NBTTagCompound costTag = new NBTTagCompound();
                costTag.setString("name", costEntry.getKey());
                costTag.setLong("amount", costEntry.getValue());
                costs.appendTag(costTag);
            }
            projectTag.setTag(TAG_COSTS, costs);
            projects.appendTag(projectTag);
        }
        nbt.setTag(TAG_PROJECTS, projects);
    }

    /**
     * Deserialize a progress model from the given tag compound.
     *
     * @param nbt the compound as written by {@link #writeToNBT(NBTTagCompound)} (may be empty or null)
     * @return the progress model — NEVER null; corrupt or unknown entries are dropped (no backwards-compatibility)
     */
    public static USSInfrastructure readFromNBT(NBTTagCompound nbt) {
        USSInfrastructure result = new USSInfrastructure();
        if (nbt == null) {
            return result;
        }
        NBTTagList projects = nbt.getTagList(TAG_PROJECTS, 10);
        for (int i = 0; i < projects.tagCount(); i++) {
            NBTTagCompound projectTag = projects.getCompoundTagAt(i);
            if (projectTag == null) {
                continue;
            }
            USSProject project = USSProject.byId(projectTag.getInteger("id"));
            if (project == null) {
                continue; // unknown project id — drop (catalog may have changed; no migration path)
            }
            NBTTagList costs = projectTag.getTagList(TAG_COSTS, 10);
            for (int j = 0; j < costs.tagCount(); j++) {
                NBTTagCompound costTag = costs.getCompoundTagAt(j);
                if (costTag == null) {
                    continue;
                }
                String name = costTag.getString("name");
                if (name.isEmpty() || project.costOf(name) == null) {
                    continue; // not part of the project — drop
                }
                long amount = Math.max(0L, costTag.getLong("amount"));
                if (amount <= 0L) {
                    continue;
                }
                result.progress.computeIfAbsent(project.id, k -> new LinkedHashMap<>())
                    .put(name, amount);
            }
        }
        return result;
    }

    // endregion

    @Override
    public String toString() {
        return "USSInfrastructure" + progress;
    }
}
