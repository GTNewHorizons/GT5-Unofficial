package gtPlusPlus.core.util.minecraft;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictUtils {

    public static boolean containsValidEntries(String aOreName) {
        boolean a = OreDictionary.doesOreNameExist(aOreName);
        List<ItemStack> b = OreDictionary.getOres(aOreName, false);

        if (!a) {
            return false;
        } else {
            return b != null && !b.isEmpty();
        }
    }
}
