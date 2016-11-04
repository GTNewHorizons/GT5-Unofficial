package gtPlusPlus.core.item.base;

import java.util.List;

import gtPlusPlus.core.lib.CORE;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BaseItemGeneric extends Item {
	public BaseItemGeneric(final String unlocalizedName, final CreativeTabs c, final int stackSize, final int maxDmg) {
		this.setUnlocalizedName(CORE.MODID + "_" + unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setCreativeTab(c);
		this.setMaxStackSize(stackSize);
		this.setMaxDamage(maxDmg);
	}

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		super.addInformation(stack, aPlayer, list, bool);
	}
}