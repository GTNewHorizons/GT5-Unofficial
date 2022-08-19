package com.github.technus.tectech.util;

import static java.lang.Math.abs;
import static java.lang.Math.ulp;

import java.util.Arrays;

public class DoubleCount {
    /**
     * Distributes count across probabilities
     *
     * @param count         the count to divide
     * @param probabilities probability ratios to divide by, descending
     * @return divided count
     * @throws ArithmeticException
     */
    public static double[] distribute(double count, double... probabilities) throws ArithmeticException {
        if (probabilities == null || Double.isNaN(count)) {
            return null;
        } else if (count == 0) {
            return new double[probabilities.length];
        } else if (Double.isInfinite(count)) {
            double[] doubles = new double[probabilities.length];
            Arrays.fill(doubles, count);
            return doubles;
        } else {
            switch (probabilities.length) {
                default: {
                    int size = probabilities.length;
                    double[] output = new double[size];
                    size--;
                    double remaining = count, previous = probabilities[size], probability, out;
                    for (int i = size - 1; i >= 0; i--) {
                        probability = probabilities[i];
                        out = count * probability;
                        out -= ulpSigned(out);

                        remaining -= out;
                        output[i] = out;

                        if (previous < probability) {
                            throw new ArithmeticException(
                                    "Malformed probability order: " + Arrays.toString(probabilities));
                        }
                        previous = probability;
                        if (probability >= 1) {
                            break;
                        }
                    }
                    if (remaining * count < 0) { // overshoot
                        finishIt(size, output, remaining);
                    } else {
                        output[size] = remaining;
                    }
                    return output;
                }
                case 1:
                    return new double[] {count};
                case 0:
                    return probabilities; // empty array at hand...
            }
        }
    }

    public static double ulpSigned(double number) {
        if (number == 0) {
            return 0;
        }
        return number > 0 ? ulp(number) : -ulp(number);
    }

    private static void finishIt(int size, double[] output, double remaining) {
        for (int i = size - 1; i >= 0; i--) {
            if (abs(output[i]) >= abs(remaining)) {
                output[i] -= remaining;
                break;
            } else {
                remaining += output[i];
                output[i] = 0;
            }
        }
    }

    public static double div(double count, double divisor) {
        if (count == 0 || abs(divisor) == 1 || abs(count) == abs(divisor)) {
            return count / divisor;
        } else {
            double result = count / divisor;
            return result - ulpSigned(result);
        }
    }

    public static double mul(double count, double multiplier) {
        if (count == 0 || multiplier == 0 || abs(multiplier) == 1 || abs(count) == 1) {
            return count * multiplier;
        } else {
            double result = count * multiplier;
            return result - ulpSigned(result);
        }
    }

    public static double sub(double count, double value) {
        if (count == 0 || value == 0 || count == value) {
            return count - value;
        } else {
            double result = count - value;
            if (result == count || result == value) {
                return result;
            }
            return result - ulpSigned(result);
        }
    }

    public static double add(double count, double value) {
        if (count == 0 || value == 0 || count == -value) {
            return count + value;
        } else {
            double result = count + value;
            if (result == count || result == value) {
                return result;
            }
            return result - ulpSigned(result);
        }
    }
}
