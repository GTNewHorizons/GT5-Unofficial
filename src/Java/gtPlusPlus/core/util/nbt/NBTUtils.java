package gtPlusPlus.core.util.nbt;

import static gtPlusPlus.core.item.ModItems.ZZZ_Empty;

import java.util.Map;

import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.entity.Entity;
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
					Map<?, ?> mInternalMap = ReflectionUtils.getField(aNBT, "tagMap");
					if (mInternalMap != null) {
						for (Map.Entry<?, ?> e : mInternalMap.entrySet()) {
							Utils.LOG_INFO("Key: " + e.getKey().toString() + " | Value: " + e.getValue());
						}
						return true;
					} else {
						Utils.LOG_INFO("Data map reflected from NBTTagCompound was not valid.");
						return false;
					}
				} 
			} 
		} catch (Throwable t) {}
		return false;
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

	public static boolean hasKey(ItemStack stack, String key) {
		final NBTTagCompound itemData = getNBT(stack);
		if (itemData.hasKey(key)) {
			return true;
		}
		return false;
	}

	public static boolean createIntegerTagCompound(ItemStack rStack, String tagName, String keyName, int keyValue) {
		final NBTTagCompound tagMain = getNBT(rStack);
		final NBTTagCompound tagNBT = new NBTTagCompound();
		tagNBT.setInteger(keyName, keyValue);
		tagMain.setTag(tagName, tagNBT);
		rStack.setTagCompound(tagMain);
		return true;
	}

	public static boolean createLongTagCompound(ItemStack rStack, String tagName, String keyName, long keyValue) {
		final NBTTagCompound tagMain = getNBT(rStack);
		final NBTTagCompound tagNBT = new NBTTagCompound();
		tagNBT.setLong(keyName, keyValue);
		tagMain.setTag(tagName, tagNBT);
		rStack.setTagCompound(tagMain);
		return true;
	}

	public static boolean createStringTagCompound(ItemStack rStack, String tagName, String keyName, String keyValue) {
		final NBTTagCompound tagMain = getNBT(rStack);
		final NBTTagCompound tagNBT = new NBTTagCompound();
		tagNBT.setString(keyName, keyValue);
		tagMain.setTag(tagName, tagNBT);
		rStack.setTagCompound(tagMain);
		return true;
	}

	public static boolean createFloatTagCompound(ItemStack rStack, String tagName, String keyName, float keyValue) {
		final NBTTagCompound tagMain = getNBT(rStack);
		final NBTTagCompound tagNBT = new NBTTagCompound();
		tagNBT.setFloat(keyName, keyValue);
		tagMain.setTag(tagName, tagNBT);
		rStack.setTagCompound(tagMain);
		return true;
	}

	public static boolean createDoubleTagCompound(ItemStack rStack, String tagName, String keyName, double keyValue) {
		final NBTTagCompound tagMain = getNBT(rStack);
		final NBTTagCompound tagNBT = new NBTTagCompound();
		tagNBT.setDouble(keyName, keyValue);
		tagMain.setTag(tagName, tagNBT);
		rStack.setTagCompound(tagMain);
		return true;
	}

	public static boolean createBooleanTagCompound(ItemStack rStack, String tagName, String keyName, boolean keyValue) {
		final NBTTagCompound tagMain = getNBT(rStack);
		final NBTTagCompound tagNBT = new NBTTagCompound();
		tagNBT.setBoolean(keyName, keyValue);
		tagMain.setTag(tagName, tagNBT);
		rStack.setTagCompound(tagMain);
		return true;
	}

	public static boolean createTagCompound(ItemStack rStack, String tagName, NBTTagCompound keyValue) {
		final NBTTagCompound tagMain = getNBT(rStack);
		NBTTagCompound tagNBT = keyValue;
		tagMain.setTag(tagName, tagNBT);
		rStack.setTagCompound(tagMain);
		return true;
	}

	public static int getIntegerTagCompound(ItemStack aStack, String tagName, String keyName) {
		NBTTagCompound aNBT = getNBT(aStack);
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag(tagName);
			if (aNBT != null) {
				return aNBT.getInteger(keyName);
			}
		}
		return 0;
	}

	public static long getLongTagCompound(ItemStack aStack, String tagName, String keyName) {
		NBTTagCompound aNBT = getNBT(aStack);
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag(tagName);
			if (aNBT != null) {
				return aNBT.getLong(keyName);
			}
		}
		return 0L;
	}

	public static String getStringTagCompound(ItemStack aStack, String tagName, String keyName) {
		NBTTagCompound aNBT = getNBT(aStack);
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag(tagName);
			if (aNBT != null) {
				return aNBT.getString(keyName);
			}
		}
		return null;
	}

	public static float getFloatTagCompound(ItemStack aStack, String tagName, String keyName) {
		NBTTagCompound aNBT = getNBT(aStack);
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag(tagName);
			if (aNBT != null) {
				return aNBT.getFloat(keyName);
			}
		}
		return 0;
	}

	public static double getDoubleTagCompound(ItemStack aStack, String tagName, String keyName) {
		NBTTagCompound aNBT = getNBT(aStack);
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag(tagName);
			if (aNBT != null) {
				return aNBT.getDouble(keyName);
			}
		}
		return 0;
	}

	public static boolean getBooleanTagCompound(ItemStack aStack, String tagName, String keyName) {
		NBTTagCompound aNBT = getNBT(aStack);
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag(tagName);
			if (aNBT != null) {
				return aNBT.getBoolean(keyName);
			}
		}
		return false;
	}

	public static NBTTagCompound getTagCompound(ItemStack aStack, String tagName) {
		NBTTagCompound aNBT = getNBT(aStack);
		if (aNBT != null && hasKey(aStack, tagName)) {
			aNBT = aNBT.getCompoundTag(tagName);
			if (aNBT != null) {
				return aNBT;
			}
		}
		return null;
	}

	public static boolean hasKeyInTagCompound(ItemStack stack, String tag, String key) {
		NBTTagCompound aNBT = stack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag(tag);
			if (aNBT.hasKey(key)) {
				return true;
			}
		}
		return false;
	}

	public static boolean tryCloneTagCompoundDataIntoSubTag(ItemStack aStack, NBTTagCompound aTagCompound) {
		try {
			NBTTagCompound aNBT = aTagCompound;
			if (aNBT != null) {
				if (!aNBT.hasNoTags()) {
					Map<?, ?> mInternalMap = ReflectionUtils.getField(aNBT, "tagMap");
					if (mInternalMap != null) {
						for (Map.Entry<?, ?> e : mInternalMap.entrySet()) {
							Utils.LOG_INFO("Key: " + e.getKey().toString() + " | Value: " + e.getValue().toString());
							if (e.getValue().getClass() == String.class){
								createStringTagCompound(aStack, "mEntityTag", (String) e.getKey(), (String) e.getValue());
							}
							else if (e.getValue().getClass() == Boolean.class || e.getValue().getClass() == boolean.class){
								createBooleanTagCompound(aStack, "mEntityTag", (String) e.getKey(), (Boolean) e.getValue());
							}
							else if (e.getValue().getClass() == Integer.class || e.getValue().getClass() == int.class){
								createIntegerTagCompound(aStack, "mEntityTag", (String) e.getKey(), (Integer) e.getValue());
							}
							else if (e.getValue().getClass() == Double.class || e.getValue().getClass() == double.class){
								createDoubleTagCompound(aStack, "mEntityTag", (String) e.getKey(), (Double) e.getValue());
							}
							else if (e.getValue().getClass() == Long.class || e.getValue().getClass() == long.class){
								createLongTagCompound(aStack, "mEntityTag", (String) e.getKey(), (Long) e.getValue());
							}
							else if (e.getValue().getClass() == Float.class || e.getValue().getClass() == float.class){
								createFloatTagCompound(aStack, "mEntityTag", (String) e.getKey(), (Float) e.getValue());
							}
							else {


							}						

						}
						return true;
					} 
				} 
			} 
			return false;
		} catch (Throwable t) {
			return false;
		}
	}
	
	public static NBTTagCompound getEntityCustomData(Entity aEntity){
		return ReflectionUtils.getField(aEntity, "customEntityData");
	}
	
	public static boolean setEntityCustomData(Entity aEntity, NBTTagCompound aTag){
		return ReflectionUtils.setField(aEntity, "customEntityData", aTag);
	}

}
