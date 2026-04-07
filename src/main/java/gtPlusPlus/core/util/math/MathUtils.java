package gtPlusPlus.core.util.math;

import java.text.NumberFormat;
import java.util.Random;

import gregtech.api.enums.GTValues;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.Utils;

public class MathUtils {

    private static final Random rand = GTPPCore.RANDOM;

    /** Formats a number with group separator and at most 2 fraction digits. */
    private static final NumberFormat sNumberFormat = NumberFormat.getInstance();

    static {
        sNumberFormat.setMaximumFractionDigits(2);
    }

    /**
     * Returns a psuedo-random number between min and max, inclusive. The difference between min and max can be at most
     * Integer.MAX_VALUE - 1.
     *
     * @param min Minimim value
     * @param max Maximim value. Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(final int min, final int max) {
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Returns a psuedo-random number between min and max, inclusive. The difference between min and max can be at most
     * Long.MAX_VALUE - 1.
     *
     * @param min Minimim value
     * @param max Maximim value. Must be greater than min.
     * @return Long between min and max, inclusive.
     * @see java.util.Random#nextLong(long)
     */
    public static long randLong(final long min, final long max) {
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return MathUtils.nextLong((max - min) + 1) + min;
    }

    private static long nextLong(final long n) {
        // error checking and 2^x checking removed for simplicity.
        long bits, val;
        do {
            bits = (MathUtils.rand.nextLong() << 1) >>> 1;
            val = bits % n;
        } while (((bits - val) + (n - 1)) < 0L);
        return val;
    }

    /**
     * Returns a psuedo-random number between min and max, inclusive. The difference between min and max can be at most
     * Double.MAX_VALUE - 1.
     *
     * @param min Minimim value
     * @param max Maximim value. Must be greater than min.
     * @return Double between min and max, inclusive.
     * @see java.util.Random#nextDouble(double)
     */
    public static double randDouble(final double min, final double max) {
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return MathUtils.nextDouble((max - min) + 1) + min;
    }

    private static double nextDouble(final double n) {
        // error checking and 2^x checking removed for simplicity.
        double bits, val;
        do {
            bits = (MathUtils.rand.nextLong() << 1) >>> 1;
            val = bits % n;
        } while (((bits - val) + (n - 1)) < 0L);
        return val;
    }

    /**
     * Returns a psuedo-random number between min and max, inclusive. The difference between min and max can be at most
     * Float.MAX_VALUE - 1.
     *
     * @param min Minimim value
     * @param max Maximim value. Must be greater than min.
     * @return Float between min and max, inclusive.
     * @see java.util.Random#nextFloat(float)
     */
    public static float randFloat(final float min, final float max) {
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return MathUtils.nextFloat((max - min) + 1) + min;
    }

    private static float nextFloat(final float n) {
        // error checking and 2^x checking removed for simplicity.
        float bits, val;
        do {
            bits = (MathUtils.rand.nextLong() << 1) >>> 1;
            val = bits % n;
        } while (((bits - val) + (n - 1)) < 0L);
        return val;
    }

    /**
     * Returns a percentage. The returned number is the % of X in Y. Supports Doubles.
     *
     * @param current Current value.
     * @param max     Maximim value. Must be greater than min.
     * @return double between min and max, inclusive.
     */
    public static double findPercentage(final double current, final double max) {
        return Math.round(((current / max) * 100) * 100.00) / 100.00;
    }

    /**
     * Returns a percentage. The returned number is the % of X in Y. Supports Floats.
     *
     * @param current Current value.
     * @param max     Maximim value. Must be greater than min.
     * @return double between min and max, inclusive.
     */
    public static float findPercentage(final float current, final float max) {
        return (float) (Math.round(((current / max) * 100) * 100.00) / 100.00);
    }

    public static int findPercentageOfInt(long input, float percentage) {
        return (int) (input * (percentage / 100.0f));
    }

    // Smooth Rounding Function

    /**
     * Returns a double. The returned number is d rounded to the nearest d.01. Supports Doubles.
     *
     * @return double Rounded value.
     */
    public static double decimalRounding(final double d) {
        return Math.round(d * 2) / 2.0;
    }

    // Smooth Rounding Function (Nearest 5)

    /**
     * Returns a double. The returned number is d rounded to the nearest d.5. Supports Doubles.
     *
     * @return double Rounded value.
     */
    public static double decimalRoundingToWholes(final double d) {
        return 5 * (Math.round(d / 5));
    }

    // Smooth Rounding Function

    /**
     * Returns a integer. The returned number is d rounded to the nearest flat integer. Supports Doubles as input.
     *
     * @return integer Rounded value.
     */
    public static int roundToClosestInt(final double d) {
        return (int) (Math.round(d * 2) / 2.0);
    }

    // Smooth Rounding Function

    /**
     * Returns a long. The returned number is d rounded to the nearest flat long. Supports Doubles as input.
     *
     * @return long Rounded value.
     */
    public static long roundToClosestLong(final double d) {
        return (long) (Math.round(d * 2) / 2.0);
    }

    /**
     * Returns a boolean. The returned boolean is based on the odd/eveness of the input. Supports ints.
     *
     * @param x Value A.
     * @return boolean Whether or not it divides evenly.
     */
    public static boolean isNumberEven(final long x) {
        return x % 2 == 0;
    }

    /**
     * Returns an int. The returned number is the value on i + 273.15F. Supports ints.
     *
     * @param i Temp in Celcius.
     * @return int The celcius temp returned as Kelvin, rounded to the readest whole.
     */
    public static float celsiusToKelvin(final int i) {
        final double f = i + 273.15F;
        return (int) decimalRoundingToWholes(f);
    }

    public static long[] simplifyNumbersToSmallestForm(final long[] inputArray) {
        final long GCD = gcd(inputArray);
        final long[] outputArray = new long[inputArray.length];
        for (int i = 0; i < inputArray.length; i++) {
            if (GCD != 0) {
                outputArray[i] = (inputArray[i] / GCD);
            } else {
                outputArray[i] = inputArray[i];
            }
        }
        if (outputArray.length > 0) {
            return outputArray;
        }
        return GTValues.emptyLongArray;
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
            result = gcd(result, input[i]);
        }
        return result;
    }

    public static int getRgbAsHex(final short[] RGBA) {
        return Utils.rgbtoHexValue(RGBA[0], RGBA[1], RGBA[2]);
    }

    public static byte safeByte(long number) {
        return (byte) clamp_long(number, Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    public static int safeInt(long number, int margin) {
        return (int) clamp_long(number, Integer.MIN_VALUE + margin, Integer.MAX_VALUE - margin);
    }

    public static int safeInt(long number) {
        return number > GTValues.V[GTValues.V.length - 1] ? safeInt(GTValues.V[GTValues.V.length - 1], 1)
            : number < Integer.MIN_VALUE ? Integer.MIN_VALUE : (int) number;
    }

    public static int getRandomFromArray(int[] mValues) {
        int[] mLargeChanceArray = new int[(mValues.length - 1) * 1000];
        int mValueSelection;
        for (int g = 0; g < mLargeChanceArray.length; g++) {
            mValueSelection = randInt(0, mValues.length - 1);
            mLargeChanceArray[g] = mValues[mValueSelection];
        }
        return mLargeChanceArray[randInt(0, mLargeChanceArray.length - 1)];
    }

    public static int getIntAverage(int[] aDataSet) {
        if (aDataSet.length == 0) {
            return 0;
        }
        int divisor = aDataSet.length;
        int total = 0;
        for (int i : aDataSet) {
            total += i;
        }
        return safeInt(total / divisor);
    }

    public static int howManyPlaces(int aValueForGen) {
        String a = String.valueOf(aValueForGen);
        return a.length();
    }

    public static int safeCast_LongToInt(long l) {
        return (int) clamp_long(l, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static long clamp_long(long number, long min, long max) {
        if (number > max) {
            return max;
        } else if (number < min) {
            return min;
        } else {
            return number;
        }
    }

    /**
     * Balances a number within a range.
     *
     * @param aInput - The number to balance
     * @param aMin   - The minimum bounds
     * @param aMax   - The maximum bounds
     * @return - An Integer which will be between the bounds, or a boundary value.
     */
    public static int balance(int aInput, int aMin, int aMax) {
        return Math.max(Math.min(aInput, aMax), aMin);
    }

}
