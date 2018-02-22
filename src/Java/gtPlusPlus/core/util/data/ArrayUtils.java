package gtPlusPlus.core.util.data;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArrayUtils {

	public static void expandArray(final Object[] someArray, final Object newValueToAdd) {
		Object[] series = someArray;
		series = addElement(series, newValueToAdd);
	}

	private static Object[] addElement(Object[] series, final Object newValueToAdd) {
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


}

