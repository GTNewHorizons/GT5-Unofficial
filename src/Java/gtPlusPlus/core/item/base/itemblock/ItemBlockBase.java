package gtPlusPlus.core.item.base.itemblock;

import gtPlusPlus.core.creative.AddToCreativeTab;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockBase extends ItemBlock {

	public ItemBlockBase(final Block block) {
		super(block);
		this.setCreativeTab(AddToCreativeTab.tabBlock);
	}

	@Override
	public int getColorFromItemStack(final ItemStack p_82790_1_, final int p_82790_2_) {

		return super.getColorFromItemStack(p_82790_1_, p_82790_2_);
	}


}