package gtPlusPlus.core.util.array;

public class Quad<K,V,C,R> {

	private final K key;
	private final V value;
	private final C value2;
	private final R value3;

	public Quad(final K key, final V value, final C value2, final R value3){
		this.key = key;
		this.value = value;
		this.value2 = value2;
		this.value3 = value3;
	}

	final public K getKey(){
		return this.key;
	}
	
	final public V getValue_1(){
		return this.value;
	}

	final public C getValue_2(){
		return this.value2;
	}

	final public R getValue_3(){
		return this.value3;
	}

}