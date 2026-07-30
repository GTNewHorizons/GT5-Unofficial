package gregtech.loaders.materials;

import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.materials.Materials;

/// The legacy material name domain: the single by-name resolution seam for every consumer that needs to
/// resolve a legacy material name to its MaterialLib counterpart and treat a miss as a plain null
/// (`GTProxy`'s ore-dictionary dispatch, the ore-adapter oredict indexing, the werkstoff and gtPlusPlus name
/// chains). Resolving here instead of via [MaterialUtils#byLegacyName] is deliberate: `byLegacyName` also resolves
/// names outside this domain (recognition-marker backings, sanitized MaterialLib registration names), which
/// would reroute the callers' miss paths -- most critically the recognition-marker fallback in
/// `GTProxy#registerOre`.
///
/// Backed by [LegacyNameDomainTable], a frozen name -> MaterialLib-name table. A name in the domain resolves
/// to its MaterialLib counterpart; every other name -- including a literal `"NULL"` and any name with no
/// MaterialLib counterpart -- resolves to null.
public final class LegacyNameDomain {

    private static Set<Material> membership;

    private LegacyNameDomain() {}

    /// The MaterialLib material `name` resolves to in the legacy name domain, or null when `name` is outside
    /// the frozen domain.
    public static @Nullable Material lookup(@Nullable String name) {
        if (name == null) return null;
        String mlName = LegacyNameDomainTable.DOMAIN.get(name);
        return mlName == null ? null : MaterialLibAPI.getMaterial("gregtech", mlName);
    }

    /// Whether `material` is a gregtech-declared legacy material. Covers every [#lookup]-reachable material
    /// plus the null sentinel ([Materials#NULL]): the sentinel is itself a declared material, but
    /// stays unreachable by name so a foreign name can never resolve onto it through [#lookup].
    public static boolean contains(@Nullable Material material) {
        if (material == null) return false;
        return membership().contains(material);
    }

    private static Set<Material> membership() {
        if (membership == null) {
            Set<Material> set = new HashSet<>(LegacyNameDomainTable.DOMAIN.size() + 1);
            for (String mlName : LegacyNameDomainTable.DOMAIN.values()) {
                Material material = MaterialLibAPI.getMaterial("gregtech", mlName);
                if (material != null) set.add(material);
            }
            set.add(Materials.NULL);
            membership = set;
        }
        return membership;
    }
}
