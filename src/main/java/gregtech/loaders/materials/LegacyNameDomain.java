package gregtech.loaders.materials;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

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

    private LegacyNameDomain() {}

    /// The MaterialLib material `name` resolves to in the legacy name domain, or null when `name` is outside
    /// the frozen domain (the former `Materials.get` `_NULL` miss).
    public static @Nullable Material lookup(@Nullable String name) {
        if (name == null) return null;
        String mlName = LegacyNameDomainTable.DOMAIN.get(name);
        return mlName == null ? null : MaterialLibAPI.getMaterial("gregtech", mlName);
    }
}
