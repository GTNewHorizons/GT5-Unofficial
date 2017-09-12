package gtPlusPlus.core.util.nbt;

import static gtPlusPlus.core.item.ModItems.ZZZ_Empty;

import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTUtils {

	public static NBTTagCompound getNBT(ItemStack aStack) {
		NBTTagCompound rNBT = aStack.getTagCompound();
		return ((rNBT == null) ? new NBTTagCompound() : rNBT);
	}

	public static void setBookTitle(ItemStack aStack, String aTitle) {
		NBTTagCompound tNBT = getNBT(aStack);
		tNBT.setString("title", aTitle);
		GT_Utility.ItemNBT.setNBT(aStack, tNBT);
	}

	public static String getBookTitle(ItemStack aStack) {
		NBTTagCompound tNBT = getNBT(aStack);
		return tNBT.getString("title");
	}

	public static ItemStack[] readItemsFromNBT(ItemStack itemstack){
		NBTTagCompound tNBT = getNBT(itemstack);
		final NBTTagList list = tNBT.getTagList("Items", 10);
		ItemStack inventory[] = new ItemStack[list.tagCount()];
		for(int i = 0;i<list.tagCount();i++){
			final NBTTagCompound data = list.getCompoundTagAt(i);
			final int slot = data.getInteger("Slot");
			if((slot >= 0) && (slot < list.tagCount())){
				if (ItemStack.loadItemStackFromNBT(data) == ItemUtils.getSimpleStack(ZZZ_Empty)){
					inventory[slot] = null;
				}
				else {
					inventory[slot] = ItemStack.loadItemStackFromNBT(data);
				}

			}
		}
		return inventory;
	}

	public static ItemStack writeItemsToNBT(ItemStack itemstack, ItemStack[] stored){
		NBTTagCompound tNBT = getNBT(itemstack);
		final NBTTagList list = new NBTTagList();
		for(int i = 0;i<stored.length;i++){
			final ItemStack stack = stored[i];
			if(stack != null){
				final NBTTagCompound data = new NBTTagCompound();
				stack.writeToNBT(data);
				data.setInteger("Slot", i);
				list.appendTag(data);
			}
			else {
				final NBTTagCompound data = new NBTTagCompound();
				ItemStack nullstack = ItemUtils.getSimpleStack(ZZZ_Empty);
				nullstack.writeToNBT(data);
				data.setInteger("Slot", i);
				list.appendTag(data);
			}
		}
		tNBT.setTag("Items", list);
		itemstack.setTagCompound(tNBT);
		return itemstack;
	}


}
