package gtPlusPlus.api.objects.minecraft;

import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ThaumcraftSmeltingCache {

	HashMap<String, Boolean> mInternalCache = new HashMap<String, Boolean>();
	
	public void addItemToCache(ItemStack aStack, Boolean aSmelting) {
		String aKey = getUniqueKey(aStack);
		mInternalCache.put(aKey, aSmelting);
	}
	
	public int canSmelt(ItemStack aStack) {
		String aKey = getUniqueKey(aStack);
		Boolean aCanSmeltValue = mInternalCache.get(aKey);
		if (aCanSmeltValue != null) {
			if (aCanSmeltValue) {
				return 1;
			}
			else {
				return 0;
			}
		}
		return -1;
	}

	public static final String getUniqueKey(ItemStack aStack) {
		Item aItem = aStack.getItem();
		int aDamage = aStack.getItemDamage();
		NBTTagCompound aNBT = (aStack.getTagCompound() != null ? aStack.getTagCompound() : new NBTTagCompound());
		return ""+Item.getIdFromItem(aItem)+""+aDamage+""+aNBT.getId();
	}

}
