package gtPlusPlus.core.creative.tabs;

import gtPlusPlus.core.block.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MiscUtilCreativeTabBlock extends CreativeTabs {

	public MiscUtilCreativeTabBlock(final String lable) {
		super(lable);
	}

	@Override
	public Item getTabIconItem() {
		return Item.getItemFromBlock(ModBlocks.MatterFabricatorEffectBlock);
	}

}
