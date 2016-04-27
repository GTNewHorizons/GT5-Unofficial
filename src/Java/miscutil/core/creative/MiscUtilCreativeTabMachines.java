package miscutil.core.creative;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class MiscUtilCreativeTabMachines extends CreativeTabs {

	public MiscUtilCreativeTabMachines(String lable) {
		super(lable);
	}

	@Override
	public Item getTabIconItem() {
		return Items.experience_bottle;
	}

}
