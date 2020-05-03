package common.itemBlocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class IB_ItemProxyEndpoint extends ItemBlock {
	
	public IB_ItemProxyEndpoint(Block block) {
		super(block);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advancedTooltips) {
		lines.add("Point the marked side to where you want to provide an inventory proxy to.");
		lines.add("Insert an Integrated Circuit to set the network channel.");
	}
}
