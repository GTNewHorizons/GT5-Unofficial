package gtPlusPlus.core.creative.tabs;

import gtPlusPlus.core.item.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MiscUtilCreativeTabTools extends CreativeTabs {

	public MiscUtilCreativeTabTools(String lable) {
		super(lable);
	}

	@Override
	public Item getTabIconItem() {
		return ModItems.itemStaballoyAxe;
	}

}
