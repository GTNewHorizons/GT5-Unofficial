package gtPlusPlus.core.util.math;

import java.util.Map;
import java.util.Random;

import gtPlusPlus.core.util.Utils;

public class MathUtils {

	/**
	 * Returns an int. The returned number is the value on i + 273.15F. Supports
	 * ints.
	 *
	 * @param i
	 *            Temp in Celcius.
	 * @return int The celcius temp returned as Kelvin, rounded to the readest
	 *         whole.
	 */
	public static float celsiusToKelvin(final int i) {
		final double f = i + 273.15F;
		return (int) MathUtils.decimalRoundingToWholes(f);
	}

	// Smooth Rounding Function
	/**
	 * Returns a double. The returned number is d rounded to the nearest d.01.
	 * Supports Doubles.
	 *
	 * @param current
	 *            Current value.
	 * @return double Rounded value.
	 */
	public static double decimalRounding(final double d) {
		return Math.round(d * 2) / 2.0;
	}

	// Smooth Rounding Function (Nearest 5)
	/**
	 * Returns a double. The returned number is d rounded to the nearest d.5.
	 * Supports Doubles.
	 *
	 * @param current
	 *            Current value.
	 * @return double Rounded value.
	 */
	public static double decimalRoundingToWholes(final double d) {
		return 5 * Math.round(d / 5);
	}

	/**
	 * Returns a boolean. The returned boolean is wether or not X evenly fits in
	 * to Y. Supports ints.
	 *
	 * @param x
	 *            Value A.
	 * @param y
	 *            Value B. Must be greater than min.
	 * @return boolean Whether or not it divides evenly.
	 */
	public static boolean divideXintoY(final int x, final int y) {
		if (x % y == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Returns a percentage. The returned number is the % of X in Y. Supports
	 * Doubles.
	 *
	 * @param current
	 *            Current value.
	 * @param max
	 *            Maximim value. Must be greater than min.
	 * @return double between min and max, inclusive.
	 */
	public static double findPercentage(final double current, final double max) {
		final double c = current / max * 100;
		final double roundOff = Math.round(c * 100.00) / 100.00;
		return roundOff;
	}

	private static long gcd(long a, long b) {
		while (b > 0) {
			final long temp = b;
			b = a % b; // % is remainder
			a = temp;
		}
		return a;
	}

	private static long gcd(final long[] input) {
		long result = input[0];
		for (int i = 1; i < input.length; i++) {
			result = MathUtils.gcd(result, input[i]);
		}
		return result;
	}

	/**
	 * Returns a hexInteger. The returned value is between min and max. Supports
	 * ints.
	 *
	 * @param min
	 *            Minimum value.
	 * @param max
	 *            Maximium value. Must be greater than min.
	 * @return hexInteger between min and max, inclusive.
	 */
	public static int generateRandomHexValue(final int min, final int max) {
		final int result = MathUtils.getHexNumberFromInt(MathUtils.randInt(min, max));
		return result;
	}

	/**
	 * Returns a random hex value. The returned value is between 000000-ffffff.
	 *
	 * @return hexInteger between min and max, inclusive.
	 */
	public static int generateSingularRandomHexValue() {
		String temp;
		final int randomInt = MathUtils.randInt(1, 5);
		final Map<Integer, String> colours = Utils.hexColourGeneratorRandom(5);

		if (colours.get(randomInt) != null && colours.size() > 0) {
			temp = colours.get(randomInt);
		}
		else {
			temp = "0F0F0F";
		}

		Utils.LOG_WARNING("Operating with " + temp);
		temp = Utils.appenedHexNotationToString(String.valueOf(temp));
		Utils.LOG_WARNING("Made " + temp + " - Hopefully it's not a mess.");
		Utils.LOG_WARNING("It will decode into " + Integer.decode(temp) + ".");
		return Integer.decode(temp);
	}

	public static double getChanceOfXOverYRuns(final double x, final double y) {
		final double z = 1 - Math.pow(1 - x, y);
		return z;
	}

	/**
	 * Returns a hexInteger. The returned number is the hex value of the input.
	 * Supports ints.
	 *
	 * @param input
	 *            Current value.
	 * @return hexInteger.
	 */
	public static int getHexNumberFromInt(final int input) {
		final String result = Integer.toHexString(input);
		final int resultINT = Integer.getInteger(result);
		return resultINT;
	}

	/**
	 * Returns a boolean. The returned boolean is based on the odd/eveness of
	 * the input. Supports ints.
	 *
	 * @param x
	 *            Value A.
	 * @return boolean Whether or not it divides evenly.
	 */
	public static boolean isNumberEven(final int x) {
		if (x % 2 == 0) {
			return true;
		}
		return false;
	}

	private static long nextLong(final Random rng, final long n) {
		// error checking and 2^x checking removed for simplicity.
		long bits, val;
		do {
			bits = rng.nextLong() << 1 >>> 1;
			val = bits % n;
		}
		while (bits - val + n - 1 < 0L);
		return val;
	}

	/**
	 * Returns a psuedo-random number between min and max, inclusive. The
	 * difference between min and max can be at most Integer.MAX_VALUE - 1.
	 *
	 * @param min
	 *            Minimim value
	 * @param max
	 *            Maximim value. Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(final int min, final int max) {

		// Usually this can be a field rather than a method variable
		final Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		final int randomNum = rand.nextInt(max - min + 1) + min;

		return randomNum;
	}

	/**
	 * Returns a psuedo-random number between min and max, inclusive. The
	 * difference between min and max can be at most Long.MAX_VALUE - 1.
	 *
	 * @param min
	 *            Minimim value
	 * @param max
	 *            Maximim value. Must be greater than min.
	 * @return Long between min and max, inclusive.
	 * @see java.util.Random#nextLong(long)
	 */
	public static long randLong(final long min, final long max) {
		// Usually this can be a field rather than a method variable
		final Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		final long randomNum = MathUtils.nextLong(rand, max - min + 1) + min;

		return randomNum;
	}

	public static int roundToClosestMultiple(final double number, final int multiple) {
		int result = multiple;
		if (number % multiple == 0) {
			return (int) number;
		}
		// If not already multiple of given number
		if (number % multiple != 0) {
			final int division = (int) (number / multiple + 1);
			result = division * multiple;
		}
		return result;
	}

	public static long[] simplifyNumbersToSmallestForm(final long[] inputArray) {
		final long GCD = MathUtils.gcd(inputArray);
		final long[] outputArray = new long[inputArray.length];
		for (int i = 0; i < inputArray.length; i++) {
			if (GCD != 0) {
				outputArray[i] = inputArray[i] / GCD;
			}
			else {
				outputArray[i] = inputArray[i];
			}
		}
		if (outputArray.length > 0) {
			return outputArray;
		}
		return null;
	}

}
