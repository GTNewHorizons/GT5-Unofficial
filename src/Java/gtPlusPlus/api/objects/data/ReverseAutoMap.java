package gtPlusPlus.api.objects.data;

import java.util.*;

public class ReverseAutoMap<N> extends AutoMap<N> {

	/**
	 * The Internal Map
	 */
	private Map<N, Integer> mInternalMapReverseLookup = new HashMap<N, Integer>();

	/**
	 * The Internal ID
	 */
	private int mInternalID = 0;
	private static final long serialVersionUID = 3771412318075131790L;

	@Override
	public Iterator<N> iterator() {		
		return values().iterator();
	}

	public synchronized boolean setValue(N object){
		int mOriginalID = this.mInternalID;
		put(object);		
		if (this.mInternalMap.get(mOriginalID).equals(object) || mOriginalID > this.mInternalID){
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public synchronized N put(N object){
		return set(object);
	}

	@Override
	public synchronized N set(N object){
		raiseInternalID();
		int newID = getNextFreeMapID();
		mInternalMapReverseLookup.put(object, newID);
		return mInternalMap.put(newID, object);
	}

	public synchronized int putToInternalMap(N object){
		return setInternalMap(object);
	}

	public synchronized int setInternalMap(N object){
		raiseInternalID();		
		int newID = getNextFreeMapID();			
		mInternalMap.put(newID, object);		
		return mInternalMapReverseLookup.put(object, newID);
	}

	private boolean raiseInternalID() {
		int mOld = mInternalID;
		mInternalID++;
		return mInternalID > mOld;
	}

	public synchronized int getNextFreeMapID() {
		if (this.mInternalMap.size() < 1 || this.mInternalMapReverseLookup.size() < 1) {
			return 0;
		}
		if (this.mInternalMap.size() == getInternalID()-1) {
			return getInternalID();
		}		
		AutoMap<Integer> free = new AutoMap<Integer>();
		values: for (int d : this.mInternalMapReverseLookup.values()) {
			sequential: for (int m=0;m<this.mInternalMap.size();m++) {

				//Counter is lower than current ID, keep counting.
				if (m < d || m == d) {
					continue sequential;
				}
				//Possible that d is missing in sequence i.e.  (m=0,1,2,3 & d=0,1,3,4)
				else if (m > d) {
					free.put(m);
					continue values;
				}
			}
		}		
		if (free.isEmpty()) {
			return 0;
		}
		else {
			return free.get(0);
		}
	}

	@Override
	public synchronized N get(int id){		
		return mInternalMap.get(id);
	}

	public synchronized int get(N key) {		
		return mInternalMapReverseLookup.get(key);
	}

	@Override
	public synchronized Collection<N> values(){
		return mInternalMap.values();
	}

	@Override
	public synchronized int size(){
		return mInternalMap.size();
	}

	@Override
	public synchronized int hashCode(){
		return mInternalMap.hashCode()+mInternalMapReverseLookup.hashCode();
	}

	@Override
	public synchronized boolean containsKey(int key){
		return mInternalMap.containsKey(key);
	}

	@Override
	public synchronized boolean containsValue(N value){
		return mInternalMap.containsValue(value);
	}

	public synchronized boolean containsKey(N key){
		return mInternalMapReverseLookup.containsKey(key);
	}

	public synchronized boolean containsValue(int value){
		return mInternalMapReverseLookup.containsValue(value);
	}

	@Override
	public synchronized boolean isEmpty(){
		return mInternalMap.isEmpty() && mInternalMapReverseLookup.isEmpty();
	}

	@Override
	public synchronized boolean clear(){
		this.mInternalID = 0;
		this.mInternalMap.clear();
		this.mInternalMapReverseLookup.clear();
		return true;
	}

	@Override
	public synchronized N[] toArray() {		
		Collection<N> col = this.mInternalMap.values();
		@SuppressWarnings("unchecked")
		N[] val = (N[]) col.toArray();		
		return val;
	}

}
