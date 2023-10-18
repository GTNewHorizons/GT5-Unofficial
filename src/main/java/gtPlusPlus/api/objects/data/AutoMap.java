package gtPlusPlus.api.objects.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class AutoMap<V> implements Iterable<V>, Cloneable, Serializable, Collection<V>, Queue<V>, List<V> {

    /**
     * The Internal Map
     */
    protected final Map<Integer, V> mInternalMap;

    protected final Map<String, Integer> mInternalNameMap;

    /**
     * The Internal ID
     */
    private int mInternalID = 0;

    private static final long serialVersionUID = 3771412318075131790L;

    public AutoMap() {
        this(new LinkedHashMap<>());
    }

    public Map<Integer, V> getMap() {
        return mInternalMap;
    }

    public AutoMap(Map<Integer, V> defaultMapType) {
        mInternalMap = defaultMapType;
        mInternalNameMap = new LinkedHashMap<>();
    }

    /**
     * Generates an AutoMap from the List.
     * 
     * @param aList - Data to be inserted into the AutoMap.
     */
    public AutoMap(List<V> aList) {
        mInternalMap = new LinkedHashMap<>();
        mInternalNameMap = new LinkedHashMap<>();
        if (aList != null && aList.size() > 0) {
            for (V obj : aList) {
                add(obj);
            }
        }
    }

    /**
     * Generates an AutoMap from a Set.
     * 
     * @param aList - Data to be inserted into the AutoMap.
     */
    public AutoMap(Set<V> aList) {
        mInternalMap = new LinkedHashMap<>();
        mInternalNameMap = new LinkedHashMap<>();
        if (aList != null && aList.size() > 0) {
            for (V obj : aList) {
                add(obj);
            }
        }
    }

    /**
     * Generates an AutoMap from a Collection.
     * 
     * @param aList - Data to be inserted into the AutoMap.
     */
    public AutoMap(Collection<V> aList) {
        mInternalMap = new LinkedHashMap<>();
        mInternalNameMap = new LinkedHashMap<>();
        if (aList != null && aList.size() > 0) {
            for (V obj : aList) {
                add(obj);
            }
        }
    }

    /**
     * Generates an AutoMap from a Array.
     * 
     * @param aArray - Data to be inserted into the AutoMap.
     */
    public AutoMap(V[] aArray) {
        mInternalMap = new LinkedHashMap<>();
        mInternalNameMap = new LinkedHashMap<>();
        if (aArray != null && aArray.length > 0) {
            for (V obj : aArray) {
                add(obj);
            }
        }
    }

    @Override
    public Iterator<V> iterator() {
        return values().iterator();
    }

    public synchronized boolean setValue(V object) {
        int mOriginalID = this.mInternalID;
        put(object);
        if (this.mInternalMap.get(mOriginalID).equals(object) || mOriginalID > this.mInternalID) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized V put(V object) {
        return set(object);
    }

    @Override
    public synchronized boolean add(V object) {
        return set(object) != null;
    }

    public synchronized V set(V object) {
        if (object == null) {
            return null;
        }
        mInternalNameMap.put("" + object.hashCode(), (mInternalID + 1));
        return mInternalMap.put(mInternalID++, object);
    }

    @Override
    public synchronized V get(int id) {
        return mInternalMap.get(id);
    }

    public synchronized Collection<V> values() {
        return mInternalMap.values();
    }

    @Override
    public synchronized int size() {
        return mInternalMap.size();
    }

    public synchronized int hashCode() {
        return mInternalMap.hashCode();
    }

    public synchronized boolean containsKey(int key) {
        return mInternalMap.containsKey(key);
    }

    public synchronized boolean containsValue(V value) {
        return mInternalMap.containsValue(value);
    }

    @Override
    public synchronized boolean isEmpty() {
        return mInternalMap.isEmpty();
    }

    @Override
    public synchronized void clear() {
        this.mInternalID = 0;
        this.mInternalMap.clear();
        this.mInternalNameMap.clear();
        return;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V[] toArray() {
        V[] toR = (V[]) java.lang.reflect.Array.newInstance(mInternalMap.get(0).getClass(), mInternalMap.size());
        for (int i = 0; i < mInternalMap.size(); i++) {
            toR[i] = mInternalMap.get(i);
        }
        return toR;
    }

    public final synchronized int getInternalID() {
        return mInternalID;
    }

    @Override
    public final synchronized boolean remove(Object value) {
        value.getClass();
        if (this.mInternalMap.containsValue(value)) {
            return this.mInternalMap.remove(mInternalNameMap.get("" + value.hashCode()), value);
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        for (V g : this.mInternalMap.values()) {
            if (g.equals(o)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V[] toArray(V[] a) {
        return (V[]) toArray();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean aTrue = true;
        for (Object g : c) {
            if (!this.contains(g)) {
                aTrue = false;
            }
        }
        return aTrue;
    }

    @Override
    public boolean addAll(Collection<? extends V> c) {
        boolean aTrue = true;
        for (V g : c) {
            if (!this.add(g)) {
                aTrue = false;
            }
        }
        return aTrue;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean aTrue = true;
        for (Object g : c) {
            if (!this.remove(g)) {
                aTrue = false;
            }
        }
        return aTrue;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        AutoMap<?> aTempAllocation = new AutoMap<>();
        boolean aTrue = false;
        aTempAllocation = this;
        aTempAllocation.removeAll(c);
        aTempAllocation.clear();
        aTrue = aTempAllocation.isEmpty();
        aTempAllocation.clear();
        return aTrue;
    }

    @Override
    public boolean offer(V e) {
        return add(e);
    }

    @Override
    public V remove() {
        V y = this.get(0);
        if (remove(y)) return y;
        else return null;
    }

    @Override
    public V poll() {
        if (this.mInternalMap.isEmpty()) {
            return null;
        }
        return remove();
    }

    @Override
    public V element() {
        if (this.mInternalMap.isEmpty()) {
            return null;
        }
        return this.get(0);
    }

    @Override
    public V peek() {
        return element();
    }

    @Override
    public boolean addAll(int index, Collection<? extends V> c) {
        for (V y : c) {
            add(y);
        }
        return true;
    }

    @Override
    public V set(int index, V element) {
        return mInternalMap.put(index, element);
    }

    @Override
    public void add(int index, V element) {
        add(element);
    }

    @Override
    public V remove(int index) {
        V h = mInternalMap.get(index);
        set(index, null);
        return h;
    }

    @Override
    public int indexOf(Object o) {
        int aCount = 0;
        for (V of : mInternalMap.values()) {
            if (of != o) {
                aCount++;
                continue;
            } else {
                return aCount;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        // TODO
        return indexOf(o);
    }

    @Override
    public ListIterator<V> listIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ListIterator<V> listIterator(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<V> subList(int fromIndex, int toIndex) {
        AutoMap<V> aNewSubList = new AutoMap<>();
        for (int slot = fromIndex; slot <= toIndex; slot++) {
            V obj = mInternalMap.get(slot);
            if (obj == null) {
                continue;
            } else {
                aNewSubList.put(obj);
            }
        }
        return aNewSubList;
    }
}
