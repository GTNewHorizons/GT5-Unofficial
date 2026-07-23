package gregtech.api.material;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

/// Ore-processing legacy `Materials` field reads ported to [Material], mirroring [MU]'s accessor pattern --
/// [GTMaterialProperties]-backed reads with the exact legacy default, or a direct passthrough where the
/// legacy field is provably never reassigned.
public class MUOre {

    private MUOre() {}

    /// The legacy `Materials#mOreMultiplier` crushed-ore yield multiplier for a material, or `1` if unset --
    /// mirrors `Materials`'s own default. Ported byte-identically to [GTMaterialProperties#ORE_MULTIPLIER]:
    /// `LegacyMaterials#build` only calls `MaterialBuilder#setOreMultiplier` when the property is present,
    /// leaving the field's own `1` default otherwise, and no bridge loader (bartworks or gtpp) ever writes
    /// `mOreMultiplier` independently.
    public static int oreMultiplier(@Nullable Material material) {
        if (material == null) return 1;
        Integer value = material.getProperty(GTMaterialProperties.ORE_MULTIPLIER);
        return value == null ? 1 : value;
    }

    /// [#oreMultiplier], for `Materials#mSmeltingMultiplier`/[GTMaterialProperties#SMELTING_MULTIPLIER] --
    /// only ever set from `Materials#setSmeltingMultiplier`, called by `LegacyMaterials#build` when the
    /// property is present.
    public static int smeltingMultiplier(@Nullable Material material) {
        if (material == null) return 1;
        Integer value = material.getProperty(GTMaterialProperties.SMELTING_MULTIPLIER);
        return value == null ? 1 : value;
    }

    /// [#oreMultiplier], for `Materials#mByProductMultiplier`/[GTMaterialProperties#BYPRODUCT_MULTIPLIER] --
    /// only ever set from `Materials#setByProductMultiplier`, called by `LegacyMaterials#build` when the
    /// property is present.
    public static int byProductMultiplier(@Nullable Material material) {
        if (material == null) return 1;
        Integer value = material.getProperty(GTMaterialProperties.BYPRODUCT_MULTIPLIER);
        return value == null ? 1 : value;
    }

    /// The legacy `Materials#mOreReplacement` substitute material for the ore-processing byproduct chain
    /// (gem/dust/crushed lookups), always `material` itself: `Materials#setOreReplacement` is the field's only
    /// writer and is never called anywhere in the codebase, so every `Materials` instance keeps its
    /// constructor's `= this` default. No [GTMaterialProperties] property backs it -- there is nothing to
    /// port beyond this passthrough.
    public static @Nullable Material oreReplacement(@Nullable Material material) {
        return material;
    }
}
