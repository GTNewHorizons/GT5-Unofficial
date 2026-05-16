package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import gregtech.api.items.MetaBaseItem;
import gregtech.api.util.GTUtility;

public class BehaviourPrintedPages extends BehaviourNone {

    public static String getTitle(ItemStack aStack) {
        return ItemStackNBT.getString(aStack, "title");
    }

    public static String getAuthor(ItemStack aStack) {
        return ItemStackNBT.getString(aStack, "author");
    }

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        if (GTUtility.isStringValid(getTitle(aStack))) {
            aList.add(getTitle(aStack));
        }
        if (GTUtility.isStringValid(getAuthor(aStack))) {
            aList.add("by " + getAuthor(aStack));
        }
        return aList;
    }
}
