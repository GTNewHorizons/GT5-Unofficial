package gtPlusPlus.core.item.general.books;

import static gtPlusPlus.core.handler.BookHandler.mBookMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.handler.BookHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.NBTUtils;

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
			
			/*bookstack = Utils.getWrittenBook(
					bookstack,
					i,
					mBookMap.get(i).mMapping, 
					mBookMap.get(i).mTitle, 
					mBookMap.get(i).mAuthor, 
					mBookMap.get(i).mPages);*/

			NBTUtils.createIntegerTagCompound(bookstack, "stats", "mMeta", i);

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
			return EnumChatFormatting.ITALIC+""+mBookMap.get(tItem.getItemDamage()).mTitle;
		}
		//NBTUtils.tryIterateNBTData(tItem);
		return "GT++ Storybook";
	}

	@Override
	public void addInformation(ItemStack tItem, EntityPlayer player, List list, boolean bool) {
		// TODO Auto-generated method stub
		if (NBTUtils.hasKey(tItem, "author")){
			list.add(EnumChatFormatting.GRAY+"Author: "+NBTUtils.getString(tItem, "author"));
		}
		else if (mBookMap.get(tItem.getItemDamage()).mAuthor != null){
			list.add(EnumChatFormatting.WHITE+"Author: "+mBookMap.get(tItem.getItemDamage()).mAuthor);
		}
		if (NBTUtils.hasKey(tItem, "title")){
			list.add(EnumChatFormatting.GRAY+"Pages: "+NBTUtils.getString(tItem, "pages"));
		}
		else if (mBookMap.get(tItem.getItemDamage()).mPages != null){
			list.add(EnumChatFormatting.WHITE+"Pages: "+mBookMap.get(tItem.getItemDamage()).mPages.length);
		}
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

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
		//player.displayGUIBook(item);
		int i = item.getItemDamage();
		ItemStack bookstack = GT_Utility.getWrittenBook(				
				mBookMap.get(i).mMapping, 
				mBookMap.get(i).mTitle, 
				mBookMap.get(i).mAuthor, 
				mBookMap.get(i).mPages);
		
		if (player.worldObj.isRemote){
			try {
				Class<?> clazz = Class.forName("net.minecraft.client.gui.GuiScreenBook");
				Constructor<?> ctor = clazz.getConstructor(EntityPlayer.class, ItemStack.class, boolean.class);
				Object object = ctor.newInstance(new Object[] { player, bookstack, false });
				Minecraft.getMinecraft().displayGuiScreen((GuiScreen) object);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Minecraft.getMinecraft().displayGuiScreen(new GuiScreenBook(player, bookstack, false));
		}
        return item;
	}
	
}
