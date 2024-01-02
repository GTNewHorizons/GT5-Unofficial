package gregtech.api.modernmaterials.effects;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@FunctionalInterface
public interface IMaterialEffect {

    void apply(ItemStack itemStack, World world, Entity player, int slotIndex, boolean isCurrentItem);
}
