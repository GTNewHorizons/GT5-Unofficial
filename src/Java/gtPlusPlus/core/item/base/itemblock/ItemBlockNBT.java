package gtPlusPlus.core.item.base.itemblock;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;

public class ItemBlockNBT extends ItemBlock {

	public ItemBlockNBT(final Block block, final CreativeTabs tab) {
		super(block);
		this.setCreativeTab(tab);
	}


}