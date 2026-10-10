package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;

/// Generates the recipes of one material part. [ShapeRecipeTable] runs a generator for each MaterialLib pair of a
/// shape, and [ForeignPartRecipes] for each [ForeignPartRecipeTable] row.
@FunctionalInterface
public interface PartRecipeGenerator {

    /// Generates the recipes of `stack`, a `prefix` part of `material`. `shape` is the MaterialLib shape the part
    /// belongs to, or null for another mod's item.
    void generate(OrePrefixes prefix, Material material, @Nullable Shape shape, ItemStack stack);
}
