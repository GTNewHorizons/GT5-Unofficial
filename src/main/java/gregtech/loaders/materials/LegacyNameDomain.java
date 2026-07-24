package gregtech.loaders.materials;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.Materials;
import gregtech.api.material.MU;

/// The legacy material name domain: the single by-name resolution seam for every consumer that used to call
/// `Materials.get(name)` and branch on its `_NULL` sentinel (`GTProxy`'s ore-dictionary dispatch, the
/// ore-adapter oredict indexing, the werkstoff and gtPlusPlus name chains). Resolving here instead of via
/// [MU#byLegacyName] is deliberate: `byLegacyName` also hits names `Materials.get` misses (recognition-marker
/// backings, sanitized MaterialLib registration names), which would reroute the callers' miss paths -- most
/// critically the recognition-marker fallback in `GTProxy#registerOre`.
///
/// Implemented today as the exact composition of `Materials.get(name)` with [MU#material]: a
/// `Materials.getMaterialsMap()` hit resolves to its MaterialLib counterpart, and a miss -- `_NULL`,
/// including a literal `"NULL"` lookup, which every `Materials.get` caller likewise treated as a miss -- maps
/// to null. The deletion step swaps this backing to a frozen generated name -> material table with the
/// identical domain (the exact facade `mName` strings, including the `LEGACY_NAME` divergents). A facade
/// registered at runtime without a MaterialLib counterpart (a third-party `MaterialBuilder` construction)
/// resolves to a miss here; the frozen table draws the same GT-owned boundary.
public final class LegacyNameDomain {

    private LegacyNameDomain() {}

    /// The MaterialLib material `name` resolves to in the legacy name domain, or null exactly when
    /// `Materials.get(name)` yields its `_NULL` miss sentinel.
    public static @Nullable Material lookup(@Nullable String name) {
        if (name == null) return null;
        Materials legacy = Materials.get(name);
        if (legacy == Materials._NULL) return null;
        return MU.material(legacy);
    }
}
