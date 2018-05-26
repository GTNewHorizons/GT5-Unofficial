package gtPlusPlus.core.item.general;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import gtPlusPlus.core.item.base.BaseItemBurnable;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.NBTUtils;

public class ItemGiantEgg extends BaseItemBurnable {

	public ItemGiantEgg(String unlocalizedName, String displayName, CreativeTabs creativeTab, int stackSize, int maxDmg,
			String description, String oredictName, int burnTime, int meta) {
		super(unlocalizedName, displayName, creativeTab, stackSize, maxDmg, description, oredictName, burnTime, meta);
	}

	@Override
	public String getItemStackDisplayName(ItemStack aStack) {
		String localName = super.getItemStackDisplayName(aStack);
		int size = 1;
		if (NBTUtils.hasKey(aStack, "size")) {
			size = NBTUtils.getInteger(aStack, "size");
		}
		else {
			NBTUtils.setInteger(aStack, "size", MathUtils.randInt(1, 8));
			size = NBTUtils.getInteger(aStack, "size");
		}
		return ""+size+localName;
	}

}
