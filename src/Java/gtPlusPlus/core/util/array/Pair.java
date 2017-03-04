package gtPlusPlus.core.util.array;

public class Pair<K,V> {

	private final K key;
	private final V value;

	public Pair(final K key, final V value){
		this.key = key;
		this.value = value;
	}

	final public K getKey(){
		return this.key;
	}

	final public V getValue(){
		return this.value;
	}

}