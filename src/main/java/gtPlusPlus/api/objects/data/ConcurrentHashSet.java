package gtPlusPlus.api.objects.data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentHashSet<V> extends ConcurrentSet<V> {

	private static final long serialVersionUID = -1293478938482781728L;
	
	public ConcurrentHashSet() {
		this(new ConcurrentHashMap<Integer, V>());
	}
	
	public ConcurrentHashSet(ConcurrentMap<Integer, V> defaultMapType) {
		super(defaultMapType);
	}	
	
}
