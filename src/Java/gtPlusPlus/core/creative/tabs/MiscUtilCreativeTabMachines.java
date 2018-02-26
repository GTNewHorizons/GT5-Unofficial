package gtPlusPlus.core.creative.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import gtPlusPlus.core.item.ModItems;

public class MiscUtilCreativeTabMachines extends CreativeTabs {


	public MiscUtilCreativeTabMachines(final String lable) {
		super(lable);
	}

	@Override
	public Item getTabIconItem() {
		return ModItems.itemPLACEHOLDER_Circuit;
	}

}
