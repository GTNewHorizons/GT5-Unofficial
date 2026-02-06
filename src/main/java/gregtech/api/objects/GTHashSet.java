package gregtech.api.objects;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

import gregtech.api.util.GTUtility;

public class GTHashSet extends AbstractSet<GTItemStack> {

    private static final Object OBJECT = new Object();
    private final transient HashMap<GTItemStack, Object> map;

    public GTHashSet() {
        map = new HashMap<>();
    }

    public GTHashSet(Collection<GTItemStack> c) {
        map = new HashMap<>(Math.max((int) (c.size() / .75f) + 1, 16));
        addAll(c);
    }

    public GTHashSet(int initialCapacity, float loadFactor) {
        map = new HashMap<>(initialCapacity, loadFactor);
    }

    public GTHashSet(int initialCapacity) {
        map = new HashMap<>(initialCapacity);
    }

    GTHashSet(int initialCapacity, float loadFactor, boolean dummy) {
        map = new LinkedHashMap<>(initialCapacity, loadFactor);
    }

    public Map<GTItemStack, Object> getMap() {
        return map;
    }

    @Nonnull
    @Override
    public Iterator<GTItemStack> iterator() {
        return map.keySet()
            .iterator();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    public boolean add(ItemStack aStack) {
        if (GTUtility.isStackInvalid(aStack)) return false;
        return map.put(new GTItemStack(aStack), OBJECT) == null;
    }

    @Override
    public boolean add(GTItemStack e) {
        return map.put(e, OBJECT) == null;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) == OBJECT;
    }

    @Override
    public void clear() {
        map.clear();
    }
}
