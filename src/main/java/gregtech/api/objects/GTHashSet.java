package gregtech.api.objects;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTechAPI;
import gregtech.api.util.GTUtility;

public class GTHashSet<E extends GTItemStack> extends AbstractSet<E> {

    private static final Object OBJECT = new Object();
    private final transient HashMap<GTItemStack, Object> map;

    public GTHashSet() {
        map = new HashMap<>();
        GregTechAPI.sItemStackMappings.add(map);
    }

    public GTHashSet(Collection<? extends E> c) {
        map = new HashMap<>(Math.max((int) (c.size() / .75f) + 1, 16));
        addAll(c);
        GregTechAPI.sItemStackMappings.add(map);
    }

    public GTHashSet(int initialCapacity, float loadFactor) {
        map = new HashMap<>(initialCapacity, loadFactor);
        GregTechAPI.sItemStackMappings.add(map);
    }

    public GTHashSet(int initialCapacity) {
        map = new HashMap<>(initialCapacity);
        GregTechAPI.sItemStackMappings.add(map);
    }

    GTHashSet(int initialCapacity, float loadFactor, boolean dummy) {
        map = new LinkedHashMap<>(initialCapacity, loadFactor);
        GregTechAPI.sItemStackMappings.add(map);
    }

    public Map<GTItemStack, Object> getMap() {
        return map;
    }

    @SuppressWarnings("unchecked") // The downcasting below will throw ClassCastException unless E is GT_ItemStack.
    @Override
    public Iterator<E> iterator() {
        return (Iterator<E>) map.keySet()
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
    public boolean add(E e) {
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
