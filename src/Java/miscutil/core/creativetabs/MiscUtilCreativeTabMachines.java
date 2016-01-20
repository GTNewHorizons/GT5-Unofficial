package miscutil.core.creativetabs;

import miscutil.core.block.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MiscUtilCreativeTabMachines extends CreativeTabs {

	public MiscUtilCreativeTabMachines(String lable) {
		super(lable);
	}

	@Override
	public Item getTabIconItem() {
		return Item.getItemFromBlock(ModBlocks.blockToolBuilder);
	}

}
