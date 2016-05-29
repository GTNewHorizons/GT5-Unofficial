package miscutil.core.item.base;

import miscutil.core.lib.CORE;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

@Deprecated
public class BaseItemGeneric extends Item
{
	public BaseItemGeneric(String s, CreativeTabs c, int stackSize, int maxDmg)
	{
		setUnlocalizedName(CORE.MODID + "_" + s);
		setTextureName(CORE.MODID + ":" + s);
		setCreativeTab(c);
		setMaxStackSize(stackSize);
		setMaxDamage(maxDmg);
	}
}