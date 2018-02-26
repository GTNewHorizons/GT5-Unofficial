package gtPlusPlus.xmod.bop.creative;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import gtPlusPlus.core.item.ModItems;

public class MiscUtilsBOPTab extends CreativeTabs {

	public MiscUtilsBOPTab(final String lable) {
		super(lable);
	}

	@Override
	public Item getTabIconItem() {
		return ModItems.itemAlkalusDisk;
	}

}
