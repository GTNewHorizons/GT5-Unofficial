package gtPlusPlus.core.item.general;

import net.minecraft.item.ItemStack;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.CoreItem;

public class ItemEmpty extends CoreItem{

	public ItemEmpty() {
		super("item.empty", AddToCreativeTab.tabMisc);
	}

	@Override
	public String getItemStackDisplayName(ItemStack tItem) {
		return "Empty";
	}

	
	
}
