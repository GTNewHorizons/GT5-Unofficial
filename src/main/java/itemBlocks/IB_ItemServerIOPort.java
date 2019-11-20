package itemBlocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class IB_ItemServerIOPort extends ItemBlock {
	
	public IB_ItemServerIOPort(Block block) {
		super(block);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advancedTooltips) {
		lines.add("I/O Port for interfacing with an Item Server.");
	}
}
