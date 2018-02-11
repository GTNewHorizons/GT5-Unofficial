package gtPlusPlus.core.util.array;

import java.io.Serializable;

public class Pair<K,V> implements Serializable {

	/**
	 * SVUID
	 */
	private static final long serialVersionUID = 1250550491092812443L;
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