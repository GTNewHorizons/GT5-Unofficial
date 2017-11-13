package gtPlusPlus.core.item.general.books;

import static gtPlusPlus.core.handler.BookHandler.mBookMap;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.handler.BookHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
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
		this.setTextureName(CORE.MODID+":"+"itemBook");
		this.setUnlocalizedName("itembookgt");
		GameRegistry.registerItem(this, "bookGT");
	
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < BookHandler.mBookMap.size(); i ++) {
			ItemStack bookstack = new ItemStack(item, 1, i);
			
			bookstack = Utils.getWrittenBook(
					bookstack,
					i,
					mBookMap.get(i).mMapping, 
					mBookMap.get(i).mTitle, 
					mBookMap.get(i).mAuthor, 
					mBookMap.get(i).mPages);

			//NBTUtils.createIntegerTagCompound(bookstack, "stats", "mMeta", i);

			GT_OreDictUnificator.registerOre("bookWritten", bookstack);
			GT_OreDictUnificator.registerOre("craftingBook", bookstack);
			list.add(bookstack);
		}
	}
	
	@Override
	public String getItemStackDisplayName(final ItemStack tItem) {
		if (NBTUtils.hasKey(tItem, "title")){
			return NBTUtils.getString(tItem, "title");
		}
		else if (tItem.getItemDamage() > -1 && tItem.getItemDamage() <= mBookMap.size()){
			return mBookMap.get(tItem.getItemDamage()).mTitle;
		}
		//NBTUtils.tryIterateNBTData(tItem);
		return "GT++ Storybook";
	}

	@Override
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
		// TODO Auto-generated method stub
		//super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}

	/*@Override
	public int getDamage(ItemStack stack) {
		try {
		return NBTUtils.getIntegerTagCompound(stack, "stats", "mMeta");
		}
		catch (Throwable t) {
			return 0;
		}
	}

	@Override
	public void setDamage(ItemStack stack, int damage) {
		NBTUtils.createIntegerTagCompound(stack, "stats", "mMeta", damage);
	}*/
	
}
