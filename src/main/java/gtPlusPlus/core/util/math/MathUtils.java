package gtPlusPlus.core.util.math;

import java.text.NumberFormat;
import java.util.Map;
import java.util.Random;

import gregtech.api.enums.GT_Values;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;

public class MathUtils {

    static final Random rand = CORE.RANDOM;

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
        return MathUtils.nextLong(rand, (max - min) + 1) + min;
    }

    private static long nextLong(final Random rng, final long n) {
        // error checking and 2^x checking removed for simplicity.
        long bits, val;
        do {
            bits = (rng.nextLong() << 1) >>> 1;
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
        return MathUtils.nextDouble(rand, (max - min) + 1) + min;
    }

    private static double nextDouble(final Random rng, final double n) {
        // error checking and 2^x checking removed for simplicity.
        double bits, val;
        do {
            bits = (rng.nextLong() << 1) >>> 1;
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
        return MathUtils.nextFloat(rand, (max - min) + 1) + min;
    }

    private static float nextFloat(final Random rng, final float n) {
        // error checking and 2^x checking removed for simplicity.
        float bits, val;
        do {
            bits = (rng.nextLong() << 1) >>> 1;
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
        if ((x % 2) == 0) {
            return true;
        }
        return false;
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

    /**
     * Returns a random hex value. The returned value is between 000000-ffffff.
     *
     * @return hexInteger between min and max, inclusive.
     */
    public static int generateSingularRandomHexValue() {
        String temp;
        final int randomInt = randInt(1, 5);
        final Map<Integer, String> colours = Utils.hexColourGeneratorRandom(5);

        if ((colours.get(randomInt) != null) && (colours.size() > 0)) {
            temp = colours.get(randomInt);
        } else {
            temp = "0F0F0F";
        }

        Logger.WARNING("Operating with " + temp);
        temp = Utils.appenedHexNotationToString(String.valueOf(temp));
        return Integer.decode(temp);
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
        return new long[] {};
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
        final int returnValue = Utils.rgbtoHexValue(RGBA[0], RGBA[1], RGBA[2]);
        return (returnValue == 0) ? 0 : returnValue;
    }

    public static byte safeByte(long number) {
        return number > Byte.MAX_VALUE ? Byte.MAX_VALUE : (byte) number;
    }

    public static short safeShort(long number) {
        return number > Short.MAX_VALUE ? Short.MAX_VALUE : (short) number;
    }

    public static int safeInt(long number, int margin) {
        return number > Integer.MAX_VALUE - margin ? Integer.MAX_VALUE - margin : (int) number;
    }

    public static int safeInt(long number) {
        return number > GT_Values.V[GT_Values.V.length - 1] ? safeInt(GT_Values.V[GT_Values.V.length - 1], 1)
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

    /*
     * Averages
     */

    public static byte getByteAverage(AutoMap<Byte> aDataSet) {
        byte[] aNewSet = new byte[aDataSet.size()];
        for (int u = 0; u < aDataSet.size(); u++) {
            byte b = getSafeByte(aDataSet.get(u));
            aNewSet[u] = b;
        }
        return getByteAverage(aNewSet);
    }

    public static short getShortAverage(AutoMap<Short> aDataSet) {
        short[] aNewSet = new short[aDataSet.size()];
        for (int u = 0; u < aDataSet.size(); u++) {
            short b = getSafeShort(aDataSet.get(u));
            aNewSet[u] = b;
        }
        return getShortAverage(aNewSet);
    }

    public static int getIntAverage(AutoMap<Integer> aDataSet) {
        int[] aNewSet = new int[aDataSet.size()];
        for (int u = 0; u < aDataSet.size(); u++) {
            int b = getSafeInt(aDataSet.get(u));
            aNewSet[u] = b;
        }
        return getIntAverage(aNewSet);
    }

    public static long getLongAverage(AutoMap<Long> aDataSet) {
        long[] aNewSet = new long[aDataSet.size()];
        for (int u = 0; u < aDataSet.size(); u++) {
            long b = getSafeLong(aDataSet.get(u));
            aNewSet[u] = b;
        }
        return getLongAverage(aNewSet);
    }

    public static byte getByteAverage(byte[] aDataSet) {
        if (aDataSet.length == 0) {
            return 0;
        }
        int divisor = aDataSet.length;
        byte total = 0;
        for (byte i : aDataSet) {
            total += i;
        }
        byte result = safeByte(total / divisor);
        return result;
    }

    public static short getShortAverage(short[] aDataSet) {
        if (aDataSet.length == 0) {
            return 0;
        }
        int divisor = aDataSet.length;
        Logger.WARNING("Calculating Average Short. Divisor: " + divisor);
        short total = 0;
        for (short i : aDataSet) {
            Logger.WARNING("Adding " + i);
            total += i;
        }
        short result = safeShort((total / divisor));
        Logger.WARNING("Average: " + result);
        return result;
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
        int result = safeInt(total / divisor);
        return result;
    }

    public static long getLongAverage(long[] aDataSet) {
        if (aDataSet.length == 0) {
            return 0;
        }
        int divisor = aDataSet.length;
        long total = 0;
        for (long i : aDataSet) {
            total += i;
        }
        return (total / divisor);
    }

    public static int howManyPlaces(int aValueForGen) {
        if (aValueForGen < 0) {
            aValueForGen = makeNegative(aValueForGen);
        }
        String a = String.valueOf(aValueForGen);
        return a.length();
    }

    /**
     * Inverts the value, making Positives into Negatives and vice versa.
     * 
     * @param aPositive - An int value, either positive or negative.
     * @return - Inverted int Value.
     */
    public static int makeNegative(int aPositive) {
        if (aPositive > 0) {
            return -aPositive;
        } else if (aPositive < 0) {
            return +aPositive;
        } else {
            return 0;
        }
    }

    public static <V> V safeCast(Object aNumberType) {
        long a1;
        double a2;
        a1 = Long.parseLong(aNumberType.toString());
        a2 = Double.parseDouble(aNumberType.toString());

        if ((aNumberType.getClass() == byte.class) || (aNumberType instanceof Byte)) {
            if (a1 >= Byte.MIN_VALUE && a1 <= Byte.MAX_VALUE) {
                String s = String.valueOf(a1);
                Byte s1 = Byte.valueOf(s);
                return (V) s1;
            }
        } else if ((aNumberType.getClass() == short.class) || (aNumberType instanceof Short)) {
            if (a1 >= Short.MIN_VALUE && a1 <= Short.MAX_VALUE) {
                String s = String.valueOf(a1);
                Short s1 = Short.valueOf(s);
                return (V) s1;
            }
        } else if ((aNumberType.getClass() == int.class) || (aNumberType instanceof Integer)) {
            if (a1 >= Integer.MIN_VALUE && a1 <= Integer.MAX_VALUE) {
                String s = String.valueOf(a1);
                Integer s1 = Integer.valueOf(s);
                return (V) s1;
            }
        } else if ((aNumberType.getClass() == long.class) || (aNumberType instanceof Long)) {
            if (a1 >= Long.MIN_VALUE && a1 <= Long.MAX_VALUE) {
                String s = String.valueOf(a1);
                Long s1 = Long.valueOf(s);
                return (V) s1;
            }
        } else if ((aNumberType.getClass() == float.class) || (aNumberType instanceof Float)) {
            if (a2 >= Float.MIN_VALUE && a2 <= Float.MAX_VALUE) {
                String s = String.valueOf(a1);
                Float s1 = Float.valueOf(s);
                return (V) s1;
            }
        } else if ((aNumberType.getClass() == double.class) || (aNumberType instanceof Double)) {
            if (a2 >= Double.MIN_VALUE && a2 <= Double.MAX_VALUE) {
                String s = String.valueOf(a1);
                Double s1 = Double.valueOf(s);
                return (V) s1;
            }
        }

        Integer o = 0;
        return (V) o;
    }

    public static byte getSafeByte(Byte b) {
        Byte a = safeCast(b);
        return a;
    }

    public static short getSafeShort(Short b) {
        Short a = safeCast(b);
        return a;
    }

    public static int getSafeInt(Integer b) {
        Integer a = safeCast(b);
        return a;
    }

    public static long getSafeLong(Long b) {
        Long a = safeCast(b);
        return a;
    }

    public static int safeCast_LongToInt(long o) {
        if (o > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else {
            int i = (int) o;
            return i;
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

    /**
     * Returns the smaller of two {@code Number}s. That is, the result the argument closer to the value of
     * {@link Long#MIN_VALUE}. If the arguments have the same value, the result is that same value.
     *
     * @param a an argument.
     * @param b another argument.
     * @return the smaller of {@code a} and {@code b}.
     */
    public static Number min(Number a, Number b) {
        return (a.longValue() <= b.longValue()) ? a : b;
    }

    /**
     * Returns the greater of two {@code Number}s. That is, the result is the argument closer to the value of
     * {@link Long#MAX_VALUE}. If the arguments have the same value, the result is that same value.
     *
     * @param a an argument.
     * @param b another argument.
     * @return the larger of {@code a} and {@code b}.
     */
    public static Number max(Number a, Number b) {
        return (a.longValue() >= b.longValue()) ? a : b;
    }

    public static String formatNumbers(long aNumber) {
        return sNumberFormat.format(aNumber);
    }

    public static String formatNumbers(double aNumber) {
        return sNumberFormat.format(aNumber);
    }
}
