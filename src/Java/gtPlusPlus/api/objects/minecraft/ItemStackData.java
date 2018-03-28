package gtPlusPlus.api.objects.minecraft;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gtPlusPlus.core.util.minecraft.ItemUtils;

public class ItemStackData {

	protected final Item mItem;
	protected final int mDamage;
	protected final int mStackSize;
	protected final NBTTagCompound mNBT;
	protected final String mUniqueDataTag;
	
	public ItemStackData (ItemStack aStack) {
		mItem = aStack.getItem();
		mDamage = aStack.getItemDamage();
		mStackSize = aStack.stackSize;
		mNBT = (aStack.getTagCompound() != null ? aStack.getTagCompound() : new NBTTagCompound());
		mUniqueDataTag = ""+Item.getIdFromItem(mItem)+""+mDamage+""+mStackSize+""+mNBT.getId();
	}
	
	public String getUniqueDataIdentifier() {
		return this.mUniqueDataTag;
	}
	
	public ItemStack getStack() {
		return ItemUtils.simpleMetaStack(mItem, mDamage, mStackSize);
	}
	
}
