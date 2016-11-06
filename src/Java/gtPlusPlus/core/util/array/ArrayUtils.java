package gtPlusPlus.core.util.array;

import java.util.Arrays;

public class ArrayUtils {

	public static void expandArray(Object[] someArray, Object newValueToAdd) {
		Object[] series = someArray;
	    series = addElement(series, newValueToAdd);
	}

	private static Object[] addElement(Object[] series, Object newValueToAdd) {
	    series  = Arrays.copyOf(series, series.length + 1);
	    series[series.length - 1] = newValueToAdd;
	    return series;
	}
	
}
