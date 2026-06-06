package gtPlusPlus.core.util.minecraft;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTUtils {

    public static void createIntegerTagCompound(ItemStack rStack, String tagName, String keyName, int keyValue) {
        final NBTTagCompound tagMain = rStack.hasTagCompound() ? rStack.getTagCompound() : new NBTTagCompound();
        final NBTTagCompound tagNBT = new NBTTagCompound();
        tagNBT.setInteger(keyName, keyValue);
        tagMain.setTag(tagName, tagNBT);
        rStack.setTagCompound(tagMain);
    }
}
