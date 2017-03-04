package gtPlusPlus.core.util.array;

import java.util.Arrays;

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

}
