package gregtech.loaders.shapeconsumers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.OreShapes;

/// Clamps MaterialLib's block-kind shapes (`block`, `ore`, `oreSmall`, the only cutover shapes backed by an
/// [ItemBlock]) to their prefix's default stack size. `gregtech.loaders.oreprocessing.ProcessingAll` applies
/// the same clamp to foreign mods' block-form items through the oredict path, which does not reach
/// MaterialLib's own items.
///
/// The clamp mutates the single [Item] backing a whole shape rather than one material's stack, so the
/// per-material consumer re-applies it once per material.
public final class ConsumerStackSizeClamp {

    private ConsumerStackSizeClamp() {}

    static void register() {
        clamp(BlockShapes.block, OrePrefixes.block);
        clamp(OreShapes.ore, OrePrefixes.ore);
        clamp(OreShapes.oreSmall, OrePrefixes.oreSmall);
    }

    private static void clamp(Shape shape, OrePrefixes prefix) {
        if (shape == null) return;
        MaterialLibAPI.registerShapeConsumer("gregtech", shape, (s, material) -> {
            ItemStack stack = MaterialLibAPI.getStack(material, s, 1);
            if (stack == null) return;
            Item item = stack.getItem();
            if (item instanceof ItemBlock && prefix.getDefaultStackSize() < item.getItemStackLimit(stack)) {
                item.setMaxStackSize(prefix.getDefaultStackSize());
            }
        });
    }
}
