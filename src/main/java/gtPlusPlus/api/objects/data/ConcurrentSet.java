package gtPlusPlus.api.objects.data;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;

public abstract class ConcurrentSet<E> extends AbstractSet<E> implements Serializable {

    private static final long serialVersionUID = -6761513279741915432L;

    private final ConcurrentMap<Integer, E> mInternalMap;
    
	private int mInternalID = 0;	

    /**
     * Creates a new instance which wraps the specified {@code map}.
     */    
    public ConcurrentSet(ConcurrentMap<Integer, E> aMap) {
    	mInternalMap = aMap;
    }

    @Override
    public int size() {
        return mInternalMap.size();
    }

    @Override
    public boolean contains(Object o) {
        return mInternalMap.containsKey(o);
    }

    @Override
    public boolean add(E o) {
        return mInternalMap.putIfAbsent(mInternalID++, o) == null;
    }

    @Override
    public boolean remove(Object o) {
        return mInternalMap.remove(o) != null;
    }

    @Override
    public void clear() {
		this.mInternalID = 0;
        mInternalMap.clear();
    }

    @Override
    public Iterator<E> iterator() {
        return mInternalMap.values().iterator();
    }
}
