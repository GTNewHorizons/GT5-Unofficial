package gtPlusPlus.core.util.array;

public class Triplet<K,V,C> {

	private final K key;
	private final V value;
	private final C count;

	public Triplet(final K key, final V value, final C value2){
		this.key = key;
		this.value = value;
		this.count = value2;
	}

	final public K getKey(){
		return this.key;
	}

	final public V getValue(){
		return this.value;
	}

	final public C getSecondValue(){
		return this.count;
	}

}