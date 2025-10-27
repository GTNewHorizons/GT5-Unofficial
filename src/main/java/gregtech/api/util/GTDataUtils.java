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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import it.unimi.dsi.fastutil.Pair;

import org.jetbrains.annotations.Nullable;

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

    public static <T> int findIndex(T[] array, Predicate<T> matcher) {
        for (int i = 0; i < array.length; i++) {
            if (matcher.test(array[i])) return i;
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

    public static <L, R> Iterator<Pair<L, R>> zip(Iterator<L> left, Iterator<R> right) {
        return new Iterator<Pair<L, R>>() {

            @Override
            public boolean hasNext() {
                boolean l = left.hasNext();
                boolean r = right.hasNext();

                if (l != r) throw new IllegalStateException("zipped iterators did not have the same length: " + (l ? "left had more than right" : "right had more than left"));

                return l && r;
            }

            @Override
            public Pair<L, R> next() {
                return Pair.of(left.next(), right.next());
            }
        };
    }

    public static <T> Iterable<T> oneshot(Iterator<T> iter) {
        return () -> iter;
    }

    public static final Gson GSON = new Gson();

    @SideOnly(Side.CLIENT)
    public static <K, V> Map<K, V> loadResourceMerged(Class<K> key, Class<V> value, ResourceLocation resource) {
        return loadResourceMerged(GSON, key, value, resource);
    }

    @SideOnly(Side.CLIENT)
    public static <K, V> Map<K, V> loadResourceMerged(Gson gson, Class<K> key, Class<V> value, ResourceLocation resource) {
        Map<String, JsonElement> temp = new HashMap<>();

        loadResourceMerged(gson, new TypeToken<Map<String, JsonElement>>(){}.getType(), resource, temp::putAll);

        Map<K, V> out = new HashMap<>();

        for (var e : temp.entrySet()) {
            out.put(gson.fromJson(new JsonPrimitive(e.getKey()), key), gson.fromJson(e.getValue(), value));
        }

        return out;
    }

    @SideOnly(Side.CLIENT)
    public static <T> void loadResourceMerged(Gson gson, Type type, ResourceLocation resource, Consumer<T> fn) {
        IResourceManager manager = Minecraft.getMinecraft().getResourceManager();

        try {
            for (IResource colourMap : manager.getAllResources(resource)) {
                fn.accept(gson.fromJson(new InputStreamReader(colourMap.getInputStream()), type));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SideOnly(Side.CLIENT)
    public static <T> T loadResource(Gson gson, Type type, ResourceLocation location) {
        IResourceManager manager = Minecraft.getMinecraft().getResourceManager();

        try {
            IResource resource = manager.getResource(location);

            return gson.fromJson(new InputStreamReader(resource.getInputStream()), type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
