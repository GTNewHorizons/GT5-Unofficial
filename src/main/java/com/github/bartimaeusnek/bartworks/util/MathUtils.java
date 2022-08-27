/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.util;

/*
 * Faster implementations for Math stuff
 */
@SuppressWarnings("unused")
public class MathUtils {

    public static long floorLong(double x) {
        if (Double.isInfinite(x) || Double.isNaN(x)) return (long) x;
        long xi = (long) x;
        return x < xi ? xi - 1 : xi;
    }

    public static long ceilLong(double x) {
        if (Double.isInfinite(x) || Double.isNaN(x)) return (long) x;
        long xi = (long) x;
        return x > xi ? xi + 1 : xi;
    }

    public static int floorInt(double x) {
        if (Double.isInfinite(x) || Double.isNaN(x)) return (int) x;
        int xi = (int) x;
        return x < xi ? xi - 1 : xi;
    }

    public static int ceilInt(float x) {
        if (Float.isInfinite(x) || Float.isNaN(x)) return (int) x;
        int xi = (int) x;
        return x > xi ? xi + 1 : xi;
    }

    public static int ceilInt(double x) {
        if (Double.isInfinite(x) || Double.isNaN(x)) return (int) x;
        int xi = (int) x;
        return x > xi ? xi + 1 : xi;
    }

    public static double floor(double x) {
        if (Double.isInfinite(x) || Double.isNaN(x)) return x;
        int xi = (int) x;
        return x < xi ? xi - 1 : xi;
    }

    public static double ceil(double x) {
        if (Double.isInfinite(x) || Double.isNaN(x)) return x;
        int xi = (int) x;
        return x > xi ? xi + 1 : xi;
    }

    public static byte clamp(byte amount, byte min, byte max) {
        byte inner = (amount <= max) ? amount : max;
        return (min >= inner) ? min : inner;
    }

    public static short clamp(short amount, short min, short max) {
        short inner = (amount <= max) ? amount : max;
        return (min >= inner) ? min : inner;
    }

    public static int clamp(int amount, int min, int max) {
        return Math.max(min, Math.min(amount, max));
    }

    public static long clamp(long amount, long min, long max) {
        return Math.max(min, Math.min(amount, max));
    }

    public static float clamp(float amount, float min, float max) {
        return Math.max(min, Math.min(amount, max));
    }

    public static double clamp(double amount, double min, double max) {
        return Math.max(min, Math.min(amount, max));
    }

    public static <T extends Comparable<T>> T clamp(T val, T min, T max) {
        return (val.compareTo(min) < 0) ? min : (val.compareTo(max) > 0) ? max : val;
    }

    public static int wrap(int input, int bound) {
        return (((input % bound) + bound) % bound);
    }

    public static long wrap(long input, long bound) {
        return (((input % bound) + bound) % bound);
    }

    public static double wrap(double input, double bound) {
        return (((input % bound) + bound) % bound);
    }

    public static float wrap(float input, float bound) {
        return (((input % bound) + bound) % bound);
    }

    public static float tanh(float x) {
        float x2 = x * x;
        float a = x * (135135.0f + x2 * (17325.0f + x2 * (378.0f + x2)));
        float b = 135135.0f + x2 * (62370.0f + x2 * (3150.0f + x2 * 28.0f));
        return clamp(a / b, -1, 1);
    }
}
