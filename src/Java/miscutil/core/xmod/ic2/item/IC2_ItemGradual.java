package miscutil.core.xmod.ic2.item;

import java.util.List;

import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.item.base.CoreItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
