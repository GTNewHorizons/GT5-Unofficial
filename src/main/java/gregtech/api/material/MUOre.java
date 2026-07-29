package gregtech.api.material;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

/// Ore-processing accessors for [Material], mirroring [MU]'s accessor pattern: [GTMaterialProperties]-backed
/// reads with a documented default.
public class MUOre {

    private MUOre() {}

    /// The crushed-ore yield multiplier for a material, or `1` if unset -- see [GTMaterialProperties#ORE_MULTIPLIER].
    public static int oreMultiplier(@Nullable Material material) {
        return material == null ? 1 : material.getProperty(GTMaterialProperties.ORE_MULTIPLIER);
    }

    /// [#oreMultiplier], for [GTMaterialProperties#SMELTING_MULTIPLIER].
    public static int smeltingMultiplier(@Nullable Material material) {
        return material == null ? 1 : material.getProperty(GTMaterialProperties.SMELTING_MULTIPLIER);
    }

    /// [#oreMultiplier], for [GTMaterialProperties#BYPRODUCT_MULTIPLIER].
    public static int byProductMultiplier(@Nullable Material material) {
        return material == null ? 1 : material.getProperty(GTMaterialProperties.BYPRODUCT_MULTIPLIER);
    }
}
