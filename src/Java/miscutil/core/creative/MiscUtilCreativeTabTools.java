package miscutil.core.creative;

import miscutil.core.item.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MiscUtilCreativeTabTools extends CreativeTabs {

	public MiscUtilCreativeTabTools(String lable) {
		super(lable);
	}

	@Override
	public Item getTabIconItem() {
		return ModItems.itemStaballoyPickaxe;
	}

}
