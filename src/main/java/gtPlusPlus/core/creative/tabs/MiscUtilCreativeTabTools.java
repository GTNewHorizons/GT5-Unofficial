package gtPlusPlus.core.creative.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import gtPlusPlus.core.item.ModItems;

public class MiscUtilCreativeTabTools extends CreativeTabs {

	public MiscUtilCreativeTabTools(final String lable) {
		super(lable);
	}

	@Override
	public Item getTabIconItem() {
		return Items.diamond_pickaxe;
	}

}
