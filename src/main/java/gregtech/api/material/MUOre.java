package gregtech.api.material;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

/// Ore-processing accessors for [Material], mirroring [MU]'s accessor pattern -- [GTMaterialProperties]-backed
/// reads with a documented default, or a direct passthrough for values that never vary from the material
/// itself.
public class MUOre {

    private MUOre() {}

    /// The crushed-ore yield multiplier for a material, or `1` if unset -- see [GTMaterialProperties#ORE_MULTIPLIER].
    public static int oreMultiplier(@Nullable Material material) {
        if (material == null) return 1;
        Integer value = material.getProperty(GTMaterialProperties.ORE_MULTIPLIER);
        return value == null ? 1 : value;
    }

    /// [#oreMultiplier], for [GTMaterialProperties#SMELTING_MULTIPLIER].
    public static int smeltingMultiplier(@Nullable Material material) {
        if (material == null) return 1;
        Integer value = material.getProperty(GTMaterialProperties.SMELTING_MULTIPLIER);
        return value == null ? 1 : value;
    }

    /// [#oreMultiplier], for [GTMaterialProperties#BYPRODUCT_MULTIPLIER].
    public static int byProductMultiplier(@Nullable Material material) {
        if (material == null) return 1;
        Integer value = material.getProperty(GTMaterialProperties.BYPRODUCT_MULTIPLIER);
        return value == null ? 1 : value;
    }

    /// The substitute material for the ore-processing byproduct chain (gem/dust/crushed lookups); always
    /// `material` itself. No [GTMaterialProperties] property backs it.
    public static @Nullable Material oreReplacement(@Nullable Material material) {
        return material;
    }
}
