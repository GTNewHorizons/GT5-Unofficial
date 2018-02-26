package gtPlusPlus.core.creative.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import gtPlusPlus.core.item.ModItems;

public class MiscUtilCreativeTabMisc extends CreativeTabs {

	public MiscUtilCreativeTabMisc(final String lable) {
		super(lable);
	}

	@Override
	public Item getTabIconItem() {
		return ModItems.itemFoodCurriedSausages;
	}

}
