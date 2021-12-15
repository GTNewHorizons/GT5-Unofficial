package gtPlusPlus.api.objects.data;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import gtPlusPlus.api.objects.Logger;


public class TypeCounter<V> implements Set<V> {

	private Map<String, InternalTypeCounterObject<V>> mInternalMap = new LinkedHashMap<String, InternalTypeCounterObject<V>>();
	private String mHighestValueKey;
	private int mHighestValue = 0;
	private final Class mClass;

	public TypeCounter(Class o) {
		Logger.WARNING("Created new TypeCounter for "+o.getName());
		mClass = o;
	}

	public static class InternalTypeCounterObject<Z> {
		private final Z mObject;
		private int mCounter = 0;

		public InternalTypeCounterObject(Z o) {
			mObject = o;
		}

		public String hash() {
			return String.valueOf(mObject.hashCode());
		}

		public Z get() {
			return mObject;
		}

		public void add() {
			mCounter++;
		}

		public int count() {
			return mCounter;
		}

	}

	public boolean add(V arg0) {
		return add(arg0, null);
	}

	public boolean add(V arg0, String aKeyName) {
		String aKey = aKeyName != null ? aKeyName : arg0.toString();
		InternalTypeCounterObject<V> aValue = mInternalMap.get(aKey);
		if (aValue == null) {
			aValue = new InternalTypeCounterObject<V>((V) arg0);
			Logger.WARNING("Adding new key to map: "+aKey);
		}
		aValue.add();
		int a = aValue.count();
		if (a > mHighestValue) {
			mHighestValue = a;
			mHighestValueKey = aKey;
			Logger.WARNING("New Highest Count - "+aKey+":"+a);
		}		
		mInternalMap.put(aKey, aValue);
		Logger.WARNING(aKey+":"+a);
		return true;
	}

	@Override
	public boolean addAll(Collection arg0) {
		boolean aReturn = true;
		for (Object o : arg0) {
			if (mClass.isInstance(o)) {
				V j = (V) o;
				boolean b = add(j);
				if (!b) {
					aReturn = false;
				}
			}			
		}
		return aReturn;
	}

	@Override
	public void clear() {
		mInternalMap.clear();		
	}

	@Override
	public boolean contains(Object arg0) {
		return mInternalMap.containsKey(arg0.toString());
	}

	@Override
	public boolean containsAll(Collection arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		return mInternalMap.isEmpty();
	}

	@Override
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object arg0) {		
		InternalTypeCounterObject<V> aValue = mInternalMap.remove(arg0.toString());
		if (aValue != null) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean removeAll(Collection arg0) {
		boolean aReturn = true;
		for (Object o : arg0) {
			boolean a = remove(o);
			if (!a) {
				aReturn = false;
			}
		}
		return aReturn;
	}

	@Override
	public boolean retainAll(Collection arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		return this.mInternalMap.size();
	}

	@Override
	public Object[] toArray() {
		Object[] aArray = new Object[this.mInternalMap.size()];
		int aPos = 0;
		for (String k : this.mInternalMap.keySet()) {
			if (k != null) {
				InternalTypeCounterObject<V> aVal = this.mInternalMap.get(k);
				aArray[aPos++] = new Pair<String, InternalTypeCounterObject<V>>(k, aVal);
			}
		}
		return aArray;		
	}

	@Override
	public V[] toArray(Object[] a) {
		Object[] aArray = new Object[a.length];
		int aPos = 0;
		for (Object k : a) {
			if (k != null) {
				aArray[aPos++] = k;
			}
		}
		return (V[]) aArray;
	}

	public V getResults() {
		InternalTypeCounterObject<V> x = mInternalMap.get(mHighestValueKey);
		return x.get();
	}
}
