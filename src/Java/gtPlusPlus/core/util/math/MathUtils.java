package gtPlusPlus.core.util.math;

import java.util.Map;
import java.util.Random;

import gregtech.api.enums.GT_Values;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;

public class MathUtils {

	/**
	 * Returns a psuedo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * Integer.MAX_VALUE - 1.
	 *
	 * @param min Minimim value
	 * @param max Maximim value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */

	final static Random rand = CORE.RANDOM;

	public static int randInt(final int min, final int max) {
		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		return rand.nextInt((max - min) + 1) + min;
	}

	public static double getChanceOfXOverYRuns(final double x, final double y){
		return (1-Math.pow((1-x), y));
	}


	/**
	 * Returns a psuedo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * Long.MAX_VALUE - 1.
	 *
	 * @param min Minimim value
	 * @param max Maximim value.  Must be greater than min.
	 * @return Long between min and max, inclusive.
	 * @see java.util.Random#nextLong(long)
	 */
	public static long randLong(final long min, final long max) {
		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		return MathUtils.nextLong(rand,(max - min) + 1) + min;
	}
	private static long nextLong(final Random rng, final long n) {
		// error checking and 2^x checking removed for simplicity.
		long bits, val;
		do {
			bits = (rng.nextLong() << 1) >>> 1;
			val = bits % n;
		} while (((bits-val)+(n-1)) < 0L);
		return val;
	}


	/**
	 * Returns a psuedo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * Double.MAX_VALUE - 1.
	 *
	 * @param min Minimim value
	 * @param max Maximim value.  Must be greater than min.
	 * @return Double between min and max, inclusive.
	 * @see java.util.Random#nextDouble(double)
	 */
	public static double randDouble(final double min, final double max) {
		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		return MathUtils.nextDouble(rand,(max - min) + 1) + min;
	}

	private static double nextDouble(final Random rng, final double n) {
		// error checking and 2^x checking removed for simplicity.
		double bits, val;
		do {
			bits = (rng.nextLong() << 1) >>> 1;
			val = bits % n;
		} while (((bits-val)+(n-1)) < 0L);
		return val;
	}

	/**
	 * Returns a psuedo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * Float.MAX_VALUE - 1.
	 *
	 * @param min Minimim value
	 * @param max Maximim value.  Must be greater than min.
	 * @return Float between min and max, inclusive.
	 * @see java.util.Random#nextFloat(float)
	 */
	public static float randFloat(final float min, final float max) {
		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		return MathUtils.nextFloat(rand,(max - min) + 1) + min;
	}

	private static float nextFloat(final Random rng, final float n) {
		// error checking and 2^x checking removed for simplicity.
		float bits, val;
		do {
			bits = (rng.nextLong() << 1) >>> 1;
			val = bits % n;
		} while (((bits-val)+(n-1)) < 0L);
		return val;
	}


	/**
	 * Returns a percentage.
	 * The returned number is the % of X in Y.
	 * Supports Doubles.
	 *
	 * @param current Current value.
	 * @param max Maximim value.  Must be greater than min.
	 * @return double between min and max, inclusive.
	 */
	public static double findPercentage(final double current, final double max){
		return Math.round(((current / max) * 100) * 100.00) / 100.00;
	}
	
	public static int findPercentageOfInt(long input, float percentage){
		return (int)(input*(percentage/100.0f));
	}


	//Smooth Rounding Function
	/**
	 * Returns a double.
	 * The returned number is d rounded to the nearest d.01.
	 * Supports Doubles.
	 *
	 * @param current Current value.
	 * @return double Rounded value.
	 */
	public static double decimalRounding(final double d) {
		return Math.round(d * 2) / 2.0;
	}


	//Smooth Rounding Function (Nearest 5)
	/**
	 * Returns a double.
	 * The returned number is d rounded to the nearest d.5.
	 * Supports Doubles.
	 *
	 * @param current Current value.
	 * @return double Rounded value.
	 */
	public static double decimalRoundingToWholes(final double d) {
		return 5*(Math.round(d/5));
	}

	//Smooth Rounding Function
	/**
	 * Returns a integer.
	 * The returned number is d rounded to the nearest flat integer.
	 * Supports Doubles as input.
	 *
	 * @param current Current value.
	 * @return integer Rounded value.
	 */
	public static int roundToClosestInt(final double d) {
		return (int) (Math.round(d * 2) / 2.0);
	}

	public static int roundToClosestMultiple(final double number, final int multiple) {
		int result = multiple;
		if ((number % multiple) == 0) {
			return (int) number;
		}
		// If not already multiple of given number
		if ((number % multiple) != 0) {
			final int division = (int) ((number / multiple) + 1);
			result = division * multiple;
		}
		return result;
	}
	
	
	//Smooth Rounding Function
		/**
		 * Returns a long.
		 * The returned number is d rounded to the nearest flat long.
		 * Supports Doubles as input.
		 *
		 * @param current Current value.
		 * @return long Rounded value.
		 */
		public static long roundToClosestLong(final double d) {
			return (long) (Math.round(d * 2) / 2.0);
		}


	/**
	 * Returns a boolean.
	 * The returned boolean is wether or not X evenly fits in to Y.
	 * Supports ints.
	 *
	 * @param x Value A.
	 * @param y Value B.  Must be greater than min.
	 * @return boolean Whether or not it divides evenly.
	 */
	public static boolean divideXintoY(final int x, final int y){
		if ((x % y) == 0)
		{
			return true;
		}
		return false;
	}


	/**
	 * Returns a boolean.
	 * The returned boolean is based on the odd/eveness of the input.
	 * Supports ints.
	 *
	 * @param x Value A.
	 * @return boolean Whether or not it divides evenly.
	 */
	public static boolean isNumberEven(final int x){
		if ((x % 2) == 0)
		{
			return true;
		}
		return false;
	}



	/**
	 * Returns an int.
	 * The returned number is the value on i + 273.15F.
	 * Supports ints.
	 *
	 * @param i Temp in Celcius.
	 * @return int The celcius temp returned as Kelvin, rounded to the readest whole.
	 */
	public static float celsiusToKelvin(final int i){
		final double f = i + 273.15F;
		return (int)decimalRoundingToWholes(f);
	}


	/**
	 * Returns a hexInteger.
	 * The returned number is the hex value of the input.
	 * Supports ints.
	 *
	 * @param input Current value.
	 * @return hexInteger.
	 */
	public static int getHexNumberFromInt(final int input){
		final String result = Integer.toHexString(input);
		final int resultINT = Integer.getInteger(result);
		return resultINT;
	}


	/**
	 * Returns a hexInteger.
	 * The returned value is between min and max.
	 * Supports ints.
	 *
	 * @param min Minimum value.
	 * @param max Maximium value.  Must be greater than min.
	 * @return hexInteger between min and max, inclusive.
	 */
	public static int generateRandomHexValue(final int min, final int max){
		return getHexNumberFromInt(randInt(min, max));
	}


	/**
	 * Returns a random hex value.
	 * The returned value is between 000000-ffffff.
	 *
	 * @return hexInteger between min and max, inclusive.
	 */
	public static int generateSingularRandomHexValue(){
		String temp;
		final int randomInt = randInt(1, 5);
		final Map<Integer, String> colours = Utils.hexColourGeneratorRandom(5);

		if ((colours.get(randomInt) != null) && (colours.size() > 0)){
			temp = colours.get(randomInt);
		}
		else {
			temp = "0F0F0F";
		}

		Logger.WARNING("Operating with "+temp);
		temp = Utils.appenedHexNotationToString(String.valueOf(temp));
		Logger.WARNING("Made "+temp+" - Hopefully it's not a mess.");
		Logger.WARNING("It will decode into "+Integer.decode(temp)+".");
		return Integer.decode(temp);
	}

	public static long[] simplifyNumbersToSmallestForm(final long[] inputArray){
		final long GCD = gcd(inputArray);
		final long[] outputArray = new long[inputArray.length];
		for (int i=0;i<inputArray.length;i++){
			if (GCD != 0) {
				outputArray[i] = (inputArray[i]/GCD);
			} else {
				outputArray[i] = inputArray[i];
			}
		}
		if (outputArray.length > 0) {
			return outputArray;
		}
		return new long[] {};
	}

	private static long gcd(long a, long b){
		while (b > 0)
		{
			final long temp = b;
			b = a % b; // % is remainder
			a = temp;
		}
		return a;
	}

	private static long gcd(final long[] input){
		long result = input[0];
		for(int i = 1; i < input.length; i++) {
			result = gcd(result, input[i]);
		}
		return result;
	}

	final public static int getRgbAsHex(final short[] RGBA){
		final int returnValue = Utils.rgbtoHexValue(RGBA[0], RGBA[1], RGBA[2]);
		return (returnValue == 0) ? 0 : returnValue;
	}


	public final static int returnLargestNumber(final int a, final int b){
		if (a > b){
			return a;
		}
		else if (a == b){
			return a;
		}
		else {
			return b;
		}
	}

	public static int safeInt(long number, int margin){
		return number>Integer.MAX_VALUE-margin ? Integer.MAX_VALUE-margin :(int)number;
	}

	public static int safeInt(long number){
		return number>GT_Values.V[GT_Values.V.length-1] ? safeInt(GT_Values.V[GT_Values.V.length-1],1) : number<Integer.MIN_VALUE ? Integer.MIN_VALUE : (int)number;
	}

	public static int getRandomFromArray(int[] mValues) {
		int[] mLargeChanceArray = new int[mValues.length*1000];
		int mValueSelection;
		for (int g = 0; g < mLargeChanceArray.length; g++) {
			mValueSelection = randInt(0, mValues.length);
			mLargeChanceArray[g] = mValues[mValueSelection];
		}
		return mLargeChanceArray[randInt(0, mLargeChanceArray.length)];
		
	}

}
