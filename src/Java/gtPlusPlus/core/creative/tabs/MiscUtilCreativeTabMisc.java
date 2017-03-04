package gtPlusPlus.core.creative.tabs;

import gtPlusPlus.core.item.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MiscUtilCreativeTabMisc extends CreativeTabs {

	public MiscUtilCreativeTabMisc(final String lable) {
		super(lable);
	}

	@Override
	public Item getTabIconItem() {
		return ModItems.itemFoodCurriedSausages;
	}

}
