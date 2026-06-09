package gregtech.api.util.extensions;

import java.util.Arrays;
import java.util.function.IntFunction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
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

    /**
     * Returns a copy of the array without the null elements. Returns the singleton
     * {@link gregtech.api.enums.GTValues#emptyFluidStackArray} if the array is empty.
     */
    @Nonnull
    public static FluidStack[] removeNullFluids(@Nonnull FluidStack[] fluids) {
        if (fluids.length == 0) return GTValues.emptyFluidStackArray;
        int count = 0;
        for (final FluidStack f : fluids) {
            if (f != null) count++;
        }
        if (count == 0) return GTValues.emptyFluidStackArray;
        final FluidStack[] a = new FluidStack[count];
        int i = 0;
        for (final FluidStack f : fluids) {
            if (f != null) {
                a[i] = f;
                i++;
            }
        }
        return a;
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

    /**
     * Returns a copy of the array without any null elements at the end. Returns the singleton
     * {@link gregtech.api.enums.GTValues#emptyItemStackArray} if the array is empty.
     */
    @Nonnull
    public static ItemStack[] removeTrailingNulls(@Nonnull ItemStack[] array) {
        if (array.length == 0) return GTValues.emptyItemStackArray;
        int nullIndex = -1;
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == null) {
                nullIndex = i;
            } else {
                break;
            }
        }
        if (nullIndex == -1) {
            // array has no trailing null
            final ItemStack[] a = new ItemStack[array.length];
            System.arraycopy(array, 0, a, 0, array.length);
            return a;
        } else if (nullIndex == 0) {
            // array has no element
            return GTValues.emptyItemStackArray;
        } else {
            // array has trailing nulls that needs removing
            final ItemStack[] a = new ItemStack[nullIndex];
            System.arraycopy(array, 0, a, 0, nullIndex);
            return a;
        }
    }

    /**
     * Returns a copy of the array without any null elements at the end. Returns the singleton
     * {@link gregtech.api.enums.GTValues#emptyFluidStackArray} if the array is empty.
     */
    @Nonnull
    public static FluidStack[] removeTrailingNulls(@Nonnull FluidStack[] array) {
        if (array.length == 0) return GTValues.emptyFluidStackArray;
        int nullIndex = -1;
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == null) {
                nullIndex = i;
            } else {
                break;
            }
        }
        if (nullIndex == -1) {
            // array has no trailing null
            final FluidStack[] a = new FluidStack[array.length];
            System.arraycopy(array, 0, a, 0, array.length);
            return a;
        } else if (nullIndex == 0) {
            // array has no element
            return GTValues.emptyFluidStackArray;
        } else {
            // array has trailing nulls that needs removing
            final FluidStack[] a = new FluidStack[nullIndex];
            System.arraycopy(array, 0, a, 0, nullIndex);
            return a;
        }
    }

    /**
     * Fixes the recipe chances array. It will return null if the array is null or if the array doesn't use any
     * meaningful % chances value. The values of the returned array will be contained in the interval [1;10000]. It may
     * return the same array instance or a new instance.
     *
     * @param chances     the input chances array to fix
     * @param expectedLen the expected length of the returned array, can be -1 if you don't need to set the length
     */
    @Nullable
    public static int[] fixChancesArray(@Nullable int[] chances, int expectedLen) {
        if (chances == null) return null;
        boolean valid = false;
        final int len = expectedLen == -1 ? chances.length : Math.min(chances.length, expectedLen);
        for (int i = 0; i < len; i++) {
            final int chance = chances[i];
            if (chance >= 0 && chance < 10000) {
                valid = true;
                break;
            }
        }
        if (!valid) return null;
        final boolean needCopy = expectedLen != -1 && expectedLen != chances.length;
        final int[] array;
        if (!needCopy) {
            array = chances;
        } else {
            array = Arrays.copyOf(chances, expectedLen);
            for (int i = chances.length; i < expectedLen; i++) {
                array[i] = 10000;
            }
        }
        for (int i = 0, len2 = array.length; i < len2; i++) {
            final int chance = array[i];
            if (chance < 0 || chance > 10000) {
                array[i] = 10000;
            }
        }
        return array;
    }
}
