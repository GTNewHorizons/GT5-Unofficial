package itemBlocks;

import java.util.List;

import common.blocks.Block_TFFTStorageFieldBlockT3;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class IB_TFFTStorageFieldBlockT3 extends ItemBlock {

	public IB_TFFTStorageFieldBlockT3(Block block) {
		super(block);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advancedTooltips) {
		lines.add("This is not a fluid tank");
		lines.add("Capacity: " + Block_TFFTStorageFieldBlockT3.getCapacity() + "L");
		lines.add("Power Draw: 2EU/t");
	}
	
}
