package gtPlusPlus.core.creative.tabs;

import gtPlusPlus.core.item.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MiscUtilCreativeTabOther extends CreativeTabs {

	public MiscUtilCreativeTabOther(String lable) {
		super(lable);
	}

	@Override
	public Item getTabIconItem() {
		return ModItems.backpack_Green;
	}

}
