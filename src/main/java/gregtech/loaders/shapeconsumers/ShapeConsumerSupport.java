package gregtech.loaders.shapeconsumers;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.MU;

/// Shared dispatch glue for the `Consumer*` classes in this package: each targets one legacy `OrePrefixes`/
/// [Shape] pair and delegates to the same [IOreRecipeRegistrator] its `gregtech.loaders.oreprocessing.Processing*`
/// counterpart already implements, resolving the legacy [Materials] and canonical stack through [MU]/
/// [MaterialLibAPI] instead of an oredict event.
///
/// The `Processing*` class itself is untouched: it keeps running through the legacy oredict-event path
/// (`gregtech.common.OreDictEventContainer`) for foreign mods' items of the same prefix, and only stops seeing
/// MaterialLib's own items once that path is taught to skip them.
final class ShapeConsumerSupport {

    private ShapeConsumerSupport() {}

    /// Registers `registrator` to run once per material generating `shape`, at MaterialLib's init, resolving
    /// the legacy `(prefix, material, stack)` triple `registrator` expects. A material with no legacy
    /// counterpart, or a shape/material pair with no resolvable stack, is skipped rather than failing the
    /// whole dispatch -- see [ShapeConsumerSupport] for why this should not happen for a `"gregtech"`-owned
    /// shape in practice.
    static void delegate(Shape shape, OrePrefixes prefix, IOreRecipeRegistrator registrator) {
        if (shape == null) return;
        MaterialLibAPI.registerShapeConsumer("gregtech", shape, (s, material) -> {
            Materials legacyMaterial = MU.materialOf(material);
            if (legacyMaterial == null) return;
            ItemStack stack = MaterialLibAPI.getStack(material, s, 1);
            if (stack == null) return;
            registrator
                .registerOre(prefix, legacyMaterial, prefix.getName() + legacyMaterial.mName, "materiallib", stack);
        });
    }
}
