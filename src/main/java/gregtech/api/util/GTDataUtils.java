package gregtech.api.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
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

    public static <T> int findIndex(T[] array, Predicate<T> matcher) {
        for (int i = 0; i < array.length; i++) {
            if (matcher.test(array[i])) return i;
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
