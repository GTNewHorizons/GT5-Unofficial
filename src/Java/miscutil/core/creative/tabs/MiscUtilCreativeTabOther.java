package miscutil.core.creative.tabs;

import miscutil.core.item.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MiscUtilCreativeTabOther extends CreativeTabs {

	public MiscUtilCreativeTabOther(String lable) {
		super(lable);
	}

	@Override
	public Item getTabIconItem() {
		return ModItems.itemHeliumBlob;
	}

}
