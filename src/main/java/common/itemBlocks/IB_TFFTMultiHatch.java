package common.itemBlocks;

import java.util.List;

import common.tileentities.TE_TFFTMultiHatch;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

@Deprecated
public class IB_TFFTMultiHatch extends ItemBlock {
	
	public IB_TFFTMultiHatch(Block block) {
		super(block);
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}

	@Override
	public boolean getHasSubtypes() {
		return true;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + stack.getItemDamage();
	}

	@SuppressWarnings({"unchecked"})
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advancedTooltips) {
		lines.add(StatCollector.translateToLocal("tile.kekztech_tfftmultihatch_block.0.desc"));
	}
}
