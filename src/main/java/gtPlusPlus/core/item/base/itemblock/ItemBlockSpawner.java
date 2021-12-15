package gtPlusPlus.core.item.base.itemblock;

import java.util.List;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.EntityRegistry.EntityRegistration;
import gtPlusPlus.plugin.villagers.tile.TileEntityGenericSpawner;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class ItemBlockSpawner extends ItemBlockMeta{
	
	private final Block mBlock;
	
	public ItemBlockSpawner(Block aBlock) {
		super(aBlock);
		mBlock = aBlock;
	}

	@Override
	public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aBool) {
		if (mBlock != null) {
			int x = this.getMetadata(aStack.getItemDamage());
			if (x >= 0) {
				try {
					EntityRegistration x1 = EntityRegistry.instance().lookupModSpawn(TileEntityGenericSpawner.mSpawners.get(x), false);
					if (x1 != null) {
						aList.add(EnumChatFormatting.RED+x1.getEntityName());
					}					
				}
				catch (Throwable t) {}
			}			
		}
		super.addInformation(aStack, aPlayer, aList, aBool);
	}

}
