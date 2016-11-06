package gtPlusPlus.core.util.math;

import gtPlusPlus.core.util.Utils;

import java.util.Map;
import java.util.Random;

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
	public static int randInt(int min, int max) {

		// Usually this can be a field rather than a method variable
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
	
	public static double getChanceOfXOverYRuns(double x, double y){
		double z = (1-Math.pow((1-x), y));
	return z;
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
	public static long randLong(long min, long max) {
		// Usually this can be a field rather than a method variable
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		long randomNum = MathUtils.nextLong(rand,(max - min) + 1) + min;

		return randomNum;
	}
	private static long nextLong(Random rng, long n) {
		// error checking and 2^x checking removed for simplicity.
		long bits, val;
		do {
			bits = (rng.nextLong() << 1) >>> 1;
			val = bits % n;
		} while (bits-val+(n-1) < 0L);
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
	public static double findPercentage(double current, double max){
		double c = ((double) current / max) * 100;
		double roundOff = Math.round(c * 100.00) / 100.00;
		return roundOff;
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
	public static double decimalRounding(double d) {
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
	public static double decimalRoundingToWholes(double d) {
		return 5*(Math.round(d/5));
	}
	
	public static int roundToClosestMultiple(double number, int multiple) {
        int result = multiple;
        if (number % multiple == 0) {
            return (int) number;
        }
        // If not already multiple of given number
        if (number % multiple != 0) {
            int division = (int) ((number / multiple) + 1);
            result = division * multiple;
        }
        return result;
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
	public static boolean divideXintoY(int x, int y){
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
	public static boolean isNumberEven(int x){
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
	public static float celsiusToKelvin(int i){
		double f = i + 273.15F;
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
	public static int getHexNumberFromInt(int input){
		String result = Integer.toHexString(input);
		int resultINT = Integer.getInteger(result);
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
	public static int generateRandomHexValue(int min, int max){
		int result = getHexNumberFromInt(randInt(min, max));
		return result;
	}

	
	/**
	 * Returns a random hex value.
	 * The returned value is between 000000-ffffff.
	 *
	 * @return hexInteger between min and max, inclusive.	 
	 */
	public static int generateSingularRandomHexValue(){
		String temp;
		int randomInt = randInt(1, 5);
		final Map<Integer, String> colours = Utils.hexColourGeneratorRandom(5);

		if (colours.get(randomInt) != null && colours.size() > 0){				
			temp = colours.get(randomInt);		
		}
		else {
			temp = "0F0F0F";	
		}

		Utils.LOG_WARNING("Operating with "+temp);	
		temp = Utils.appenedHexNotationToString(String.valueOf(temp));
		Utils.LOG_WARNING("Made "+temp+" - Hopefully it's not a mess.");
		Utils.LOG_WARNING("It will decode into "+Integer.decode(temp)+".");
		return Integer.decode(temp);
	}
	
	public static long[] simplifyNumbersToSmallestForm(long[] inputArray){
		long GCD = gcd(inputArray);
		long[] outputArray = new long[inputArray.length];
		for (int i=0;i<inputArray.length;i++){
			if (GCD != 0)
			outputArray[i] = (inputArray[i]/GCD);
			else
				outputArray[i] = inputArray[i];
		}
		if (outputArray.length > 0)
		return outputArray;
		return null;
	}
	
	private static long gcd(long a, long b){
	    while (b > 0)
	    {
	        long temp = b;
	        b = a % b; // % is remainder
	        a = temp;
	    }
	    return a;
	}
	
	private static long gcd(long[] input){
	    long result = input[0];
	    for(int i = 1; i < input.length; i++) result = gcd(result, input[i]);
	    return result;
	}
	
	

}
