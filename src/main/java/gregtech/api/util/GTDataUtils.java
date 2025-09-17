package gregtech.api.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;

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

    public static <T> int findIndex(T[] array, T value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) return i;
        }

        return -1;
    }

    public static <T> T getIndexSafe(T[] array, int index) {
        return array == null || index < 0 || index >= array.length ? null : array[index];
    }

    public static <T> T getIndexSafe(List<T> list, int index) {
        return list == null || index < 0 || index >= list.size() ? null : list.get(index);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static <T> HashSet<T> dedupList(List<T> l) {
        HashSet<T> set = new HashSet<>();

        l.removeIf(t -> !set.add(t));

        return set;
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
}
