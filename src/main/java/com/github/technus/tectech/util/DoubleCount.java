package com.github.technus.tectech.util;

import java.util.Arrays;

import static java.lang.Math.*;
import static java.lang.Math.max;
import static java.lang.Math.ulp;

public class DoubleCount {
    public static double[] distribute(double count,double... probabilities) throws ArithmeticException {
        if (probabilities == null) {
            return null;
        } else if (count == 0) {
            return new double[probabilities.length];
        } else {
            switch (probabilities.length) {
                default: {
                    int size = probabilities.length;
                    double[] output = new double[size];
                    size--;
                    double remaining = count, previous = probabilities[size], probability, out, sum = 0;
                    for (int i = size - 1; i >= 0; i--) {
                        probability = probabilities[i];
                        remaining -= out = count * probability - ulp(probability);
                        sum += output[i] = out;
                        if (previous < probability) {
                            throw new ArithmeticException("Malformed probability order: " + Arrays.toString(probabilities));
                        }
                        previous = probability;
                        if (probability >= 1) {
                            break;
                        }
                    }
                    if (remaining * count < 0) {
                        finishIt(size, output, remaining);
                    } else {
                        sum += output[size] = remaining - ulp(remaining) * size;
                        if (sum > count) {
                            remaining = sum - count;
                            finishIt(size, output, remaining);
                        }
                    }
                    return output;
                }
                case 1:
                    return new double[]{count};
                case 0:
                    return probabilities;//empty array at hand...
            }
        }
    }

    private static void finishIt(int size, double[] output, double remaining) {
        for (int i = size - 1; i >= 0; i--) {
            if (abs(output[i]) >= abs(remaining)) {
                output[i] -= remaining;
                break;
            } else {
                remaining+=output[i];
                output[i]=0;
            }
        }
    }

    public static double div(double count,double divisor){
        if (divisor == 0) {
            throw new ArithmeticException("Divide by 0");
        }else if(count==0 || divisor==1){
            return count;
        }else if(divisor==-1){
            return -count;
        } else {
            double result = count / divisor;
            if(result*count<0){
                return 0;
            }
            return result-ulp(result);
        }
    }

    public static double mul(double count,double multiplier){
        if(count==0 || multiplier==1){
            return count;
        }else if(multiplier==-1){
            return -count;
        } else {
            double result = count * multiplier;
            if(result*count<0){
                return 0;
            }
            return result-ulp(result);
        }
    }

    public static double sub(double count,double value){
        if(value==0){
            return count;
        }
        if(count==0){
            return -value;
        }
        if(value==count){
            return 0;
        }
        return value < 0 ? addInternal(count, -value) : subInternal(count, value);
    }

    public static double add(double count,double value){
        if(value==0){
            return count;
        }
        if(count==0){
            return value;
        }
        return value < 0 ? subInternal(count, -value) : addInternal(count, value);
    }

    private static double subInternal(double count,double value){
        double result = count - max(value,ulp(count));
        if (result+value>count || value>count-result){
            result-=ulp(result);
        }
        return result;
    }

    private static double addInternal(double count,double value){
        double result = count + value;
        if (result-value>count || result-count>value){
            result-=ulp(result);
        }
        return result;
    }
}
