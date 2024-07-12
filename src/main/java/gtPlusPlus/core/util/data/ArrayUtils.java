package gtPlusPlus.core.util.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;

public class ArrayUtils {

    public static <V> V[] insertElementAtIndex(V[] aArray, int aIndex, V aObjectToInsert) {
        V[] newArray = Arrays.copyOf(aArray, aArray.length + 1);
        for (int i = 0; i < aIndex; i++) {
            newArray[i] = aArray[i];
        }
        newArray[aIndex] = aObjectToInsert;
        for (int i = (aIndex + 1); i < newArray.length; i++) {
            newArray[i] = aArray[i - 1];
        }
        return newArray;
    }

    public static Object[] removeNulls(final Object[] v) {
        List<Object> list = new ArrayList<>(Arrays.asList(v));
        list.removeAll(Collections.singleton((Object) null));
        return list.toArray(new Object[list.size()]);
    }

    public static ItemStack[] removeNulls(final ItemStack[] v) {
        List<ItemStack> list = new ArrayList<>(Arrays.asList(v));
        list.removeAll(Collections.singleton((ItemStack) null));
        return list.toArray(new ItemStack[list.size()]);
    }

    public static String toString(Object[] aArray, String string) {
        return org.apache.commons.lang3.ArrayUtils.toString(aArray, string);
    }

    public static String toString(Object[] aArray) {
        return org.apache.commons.lang3.ArrayUtils.toString(aArray);
    }

}
