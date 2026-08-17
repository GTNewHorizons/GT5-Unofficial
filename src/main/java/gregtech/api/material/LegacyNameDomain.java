package gregtech.api.material;

import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.materials.Materials;

/// The legacy material name domain: the by-name resolution seam for consumers that resolve a legacy material
/// name to its MaterialLib counterpart and treat a miss as a plain null.
///
/// Distinct from [MaterialUtils#byLegacyName], which also resolves names outside this domain
/// (recognition-marker backings, sanitized MaterialLib registration names); a consumer whose miss path carries
/// its own fallback resolves here.
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
    /// plus the null sentinel ([Materials#NULL]), which is a declared material but unreachable by name.
    public static boolean contains(@Nullable Material material) {
        if (material == null) return false;
        return membership().contains(material);
    }

    private static Set<Material> membership() {
        if (membership == null) {
            Set<Material> set = new HashSet<>(LegacyNameDomainTable.DOMAIN.size() + 1);
            for (String mlName : LegacyNameDomainTable.DOMAIN.values()) {
                Material material = MaterialLibAPI.getMaterial("gregtech", mlName);
                if (material == null) {
                    throw new IllegalStateException("Legacy name domain row " + mlName + " no longer resolves");
                }
                set.add(material);
            }
            set.add(Materials.NULL);
            membership = set;
        }
        return membership;
    }
}
