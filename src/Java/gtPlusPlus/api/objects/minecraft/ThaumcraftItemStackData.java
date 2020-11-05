package gtPlusPlus.api.objects.minecraft;

import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

public class ThaumcraftItemStackData {
	
	protected final Item mItem;
	protected final int mDamage;
	protected final int mStackSize;
	protected final NBTTagCompound mNBT;
	protected final String mUniqueDataTag;
	private final AspectList mAspectList;
	
	public ThaumcraftItemStackData (ItemStack aStack) {
		mItem = aStack.getItem();
		mDamage = aStack.getItemDamage();
		mStackSize = aStack.stackSize;
		mNBT = (aStack.getTagCompound() != null ? aStack.getTagCompound() : new NBTTagCompound());
		mUniqueDataTag = ""+Item.getIdFromItem(mItem)+""+mDamage+""+mNBT.getId();	
		mAspectList = ThaumcraftCraftingManager.getObjectTags(aStack);
	}
	
	public String getUniqueDataIdentifier() {
		return this.mUniqueDataTag;
	}
	
	public ItemStack getStack() {
		ItemStack aTemp =  ItemUtils.simpleMetaStack(mItem, mDamage, mStackSize);
		aTemp.setTagCompound(mNBT);
		return aTemp;
	}
	
	public AspectList getAspectList() {
		return mAspectList;
	}
	
	public boolean doesItemStackDataMatch(ItemStack aStack) {
		if (aStack == null) {
			return false;
		}
		Item aItem = aStack.getItem();
		int aMeta = aStack.getItemDamage();
		if (aItem != null) {
			if (aItem == mItem && aMeta == mDamage) {
				return true;
			}
		}		
		return false;
	}
	
	
}
