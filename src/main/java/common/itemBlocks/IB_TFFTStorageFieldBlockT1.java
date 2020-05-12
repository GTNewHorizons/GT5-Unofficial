package common.itemBlocks;

import java.util.List;

import common.blocks.Block_TFFTStorageFieldBlockT1;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class IB_TFFTStorageFieldBlockT1 extends ItemBlock {

	public IB_TFFTStorageFieldBlockT1(Block block) {
		super(block);
	}
	
	@SuppressWarnings({"unchecked"})
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advancedTooltips) {
		lines.add(StatCollector.translateToLocal("tile.kekztech_tfftstoragefieldblock1_block.0.desc"));
		lines.add("Capacity: " + Block_TFFTStorageFieldBlockT1.getCapacity() + "L");
		lines.add("Power Draw: +0.5EU/t");
	}
	
}
