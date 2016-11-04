package gtPlusPlus.core.item.base;

import java.util.List;

import gtPlusPlus.core.lib.CORE;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BaseItemGeneric extends Item
{
	public BaseItemGeneric(String unlocalizedName, CreativeTabs c, int stackSize, int maxDmg)
	{
		setUnlocalizedName(CORE.MODID + "_" + unlocalizedName);
		setTextureName(CORE.MODID + ":" + unlocalizedName);
		setCreativeTab(c);
		setMaxStackSize(stackSize);
		setMaxDamage(maxDmg);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		super.addInformation(stack, aPlayer, list, bool);
	}	
}