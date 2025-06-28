package gregtech.api.util.extensions;

import java.util.function.IntFunction;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GTUtility;

public class ArrayExt {

    public static int[] of(int... objects) {
        return objects;
    }

    public static float[] of(float... objects) {
        return objects;
    }

    public static double[] of(double... objects) {
        return objects;
    }

    public static char[] of(char... objects) {
        return objects;
    }

    public static byte[] of(byte... objects) {
        return objects;
    }

    public static long[] of(long... objects) {
        return objects;
    }

    @SafeVarargs
    public static <T> T[] of(T... objects) {
        return objects;
    }

    /**
     * Returns true if the array is not null and of length 0
     */
    public static <T> boolean isArrayEmpty(T[] a) {
        return a != null && a.length == 0;
    }

    /**
     * Returns a copy of the array unless it is of length 0
     */
    public static FluidStack[] copyFluidsIfNonEmpty(FluidStack... aStacks) {
        if (aStacks == null) return null;
        if (aStacks.length == 0) return aStacks;
        FluidStack[] rStacks = new FluidStack[aStacks.length];
        for (int i = 0; i < aStacks.length; i++) if (aStacks[i] != null) rStacks[i] = aStacks[i].copy();
        return rStacks;
    }

    /**
     * Returns a copy of the array unless it is of length 0
     */
    public static ItemStack[] copyItemsIfNonEmpty(ItemStack... aStacks) {
        if (aStacks == null) return null;
        if (aStacks.length == 0) return aStacks;
        ItemStack[] rStacks = new ItemStack[aStacks.length];
        for (int i = 0; i < aStacks.length; i++) rStacks[i] = GTUtility.copy(aStacks[i]);
        return rStacks;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public static <T> T[] withoutNulls(T[] array, IntFunction<T[]> arrayFactory) {
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                count++;
            }
        }

        T[] newArr = arrayFactory.apply(count);
        if (count == 0) return newArr;

        int j = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                newArr[j] = array[i];
                j++;
            }
        }

        return newArr;
    }

    public static <T> T[] withoutTrailingNulls(T[] array, IntFunction<T[]> arrayFactory) {
        int firstNull = -1;
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == null) {
                firstNull = i;
            } else {
                break;
            }
        }

        if (firstNull == -1) {
            T[] newArray = arrayFactory.apply(array.length);
            System.arraycopy(array, 0, newArray, 0, array.length);
            return newArray;
        } else if (firstNull == 0) {
            return arrayFactory.apply(0);
        } else {
            T[] newArray = arrayFactory.apply(firstNull);
            System.arraycopy(array, 0, newArray, 0, firstNull);
            return newArray;
        }
    }
}
