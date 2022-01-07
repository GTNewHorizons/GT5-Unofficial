package gtPlusPlus.xmod.ic2.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.CoreItem;

public class IC2_ItemGradual
extends CoreItem
{
	public IC2_ItemGradual(final String internalName)
	{
		super(internalName, AddToCreativeTab.tabMachines, 1, 10000, "", EnumRarity.uncommon);
		this.setNoRepair();
	}

	@Override
	public boolean isDamaged(final ItemStack stack)
	{
		return this.getDamage(stack) > 1;
	}

	@Override
	public boolean showDurabilityBar(final ItemStack stack)
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(final Item item, final CreativeTabs tabs, final List itemList)
	{
		itemList.add(new ItemStack(this, 1, 1));
	}
}
