package gtPlusPlus.core.util.nbt;

import static gtPlusPlus.core.item.ModItems.ZZZ_Empty;

import java.util.Map;

import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
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

	public static ItemStack[] readItemsFromNBT(ItemStack itemstack) {
		NBTTagCompound tNBT = getNBT(itemstack);
		final NBTTagList list = tNBT.getTagList("Items", 10);
		ItemStack inventory[] = new ItemStack[list.tagCount()];
		for (int i = 0; i < list.tagCount(); i++) {
			final NBTTagCompound data = list.getCompoundTagAt(i);
			final int slot = data.getInteger("Slot");
			if ((slot >= 0) && (slot < list.tagCount())) {
				if (ItemStack.loadItemStackFromNBT(data) == ItemUtils.getSimpleStack(ZZZ_Empty)) {
					inventory[slot] = null;
				} else {
					inventory[slot] = ItemStack.loadItemStackFromNBT(data);
				}

			}
		}
		return inventory;
	}

	public static ItemStack[] readItemsFromNBT(ItemStack itemstack, String customkey) {
		NBTTagCompound tNBT = getNBT(itemstack);
		final NBTTagList list = tNBT.getTagList(customkey, 10);
		ItemStack inventory[] = new ItemStack[list.tagCount()];
		for (int i = 0; i < list.tagCount(); i++) {
			final NBTTagCompound data = list.getCompoundTagAt(i);
			final int slot = data.getInteger("Slot");
			if ((slot >= 0) && (slot < list.tagCount())) {
				if (ItemStack.loadItemStackFromNBT(data) == ItemUtils.getSimpleStack(ZZZ_Empty)) {
					inventory[slot] = null;
				} else {
					inventory[slot] = ItemStack.loadItemStackFromNBT(data);
				}

			}
		}
		return inventory;
	}

	public static ItemStack writeItemsToNBT(ItemStack itemstack, ItemStack[] stored) {
		NBTTagCompound tNBT = getNBT(itemstack);
		final NBTTagList list = new NBTTagList();
		for (int i = 0; i < stored.length; i++) {
			final ItemStack stack = stored[i];
			if (stack != null) {
				final NBTTagCompound data = new NBTTagCompound();
				stack.writeToNBT(data);
				data.setInteger("Slot", i);
				list.appendTag(data);
			} else {
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

	public static ItemStack writeItemsToNBT(ItemStack itemstack, ItemStack[] stored, String customkey) {
		NBTTagCompound tNBT = getNBT(itemstack);
		final NBTTagList list = new NBTTagList();
		for (int i = 0; i < stored.length; i++) {
			final ItemStack stack = stored[i];
			if (stack != null) {
				final NBTTagCompound data = new NBTTagCompound();
				stack.writeToNBT(data);
				data.setInteger("Slot", i);
				list.appendTag(data);
			}
		}
		tNBT.setTag(customkey, list);
		itemstack.setTagCompound(tNBT);
		return itemstack;
	}

	public static ItemStack writeItemsToGtCraftingComponents(ItemStack rStack, ItemStack[] stored, boolean copyTags) {

		if (copyTags) {
			for (int i = 0; i < stored.length; i++) {
				if (stored[i] != null && stored[i].hasTagCompound()) {
					rStack.setTagCompound((NBTTagCompound) stored[i].getTagCompound().copy());
					break;
				}
			}
		}

		NBTTagCompound rNBT = rStack.getTagCompound(), tNBT = new NBTTagCompound();
		if (rNBT == null)
			rNBT = new NBTTagCompound();
		for (int i = 0; i < 9; i++) {
			ItemStack tStack = stored[i];
			if (tStack != null && GT_Utility.getContainerItem(tStack, true) == null
					&& !(tStack.getItem() instanceof GT_MetaGenerated_Tool)) {
				tStack = GT_Utility.copyAmount(1, tStack);
				if (GT_Utility.isStackValid(tStack)) {
					GT_ModHandler.dischargeElectricItem(tStack, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false,
							true);
					tNBT.setTag("Ingredient." + i, tStack.writeToNBT(new NBTTagCompound()));
				}
			}
		}
		rNBT.setTag("GT.CraftingComponents", tNBT);
		rStack.setTagCompound(rNBT);
		return rStack;
	}

	public static void setBoolean(ItemStack aStack, String aTag, boolean aBoolean) {
		NBTTagCompound tNBT = getNBT(aStack);
		tNBT.setBoolean(aTag, aBoolean);
		GT_Utility.ItemNBT.setNBT(aStack, tNBT);
	}

	public static boolean getBoolean(ItemStack aStack, String aTag) {
		NBTTagCompound tNBT = getNBT(aStack);
		return tNBT.getBoolean(aTag);
	}

	public static void setInteger(ItemStack aStack, String aTag, int aInt) {
		NBTTagCompound tNBT = getNBT(aStack);
		tNBT.setInteger(aTag, aInt);
		GT_Utility.ItemNBT.setNBT(aStack, tNBT);
	}

	public static int getInteger(ItemStack aStack, String aTag) {
		NBTTagCompound tNBT = getNBT(aStack);
		return tNBT.getInteger(aTag);
	}

	public static void setLong(ItemStack aStack, String aTag, long aInt) {
		NBTTagCompound tNBT = getNBT(aStack);
		tNBT.setLong(aTag, aInt);
		GT_Utility.ItemNBT.setNBT(aStack, tNBT);
	}

	public static long getLong(ItemStack aStack, String aTag) {
		NBTTagCompound tNBT = getNBT(aStack);
		return tNBT.getLong(aTag);
	}

	public static void setFloat(ItemStack aStack, String aTag, float aInt) {
		NBTTagCompound tNBT = getNBT(aStack);
		tNBT.setFloat(aTag, aInt);
		GT_Utility.ItemNBT.setNBT(aStack, tNBT);
	}

	public static float getFloat(ItemStack aStack, String aTag) {
		NBTTagCompound tNBT = getNBT(aStack);
		return tNBT.getFloat(aTag);
	}

	public static void setString(ItemStack aStack, String aTag, String aString) {
		NBTTagCompound tNBT = getNBT(aStack);
		tNBT.setString(aTag, aString);
		GT_Utility.ItemNBT.setNBT(aStack, tNBT);
	}

	public static String getString(ItemStack aStack, String aTag) {
		NBTTagCompound tNBT = getNBT(aStack);
		return tNBT.getString(aTag);
	}

	public static boolean doesStringExist(ItemStack aStack, String aTag) {
		NBTTagCompound tNBT = getNBT(aStack);
		return tNBT.hasKey(aTag);
	}

	public static boolean tryIterateNBTData(ItemStack aStack) {
		try {
			NBTTagCompound aNBT = NBTUtils.getNBT(aStack);
			if (aNBT != null) {
				if (!aNBT.hasNoTags()) {
					int tagCount = 0;
					Map<?, ?> mInternalMap = ReflectionUtils.getField(aNBT, "tagMap");

					if (mInternalMap != null) {
						// mInternalMap.forEach((k, v) -> Utils.LOG_INFO("Key: "
						// + k + ": Value: " + v));
						for (Map.Entry<?, ?> e : mInternalMap.entrySet()) {
							Utils.LOG_INFO("Key: " + e.getKey().toString() + " | Value: " + e.getValue().toString());
						}
					} else {
						Utils.LOG_INFO("Data map reflected from NBTTagCompound was not valid.");
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
			return true;
		} catch (Throwable t) {
			return false;
		}
	}

	// Botania soulbind handling
	public static boolean setBotanicaSoulboundOwner(ItemStack aStack, String aName) {
		final String TAG_SOULBIND = "soulbind";
		NBTTagCompound tNBT = getNBT(aStack);
		tNBT.setString(TAG_SOULBIND, aName);
		GT_Utility.ItemNBT.setNBT(aStack, tNBT);
		if (NBTUtils.doesStringExist(aStack, TAG_SOULBIND)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getBotanicaSoulboundOwner(ItemStack aStack) {
		final String TAG_SOULBIND = "soulbind";
		NBTTagCompound tNBT = getNBT(aStack);
		return tNBT.getString(TAG_SOULBIND);
	}

	public static boolean hasKey(ItemStack stack, String key){
		if (stack.hasTagCompound())				{
			final NBTTagCompound itemData = stack.getTagCompound();
			if (itemData.hasKey(key)){
				return true;
			}
		}
		return false;
	}

}
