package gregtech.api.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectIterators;

/**
 * Various util methods for managing raw data structures that are minecraft/gt agnostic.
 */
public class GTDataUtils {

    public static <S, T> List<T> mapToList(Collection<S> in, Function<S, T> mapper) {
        List<T> out = new ArrayList<>(in.size());
        for (S s : in) out.add(mapper.apply(s));
        return out;
    }

    public static <S, T> List<T> mapToList(S[] in, Function<S, T> mapper) {
        List<T> out = new ArrayList<>(in.length);
        for (S s : in) out.add(mapper.apply(s));
        return out;
    }

    public static <S, T> T[] mapToArray(Collection<S> in, IntFunction<T[]> ctor, Function<S, T> mapper) {
        T[] out = ctor.apply(in.size());

        Iterator<S> iter = in.iterator();
        for (int i = 0; i < out.length && iter.hasNext(); i++) {
            out[i] = mapper.apply(iter.next());
        }

        return out;
    }

    public static <S, T> T[] mapToArray(S[] in, IntFunction<T[]> ctor, Function<S, T> mapper) {
        T[] out = ctor.apply(in.length);
        for (int i = 0; i < out.length; i++) out[i] = mapper.apply(in[i]);
        return out;
    }

    public static int countNonNulls(Object[] array) {
        int l = array.length;
        int count = 0;

        // noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < l; i++) {
            if (array[i] != null) count++;
        }

        return count;
    }

    public static <T> T[] withoutNulls(T[] array) {
        if (array.length == 0) return array;

        int nonNullCount = GTDataUtils.countNonNulls(array);

        if (nonNullCount == array.length) return array;

        T[] out = Arrays.copyOf(array, nonNullCount);

        int j = 0, l = array.length;

        for (int i = 0; i < l; i++) {
            T t = array[i];

            if (t != null) out[j++] = t;
        }

        return out;
    }

    public static <T> ArrayList<T> filterList(List<T> input, Predicate<T> filter) {
        ArrayList<T> output = new ArrayList<>(input.size());

        for (int i = 0, inputSize = input.size(); i < inputSize; i++) {
            T t = input.get(i);

            if (filter.test(t)) {
                output.add(t);
            }
        }

        return output;
    }

    public static <T, S extends T> void addAllFiltered(List<S> input, List<T> output, Predicate<S> filter) {
        for (int i = 0, inputSize = input.size(); i < inputSize; i++) {
            S s = input.get(i);

            if (filter.test(s)) {
                output.add(s);
            }
        }
    }

    /**
     * Upcasts a list of a concrete type into a list of interfaces since java can't do this implicitly with generics.
     */
    public static <I, T extends I> ArrayList<I> upcast(List<T> input) {
        ArrayList<I> output = new ArrayList<>(input.size());

        for (int i = 0, inputSize = input.size(); i < inputSize; i++) {
            output.add(input.get(i));
        }

        return output;
    }

    public static <T> int findIndex(T[] array, T value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) return i;
        }

        return -1;
    }

    @Nullable
    public static <T> T getIndexSafe(@Nullable T @Nullable [] array, int index) {
        return array == null || index < 0 || index >= array.length ? null : array[index];
    }

    @Nullable
    public static <T> T getIndexSafe(@Nullable List<@Nullable T> list, int index) {
        return list == null || index < 0 || index >= list.size() ? null : list.get(index);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static <T> HashSet<T> dedupList(List<T> l) {
        HashSet<T> set = new HashSet<>();

        l.removeIf(t -> !set.add(t));

        return set;
    }

    /** A simple, low allocation Iterable that contains one value. */
    public static <T> Iterable<T> singletonIterable(T object) {
        return () -> ObjectIterators.singleton(object);
    }

    public static <T> Stream<T> ofNullableStream(T value) {
        return value == null ? Stream.empty() : Stream.of(value);
    }

    public static <T> T[] concat(T[]... arrays) {
        int totalLength = 0;

        int l = arrays.length;

        for (int i = 0; i < l; i++) {
            totalLength += arrays[i].length;
        }

        T[] out = Arrays.copyOf(arrays[0], totalLength);

        int cursor = arrays[0].length;

        for (int i = 1; i < l; i++) {
            T[] curr = arrays[i];

            int currLength = curr.length;
            System.arraycopy(curr, 0, out, cursor, currLength);
            cursor += currLength;
        }

        return out;
    }

    public static int[] intersect(int[] a, int[] b) {
        IntLinkedOpenHashSet a2 = new IntLinkedOpenHashSet(a);
        IntLinkedOpenHashSet b2 = new IntLinkedOpenHashSet(b);

        IntArrayList out = new IntArrayList();

        a2.forEach((int i) -> {
            if (b2.contains(i)) {
                out.add(i);
            }
        });

        return out.toIntArray();
    }
}
