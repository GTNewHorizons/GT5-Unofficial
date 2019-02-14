package gtPlusPlus.api.objects.data;

import java.io.Serializable;
import java.util.*;

public class AutoMap<V> implements Iterable<V>, Cloneable, Serializable {

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
		this(new LinkedHashMap<Integer, V>());
	}
	
	public AutoMap(Map<Integer, V> defaultMapType) {
		mInternalMap = defaultMapType;
		mInternalNameMap = new LinkedHashMap<String, Integer>();
	}
	
	@Override
	public Iterator<V> iterator() {		
		return values().iterator();
	}

	public synchronized boolean setValue(V object){
		int mOriginalID = this.mInternalID;
		put(object);		
		if (this.mInternalMap.get(mOriginalID).equals(object) || mOriginalID > this.mInternalID){
			return true;
		}
		else {
			return false;
		}
	}
	
	public synchronized V put(V object){
		return set(object);
	}
	
	public synchronized V add(V object){
		return set(object);
	}
	
	public synchronized V set(V object){
		if (object == null) {
			return null;
		}
		mInternalNameMap.put(""+object.hashCode(), (mInternalID+1));
		return mInternalMap.put(mInternalID++, object);
	}
	
	public synchronized V get(int id){
		return mInternalMap.get(id);
	}
	
	public synchronized Collection<V> values(){
		return mInternalMap.values();
	}
	
	public synchronized int size(){
		return mInternalMap.size();
	}
	
	public synchronized int hashCode(){
		return mInternalMap.hashCode();
	}
	
	public synchronized boolean containsKey(int key){
		return mInternalMap.containsKey(key);
	}
	
	public synchronized boolean containsValue(V value){
		return mInternalMap.containsValue(value);
	}
	
	public synchronized boolean isEmpty(){
		return mInternalMap.isEmpty();
	}
	
	public synchronized boolean clear(){
		this.mInternalID = 0;
		this.mInternalMap.clear();
		this.mInternalNameMap.clear();
		return true;
	}
	
	private final Class getMapType() {
		Class<? extends Object> g = mInternalMap.get(0).getClass();
		return g;
	}
	
	public synchronized V[] toArray() {		
		/*Collection<V> col = this.mInternalMap.values();	
		List<V> abcList = new ArrayList<V>();		
		for (V g : col) {
			abcList.add(g);
		}
		return (V[]) abcList.toArray();*/
		return (V[]) new AutoArray(this).getGenericArray();
	}

	public synchronized final int getInternalID() {
		return mInternalID;
	}
	
	public synchronized final boolean remove(V value) {
		value.getClass();
		if (this.mInternalMap.containsValue(value)) {
			return this.mInternalMap.remove(mInternalNameMap.get(""+value.hashCode()), value);
		}
		return false;
	}
	
	private class AutoArray<E> {
		
		private final V[] arr;
		public final int length;
		
		public AutoArray(AutoMap aMap) {		
			this.arr = (V[]) java.lang.reflect.Array.newInstance(aMap.getMapType(), aMap.size());
			this.length = aMap.size();
		}
		private V get(int i) {
			return arr[i];
		}
		private void set(int i, V e) {
			arr[i] = e;
		}
		protected V[] getGenericArray() {
			return arr;
		}
		@Override
		public String toString() {
			return Arrays.toString(arr);
		}
	}
  
}
