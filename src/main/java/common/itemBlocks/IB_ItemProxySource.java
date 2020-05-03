package common.itemBlocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class IB_ItemProxySource extends ItemBlock {
	
	public IB_ItemProxySource(Block block) {
		super(block);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advancedTooltips) {
		lines.add("Point to an inventory to act as source for the item proxy network.");
		lines.add("Insert an Integrated Circuit to set the network channel.");
		lines.add("Only one source can use one channel on the same network.");
	}
}
