package gtPlusPlus.api.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public interface IEntityCatcher {

	public boolean hasEntity(ItemStack aStack);
	
	public Entity getStoredEntity(ItemStack aStack);
	
	public boolean setStoredEntity(ItemStack aStack, Entity aEntity);
	
	public Class getStoredEntityClass(ItemStack aStack);
	
}
