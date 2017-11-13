package gtPlusPlus.core.item.general.books;

import java.util.List;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.nbt.NBTUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;

public class ItemBaseBook extends ItemWritableBook{

	public ItemBaseBook(){
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setMaxStackSize(1);
		this.setNoRepair();
		this.setMaxDamage(Short.MAX_VALUE);
		this.setTextureName(CORE.MODID+":"+"itemBook");
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < Utils.getBookCount(); i ++) {
			list.add(new ItemStack(item, 1, i));
		}
	}
	
	@Override
	public String getItemStackDisplayName(final ItemStack tItem) {
		if (NBTUtils.hasKey(tItem, "mMeta")){
			return NBTUtils.getBookTitle(tItem);
		}
		return "GT++ Storybook";
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
		// TODO Auto-generated method stub
		super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public int getDamage(ItemStack stack) {
		if (NBTUtils.hasKey(stack, "mMeta")){
			return NBTUtils.getInteger(stack, "mMeta");
		}
		return 0;
	}

	@Override
	public int getDisplayDamage(ItemStack stack) {
		return 0;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}

	@Override
	public void setDamage(ItemStack stack, int damage) {
		NBTUtils.setInteger(stack, "mMeta", damage);
	}

}
