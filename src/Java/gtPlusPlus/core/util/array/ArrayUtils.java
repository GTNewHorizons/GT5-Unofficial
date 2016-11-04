package gtPlusPlus.core.util.array;

import java.util.Arrays;

public class ArrayUtils {

	private static Object[] addElement(Object[] series, final Object newValueToAdd) {
		series = Arrays.copyOf(series, series.length + 1);
		series[series.length - 1] = newValueToAdd;
		return series;
	}

	public static void expandArray(final Object[] someArray, final Object newValueToAdd) {
		Object[] series = someArray;
		series = ArrayUtils.addElement(series, newValueToAdd);
	}

}
