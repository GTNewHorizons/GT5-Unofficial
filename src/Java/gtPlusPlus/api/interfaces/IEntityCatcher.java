package gtPlusPlus.api.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gtPlusPlus.api.objects.minecraft.BlockPos;

public interface IEntityCatcher {

	public boolean hasEntity(ItemStack aStack);
	
	public Entity getStoredEntity(World aWorld, ItemStack aStack);
	
	public boolean setStoredEntity(World aWorld, ItemStack aStack, Entity aEntity);
	
	public Class<? extends Entity> getStoredEntityClass(ItemStack aStack);
	
	public boolean spawnStoredEntity(World aWorld, ItemStack aStack, BlockPos aPos);
	
}
