package gtPlusPlus.core.util.data;

import java.util.*;

import net.minecraft.item.ItemStack;

import gtPlusPlus.api.objects.data.AutoMap;

public class ArrayUtils {

	public static <V> V[] expandArray(final V[] someArray, final V newValueToAdd) {
		V[] series = someArray;
		series = addElement(series, newValueToAdd);
		return series;
	}

	private static <V> V[] addElement(V[] series, final V newValueToAdd) {
		series  = Arrays.copyOf(series, series.length + 1);
		series[series.length - 1] = newValueToAdd;
		return series;
	}
	
	/*public static <V> Object getMostCommonElement(List<V> list) {
		Optional r = list.stream().map(V::getTextureSet).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey);
		return r.get();
	}*/

	public static ItemStack[] removeNulls(final ItemStack[] v) {
		List<ItemStack> list = new ArrayList<ItemStack>(Arrays.asList(v));
		list.removeAll(Collections.singleton((ItemStack)null));
		return list.toArray(new ItemStack[list.size()]);
	}

	@SuppressWarnings("unchecked")
	public static <T> Set<T> combineSetData(Set<T> S, Set<T> J) {
		Set<T> mData = new HashSet<T>();
		T[] array1 = (T[]) S.toArray();		
		Collections.addAll(mData, array1);
		T[] array2 = (T[]) J.toArray();		
		Collections.addAll(mData, array2);		
		return mData;
	}

	public static <A> AutoMap<A> mergeTwoMaps(AutoMap<A> a, AutoMap<A> b) {
		AutoMap<A> c = new AutoMap<A>();
		for (A g : a.values()) {
			c.put(g);
		}
		for (A g : b.values()) {
			c.put(g);
		}		
		return c;
	}

}

