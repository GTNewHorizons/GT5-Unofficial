package miscutil.core.creative.tabs;

import miscutil.core.block.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MiscUtilCreativeTabBlock extends CreativeTabs {

	public MiscUtilCreativeTabBlock(String lable) {
		super(lable);
	}

	@Override
	public Item getTabIconItem() {
		return Item.getItemFromBlock(ModBlocks.MatterFabricatorEffectBlock);
	}

}
