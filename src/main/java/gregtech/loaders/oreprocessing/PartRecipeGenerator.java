package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;

/// Generates the recipes of one material part. [ShapeRecipeTable] runs a generator for each MaterialLib pair of a
/// shape, [ForeignPartRecipes] for each foreign log, plank, slab and crop registration and each
/// [ForeignPartRecipeTable]
/// row.
@FunctionalInterface
public interface PartRecipeGenerator {

    /// Generates the recipes of `stack`, a `prefix` part of `material` named `oreName` in the ore dictionary.
    /// `material` is the `NULL` material under a prefix that is not material-based.
    void generate(OrePrefixes prefix, Material material, String oreName, ItemStack stack);
}
