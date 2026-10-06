package gregtech.loaders.shapeconsumers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.OreShapes;

/// Clamps the `block`, `ore` and `oreSmall` items to their prefix's default stack size.
/// `gregtech.loaders.oreprocessing.ProcessingAll` applies the same clamp to other mods' blocks through the oredict
/// path.
///
/// The clamp mutates the [Item] shared by a whole shape, so each material's dispatch reapplies it.
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
