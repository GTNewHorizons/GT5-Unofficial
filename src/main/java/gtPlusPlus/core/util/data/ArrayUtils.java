package gtPlusPlus.core.util.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

public class ArrayUtils {

    public static <V> V[] insertElementAtIndex(V[] aArray, int aIndex, V aObjectToInsert) {
        V[] newArray = Arrays.copyOf(aArray, aArray.length + 1);
        if (aIndex >= 0) {
            System.arraycopy(aArray, 0, newArray, 0, aIndex);
        }
        newArray[aIndex] = aObjectToInsert;
        if (newArray.length - (aIndex + 1) >= 0) {
            System.arraycopy(aArray, aIndex + 1 - 1, newArray, aIndex + 1, newArray.length - (aIndex + 1));
        }
        return newArray;
    }

    public static Object[] removeNulls(@Nonnull final Object[] v) {
        List<Object> list = new ArrayList<>(Arrays.asList(v));
        list.removeAll(Collections.singleton(null));
        return list.toArray(new Object[0]);
    }

    public static ItemStack[] removeNulls(@Nonnull final ItemStack[] v) {
        List<ItemStack> list = new ArrayList<>(Arrays.asList(v));
        list.removeAll(Collections.singleton((ItemStack) null));
        return list.toArray(new ItemStack[0]);
    }

    public static String toString(Object[] aArray, String string) {
        return org.apache.commons.lang3.ArrayUtils.toString(aArray, string);
    }

    public static String toString(Object[] aArray) {
        return org.apache.commons.lang3.ArrayUtils.toString(aArray);
    }

}
