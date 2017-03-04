package gtPlusPlus.core.creative.tabs;

import gtPlusPlus.core.item.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MiscUtilCreativeTabMachines extends CreativeTabs {


	public MiscUtilCreativeTabMachines(final String lable) {
		super(lable);
	}

	@Override
	public Item getTabIconItem() {
		return ModItems.itemPLACEHOLDER_Circuit;
	}

}
