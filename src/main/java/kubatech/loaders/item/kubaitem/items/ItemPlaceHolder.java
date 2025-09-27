package kubatech.loaders.item.kubaitem.items;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import kubatech.api.enums.ItemList;
import kubatech.loaders.item.kubaitem.ItemProxy;

public class ItemPlaceHolder extends ItemProxy {

    public ItemPlaceHolder() {
        super("placeholder_item");
    }

    @Override
    public String getDisplayName(ItemStack stack) {
        if (stack.hasTagCompound()) {
            String modID = stack.getTagCompound()
                .getString("modID");
            String itemName = stack.getTagCompound()
                .getString("itemName");
            int meta = stack.getTagCompound()
                .getInteger("meta");
            if (modID == null || modID.isEmpty() || itemName == null || itemName.isEmpty()) {
                return "Invalid Item";
            }
            if (meta != 0) {
                return "[PLACEHOLDER] " + modID + ":" + itemName + " (meta: " + meta + ")";
            }
            return "[PLACEHOLDER] " + modID + ":" + itemName;
        }
        return "Invalid Item";
    }

    public static ItemStack getItem(String modID, String itemName, int meta) {
        ItemStack stack = ItemList.PlaceHolderItem.get(1L);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("modID", modID);
        tag.setString("itemName", itemName);
        tag.setInteger("meta", meta);
        stack.setTagCompound(tag);
        return stack;
    }
}
