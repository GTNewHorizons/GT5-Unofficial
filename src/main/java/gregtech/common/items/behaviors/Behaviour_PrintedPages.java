package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.items.MetaBaseItem;
import gregtech.api.util.GT_Utility;

public class Behaviour_PrintedPages extends Behaviour_None {

    public static String getTitle(ItemStack aStack) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            return "";
        }
        return tNBT.getString("title");
    }

    public static String getAuthor(ItemStack aStack) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            return "";
        }
        return tNBT.getString("author");
    }

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        if (GT_Utility.isStringValid(getTitle(aStack))) {
            aList.add(getTitle(aStack));
        }
        if (GT_Utility.isStringValid(getAuthor(aStack))) {
            aList.add("by " + getAuthor(aStack));
        }
        return aList;
    }
}
