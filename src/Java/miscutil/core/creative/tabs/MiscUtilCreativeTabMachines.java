package miscutil.core.creative.tabs;

import miscutil.core.item.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MiscUtilCreativeTabMachines extends CreativeTabs {


	public MiscUtilCreativeTabMachines(String lable) {
		super(lable);
	}

	@Override
	public Item getTabIconItem() {		
		return ModItems.itemPLACEHOLDER_Circuit;
	}

}
