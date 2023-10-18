package gtPlusPlus.core.util.minecraft;

import static gtPlusPlus.core.item.ModItems.ZZZ_Empty;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;

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

    public static ItemStack writeItemsToGtCraftingComponents(ItemStack rStack, ItemStack[] input, boolean copyTags) {
        try {
            ItemStack stored[] = new ItemStack[9];
            if (input.length != 9) {
                for (int e = 0; e < input.length; e++) {
                    if (input[e] != null) stored[e] = input[e];
                }
            }

            if (copyTags) {
                for (ItemStack itemStack : stored) {
                    if (itemStack != null && itemStack.hasTagCompound()) {
                        rStack.setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                        break;
                    }
                }
            }

            NBTTagCompound rNBT = rStack.getTagCompound(), tNBT = new NBTTagCompound();
            if (rNBT == null) rNBT = new NBTTagCompound();
            for (int i = 0; i < 9; i++) {
                ItemStack tStack = stored[i];
                if (tStack != null && GT_Utility.getContainerItem(tStack, true) == null
                        && !(tStack.getItem() instanceof GT_MetaGenerated_Tool)) {
                    tStack = GT_Utility.copyAmount(1, tStack);
                    if (GT_Utility.isStackValid(tStack)) {
                        GT_ModHandler
                                .dischargeElectricItem(tStack, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false, true);
                        tNBT.setTag("Ingredient." + i, tStack.writeToNBT(new NBTTagCompound()));
                    }
                }
            }
            rNBT.setTag("GT.CraftingComponents", tNBT);
            rStack.setTagCompound(rNBT);
        } catch (Throwable t) {
            t.printStackTrace();
        }
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

    public static void setString(ItemStack aStack, String aTag, String aString) {
        NBTTagCompound tNBT = getNBT(aStack);
        tNBT.setString(aTag, aString);
        GT_Utility.ItemNBT.setNBT(aStack, tNBT);
    }

    public static String getString(ItemStack aStack, String aTag) {
        NBTTagCompound tNBT = getNBT(aStack);
        return tNBT.getString(aTag);
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

    public static boolean hasTagCompound(ItemStack aStack) {
        return aStack.hasTagCompound();
    }

    public static void createEmptyTagCompound(ItemStack aStack) {
        if (!hasTagCompound(aStack)) {
            NBTTagCompound aTag = new NBTTagCompound();
            aStack.setTagCompound(aTag);
        }
    }
}
