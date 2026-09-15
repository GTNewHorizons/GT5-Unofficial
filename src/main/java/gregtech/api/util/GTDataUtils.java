package gregtech.api.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectIterators;

/**
 * Various util methods for managing raw data structures that are minecraft/gt agnostic.
 */
@SuppressWarnings({ "UnstableApiUsage", "unused" })
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

        for (Object o : array) {
            if (o != null) count++;
        }

        return count;
    }

    public static <T> T[] withoutNulls(T[] array) {
        if (array.length == 0) return array;

        int nonNullCount = GTDataUtils.countNonNulls(array);

        if (nonNullCount == array.length) return array;

        T[] out = Arrays.copyOf(array, nonNullCount);

        int j = 0, l = array.length;

        for (T t : array) {
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

    public static <T> int findIndex(T[] array, Predicate<T> matcher) {
        for (int i = 0; i < array.length; i++) {
            if (matcher.test(array[i])) return i;
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

    @SafeVarargs
    public static <T> T[] concat(T[]... arrays) {
        int totalLength = 0;

        int l = arrays.length;

        for (T[] array : arrays) {
            totalLength += array.length;
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

    public static <K, V, P extends Pair<K, V>> Collector<? super P, ?, Multimap<K, V>> toMultiMap(
        MultimapBuilder<K, V> map) {
        return toMultiMap(map::build);
    }

    public static <K, V, P extends Pair<K, V>, M extends Multimap<K, V>> Collector<? super P, ?, M> toMultiMap(
        Supplier<M> map) {
        return new Collector<P, M, M>() {

            @Override
            public Supplier<M> supplier() {
                return map;
            }

            @Override
            public BiConsumer<M, P> accumulator() {
                return (map, pair) -> map.put(pair.left(), pair.right());
            }

            @Override
            public BinaryOperator<M> combiner() {
                return (map1, map2) -> {
                    map1.putAll(map2);

                    return map1;
                };
            }

            @Override
            public Function<M, M> finisher() {
                return Function.identity();
            }

            @Override
            public Set<Characteristics> characteristics() {
                return new HashSet<>(Arrays.asList(Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED));
            }
        };

    }

    public static <L, R> Iterator<Pair<L, R>> zip(Iterator<L> left, Iterator<R> right) {
        return new Iterator<>() {

            @Override
            public boolean hasNext() {
                boolean l = left.hasNext();
                boolean r = right.hasNext();

                if (l != r) throw new IllegalStateException(
                    "zipped iterators did not have the same length: "
                        + (l ? "left had more than right" : "right had more than left"));

                return l && r;
            }

            @Override
            public Pair<L, R> next() {
                return Pair.of(left.next(), right.next());
            }
        };
    }

    /// Converts an [Iterator] into an [Iterable], so that you can pass a custom iterator into a for-each loop.
    /// The iterator will be exhausted after the loop breaks, but keep in mind that breaking early may cause the
    /// iterator to still have some elements.
    /// ```java
    /// Iterator<T> iter = ...;
    ///
    /// for (T value : oneshot(iter)) {
    /// ...
    /// }
    /// ```
    public static <T> Iterable<T> oneshot(Iterator<T> iter) {
        return () -> iter;
    }

    public static final Gson GSON = new Gson();

    @SideOnly(Side.CLIENT)
    public static <K, V> Map<K, V> loadResourceMerged(Class<K> key, Class<V> value, ResourceLocation resource) {
        return loadResourceMerged(GSON, key, value, resource);
    }

    /// Scans all resource packs for the given resource and merges them into one object. Entries with identical keys are
    /// replaced - the entry from the bottom-most resource pack takes priority over all prior entries.
    @SideOnly(Side.CLIENT)
    public static <K, V> Map<K, V> loadResourceMerged(Gson gson, Class<K> key, Class<V> value, ResourceLocation loc) {
        // Temporarily load the data into a string -> json map so that we can replace identical entries based on the
        // key.
        Map<String, JsonElement> temp = new HashMap<>();

        loadResourceMerged(gson, new TypeToken<Map<String, JsonElement>>() {}.getType(), loc, temp::putAll);

        Map<K, V> out = new HashMap<>();

        for (var e : temp.entrySet()) {
            // Convert the String keys and JsonElement values into their proper values, now that we've scanned all
            // resource packs
            out.put(gson.fromJson(new JsonPrimitive(e.getKey()), key), gson.fromJson(e.getValue(), value));
        }

        return out;
    }

    /// Scans all resources packs for the given resource, and merges all of them into one object. This method does not
    /// perform any of the merging logic, see [#loadResourceMerged(Gson, Class, Class, ResourceLocation)].
    @SideOnly(Side.CLIENT)
    public static <T> void loadResourceMerged(Gson gson, Type type, ResourceLocation loc, Consumer<T> fn) {
        IResourceManager manager = Minecraft.getMinecraft()
            .getResourceManager();

        try {
            for (IResource resource : manager.getAllResources(loc)) {
                fn.accept(gson.fromJson(new InputStreamReader(resource.getInputStream()), type));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /// Loads a resource from the resource manager and parses it into the given [Type], with the given [Gson] object.
    @SideOnly(Side.CLIENT)
    public static <T> T loadResource(Gson gson, Type type, ResourceLocation location) {
        IResourceManager manager = Minecraft.getMinecraft()
            .getResourceManager();

        try {
            IResource resource = manager.getResource(location);

            return gson.fromJson(new InputStreamReader(resource.getInputStream()), type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
