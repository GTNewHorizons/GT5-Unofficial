package gtPlusPlus.xmod.ic2.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.CoreItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;

public class IC2_ItemGradual
extends CoreItem
{
	public IC2_ItemGradual(String internalName)
	{
		super(internalName, AddToCreativeTab.tabMachines, 1, 10000, "", EnumRarity.uncommon);
		setNoRepair();
	}

	@Override
	public boolean isDamaged(ItemStack stack)
	{
		return getDamage(stack) > 1;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List itemList)
	{
		itemList.add(new ItemStack(this, 1, 1));
	}
}
