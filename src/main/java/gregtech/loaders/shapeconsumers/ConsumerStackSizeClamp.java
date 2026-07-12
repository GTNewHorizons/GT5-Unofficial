package gregtech.loaders.shapeconsumers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2BlockShapes;
import gregtech.api.enums.materials2.Materials2OreShapes;

/// Reproduces `gregtech.loaders.oreprocessing.ProcessingAll`'s stack-size clamp for MaterialLib's block-kind
/// shapes (`block`, `ore`, `oreSmall` -- the only cutover shapes backed by an [ItemBlock], since every
/// item-shape prefix's [com.ruling_0.materiallib.api.ShapeItem] is not one). `ProcessingAll` stays registered
/// on every legacy prefix and keeps clamping foreign mods' block-form items through the oredict path; this
/// reproduces its effect for MaterialLib's own items, which stop reaching that path once it is taught to skip
/// them (see `gregtech.common.OreDictEventContainer`).
///
/// Unlike the other `Consumer*` classes, this does not delegate to a `Processing*` instance: the clamp is a
/// one-time mutation of the shared [Item] backing a whole shape (every material's stack of a block-kind shape
/// is the same [Item], selected by metadata), so re-running it once per material is redundant but harmless --
/// exactly as it was under the legacy per-material oredict dispatch.
public final class ConsumerStackSizeClamp {

    private ConsumerStackSizeClamp() {}

    static void register() {
        clamp(Materials2BlockShapes.block, OrePrefixes.block);
        clamp(Materials2OreShapes.ore, OrePrefixes.ore);
        clamp(Materials2OreShapes.oreSmall, OrePrefixes.oreSmall);
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
