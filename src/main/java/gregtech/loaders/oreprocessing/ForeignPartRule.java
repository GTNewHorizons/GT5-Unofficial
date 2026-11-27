package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;

/// Generates the recipes of one ore-dictionary registration of a log, plank, slab or crop, whichever mod registers
/// it; see [ForeignPartRecipes#run].
@FunctionalInterface
public interface ForeignPartRule {

    /// Generates the recipes of `stack`, registered as `oreName` under `prefix` and `material`.
    void generate(OrePrefixes prefix, Material material, String oreName, ItemStack stack);
}
