package gregtech.loaders.materials;

import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.materials2.Materials2Materials;

/// The legacy material name domain: the single by-name resolution seam for every consumer that used to call
/// `Materials.get(name)` and branch on its `_NULL` sentinel (`GTProxy`'s ore-dictionary dispatch, the
/// ore-adapter oredict indexing, the werkstoff and gtPlusPlus name chains). Resolving here instead of via
/// [MU#byLegacyName] is deliberate: `byLegacyName` also hits names `Materials.get` misses (recognition-marker
/// backings, sanitized MaterialLib registration names), which would reroute the callers' miss paths -- most
/// critically the recognition-marker fallback in `GTProxy#registerOre`.
///
/// Backed by [LegacyNameDomainTable], a frozen generated name -> MaterialLib-name table with the exact domain
/// the former `Materials.getMaterialsMap()` carried (every facade `mName`, including the `LEGACY_NAME`
/// divergents). A name in the domain resolves to its MaterialLib counterpart; every other name -- what
/// `Materials.get` returned its `_NULL` miss sentinel for, including a literal `"NULL"` and any third-party
/// `MaterialBuilder` construction with no MaterialLib counterpart -- resolves to null, drawing the same
/// GT-owned boundary the composition of `Materials.get` with [MU#material] drew.
public final class LegacyNameDomain {

    private static Set<Material> membership;

    private LegacyNameDomain() {}

    /// The MaterialLib material `name` resolves to in the legacy name domain, or null when `name` is outside
    /// the frozen domain (the former `Materials.get` `_NULL` miss).
    public static @Nullable Material lookup(@Nullable String name) {
        if (name == null) return null;
        String mlName = LegacyNameDomainTable.DOMAIN.get(name);
        return mlName == null ? null : MaterialLibAPI.getMaterial("gregtech", mlName);
    }

    /// Whether `material` is a gregtech-declared legacy material. Covers every [#lookup]-reachable material
    /// plus the null sentinel ([Materials2Materials#NULL]): the sentinel is itself a declared material, but
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
            set.add(Materials2Materials.NULL);
            membership = set;
        }
        return membership;
    }
}
