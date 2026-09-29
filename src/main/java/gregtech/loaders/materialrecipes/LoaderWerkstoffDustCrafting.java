package gregtech.loaders.materialrecipes;

import java.util.List;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;

/// Crafting-grid dust recipes for the materials carried over from bartworks' Werkstoff: the dust / small dust /
/// tiny dust size conversions for materials below IV processing tier, and hammering crushed, purified and
/// centrifuged ore into dust for materials with an ore. `GTProxy`'s tool-usage loop generates the equivalents for
/// GregTech's own materials only.
public final class LoaderWerkstoffDustCrafting {

    private static final long BITS = GTModHandler.RecipeBits.BUFFERED
        | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS;

    private LoaderWerkstoffDustCrafting() {}

    public static void run() {
        for (Material material : MaterialLibAPI.getMaterials()) {
            List<String> prefixes = material.getProperty(GTMaterialProperties.WERKSTOFF_PREFIXES);
            if (prefixes == null || !prefixes.contains("dust") || MaterialUtils.isGregTechMaterial(material)) continue;
            if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) addSizeConversions(material);
            if (prefixes.contains("ore")) addHammering(material);
        }
    }

    private static void addSizeConversions(Material material) {
        if (MaterialParts.stack(OrePrefixes.dustTiny, material, 1) != null) {
            GTModHandler.addCraftingRecipe(
                MaterialParts.stack(OrePrefixes.dust, material, 1),
                BITS,
                new Object[] { "TTT", "TTT", "TTT", 'T', OrePrefixes.dustTiny.ingredient(material) });
            GTModHandler.addCraftingRecipe(
                MaterialParts.stack(OrePrefixes.dustTiny, material, 9),
                BITS,
                new Object[] { "T  ", 'T', OrePrefixes.dust.ingredient(material) });
        }
        if (MaterialParts.stack(OrePrefixes.dustSmall, material, 1) != null) {
            GTModHandler.addCraftingRecipe(
                MaterialParts.stack(OrePrefixes.dust, material, 1),
                BITS,
                new Object[] { "TT ", "TT ", 'T', OrePrefixes.dustSmall.ingredient(material) });
            GTModHandler.addCraftingRecipe(
                MaterialParts.stack(OrePrefixes.dustSmall, material, 4),
                BITS,
                new Object[] { " T ", 'T', OrePrefixes.dust.ingredient(material) });
        }
    }

    private static void addHammering(Material material) {
        hammer(OrePrefixes.dustImpure, OrePrefixes.crushed, material);
        hammer(OrePrefixes.dustPure, OrePrefixes.crushedPurified, material);
        hammer(OrePrefixes.dust, OrePrefixes.crushedCentrifuged, material);
    }

    private static void hammer(OrePrefixes output, OrePrefixes input, Material material) {
        if (MaterialParts.stack(output, material, 1) == null || MaterialParts.stack(input, material, 1) == null) return;
        GTModHandler.addCraftingRecipe(
            MaterialParts.stack(output, material, 1),
            BITS,
            new Object[] { "h  ", "W  ", 'W', input.ingredient(material) });
    }
}
