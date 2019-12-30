package gtPlusPlus.api.objects.data;

import java.util.Collection;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import gtPlusPlus.api.objects.random.XSTR;

public class WeightedCollection<E> implements Map<Integer, E> {

	private NavigableMap<Integer, E> map = new TreeMap<Integer, E>();
	private Random random;
	private int total = 0;

	public WeightedCollection() {
		this(new XSTR());
	}

	public WeightedCollection(Random random) {
		this.random = random;
	}

	public E add(int weight, E object) {
		if (weight <= 0) return null;
		total += weight;
		return map.put(total, object);
	}

	private E next() {
		int value = random.nextInt(total) + 1; // Can also use floating-point weights
		return map.ceilingEntry(value).getValue();
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
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public E get() {
		return next();
	}
	
	@Override
	public E get(Object key) {
		return next();
	}

	@Override
	public void putAll(Map m) {
		map.putAll(m);		
	}

	@Override
	public void clear() {
		map.clear();
		this.total = 0;
	}

	@Override
	public Set keySet() {
		return map.keySet();
	}

	@Override
	public Collection values() {
		return map.values();
	}

	@Override
	public Set entrySet() {
		return map.entrySet();
	}

	@Override
	public E put(Integer key, E value) {
		return add(key, value);
	}

	@Override
	public E remove(Object key) {
		return map.remove(key);
	}

}